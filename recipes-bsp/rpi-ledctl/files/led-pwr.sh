#!/bin/sh
# SPDX-License-Identifier: MIT
# Usage: led-pwr.sh <on|off>

echo none > /sys/class/leds/PWR/trigger

if [ "$1" = "on" ]; then
    echo 1 > /sys/class/leds/PWR/brightness
else
    echo 0 > /sys/class/leds/PWR/brightness
fi
