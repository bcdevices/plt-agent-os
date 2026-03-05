# python3-sly
SUMMARY = "Python implementation of the lex and yacc tools"
HOMEPAGE ="https://pypi.org/project/sly"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=40c72247cf23e6d5397482dae055914a"

PYPI_PACKAGE = "sly"
SRC_URI[sha256sum] = "251d42015e8507158aec2164f06035df4a82b0314ce6450f457d7125e7649024"

inherit pypi pkgconfig python_setuptools_build_meta
