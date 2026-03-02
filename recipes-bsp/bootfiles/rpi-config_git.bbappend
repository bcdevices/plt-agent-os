
# Allow console access on GPIO 14/15 for RPi5
# The default console is routed to an inaccessible header
do_deploy:append() {
    # UART support
    if [ "${ENABLE_UART}" = "1" ]; then
        echo "dtoverlay=uart0" >>$CONFIG
        echo "dtparam=uart0_console" >>$CONFIG
    fi
}
