SUMMARY = "Initramfs to expose SD card for flashing using a PLT"

# TODO: Should this be open source?
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
# Remove default features to keep image small
IMAGE_FEATURES = ""
# Remove additional language support to keep image small
IMAGE_LINGUAS = ""
INITRAMFS_FSTYPE = "cpio.gz"

# Some BSP's use IMAGE_FSTYPES:<machine override> which would override
# the above variable, so we need to set it here as well with anon python
python () {
    d.setVar("IMAGE_FSTYPES", d.getVar("INITRAMFS_FSTYPES"))
}

inherit core-image

PACKAGE_INSTALL:append = " \
    initramfs-manufacturing-scripts \
"
IMAGE_FEATURES:append = " \
    allow-empty-password \
    allow-root-login \
    empty-root-password \
"

PACKAGE_EXCLUDE:append = " busybox-syslog"

IMAGE_NAME_SUFFIX = ""
INITRAMFS_MAXSIZE = "250000"
IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
