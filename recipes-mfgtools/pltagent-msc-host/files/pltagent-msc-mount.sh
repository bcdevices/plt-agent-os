#!/bin/sh
sudo /opt/pltagent/sbin/pltagent-msc-host-handler.sh mount "$1"
RETVAL=$?
if [ $RETVAL -ne 0 ]; then
	echo "$0:FAIL"
	exit 1
fi
echo "$0:PASS"
echo "PASS"
