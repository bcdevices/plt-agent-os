SUMMARY = "PLT Agent CAM"
HOMEPAGE = "https://bcdevices.com/pages/production-line-tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://pltagent-cam.sh"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

inherit allarch

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/pltagent-cam.sh ${D}${bindir}/pltagent-cam
}

#RDEPENDS:${PN} += " util-linux-agetty"

