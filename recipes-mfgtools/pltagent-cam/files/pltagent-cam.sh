#!/bin/sh
# Wrapper for libcamera-still with support for auto and fixed capture modes

set -e

usage() {
    echo "Usage: $0 [auto|fixed] [output_file]"
    echo "  auto         Use automatic exposure and gain (default)"
    echo "  fixed        Use predefined shutter, gain, and awbgains"
    echo "  output_file  Path to save the image (default: /tmp/capture.jpg)"
    exit 1
}

MODE=${1:-auto}
OUTFILE=${2:-/tmp/capture.jpg}

case "$MODE" in
    auto)
        echo "Capturing image in AUTO mode..."
        exec libcamera-still \
            --immediate \
            --preview 0,0,1920,1080 \
            -o "$OUTFILE"
        ;;
    fixed)
        echo "Capturing image in FIXED mode..."
        exec libcamera-still \
            --immediate \
            --shutter 1000 \
            --gain 1.723906 \
            --awbgains 2.195293,1.572533 \
            -o "$OUTFILE"
        ;;
    *)
        echo "Invalid mode: $MODE"
        usage
        ;;
esac
