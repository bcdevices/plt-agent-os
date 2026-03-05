# python3-deepmerge
SUMMARY = "tools to handle merging of nested data structures in python."
HOMEPAGE ="https://pypi.org/project/deepmerge"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5461efe2d19ce359c7d72d7be3c05e1c"

PYPI_PACKAGE = "deepmerge"
SRC_URI[sha256sum] = "4c27a0db5de285e1a7ceac7dbc1531deaa556b627dea4900c8244581ecdfea2d"

inherit pypi pkgconfig python_setuptools_build_meta

DEPENDS += " python3-setuptools-scm-native"
