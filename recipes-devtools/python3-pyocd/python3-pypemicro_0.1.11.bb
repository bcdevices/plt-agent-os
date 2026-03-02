# python3-pypemicro
SUMMARY = "Python PEMicro debug probes support"
DESCRIPTION = "\
  This is simple package that provides Python interface for \
  PEMicro debug probes precompiled libraries. The package \
  provides most of functionality of the PEMicro libraries \
  and their debug probes. \
  \
  The package is tested only with Multilink/FX and Cyclone/FX \
  probes on NXP ARM microcontrollers. \
"
HOMEPAGE = "https://github.com/nxp-mcuxpresso/pypemicro"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f6751a0652c0033100252953bfcf00d2"
PYPI_PACKAGE = "pypemicro"
SRC_URI[sha256sum] = "284d3ce6ef7220fb2e12be3518d5b01c59ba2801e082fa86a8ff428464682c4d"

inherit pypi python_setuptools_build_meta

do_install:append() {
	rm -rf ${D}/usr/lib/python3.*/site-packages/pypemicro/libs/MacOS
	rm -rf ${D}/usr/lib/python3.*/site-packages/pypemicro/libs/Linux
	rm -rf ${D}/usr/lib/python3.*/site-packages/pypemicro/libs/Windows
}
