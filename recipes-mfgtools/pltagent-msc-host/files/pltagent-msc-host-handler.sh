#!/bin/bash

# SPDX-License-Identifier: Apache-2.0
#
# Copyright (c) 2023 Blue Clover Devices
#
# Handler for USB-MSC gadget driver

# References:
#- https://developer.ridgerun.com/wiki/index.php?title=How_to_use_mass_storage_gadget
#- https://linuxlink.timesys.com/docs/wiki/engineering/HOWTO_Use_USB_Gadget_File_Storage

should_mount=0
should_present=0

CMD="$1"
CMDOPT="$2"

if [ "$CMD" = "mount" ]; then
	should_mount=1
fi

if [ "$CMD" = "present" ]; then
	should_present=1
fi

# USB Device Controller
#udc_dev="*"
#udc_dev="fe980000.usb" (RPi4)
# Determine UDC automatically
udc_list=$(ls /sys/class/udc)
udc_count=$(echo "$udc_list" | wc -l)

if [ "$udc_count" -eq 0 ]; then
	echo "Error: No UDC found in /sys/class/udc!" >&2
	exit 1
elif [ "$udc_count" -gt 1 ]; then
	echo "Error: Multiple UDCs found in /sys/class/udc: $udc_list" >&2
	exit 1
fi

udc_dev="$udc_list"

sysfs_udc_dev_path="/sys/class/udc/${udc_dev}"
sysfs_udc_state_path="${sysfs_udc_dev_path}/state"

sysfs_msc_lun_path="/sys/kernel/config/usb_gadget/pltagent/functions/mass_storage.0/lun.0"
sysfs_msc_file_path="${sysfs_msc_lun_path}/file"
sysfs_msc_forced_eject_path="${sysfs_msc_lun_path}/forced_eject"

backing_file_path="/run/pltagent/usb-msc.img"
backing_file_size="256M"
partition_offset_dosfs="1024"
partition_offset="1024K"

local_mount_path="/run/pltagent/mnt"
loopdev="/dev/loop0"


if [ ! -f "${sysfs_udc_state_path}" ]; then
	echo "FAIL:${sysfs_udc_state_path} not found"
	exit 1
fi
if [ ! -f "${sysfs_msc_file_path}" ]; then
	echo "FAIL:${sysfs_msc_file_path} not found"
	exit 1
fi
if [ ! -f "${sysfs_msc_forced_eject_path}" ]; then
	echo "FAIL:${sysfs_msc_forced_eject_path} not found"
	exit 1
fi

mkdir -m 0755 -p "${local_mount_path}"

gen_image() {
	dd if=/dev/zero of="${backing_file_path}" bs=${backing_file_size} count=1
	/usr/sbin/parted --script "${backing_file_path}" -- mklabel msdos \
		mkpart primary fat16 1MiB -2048s
	/usr/sbin/mkfs.vfat.dosfstools -v \
		--offset="${partition_offset_dosfs}" \
		-S 1024 -F 16 -n RAMDISK "${backing_file_path}"
}

mount_locally() {
	if [ "$CMDOPT" = "-forced_eject" ]; then
		echo "Forcing eject.."
		echo "1" >"${sysfs_msc_forced_eject_path}"
		echo "" >"${sysfs_msc_file_path}"
	elif [ "$CMDOPT" = "-await_eject" ]; then
		while [ ! -z "$(cat "${sysfs_msc_file_path}")" ]; do
			sleep 1
			echo "Awaiting eject.."
		done
		echo "Ejected"
	else
		# Do not clear LUN unless explicitly ejecting
		# This prevents USB re-enumeration mid-operation
		true
	fi

	if [ ! -f "${backing_file_path}" ]; then
		gen_image
	fi

	echo "partition_offset: ${partition_offset}"

	/sbin/losetup -o "${partition_offset}" "${loopdev}" "${backing_file_path}"
	mount -t vfat "${loopdev}" "${local_mount_path}"
}

check_is_mounted() {
	mount_entry=$(cat /proc/mounts | cut -d\  -f2 | grep ${local_mount_path})
	echo " mount_entry: '${mount_entry}'"
	if [ -n "${mount_entry}" ]; then return 1; fi
	return 0
}

unpresent() {
	if [ "$CMDOPT" = "-forced_eject" ]; then
		echo "Forcing eject.."
		echo "1" >"${sysfs_msc_forced_eject_path}"
		echo "" >"${sysfs_msc_file_path}"
	elif [ "$CMDOPT" = "-await_eject" ]; then
		while [ ! -z "$(cat "${sysfs_msc_file_path}")" ]; do
			sleep 1
			echo "Awaiting eject.."
		done
		echo "Ejected"
	else
		# Do NOT detach LUN unless forced eject is called
		# to prevent USB re-enumeration mid-operation
		echo "1" >"${sysfs_msc_forced_eject_path}"
	fi
}

present_to_usbhost() {
	umount "${local_mount_path}" 2>/dev/null
	/sbin/losetup -D
	if [ ! -f "${backing_file_path}" ]; then
		gen_image
	fi
	echo "${backing_file_path}" >"${sysfs_msc_file_path}"
}

check_is_presented() {
	sysfs_msc_file=$(cat ${sysfs_msc_file_path})
	if [ -z "${sysfs_msc_file}" ]; then return 0; fi
	return 1
}

# -----------------------------
# state
# -----------------------------
can_mount=0
can_present=0

check_is_presented
is_presented=$?

udc_state=$(cat ${sysfs_udc_state_path})

case ${udc_state} in
"not attached")
	echo "USB gadget is not attached"
	;;
"addressed")
	echo "USB gadget is being addressed"
	;;
"configured")
	echo "USB gadget is configured (connected to PLT)"
	can_present=1
	;;
"suspended")
	echo "USB gadget is suspended (disconnected from PLT)"
	;;
"powered")
	echo "USB gadget is powered (connected to a power supply)"
	;;
*)
	echo "WARN:udc_state:'${udc_state}'"
	;;
esac

check_is_mounted
is_mounted=$?

if [ ${is_mounted} -eq 1 ]; then
	can_present=0
fi

echo "is_presented: ${is_presented}"
echo "   udc_state: ${udc_state}"
echo "  is_mounted: ${is_mounted}"
echo "   can_mount: ${can_mount}"
echo " can_present: ${can_present}"

case ${is_mounted} in
0)
	if [ ${should_mount} -eq 1 ]; then
		echo "Mounting disk image locally.."
		mount_locally
		is_presented=0
	else
		echo "Disk image is not mounted locally"
	fi
	;;
1)
	echo "Disk image is mounted locally"
	;;
*)
	echo "FAIL:is_mounted:'${is_mounted}'"
	exit -1
	;;
esac

case ${is_presented} in
0)
	if [ ${should_present} -eq 1 ]; then
		echo "Presenting disk image to PLT.."
		present_to_usbhost
	else
		echo "Disk image is not presented to PLT"
	fi
	;;
1)
	echo "Disk image is presented to PLT"
	if [ ${should_present} -eq 1 ]; then
		echo "Retracting disk image from PLT.."
		unpresent
		echo "Presenting disk image to PLT.."
		present_to_usbhost
	else
		echo "Disk already presented to PLT"
	fi
	;;
*)
	echo "FAIL:is_presented:${is_presented}"
	exit -1
	;;
esac
