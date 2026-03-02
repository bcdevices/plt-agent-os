# segger-jlink-placeholder
SUMMARY = "Environment placeholder for SEGGER J-Link Software and Documentation Pack"
DESCRIPTION = "Installs dependencies and a placeholder directory for jlink. The binary must be manually installed to /opt/SEGGER/JLink"
HOMEPAGE = "https://www.segger.com/downloads/jlink/"

# SEGGER J-Link Software and Documentation Pack is proprietary, this placeholder is not.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

#S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

SRC_URI = "https://www.segger.com/downloads/jlink/JLink_Linux_V916a_arm64.tgz"
SRC_URI[sha256sum] = "3dfc155c9e496d2998fd0d159caa1593e3d2f8360bd71d71b60693c1cac50b59"
FETCHCMD_wget = "/usr/bin/env wget -t 2 -T 100 --post-data='accept_license_agreement=accepted&non_emb_ctr=confirmed'"

CONFFILES:${PN} = "${sysconfdir}/udev/rules.d/99-jlink.rules"

FILES:${PN} = " \
  /opt/SEGGER/JLink \
  /opt/SEGGER/JLink_V916a \
  ${nonarch_base_libdir}/udev/rules.d/99-jlink.rules \
  ${bindir} \
"

RDEPENDS:${PN} = " \
  glibc \
  libudev \
"

#libedit
#libx11
#fontconfig
#freetype
#libxrender
#libxfixes
# libxcursor
# libsm

# Avoid checking ldflags (pre-built binary)
# Avoid checking dev-so symlinks (pre-packaged libs)
INSANE_SKIP:${PN} += "ldflags dev-so"

