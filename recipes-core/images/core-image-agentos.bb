# meta-agentos/recipes-core/images/core-image-agentos.bb

SUMMARY = "Minimal image for AgentOS SBC in PPC"
LICENSE = "MIT"

inherit core-image

IMAGE_FEATURES:append = " allow-empty-password"
IMAGE_FEATURES:append = " allow-root-login"
IMAGE_FEATURES:append = " empty-root-password"
IMAGE_FEATURES:append = " read-only-rootfs"
#IMAGE_FEATURES += "ssh-server-openssh"

# Base on core-image-minimal and add extras here
IMAGE_INSTALL += " \
    openssh \
    i2c-tools \
    usbutils \
    vim-tiny \
    less \
"

IMAGE_INSTALL:append = " packagegroup-agentos-base"
IMAGE_INSTALL:append = " packagegroup-bsp-per101"
IMAGE_INSTALL:append = " packagegroup-modbus"

IMAGE_INSTALL:append = " raspi-utils"
IMAGE_INSTALL:append:raspberrypi4 = " rpi-eeprom"
IMAGE_INSTALL:append:raspberrypi4-64 = " rpi-eeprom"
IMAGE_INSTALL:append:raspberrypi5 = " rpi-eeprom"

WKS_FILE = "core-image-agentos.wks"

# Create an empty data directory in the rootfs for runtime use by AgentOS
ROOTFS_POSTINSTALL_COMMAND:append = " create_data_dir;"
create_data_dir() {
    install -d ${IMAGE_ROOTFS}/data
}
