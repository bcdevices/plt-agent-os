#!/bin/sh

# pltagent-usbgadget-handler.sh

# https://www.kernel.org/doc/Documentation/usb/mass-storage.txt
# https://developer.toradex.com/linux-bsp/application-development/peripheral-access/usb-device-mode-linux/
# https://wiki.tizen.org/USB/Linux_USB_Layers/Configfs_Composite_Gadget/Usage_eq._to_g_mass_storage.ko
# https://www.spinics.net/lists/linux-usb/msg76388.html

OPTIND=1
should_disable=1
should_enable=1

while getopts "d:e:" opt; do
        case "$opt" in
        d)
                should_disable=$OPTARG
                ;;
        e)
                should_enable=$OPTARG
                ;;
        esac
done

shift $((OPTIND - 1))

[ "${1:-}" = "--" ] && shift

/sbin/modprobe libcomposite

gen_image() {
	dd if=/dev/zero of="${backing_file_path}" bs=${backing_file_size} count=1
	/usr/sbin/parted --script "${backing_file_path}" -- mklabel msdos \
		mkpart primary fat16 1MiB -2048s
	/usr/sbin/mkfs.vfat.dosfstools -v \
		--offset="${partition_offset_dosfs}" \
		-S 1024 -F 16 -n RAMDISK "${backing_file_path}"
}

present_to_usbhost() {
        mkdir -m 0755 -p "${local_mount_path}"
	umount "${local_mount_path}" 2>/dev/null
	/sbin/losetup -D
	if [ ! -f "${backing_file_path}" ]; then
		gen_image
	fi
	echo "${backing_file_path}" >"${sysfs_msc_file_path}"
}
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

# Mount configfs
#mount -t configfs none /sys/kernel/config
sysfs_usb_gadget_path="/sys/kernel/config/usb_gadget"

usb_gadget_devname="pltagent"

# Create gadget
sysfs_usb_gadget_devpath="${sysfs_usb_gadget_path}/${usb_gadget_devname}"
mkdir "${sysfs_usb_gadget_devpath}"

# Use Protoype vendor and product id
echo 0x6666 >"${sysfs_usb_gadget_devpath}/idVendor"
echo 0x8001 >"${sysfs_usb_gadget_devpath}/idProduct"

# English language strings
mkdir "${sysfs_usb_gadget_devpath}/strings/0x409"
cat "/proc/device-tree/serial-number" >"${sysfs_usb_gadget_devpath}/strings/0x409/serialnumber"
echo "Production" >"${sysfs_usb_gadget_devpath}/strings/0x409/manufacturer"
cat "/proc/device-tree/model" >"${sysfs_usb_gadget_devpath}/strings/0x409/product"

# Create Ramdisk config
mkdir "${sysfs_usb_gadget_devpath}/configs/c.1"
mkdir "${sysfs_usb_gadget_devpath}/configs/c.1/strings/0x409"
echo "Ramdisk config" >"${sysfs_usb_gadget_devpath}/configs/c.1/strings/0x409/configuration"

# ACM
mkdir "${sysfs_usb_gadget_devpath}/functions/acm.0"
ln -s -t "${sysfs_usb_gadget_devpath}/configs/c.1" "${sysfs_usb_gadget_devpath}/functions/acm.0"

# MSC
sysfs_msc_dir="${sysfs_usb_gadget_devpath}/functions/mass_storage.0"
mkdir "${sysfs_msc_dir}"
ln -s -t "${sysfs_usb_gadget_devpath}/configs/c.1" "${sysfs_msc_dir}"
sysfs_msc_lun_path="${sysfs_msc_dir}/lun.0"
local_mount_path="/run/pltagent/mnt"
backing_file_path="/run/pltagent/usb-msc.img"
backing_file_size="256M"
partition_offset_dosfs="1024"
sysfs_msc_file_path="${sysfs_msc_lun_path}/file"

if [ $should_disable -eq 1 ]; then
	# Disable existing for the USB controller
	echo "" >"${sysfs_usb_gadget_devpath}/UDC" 2>/dev/null
fi
if [ $should_enable -eq 1 ]; then
	# HACK: avoid -EBUSY
	sleep 1
	present_to_usbhost
	sync
	# Enable new configuration for the USB controller
	echo "${udc_dev}" >"${sysfs_usb_gadget_devpath}/UDC"
	systemctl start serial-getty@ttyGS0
fi
