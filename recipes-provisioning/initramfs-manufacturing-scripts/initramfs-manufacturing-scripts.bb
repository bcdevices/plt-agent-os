DESCRIPTION = "Scripts for manufacturing image initramfs"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:append := ":${THISDIR}/files"
SRC_URI = "file://init.sh"

inherit allarch
S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"
FILES:${PN} += "init"

RDEPENDS:${PN}:append = " \
    util-linux \
    busybox \
    raspi-utils \
"

do_install() {
    install -m 0755 ${UNPACKDIR}/init.sh ${D}/init
}
