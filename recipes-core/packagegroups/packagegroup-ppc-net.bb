# packagegroup-plt-net

inherit packagegroup

RDEPENDS:${PN} = "\
    atftp \
    binutils \
    canutils \
    dnsmasq \
    dosfstools \
    edid-decode \
    flashrom \
    i2c-tools \
    lshw \
    mtd-utils \
    parted \
    pcsc-lite \
    picocom \
    read-edid \
    sigrok-cli \
    spitools \
    squashfs-tools \
    srecord \
    stm32flash \
    sudo \
    tftp-hpa \
    util-linux-blkid \
    vim \
"
