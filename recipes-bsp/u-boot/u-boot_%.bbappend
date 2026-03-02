# u-boot_%.bbappend 

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI:append = " file://splash.cfg file://rpi5-autoboot.cfg"
UBOOT_CONFIG_FRAGMENTS:append = " ${WORKDIR}/splash.cfg"
UBOOT_CONFIG_FRAGMENTS:append:raspberrypi5 = " ${WORKDIR}/rpi5-autoboot.cfg"

do_configure:append:raspberrypi5() {
    # Remove any existing CONFIG_BOOTDELAY= lines
    sed -i '/^CONFIG_BOOTDELAY=/d' "${B}/.config"
    # Disable U-Boot interrupt timeout to avoid
    # boot issues without a connected debug UART
    # This is a known U-Boot issue discussed in:
    # https://bugzilla.opensuse.org/show_bug.cgi?id=1251192
    # https://lists.denx.de/pipermail/u-boot/2025-January/576305.html
    echo "CONFIG_BOOTDELAY=-2" >> ${B}/.config
}
