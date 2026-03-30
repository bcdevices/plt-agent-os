SUMMARY = "A Raspberry Pi boot partition only image"
LICENSE = "CLOSED"

# Do not install any extra packages, rely only on base system files
IMAGE_INSTALL = ""
IMAGE_FSTYPES = "wic.bz2 wic.bmap"
PACKAGE_INSTALL = ""

inherit core-image

# Specify the custom WKS file to use
WKS_FILE = "rpi-boot-only.wks"

# auto_initramfs in config.txt does not work in specific versions of the Raspberry Pi 5
# firmware. Name the file initramfs_2712 to support this, but use
# initramfs initramfs_2712 followkernel in config.txt to support all versions of the firmware.
RPI_EXTRA_IMAGE_BOOT_FILES:append = " ${INITRAMFS_IMAGE_NAME}.cpio.gz;initramfs_2712"
# This ensures the kernel and dtb files are built and available as artifacts for wic
EXTRA_IMAGEDEPENDS += "virtual/kernel"

INITRAMFS_IMAGE = "initramfs-manufacturing-agentos"

