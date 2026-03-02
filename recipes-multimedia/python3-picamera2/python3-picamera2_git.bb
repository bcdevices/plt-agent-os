# python3-picamera2_git.bb
SUMMARY = "Python interface to the Raspberry Pi camera module via libcamera"
DESCRIPTION = "This package provides a pure Python interface to the Raspberry Pi camera module via libcamera for Python 3"
HOMEPAGE = "https://github.com/raspberrypi/picamera2" 
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6541a38108b5accb25bd55a14e76086d"

SRC_URI = "git://github.com/raspberrypi/picamera2.git;protocol=https;branch=main"
SRCREV = "2082fe613a12b143836a9d44cfeecd0f78ed3351"

PV = "0.3.30+git${SRCPV}"
S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS:${PN} = " \
    python3-numbers \
    python3-ctypes \
    python3-colorzero \
    libcamera \
"

DEPENDS += "python3-pybind11"

# RDEPENDS:${PN} += "libcamera-pycamera"

COMPATIBLE_HOST = "null"
COMPATIBLE_HOST:rpi = "(.*)"
