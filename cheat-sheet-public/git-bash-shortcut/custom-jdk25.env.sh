#
# The shortcut target:
#   "C:\Users\jtokarski\AppData\Local\Programs\Git\git-bash.exe" -c "bash --rcfile /c/dev/flowable-demo/cheat-sheet-public/git-bash-shortcut/custom-jdk25.env.sh -i"
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#

#
[ -f ~/.bashrc ] && source ~/.bashrc

export JAVA_HOME="/c/java_installs/jdk-25.0.5+8"
export MAVEN_HOME="/c/java_installs/apache-maven-3.10.0"
export NODEJS_HOME="/c/java_installs/node-v26.13.0-win-x64"

export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$NODEJS_HOME:$PATH"

#
#
#
cd /c/dev/flowable-demo/cheat-sheet-public/git-bash-shortcut
