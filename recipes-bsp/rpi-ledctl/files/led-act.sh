#!/bin/sh
# SPDX-License-Identifier: MIT
# Usage: led-act.sh <on|off>

echo none > /sys/class/leds/ACT/trigger

if [ "$1" = "on" ]; then
    echo 1 > /sys/class/leds/ACT/brightness
else
    echo 0 > /sys/class/leds/ACT/brightness
fi
