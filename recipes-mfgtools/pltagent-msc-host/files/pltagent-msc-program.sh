#!/bin/sh
/opt/pltagent/bin/prog.sh $@
RETVAL=$?
if [ $RETVAL -ne 0 ]; then
	echo "$0:FAIL"
	exit 1
fi
echo "$0:PASS"
echo "PASS"
