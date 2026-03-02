# sudo-lib.bbappend
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += " file://pltagent"
do_install:append() {
    install -D -m 0440 ${UNPACKDIR}/pltagent ${D}${sysconfdir}/sudoers.d/pltagent
}
