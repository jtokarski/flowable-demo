
import subprocess
from os_check import assert_windows



assert_windows()

# The git-bash.exe is not added to PATH with Git Bash installation
git_bash_path = "C:/Users/jtokarski/AppData/Local/Programs/Git/git-bash.exe"
ping_cmd = f"ping -n 8 localhost"
git_bash_cmd = [git_bash_path, "--cd=c:/", "-c", f"{ping_cmd}; read -p 'Press enter to close...'"]

# We don't need CREATE_NEW_CONSOLE here because git-bash.exe
# is a GUI app that spawns its own window by default.
ping_proc = subprocess.Popen(git_bash_cmd, shell=True)

# Looks like ping_proc.wait() is not necessary

