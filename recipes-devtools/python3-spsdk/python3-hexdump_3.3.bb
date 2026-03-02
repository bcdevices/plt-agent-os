# python3-hexdump
SUMMARY = "Recipe to embedded the Python PiP Package hexdump"
HOMEPAGE ="https://pypi.org/project/hexdump"

LICENSE = "PD"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/PD;md5=b3597d12946881e13cb3b548d1173851"

PYPI_PACKAGE = "hexdump"
PYPI_PACKAGE_EXT = "zip"
PYPI_SRC_URI:append = ";subdir=hexdump-${PV}"
SRC_URI[sha256sum] = "d781a43b0c16ace3f9366aade73e8ad3a7bd5137d58f0b45ab2d3f54876f20db"

inherit pypi setuptools3

do_install:append() {
	rm -rf ${D}/usr/data
}
