
import subprocess
from os_check import assert_windows



assert_windows()

ping_cmd = f"ping -n 8 localhost"
powershell_cmd = f"powershell -NoExit -Command {ping_cmd}"
ping_proc = subprocess.Popen(powershell_cmd, creationflags=subprocess.CREATE_NEW_CONSOLE)

# Looks like ping_proc.wait() is not necessary
