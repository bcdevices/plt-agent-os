# stm32cubeprg
DESCRIPTION = "STM32 Cube Programmer."
LICENSE = "CLOSED"
HOMEPAGE = "https://www.st.com/en/development-tools/stm32cubeprog.html"
SRC_URI = "file://stm32cubeprogrammer_2.21.0_arm64.deb;subdir=${BPN}-${PV}"

inherit bin_package

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_SYSROOT_STRIP = "1"

INSANE_SKIP:${PN} = "ldflags dev-so already-stripped arch dev-deps file-rdeps"
INSANE_SKIP:${PN}-dev = "ldflags dev-elf file-rdeps"

# Keep all .so files in the main package — they are prebuilt runtime libs, not dev symlinks.
FILES:${PN}-dev = ""
FILES:${PN} += "${libdir}/*.so"

# The .deb installs duplicate shared libs into /usr/bin/ — remove them
# so the shlib resolver doesn't see two providers for the same lib.
do_install:append() {
    rm -f ${D}${bindir}/libicu*.so* ${D}${bindir}/libusb*.so*
    # Remove libs that conflict with system packages
    rm -f ${D}${libdir}/libcrypto.so* ${D}${libdir}/libssl.so*
}

DEPENDS = "glibc libgcc gcc-runtime"

RDEPENDS:${PN} += "bash"
RDEPENDS:${PN} += "libudev"
RDEPENDS:${PN} += "glibc"
RDEPENDS:${PN} += "libstdc++"
RDEPENDS:${PN} += "zlib"
RDEPENDS:${PN} += "libpcre2-16"

FILES:${PN} += " /opt/stm32cubeprg/*"

# Build for generic aarch64, not specific arch like cortex-a52
PACKAGE_ARCH = "${TUNE_ARCH}"
