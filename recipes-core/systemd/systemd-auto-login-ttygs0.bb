# systemd-auto-login-ttygs0.bb
SUMMARY = "Enable auto-login on ttyGS0 for pltagent"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r1"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://override.conf"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install() {
    install -d ${D}${systemd_system_unitdir}/serial-getty@ttyGS0.service.d
    install -m 0644 ${S}/override.conf ${D}${systemd_system_unitdir}/serial-getty@ttyGS0.service.d/override.conf
}

FILES:${PN} += "${systemd_system_unitdir}/serial-getty@ttyGS0.service.d"
