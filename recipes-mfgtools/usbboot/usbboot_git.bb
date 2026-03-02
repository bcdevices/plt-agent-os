# usbboot_git.bb
SUMMARY = "RPi usbboot utility for mass storage mode"
HOMEPAGE = "https://github.com/raspberrypi/usbboot"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"
SRC_URI = "git://github.com/raspberrypi/usbboot.git;protocol=https;branch=master"
SRCREV = "aeb8924aa253ed4e44fdabf4052ec18f70b8d685"

S = "${WORKDIR}/git"

DEPENDS += "libusb1 pkgconfig"

inherit pkgconfig

do_compile() {
    oe_runmake CC_FOR_BUILD="${BUILD_CC}"
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 rpiboot ${D}${bindir}/rpiboot
}
