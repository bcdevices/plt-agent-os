# python3-pylink-square
SUMMARY = "Python interface for SEGGER J-Link"
HOMEPAGE ="https://pypi.org/project/pylink-square"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=834c4b2cb72db13143b58bebff15bafb"
LICENSE = "Apache-2.0"

PYPI_PACKAGE = "pylink-square"
SRC_URI[sha256sum] = "736cf9e0272e87cdca1189f632f8d511e8f079c0ab96252fc331189490253ac3"

inherit pypi setuptools3

DEPENDS += " python3-six-native"

RDEPENDS:${PN} += " python3-future"
RDEPENDS:${PN} += " python3-setuptools"

# To use SEGGER J-Link runtime library in pylink-square:
# ```
# export LD_LIBRARY_PATH="/opt/SEGGER/JLink:$LD_LIBRARY_PATH"
# ```
#RRECOMMENDS:${PN} += " segger-jlink"
