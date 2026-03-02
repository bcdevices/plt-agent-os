# python3-capstone
SUMMARY = "Capstone disassembly enginde"
HOMEPAGE ="http://www.capstone-engine.org/"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=1cfbff4f40612b0144e498a47c91499c"
LICENSE = "BSD-3-Clause"

PYPI_PACKAGE = "capstone"
SRC_URI[sha256sum] = "2842913092c9b69fd903744bc1b87488e1451625460baac173056e1808ec1c66"

inherit pypi setuptools3

do_install:append() {
	rm -f ${D}/usr/lib/python3.*/site-packages/capstone/lib/libcapstone.a
}
