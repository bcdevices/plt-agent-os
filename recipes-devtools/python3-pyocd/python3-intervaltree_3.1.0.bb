# python3-intervaltree
SUMMARY = "Editable interval tree data structure for Python 2 and 3"
HOMEPAGE ="https://pypi.org/project/intervaltree"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"
LICENSE = "Apache-2.0"

PYPI_PACKAGE = "intervaltree"
SRC_URI[sha256sum] = "902b1b88936918f9b2a19e0e5eb7ccb430ae45cde4f39ea4b36932920d33952d"

inherit pypi setuptools3

RDEPENDS:${PN} += " python3-sortedcontainers"
