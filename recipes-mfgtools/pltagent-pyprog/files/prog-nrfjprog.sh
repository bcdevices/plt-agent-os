#!/bin/bash

PATH="/opt/nrf-command-line-tools/bin:$PATH"
export PATH="/opt/nrf-command-line-tools/bin:$PATH"

OPTIND=1
should_erase=1
should_program=1
pyocd_pack="/opt/sample-dut-fw/nxp-MIMXRT685S_DFP-12.0.0.pack"
nrfjprog_family="UNKNOWN"
# --family NRF51
# --family NRF52
# --family NRF53
# --family NRF91

while getopts "e:p:t:x:" opt; do
	case "$opt" in
	e)
		should_erase=$OPTARG
		;;
	p)
		should_program=$OPTARG
		;;
	f)
		nrfjprog_family=$OPTARG
		;;
	esac
done

shift $((OPTIND - 1))

[ "${1:-}" = "--" ] && shift

common_args=""
# common_args="--jdll <file>"
# --jdll <file>
# -s  --snr <serial_number> 
# -c  --clockspeed <speed> 
# --recover    
# --rbp <level>
# -p  --pinreset
# -r  --reset
# -d  --debugreset

if [ ${should_erase} -eq 1 ]; then
	nrfjprog --family "${nrfjprog_family}" --eraseall $@
	# --qspieraseall        
fi

if [ ${should_program} -eq 1 ]; then
	nrfjprog $common_args --family "${nrfjprog_family}" $@
fi

# --program <hex_file> [--sectorerase | --chiperase | --sectoranduicrerase |
#                              --recover] [--qspisectorerase | --qspichiperase]
#
# --verify [<image_file>] [--fast] 
