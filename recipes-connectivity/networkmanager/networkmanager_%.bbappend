do_install:append() {
    ln -sf /dev/null ${D}${systemd_system_unitdir}/NetworkManager-wait-online.service
}
