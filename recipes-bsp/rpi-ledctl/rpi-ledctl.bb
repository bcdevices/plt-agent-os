SUMMARY = "Simple scripts to control ACT and PWR LEDs on Raspberry Pi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://led-act.sh;md5=cdaf231ddfe6bb671b692af3df5462cb"
SRC_URI = "file://led-act.sh \
           file://led-pwr.sh"
inherit allarch
S = "${WORKDIR}"
do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/led-act.sh ${D}${bindir}/led-act
    install -m 0755 ${S}/led-pwr.sh ${D}${bindir}/led-pwr
}
