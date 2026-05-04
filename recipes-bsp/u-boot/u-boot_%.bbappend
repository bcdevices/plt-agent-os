# u-boot_%.bbappend 

# Update to v2026.04 for better RPi5 support/fixes
SRCREV = "88dc2788777babfd6322fa655df549a019aa1e69"
PV = "2026.04"
# These patches are already included in 2026.04, remove to avoid patch failures
SRC_URI:remove = "file://CVE-2024-57254.patch \
                  file://CVE-2024-57255.patch \
                  file://CVE-2024-57256.patch \
                  file://CVE-2024-57257.patch \
                  file://CVE-2024-57258-1.patch \
                  file://CVE-2024-57258-2.patch \
                  file://CVE-2024-57258-3.patch \
                  file://CVE-2024-57259.patch \
                  file://CVE-2024-42040.patch \
"
# This rpi specific patch is also included in 2026.04, remove to avoid patch failures
SRC_URI:remove:rpi = "file://0001-rpi-always-set-fdt_addr-with-firmware-provided-FDT-address.patch"

DEPENDS:append = " gnutls-native"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " file://splash.cfg file://rpi5-autoboot.cfg"
UBOOT_CONFIG_FRAGMENTS:append = " ${WORKDIR}/splash.cfg"
UBOOT_CONFIG_FRAGMENTS:append:raspberrypi5 = " ${WORKDIR}/rpi5-autoboot.cfg"
