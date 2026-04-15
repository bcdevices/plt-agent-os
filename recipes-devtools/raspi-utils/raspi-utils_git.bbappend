# Append file for raspi-utils recipe to add additional tools and dependencies for manufacturing image
# @TODO: This appears to be a merged change in the scarthgap LTS branch,
# should consider moving to LTS
RCONFLICTS:${PN} = "userland"
DEPENDS:append = " dtc"
PACKAGES =+ " ${PN}-raspinfo"
PACKAGES =+ " ${PN}-ovmerge"
RDEPENDS:${PN}-raspinfo += " bash"
RDEPENDS:${PN}-ovmerge += " perl"

FILES:${PN}-raspinfo += "${bindir}/raspinfo"
FILES:${PN}-ovmerge += "${bindir}/ovmerge"

FILES:${PN}:append = " \
    ${datadir}/bash-completion/completions/vcgencmd \
"

OECMAKE_TARGET_COMPILE += "\
    eeptools/all \
    otpset/all \
    overlaycheck/all \
    ovmerge/all \
    raspinfo/all \
    vcgencmd/all \
    vclog/all \
    vcmailbox/all \
"

OECMAKE_TARGET_INSTALL += "\
    eeptools/install \
    otpset/install \
    overlaycheck/install \
    ovmerge/install \
    raspinfo/install \
    vcgencmd/install \
    vclog/install \
    vcmailbox/install \
"
