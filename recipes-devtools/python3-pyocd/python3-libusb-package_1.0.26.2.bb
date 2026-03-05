# python3-libusb-package
SUMMARY = "Package containing libusb so it can be installed via Python package managers"
HOMEPAGE ="https://pypi.org/project/libusb-package"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e23fadd6ceef8c618fc1c65191d846fa"
LICENSE = "Apache-2.0"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "libusb-package"
# version 1.0.26.2 archive is not published on PyPi; use git instead
PYPI_SRC_URI = "git://github.com/pyocd/libusb-package;protocol=https;branch=main;subdir=libusb-package-${PV}"
SRCREV = "7a62b644f3cc075a37e8d2e255ee9941ce2ca752"

DEPENDS += " python3-setuptools-scm-native"
DEPENDS += " python3-tomli-native"
