# packagegroup-ppc-sbc
DESCRIPTION = "PPC SBC package group"
LICENSE = "MIT"

inherit packagegroup

RDEPENDS:${PN} = "\
    pltagent \
    pltagent-msc-host \
"

RDEPENDS:${PN} += " segger-jlink-placeholder"
RDEPENDS:${PN} += " nrfutil-placeholder"
RDEPENDS:${PN} += " usbboot"
