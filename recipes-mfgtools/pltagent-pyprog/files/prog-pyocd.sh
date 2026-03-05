#!/bin/sh

OPTIND=1
should_erase=1
should_program=1
pyocd_pack="/opt/sample-dut-fw/nxp-MIMXRT685S_DFP-12.0.0.pack"
pyocd_target="mimxrt685s"

while getopts "e:p:t:x:" opt; do
	case "$opt" in
	e)
		should_erase=$OPTARG
		;;
	p)
		should_program=$OPTARG
		;;
	t)
		pyocd_target=$OPTARG
		;;
	x)
		pyocd_pack=$OPTARG
		;;
	esac
done

shift $((OPTIND - 1))

[ "${1:-}" = "--" ] && shift

pack_args="--pack ${pyocd_pack}"

if [ ${should_erase} -eq 1 ]; then
	pyocd erase ${pack_args} -t "${pyocd_target}" --chip $@
fi

if [ ${should_program} -eq 1 ]; then
	pyocd flash ${pack_args} -t "${pyocd_target}" $@
fi
