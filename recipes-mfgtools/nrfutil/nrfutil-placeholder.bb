# nrfutil-placeholder
SUMMARY = "Environment placeholder for Nordic nRF Util"
DESCRIPTION = "Installs dependencies and a placeholder directory for nrfutil. The binary must be manually installed to /opt/nrfutil/nrfutil."
HOMEPAGE = "https://www.nordicsemi.com/Products/Development-tools/nRF-Util"
# nRF-Util is proprietary, this placeholder is not.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "https://files.nordicsemi.com/artifactory/swtools/external/nrfutil/executables/aarch64-unknown-linux-gnu/nrfutil \
           file://.nrfutil"
SRC_URI[sha256sum] = "9df546a5a7e8c82b896f70db4424dba08cbca32c1aedd2affdde5782b782d81b"
#S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

DEPENDS += "pltagent"

RDEPENDS:${PN} = " \
  libudev \
"

CONFFILES:${PN} = "${sysconfdir}/udev/rules.d/*rules"

do_install() {
    install -d ${D}/opt/nrfutil
    install -m 0755 ${S}/nrfutil ${D}/opt/nrfutil/nrfutil
    install -d ${D}${bindir}
    ln -s /opt/nrfutil/nrfutil ${D}${bindir}/nrfutil
    # Install udev rules
    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -m 0644 ${S}/.nrfutil/share/nrfutil-device/udev/rules.d/*.rules ${D}${nonarch_base_libdir}/udev/rules.d/
    # Install nrfutil runtime directory with correct permissions for pltagent user
    install -d ${D}${datadir}/nrfutil-runtime/
    cp -r ${S}/.nrfutil/. ${D}${datadir}/nrfutil-runtime/
    #chown -R pltagent:adm ${D}${localstatedir}/lib/pltagent/.nrfutil
    chmod -R 0755 ${D}${datadir}/nrfutil-runtime/
    chmod 0644 ${D}${datadir}/nrfutil-runtime/installed/nrfutil-aarch64-unknown-linux-gnu/*
    chmod 0644 ${D}${datadir}/nrfutil-runtime/installed/nrfutil-aarch64-unknown-linux-gnu/*
    chmod 0644 ${D}${datadir}/nrfutil-runtime/installed/nrfutil-device-aarch64-unknown-linux-gnu/*
    chmod 0644 ${D}${datadir}/nrfutil-runtime/registry/cache/files.nordicsemi.com-18166308991710433794/*
    chmod 0644 ${D}${datadir}/nrfutil-runtime/share/nrfutil-device/licenses.json
    chmod 0644 ${D}${datadir}/nrfutil-runtime/share/nrfutil-device/udev/rules.d/*.rules
    chmod 0644 ${D}${datadir}/nrfutil-runtime/share/nrfutil-core/LICENSE
}

pkg_postinst:${PN}() {
    mkdir -p $D${localstatedir}/lib/pltagent/.nrfutil
    cp -r $D${datadir}/nrfutil-runtime/. \
        $D${localstatedir}/lib/pltagent/.nrfutil/
}
#chown -R pltagent:adm $D${localstatedir}/lib/pltagent/.nrfutil

FILES:${PN} = " \
  /opt/nrfutil \
  ${bindir}/nrfutil \
  ${nonarch_base_libdir}/udev/rules.d/*rules \
  ${datadir}/nrfutil-runtime/* \
"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

# All runtime dependencies the real nrfutil binary expects
RDEPENDS:${PN} += "libusb1 libudev libedit"
#RRECOMMENDS:${PN} = "segger-jlink"

PACKAGE_ARCH = "${TUNE_ARCH}"
RCONFLICTS:${PN} = "nrfutil"
