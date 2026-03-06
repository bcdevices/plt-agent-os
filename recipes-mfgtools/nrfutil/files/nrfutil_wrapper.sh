#!/bin/sh
# Wrapper script for nrfutil to always set log output to stdout
# and use json format for logs. This allows execution on a readonly filesystem
# and ensures that logs are visible in the console output of pltagent.
exec /opt/nrfutil/nrfutil --json --log-output stdout "$@"