do_install() {
    SEGGER_SRC_DIR=${S}/$(ls "${S}/" | head -n 1)
    SEGGER_INSTALL_DIR=${D}/opt/SEGGER/JLink_V916a

    install -d ${SEGGER_INSTALL_DIR}

    # Installs everything
    # cp -r --no-dereference --preserve=mode,links ${SEGGER_SRC_DIR}/* ${SEGGER_INSTALL_DIR}

    # Avoid installing everything from the JLink package as this requires a bunch of GUI dependencies
    #install -m 0755 ${SEGGER_SRC_DIR}/JFlashExe ${SEGGER_INSTALL_DIR}/
    install -m 0755 ${SEGGER_SRC_DIR}/JLinkExe ${SEGGER_INSTALL_DIR}/
    # Shared libs
    install -m 0755 ${SEGGER_SRC_DIR}/libjlinkarm.so ${SEGGER_INSTALL_DIR}/
    install -m 0755 ${SEGGER_SRC_DIR}/libjlinkarm.so.9 ${SEGGER_INSTALL_DIR}/
    install -m 0755 ${SEGGER_SRC_DIR}/libjlinkarm.so.9.16.1 ${SEGGER_INSTALL_DIR}/
    # Udev rules for user access
    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -m 0644 ${SEGGER_SRC_DIR}/99-jlink.rules ${D}${nonarch_base_libdir}/udev/rules.d/99-jlink.rules

    # Segger binaries
    install -d ${D}${bindir}
    # @TODO: determine what of the below is actually required
    # change to just be JLink_V916a instead of hardcoded
    ln -s /opt/SEGGER/JLink_V916a ${D}/opt/SEGGER/JLink
    #ln -s /opt/SEGGER/JLink_V916a/DDConditionerExe ${D}${bindir}/DDConditionerExe
    #ln -s /opt/SEGGER/JLink_V916a/DevProExe ${D}${bindir}/DevProExe
    #ln -s /opt/SEGGER/JLink_V916a/JFlashExe ${D}${bindir}/JFlash
    #ln -s /opt/SEGGER/JLink_V916a/JFlashExe ${D}${bindir}/JFlashExe
    #ln -s /opt/SEGGER/JLink_V916a/JFlashLiteExe ${D}${bindir}/JFlashLite
    #ln -s /opt/SEGGER/JLink_V916a/JFlashLiteExe ${D}${bindir}/JFlashLiteExe
    #ln -s /opt/SEGGER/JLink_V916a/JFlashSPICLExe ${D}${bindir}/JFlashSPI_CL
    #ln -s /opt/SEGGER/JLink_V916a/JFlashSPICLExe ${D}${bindir}/JFlashSPICLExe
    #ln -s /opt/SEGGER/JLink_V916a/JFlashSPIExe ${D}${bindir}/JFlashSPI
    #ln -s /opt/SEGGER/JLink_V916a/JFlashSPIExe ${D}${bindir}/JFlashSPIExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkConfigExe ${D}${bindir}/JLinkConfig
    #ln -s /opt/SEGGER/JLink_V916a/JLinkConfigExe ${D}${bindir}/JLinkConfigExe
    ln -s /opt/SEGGER/JLink_V916a/JLinkExe ${D}${bindir}/JLinkExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkGDBServerCLExe ${D}${bindir}/JLinkGDBServer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkGDBServerCLExe ${D}${bindir}/JLinkGDBServerCLExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkGDBServerExe ${D}${bindir}/JLinkGDBServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkGUIServerExe ${D}${bindir}/JLinkGUIServer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkGUIServerExe ${D}${bindir}/JLinkGUIServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkLicenseManagerExe ${D}${bindir}/JLinkLicenseManager
    #ln -s /opt/SEGGER/JLink_V916a/JLinkLicenseManagerExe ${D}${bindir}/JLinkLicenseManagerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRegistrationExe ${D}${bindir}/JLinkRegistration
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRegistrationExe ${D}${bindir}/JLinkRegistrationExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRemoteServerCLExe ${D}${bindir}/JLinkRemoteServer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRemoteServerCLExe ${D}${bindir}/JLinkRemoteServerCLExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRemoteServerExe ${D}${bindir}/JLinkRemoteServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRTTClientExe ${D}${bindir}/JLinkRTTClient
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRTTLoggerExe ${D}${bindir}/JLinkRTTLogger
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRTTLoggerExe ${D}${bindir}/JLinkRTTLoggerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRTTViewerExe ${D}${bindir}/JLinkRTTViewer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkRTTViewerExe ${D}${bindir}/JLinkRTTViewerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkServerExe ${D}${bindir}/JLinkServer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkServerExe ${D}${bindir}/JLinkServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkSTM32Exe ${D}${bindir}/JLinkSTM32
    #ln -s /opt/SEGGER/JLink_V916a/JLinkSTM32Exe ${D}${bindir}/JLinkSTM32Exe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkSWOViewerExe ${D}${bindir}/JLinkSWOViewer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkSWOViewerCLExe ${D}${bindir}/JLinkSWOViewerCLExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkSWOViewerExe ${D}${bindir}/JLinkSWOViewerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkUSBWebServerExe ${D}${bindir}/JLinkUSBWebServer
    #ln -s /opt/SEGGER/JLink_V916a/JLinkUSBWebServerExe ${D}${bindir}/JLinkUSBWebServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JLinkXVCDServerExe ${D}${bindir}/JLinkXVCDServerExe
    #ln -s /opt/SEGGER/JLink_V916a/JMemExe ${D}${bindir}/JMem
    #ln -s /opt/SEGGER/JLink_V916a/JMemExe ${D}${bindir}/JMemExe
    #ln -s /opt/SEGGER/JLink_V916a/JRunExe ${D}${bindir}/JRun
    #ln -s /opt/SEGGER/JLink_V916a/JRunExe ${D}${bindir}/JRunExe
    #ln -s /opt/SEGGER/JLink_V916a/JScopeExe ${D}${bindir}/JScopeExe
    ln -s /opt/SEGGER/JLink_V916a/JTAGLoadExe ${D}${bindir}/JTAGLoadExe
}

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

#RRECOMMENDS:${PN} = " dpkg"
#RDEPENDS:${PN} = " dpkg"

RCONFLICTS:${PN} = "segger-jlink"
