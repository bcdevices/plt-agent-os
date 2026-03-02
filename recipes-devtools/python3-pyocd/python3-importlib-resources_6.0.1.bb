SUMMARY = "Read resources from Python packages"
HOMEPAGE = "https://pypi.org/project/importlib-resources/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "importlib_resources"
UPSTREAM_CHECK_REGEX = "/importlib-resources/(?P<pver>(\d+[\.\-_]*)+)/"

SRC_URI[sha256sum] = "4359457e42708462b9626a04657c6208ad799ceb41e5c58c57ffa0e6a098a5d4"

S = "${WORKDIR}/importlib_resources-${PV}"

DEPENDS += "python3-setuptools-scm-native python3-toml-native"
#RDEPENDS:${PN} += "python3-zipp python3-pathlib2"
RDEPENDS:${PN}:append:class-target = " python3-misc"
RDEPENDS:${PN}:append:class-nativesdk = " python3-misc"

BBCLASSEXTEND = "native nativesdk"
