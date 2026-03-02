# pltagent-msc-host
DESCRIPTION = "PLT Agent USB Mass Storage Class host."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "\
  file://pltagent-msc-host-handler.sh \
  file://pltagent-msc-host-handler-ext2.sh \
  file://pltagent-msc-mount.sh \
  file://pltagent-msc-present.sh \
  file://pltagent-msc-profile.sh \
  file://pltagent-msc-program.sh \
  file://pltagent-prompt.bashrc \
  file://pltagent-usbgadget-handler.sh \
"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

FILES:${PN} += " /opt/pltagent/bin/pltagent-msc-mount"
FILES:${PN} += " /opt/pltagent/bin/pltagent-msc-mount"
FILES:${PN} += " /opt/pltagent/bin/pltagent-msc-present"
FILES:${PN} += " /opt/pltagent/bin/pltagent-msc-program"
FILES:${PN} += " /opt/pltagent/bin/pltagent-msc-profile.sh"
FILES:${PN} += " /opt/pltagent/etc/pltagent-prompt.bashrc"
FILES:${PN} += " /opt/pltagent/sbin/pltagent-msc-host-handler.sh"
FILES:${PN} += " /opt/pltagent/sbin/pltagent-msc-host-handler-ext2.sh"
FILES:${PN} += " /opt/pltagent/sbin/pltagent-usbgadget-handler.sh"

DEPENDS += " packagegroup-base"
DEPENDS += " pltagent"
#DEPENDS += " wic-tools"

RDEPENDS:${PN} += " bash"
RDEPENDS:${PN} += " coreutils"
RDEPENDS:${PN} += " dosfstools"
RDEPENDS:${PN} += " e2fsprogs-e2fsck"
RDEPENDS:${PN} += " e2fsprogs-mke2fs"
RDEPENDS:${PN} += " parted"
#RDEPENDS:${PN} += " pltagent-prog"
RDEPENDS:${PN} += " util-linux-losetup"
RDEPENDS:${PN} += " sudo"

RRECOMMENDS:${PN} += " kernel-module-gadgetfs"
RRECOMMENDS:${PN} += " kernel-module-g-file-storage"
RRECOMMENDS:${PN} += " kernel-module-g-mass-storage"

do_install () {
        install -m 0755 -d "${D}/opt/pltagent/bin"
        install -m 0755 "${S}/pltagent-msc-mount.sh" "${D}/opt/pltagent/bin/pltagent-msc-mount"
        install -m 0755 "${S}/pltagent-msc-present.sh" "${D}/opt/pltagent/bin/pltagent-msc-present"
        install -m 0755 "${S}/pltagent-msc-program.sh" "${D}/opt/pltagent/bin/pltagent-msc-program"
        install -m 0755 -d "${D}/opt/pltagent/etc"
        install -m 0755 "${S}/pltagent-prompt.bashrc" "${D}/opt/pltagent/etc/pltagent-prompt.bashrc"
        install -m 0755 -d "${D}/opt/pltagent/sbin"
        install -m 0755 "${S}/pltagent-msc-host-handler.sh" "${D}/opt/pltagent/sbin"
	install -m 0755 "${S}/pltagent-msc-host-handler-ext2.sh" "${D}/opt/pltagent/sbin/pltagent-msc-host-handler-ext2.sh"
        install -m 0755 "${S}/pltagent-usbgadget-handler.sh" "${D}/opt/pltagent/sbin"
        install -m 0755 -d "${D}/etc/profile.d"
        install -m 0644 "${S}/pltagent-msc-profile.sh" "${D}/etc/profile.d/pltagent-msc.sh"
}

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
SYSTEMD_SERVICE:${PN} = "pltagent-msc.service"

SRC_URI:append = " file://pltagent-msc.service "

# TODO: add pltagent to plugdev group

do_install:append() {
        install -d ${D}/${systemd_unitdir}/system
        install -m 0644 ${S}/pltagent-msc.service ${D}/${systemd_unitdir}/system
}

FILES:${PN} += "${systemd_unitdir}/system"
