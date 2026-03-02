SUMMARY = "U-Boot splash bitmap for AgentOS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = "file://splash.bmp"

UNPACKDIR = "${WORKDIR}/sources"
S = "${UNPACKDIR}"
inherit allarch

do_install() {
    install -d ${D}/boot
    install -m 0644 ${UNPACKDIR}/splash.bmp ${D}/boot/splash.bmp
}

FILES:${PN} = "/boot/splash.bmp"
