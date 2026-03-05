#!/usr/bin/env python3
# nrf52_flash.py — Python 3.6+ compatible
#
# Defaults to JSON-pretty logs (logs go to stdout for easy capture)
# Final line is always: nrf52_flash:DONE:<rc>:<errormsg>

import argparse
import os
import shutil
import subprocess
import sys
from typing import List, Optional, Tuple

def final_exit(rc: int, errormsg: str) -> None:
    """Print the mandatory final status line and exit."""
    clean = (errormsg or "").strip().replace("\n", " ").replace("\r", " ")
    print("nrf52_flash:DONE:{0}:{1}".format(rc, clean))
    sys.exit(rc)

def ensure_tools(nrfutil_bin: str) -> None:
    if shutil.which(nrfutil_bin) is None:
        final_exit(1, "Error: '{0}' not found in PATH. Install with: nrfutil install device".format(nrfutil_bin))

def run(cmd: List[str]) -> Tuple[int, str]:
    """Run a command, stream combined stdout+stderr to our stdout, return (exit_code, last_nonempty_line)."""
    last_line = ""
    try:
        proc = subprocess.Popen(
            cmd,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            universal_newlines=True,  # Py3.6-compatible (text=True is 3.7+)
            bufsize=1
        )
    except Exception as e:
        return (1, "spawn failed: {0}".format(e))

    assert proc.stdout is not None
    for line in proc.stdout:
        if not isinstance(line, str):
            try:
                line = line.decode("utf-8", "replace")
            except Exception:
                line = str(line)
        print(line, end="")
        s = line.strip()
        if s:
            last_line = s
    proc.wait()
    return (proc.returncode, last_line)

def nrfutil_base(nrfutil_bin: str, json_mode: str, skip_overhead: bool, log_output: str) -> List[str]:
    base = [nrfutil_bin]
    if log_output:
        base += ["--log-output", log_output]
    if json_mode == "pretty":
        base += ["--json-pretty"]
    elif json_mode == "compact":
        base += ["--json"]
    if skip_overhead:
        base += ["--skip-overhead"]
    base += ["device"]
    return base

def add_probe_selection(cmd: List[str], serial_number: Optional[str]) -> List[str]:
    if serial_number:
        cmd += ["--serial-number", serial_number]
    return cmd

def main() -> None:
    p = argparse.ArgumentParser(description="Recover/erase, optional program, and reset an nRF52 using nrfutil.")
    p.add_argument("--hex", help="Path to application HEX to program (optional)")
    p.add_argument("--sn", "--serial-number", dest="serial_number", help="Probe/device serial number to target")
    p.add_argument("--nrfutil", default=os.environ.get("NRFUTIL", "nrfutil"), help="nrfutil binary (default: nrfutil)")
    p.add_argument("--recover", action="store_true", help="Recover device before programming (chip erase & unlock)")
    p.add_argument("--erase", action="store_true", help="Erase device before programming")
    p.add_argument("--no-program", action="store_true", help="Do not program even if --hex is provided")
    p.add_argument("--no-json", action="store_true", help="Disable JSON logging entirely")
    p.add_argument("--json", action="store_true", help="Use compact JSON instead of pretty (default is pretty)")
    p.add_argument("--skip-overhead", action="store_true", help="With JSON, output only data payload from info messages")
    p.add_argument("--verify", action="store_true", help="Verify after program (VERIFY_READ)")
    p.add_argument("--no-reset", action="store_true", help="Do not reset/run after operations")
    p.add_argument("--extra-program-options", default="", help="Comma-separated nrfutil program options")
    args = p.parse_args()

    try:
        ensure_tools(args.nrfutil)

        # Decide JSON mode
        if args.no_json:
            json_mode = "none"
        elif args.json:
            json_mode = "compact"
        else:
            json_mode = "pretty"  # default

        # Decide what to do
        do_recover = bool(args.recover)
        do_erase = bool(args.erase)
        do_program = bool(args.hex) and not args.no_program

        if not (do_recover or do_erase or do_program):
            final_exit(2, "Nothing to do: specify --recover and/or --erase and/or provide --hex")

        if do_program and not os.path.exists(args.hex):
            final_exit(1, "HEX not found: {0}".format(args.hex))

        base = nrfutil_base(args.nrfutil, json_mode, args.skip_overhead, "stdout")

        # Step 1: Recover or Erase
        if do_recover:
            cmd = add_probe_selection(base + ["recover"], args.serial_number)
            rc, last = run(cmd)
            if rc != 0:
                final_exit(rc, last or "Recover failed")

        if do_erase:
            cmd = add_probe_selection(base + ["erase", "--all"], args.serial_number)
            rc, last = run(cmd)
            if rc != 0:
                final_exit(rc, last or "Erase failed")

        # Step 2: Program (optional)
        if do_program:
            program_cmd = add_probe_selection(base + ["program", "--firmware", args.hex], args.serial_number)
            opt_list = []
            if args.verify:
                opt_list.append("verify=VERIFY_READ")
            if "chip_erase_mode=" not in args.extra_program_options:
                opt_list.append("chip_erase_mode=ERASE_RANGES_TOUCHED_BY_FIRMWARE")
            if args.extra_program_options:
                for item in args.extra_program_options.split(","):
                    item = item.strip()
                    if item:
                        opt_list.append(item)
            if opt_list:
                program_cmd += ["--options", ",".join(opt_list)]
            rc, last = run(program_cmd)
            if rc != 0:
                final_exit(rc, last or "Program failed")

        # Step 3: Reset
        if not args.no_reset:
            reset_cmd = add_probe_selection(base + ["reset", "--reset-kind", "RESET_DEFAULT"], args.serial_number)
            rc, last = run(reset_cmd)
            if rc != 0:
                final_exit(rc, last or "Reset failed")

        final_exit(0, "")

    except KeyboardInterrupt:
        final_exit(130, "Interrupted")
    except Exception as e:
        final_exit(1, "Unhandled exception: {0}".format(e))

if __name__ == "__main__":
    main()
