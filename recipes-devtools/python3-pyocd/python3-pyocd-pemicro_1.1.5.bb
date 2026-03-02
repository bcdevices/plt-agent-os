# python3-pyocd-pemicro
SUMMARY = "PEMicro probe plugin for pyOCD"
HOMEPAGE ="https://github.com/pyocd/pyocd-pemicro"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f6751a0652c0033100252953bfcf00d2"

PYPI_PACKAGE = "pyocd-pemicro"
SRC_URI[sha256sum] = "fc3e7c8bfcf3acd4902d32e3c06ad87347a1cb0a6a9324d8bfda80eda78ca024"

inherit pypi python_setuptools_build_meta

DEPENDS += " python3-setuptools-scm-native"
