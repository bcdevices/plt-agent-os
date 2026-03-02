# python3-libusbsio
SUMMARY = "Python wrapper for NXP libusbsio binary library"
DESCRIPTION = "\
  The NXP libusbsio is a binary library for Win/Linux/MacOS systems \
  used to exercise SPI, I2C bus and GPIO pins over USBSIO interface \
  of NXP LPCLink2 and MCUlink Pro devices. \
  \
  This Python component provides a wrapper object which encapsulates \
  the binary library and exposes its API to Python applications. \
"
HOMEPAGE ="https://pypi.org/project/libusbsio"
# https://www.nxp.com/design/software/development-software/libusbsio-host-library-for-usb-enabled-mcus:LIBUSBSIO

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license/BSD-3-clause.txt;md5=1076c1c40acc679330f3d60bfea1c23b"
LIC_FILES_CHKSUM = "file://license/LICENSE-hidapi-bsd.txt;md5=146f1fc708cdb0edb71523b71655411a"
LIC_FILES_CHKSUM = "file://license/SoftwareContentRegister.txt;md5=9bcd2e15298ff8f472daf21cceb5618a"

PYPI_PACKAGE = "libusbsio"
SRC_URI[sha256sum] = "7d2e81f4aedccbe8a3c327b002c7750dd374abdf1ef6d54780728e653af9124d"

inherit pypi pkgconfig python_setuptools_build_meta

COMPATIBLE_HOST = "(aarch64.*|x86_64.*)-linux"

do_install:append() {
	if [ "${TARGET_ARCH}" != "x86_64" ]; then
	  rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/linux_x86_64/*
	fi
	if [ "${TARGET_ARCH}" != "aarch64" ]; then
	  rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/linux_aarch64/libusbsio.so
	fi
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/osx_arm64/*
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/x64/*
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/linux_armv7l/*
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/linux_i686/*
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/osx_x86_64/*
	rm -rf ${D}/usr/lib/python3.*/site-packages/libusbsio/bin/Win32/*
}

DEPENDS += " python3-setuptools-scm-native"

RDEPENDS:${PN} += " libudev"

PACKAGE_ARCH = "${TUNE_ARCH}"
