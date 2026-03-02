# packagegroup-agentos-base
DESCRIPTION = "AgentOS Base package group"

inherit packagegroup

RDEPENDS:${PN} += " atftp"
RDEPENDS:${PN} += " binutils"
RDEPENDS:${PN} += " canutils"
RDEPENDS:${PN} += " dnsmasq"
RDEPENDS:${PN} += " dosfstools"
RDEPENDS:${PN} += " edid-decode"
RDEPENDS:${PN} += " flashrom"
RDEPENDS:${PN} += " i2c-tools"
RDEPENDS:${PN} += " lshw"
RDEPENDS:${PN} += " mtd-utils"
RDEPENDS:${PN} += " parted"
RDEPENDS:${PN} += " pcsc-lite"
RDEPENDS:${PN} += " picocom"
RDEPENDS:${PN} += " read-edid"
RDEPENDS:${PN} += " sigrok-cli"
RDEPENDS:${PN} += " spitools"
RDEPENDS:${PN} += " squashfs-tools"
RDEPENDS:${PN} += " srecord"
RDEPENDS:${PN} += " stm32flash"
RDEPENDS:${PN} += " sudo"
RDEPENDS:${PN} += " util-linux-blkid"

RDEPENDS:${PN} += " libcamera-apps"

RDEPENDS:${PN} += " pltagent"
RDEPENDS:${PN} += " pltagent-cam"
RDEPENDS:${PN} += " pltagent-msc-host"
RDEPENDS:${PN} += " pltagent-pyprog"
RDEPENDS:${PN} += " systemd-auto-login-ttygs0"
RDEPENDS:${PN} += " uboot-splash-agentos"

RDEPENDS:${PN} += " segger-jlink-placeholder"
RDEPENDS:${PN} += " nrfutil-placeholder"
RDEPENDS:${PN} += " usbboot"

