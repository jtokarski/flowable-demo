#
# The shortcut target:
#   "C:\Users\jtokarski\AppData\Local\Programs\Git\git-bash.exe" -c "bash --rcfile /c/dev/flowable-demo/cheat-sheet-public/git-bash-shortcut/custom-jdk27.env.sh -i"
#
#
# - The M2_HOME id deprecated since Maven 3.5.0.
# - The MAVEN_HOME is not actually used by the mvn script itself in most versions. It matters only
#   to IDEs, CI tools, or your own scripts that read it. I use it for readability of this script.
# - The JAVA_HOME is required by Maven.
# - The NODEJS_HOME in informal and uncommon convention. I use it for readability of this script.
# - Note that this script uses Unix-style paths (/c/Users/...)
# - The default size of new Git Bash window may be too small, to adjust it create file
#     ~/.minttyrc
#     --------------------------
#     FontHeight=10
#     Columns=140
#     Rows=40
#     -------------------------
#

# Load the user's normal config first (login shells skip .bashrc by default)
[ -f ~/.bashrc ] && source ~/.bashrc

export JAVA_HOME="/c/java_installs/jdk-27.0.1.0.2+1"
export MAVEN_HOME="/c/java_installs/apache-maven-3.10.0"
export NODEJS_HOME="/c/java_installs/node-v27.0.0-alpha.0.1.0-win-x64"

export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$NODEJS_HOME:$PATH"

#
# Git Bash window to be started in this directory:
#
cd /c/dev/flowable-demo/cheat-sheet-public/git-bash-shortcut
