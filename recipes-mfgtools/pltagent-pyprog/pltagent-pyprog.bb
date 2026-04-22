# pltagent-pyprog
DESCRIPTION = "PLT Agent Python Helpers."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "\
  file://prog-nrf9160-modem.py \
  file://61-pltagent-prog.rules \
  file://prog-pyprog-profile.sh \
  file://nrf52_flash.py \
"

S = "${WORKDIR}"

RDEPENDS:${PN} += " packagegroup-base"
RDEPENDS:${PN} += " pltagent"
RDEPENDS:${PN} += " bash"
RDEPENDS:${PN} += " python3"

do_install () {
        install -m 0755 -d ${D}/opt/pltagent/bin
        install -m 0755 ${S}/prog-nrf9160-modem.py ${D}/opt/pltagent/bin
        install -m 0755 ${S}/nrf52_flash.py ${D}/opt/pltagent/bin
        install -m 0755 -d "${D}/etc/profile.d"
        install -m 0644 ${S}/prog-pyprog-profile.sh ${D}/etc/profile.d
        install -d ${D}/etc/udev/rules.d
        install -m 0644 ${S}/61-pltagent-prog.rules ${D}/etc/udev/rules.d
}

FILES:${PN} += " /opt/pltagent/bin/prog-nrf9160-modem.py"
FILES:${PN} += " /opt/pltagent/bin/nrf52_flash.py"
FILES:${PN} += " /opt/pltagent/bin/pltagent-pyprog-profile.sh"
FILES:${PN} += "/etc/udev/rules.d"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system plugdev"
