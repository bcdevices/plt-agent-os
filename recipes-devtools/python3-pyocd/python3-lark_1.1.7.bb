# python3-lark
SUMMARY = "Python package for a modern parsing library"
HOMEPAGE ="https://pypi.org/project/lark"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fcfbf1e2ecc0f37acbb5871aa0267500"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "lark"
SRC_URI[sha256sum] = "be7437bf1f37ab08b355f29ff2571d77d777113d0a8c4352b0c513dced6c5a1e"

DEPENDS += "python3-setuptools-scm-native python3-toml-native"
RDEPENDS:${PN}:append:class-target = " python3-misc"
RDEPENDS:${PN}:append:class-nativesdk = " python3-misc"
BBCLASSEXTEND = "native nativesdk"
