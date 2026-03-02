#!/bin/sh

OPTIND=1
should_erase=1
should_program=1
isp_mode=0
sdp_port="/dev/ttyAMA0"
mboot_port="/dev/ttyACM0"
usb_pidvid="0x1fc9:0x0020"
spsdk_target="rt6xx"
#blhost_connection="-u rt6xx"
blhost_connection="-p ${mboot_port}"
#sdphost_connection="-u rt6xx"
sdphost_connection="-p ${sdp_port}"
fail="FAIL:$(basename $0 .sh)"

while getopts "e:p:t:i:" opt; do
	case "$opt" in
	e)
		should_erase=$OPTARG
		;;
	p)
		should_program=$OPTARG
		;;
	t)
		spsdk_target=$OPTARG
		;;
	i)
		isp_mode=$OPTARG
		;;
	esac
done

shift $((OPTIND - 1))

[ "${1:-}" = "--" ] && shift

image_path="$1"

# 0: USB Composite
# 1: UART
nxpdebugmbox -i pyocd -v ispmode -m ${isp_mode}
RETVAL=$?
if [ ${RETVAL} -ne 0 ]; then
	echo "${fail}:ispmode"
	exit 1
fi

nxpdevscan -v
RETVAL=$?
if [ ${RETVAL} -ne 0 ]; then
	echo "${fail}:nxpdevscan"
	exit 1
fi
#Port: /dev/ttyACM0
#Type: mboot device
#Port: /dev/ttyAMA0
#Type: SDP device

blhost ${blhost_connection} -v get-property current-version
#

#sdphost ${sdphost_connection} -v error-status
#RETVAL=$?
#if [ ${RETVAL} -ne 0 ]; then
#	echo "${fail}:error-status"
#	exit 1
#fi
# Status (HAB mode) = 68157696 (0x4100100) .
# Response status = 83822619 (0x4ff081b) .

#blhost ${blhost_connection} -v list-memory

#if [ ${should_erase} -eq 1 ]; then
#	blhost ${blhost_connection} -v flash-erase-region 0 0x10000
#	RETVAL=$?
#	if [ ${RETVAL} -ne 0 ]; then
#		echo "${fail}:flash-erase-region"
#		exit 1
#	fi
#fi

if [ ${should_program} -eq 1 ]; then
	#blhost -p ${blhost_connection} -v write-memory 0 ${image_path}
	#RETVAL=$?
	#if [ ${RETVAL} -ne 0 ]; then
	#	echo "${fail}:write-memory"
	#	exit 1
	#fi
	blhost ${blhost_connection} -v flash-image ${image_path}
	RETVAL=$?
	if [ ${RETVAL} -ne 0 ]; then
		echo "${fail}:flash-image"
		exit 1
	fi
fi
