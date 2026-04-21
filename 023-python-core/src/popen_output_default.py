
import subprocess
from os_check import assert_windows



assert_windows()

ping_cmd = ["ping", "-n", "8", "localhost"]
ping_proc = subprocess.Popen(ping_cmd)
ping_proc.wait()
