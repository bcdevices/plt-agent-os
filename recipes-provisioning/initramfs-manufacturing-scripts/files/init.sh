#!/bin/sh

export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

DISK=/dev/mmcblk0
# 10 seconds
TIMEOUT=100

do_mount_fs() {
    grep -q "$1" /proc/filesystems || return
    mkdir -p "$2"
    mount -t "$1" "$1" "$2"
}

do_mknod() {
    test -e "$1" || mknod "$1" "$2" "$3" "$4"
}

panic() {
    echo "PANIC: $1"
    exec setsid sh -c "exec sh -i <>/dev/${CONSOLE} 1>&0 2>&1"
}


wait_for_disk() {
    COUNT=0
    while [ ! -b "$DISK" ]; do
        COUNT=$((COUNT + 1))
        if [ "$COUNT" -ge "$TIMEOUT" ]; then
            panic "Timed out waiting for $DISK"
        fi

        sleep 0.1
    done

    info "Found disk - $DISK"
}

# Set up /sys
mkdir -p /sys
mount -t sysfs -o nodev,noexec,nosuid sysfs /sys
do_mount_fs debugfs /sys/kernel/debug

# Set up /proc
mkdir -p /proc
mount -t proc -o nodev,noexec,nosuid proc /proc

# Set up /dev
mkdir -p /dev
do_mount_fs devtmpfs /dev
do_mount_fs devpts /dev/pts
do_mount_fs tmpfs /dev/shm
mkdir -p /dev/console
do_mknod /dev/console c 5 1
do_mknod /dev/null c 1 3
do_mknod /dev/zero c 1 5

# Set up runtime directories
mkdir -p /tmp
mkdir -p /run
mkdir -p /var/run

# Obtain and split kernel cmdline into individual variables
read -r CMDLINE < /proc/cmdline
for param in $CMDLINE; do
    case $param in
        console=tty*)
            CONSOLE="${param#console=}"
            # Take only the part before the comma
            CONSOLE="${CONSOLE%%,*}"
            info "Console - $CONSOLE"
            ;;
    esac
done

# Wait for the disk to appear
wait_for_disk

# Below taken from mass-storage-gadget code in raspberry pi github repo
# https://github.com/raspberrypi/buildroot/blob/mass-storage-gadget64/board/raspberrypi64-mass-storage-gadget/rootfs_overlay/usr/local/bin/configure-gadgets
teardown() {
   umount "${CONFIGFS}" > /dev/null 2>&1
   rmmod libcomposite > /dev/null 2>&1
   rmdir "${CONFIGFS}"
}

CONFIGFS=/tmp/configfs
if [ -d "${CONFIGFS}" ]; then
   teardown || true
fi

# Disable activity LED, otherwise eMMC will be disabled on CM3
if grep -q 2837 /proc/device-tree/compatible; then
   vcmailbox 0x00038043 20 4 129 1 0 0 1 || true
   sleep 3
fi

if lspci | grep -iq "USB Controller"; then
   # For slow to init drives it might be better to use udev events
   # However, that's unecessary complexity for a rare use-case
   # right now.
   echo "Detected USB Controller - delaying for drive init"
   sleep 5
fi

mkdir -p "${CONFIGFS}"
modprobe libcomposite
modprobe dwc2
modprobe configfs
mount none "${CONFIGFS}" -t configfs
cd "${CONFIGFS}/usb_gadget"
mkdir -p rpimsg
cd rpimsg
echo 0x0a5c > idVendor  # Broadcom
echo 0x0104 > idProduct # Multifunction Composite Gadget
echo 0x0100 > bcdDevice # v1.0.0
echo 0x0200 > bcdUSB    # USB2
mkdir -p strings/0x409
grep Serial /proc/cpuinfo | awk '{print $3}'    > strings/0x409/serialnumber
echo "Raspberry Pi"                             > strings/0x409/manufacturer
echo "Raspberry Pi multi-function USB device"   > strings/0x409/product
mkdir -p configs/c.1/strings/0x409
echo "Config 1: ACM+MSD gadget" > configs/c.1/strings/0x409/configuration
echo 250 > configs/c.1/MaxPower
id=0
for dev in mmcblk0 nvme0n1 sda sdb sdc sdd; do
   if [ -e /dev/${dev} ]; then
      echo "Creating mass storage gadget for /dev/${dev}" > /dev/tty0
      mkdir -p functions/mass_storage.usb${id}
      echo 1 > functions/mass_storage.usb${id}/stall
      echo 0 > functions/mass_storage.usb${id}/lun.0/cdrom
      echo 0 > functions/mass_storage.usb${id}/lun.0/ro
      echo 0 > functions/mass_storage.usb${id}/lun.0/nofua
      echo /dev/${dev} > functions/mass_storage.usb${id}/lun.0/file
      echo "${dev}" > functions/mass_storage.usb${id}/lun.0/inquiry_string
      ln -s functions/mass_storage.usb${id} configs/c.1/
      id=$((id + 1))
   fi
done
mkdir -p functions/acm.usb0
ln -s functions/acm.usb0 configs/c.1/

count=0
while [ ${count} -lt 15 ]; do
   udc="$(ls /sys/class/udc)"
   if [ -n "${udc}" ]; then
      echo "Found UDC ${udc}"
      echo "${udc}" > UDC
      break
   fi
   count=$((count + 1))
   sleep 1
done

echo "Mass storage gadget init complete - $(cat UDC)"
modprobe usb_f_acm

echo "Waiting for ttyGS0..."
while [ ! -e /dev/ttyGS0 ]; do
    sleep 0.1
done

echo "Spawning shell on ttyGS0"

setsid /sbin/agetty -8 -L ttyGS0 115200 vt100 -a root

