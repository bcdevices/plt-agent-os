# stm8flash_git.bb
SUMMARY = "STM8 SWIM flashing tool for ST-Link"
DESCRIPTION = "Command-line utility to flash STM8 microcontrollers using ST-Link and SWIM"
HOMEPAGE = "https://github.com/vdudouyt/stm8flash"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE-CHANGE;md5=de93032523a5f495782678b4dcdaf49a"
SRC_URI = "git://github.com/vdudouyt/stm8flash.git;protocol=https;branch=master"
SRCREV = "0d84fad229a23813fcf3ef69ba2693f1a53c55fd"

PV = "1.0+git${SRCPV}"
S = "${WORKDIR}/git"

DEPENDS = "libusb1"

inherit pkgconfig

do_compile() {
    # Bypass Makefile to apply Yocto LDFLAGS
    USB_CFLAGS="$(pkg-config --cflags libusb-1.0)"
    USB_LIBS="$(pkg-config --libs libusb-1.0)"

    for f in ${S}/*.c; do
        base=$(basename $f .c)
        ${CC} ${CFLAGS} ${CPPFLAGS} ${USB_CFLAGS} -c $f -o $base.o
    done

    ${CC} ${LDFLAGS} -o stm8flash *.o ${USB_LIBS}
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 stm8flash ${D}${bindir}/stm8flash
}
