#!/bin/sh

OPTIND=1

jlink_device="MIMXRT685S_M33"
jlink_if="SWD"
jlink_speed=4000
jlink_command_file="$(mktemp /tmp/pltagent.jlink-commands.XXXXXX.cmd)"
verbose=0

while getopts "t:i:s:" opt; do
	case "$opt" in
	t)
		jlink_device=$OPTARG
		;;
	i)
		jlink_if=$OPTARG
		;;
	s)
		jlink_speed=$OPTARG
		;;
	esac
done

shift $((OPTIND - 1))

[ "${1:-}" = "--" ] && shift

fw_path="$1"

echo "r" >"${jlink_command_file}"
echo "h" >>"${jlink_command_file}"
echo "loadfile ${fw_path}" >>"${jlink_command_file}"
echo "r" >>"${jlink_command_file}"
echo "q" >>"${jlink_command_file}"

JLinkExe \
	-Device "${jlink_device}" \
	-If "${jlink_if}" \
	-Speed ${jlink_speed} \
	-AutoConnect 1 \
	-CommandFile "${jlink_command_file}"
