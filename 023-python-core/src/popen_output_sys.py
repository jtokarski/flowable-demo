
import subprocess
import sys
from os_check import assert_windows



assert_windows()

ping_cmd = ["ping", "-n", "8", "localhost"]
ping_proc = subprocess.Popen(ping_cmd, stdout=sys.stdout, stderr=sys.stderr)
ping_proc.wait()

#
# There are other ways to redirect command output - including ones that allow capturing to string.
# But for now they are not needed.
#
