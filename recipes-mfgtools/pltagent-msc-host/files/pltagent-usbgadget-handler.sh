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
mkdir "${sysfs_usb_gadget_devpath}/functions/mass_storage.0"
ln -s -t "${sysfs_usb_gadget_devpath}/configs/c.1" "${sysfs_usb_gadget_devpath}/functions/mass_storage.0"

if [ $should_disable -eq 1 ]; then
	# Disable existing for the USB controller
	echo "" >"${sysfs_usb_gadget_devpath}/UDC" 2>/dev/null
fi
if [ $should_enable -eq 1 ]; then
	# HACK: avoid -EBUSY
	sleep 1
	# Enable new configuration for the USB controller
	echo "${udc_dev}" >"${sysfs_usb_gadget_devpath}/UDC"

	systemctl start serial-getty@ttyGS0
fi
