SUMMARY = "PLT Agent"
DESCRIPTION = "\
    Blue Clover's Production Line Tool (PLT) performs \
    firmware programming and testing in a production environment. \
    \
    To interact with the Device-under-Test (DUT) during In-Circuit \
    Testing (ICT) in production, a custom Pogo-Pin Cassette (PPC) \
    can be inserted in the ICT Chassis, with Pogo-Pins lining up \
    with the test points of the DUT circuit board. \
    \
    To have the DUT interact with custom software during production \
    testing, this PPC can be equipped with a Single-Board Computer \
    (SBC) loaded with custom firmware. \
    \
    If you want to have the PLT control the SBC in the PPC, \
    include this package in the firmware image for the SBC. \
"
HOMEPAGE = "https://bcdevices.com/pages/production-line-tool"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://pltagent-profile"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

inherit allarch
inherit useradd

USERADD_PACKAGES = "${PN}"
# set empty password, assumes 'IMAGE_FEATURES:append = " allow-empty-password"'
USERADD_PARAM:${PN} = "-m -g ${BPN} -G adm -r -d ${localstatedir}/lib/${BPN} \
                       -s /bin/sh -c 'pltagent account' -p '' ${BPN}"
GROUPADD_PARAM:${PN} = "-r ${BPN}"
GROUPMEMS_PARAM:${PN} = "-g video -a pltagent; -g audio -a pltagent; -g plugdev -a pltagent"
RDEPENDS:${PN} += " util-linux-agetty atftp dnsmasq usbip-tools"

do_install () {
	install -d 755 ${D}${localstatedir}/lib/${BPN}
	install -m 644 ${S}/${BPN}-profile ${D}${localstatedir}/lib/${BPN}/.profile
	chown -R pltagent:adm ${D}${localstatedir}/lib/${BPN}
}

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
SYSTEMD_SERVICE:${PN} = "pltagent.service"

SRC_URI:append = " file://pltagent.service "
FILES:${PN} += "${systemd_unitdir}/system/pltagent.service"

do_install:append() {
        install -d ${D}/${systemd_unitdir}/system
        install -m 0644 ${S}/pltagent.service ${D}/${systemd_unitdir}/system
}
