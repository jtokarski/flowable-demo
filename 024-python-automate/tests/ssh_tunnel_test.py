
import subprocess
import time

import mysql.connector


#
#
# A quick reminder on generating and installing SSH id key:
#   $ ssh-keygen -t ed25519 -b 4096 -f win-host
#   $ ssh-copy-id -i ./win-host jtokarski@192.168.32.3
# (... yes, ed25519 is the NEW standard)
#
# Verifying if server/port is accessible from jumphost (two ways):
#   $ nc -zv localhost 13306
#   $ curl -v localhost:13306
#
# Example command to create SSH tunnel:
#                  (host)   :  (jumphost)
#   $ ssh -L localhost:23306:localhost:13306 -i /c/dev/flowable-demo/024-python-automate/win-host jtokarski@192.168.32.3
#   (it creates tunnel and opens remote shell)
# Or, optionally with -N option so that it doesn't open the shell:
#   $ ssh -N -L localhost:23306:localhost:13306 -i /c/dev/flowable-demo/024-python-automate/win-host jtokarski@192.168.32.3
#
#
def test_subprocess_ssh_tunnel():
  ssh_cmd = [
    "ssh",
    "-N",
    "-L", "localhost:23306:localhost:13306",
    "-i", "c:/dev/flowable-demo/024-python-automate/win-host",
    "jtokarski@192.168.32.3"
  ]
  DB_COORDINATES: dict[str, str | int] = {
    "host": "localhost",
    "port": 23306,
    "user": "root",
    "password": "..................",
    "database": "mysql"
  }
  try:
    tunnel_process = subprocess.Popen(ssh_cmd)
    time.sleep(2)
    connection = mysql.connector.connect(**DB_COORDINATES)
    assert connection.is_connected()
    cursor = connection.cursor()
    cursor.execute("SELECT @@version AS mysql_version, @@version_comment AS mysql_version_comment")
    result = cursor.fetchone()
    print(f" --- mysql_version: {result[0]}, mysql_version_comment: {result[1]}")
  except mysql.connector.Error as error:
    print(f"ERROR: {error}")
  finally:
    try:
      cursor.close()
      connection.close()
      tunnel_process.terminate()
    except mysql.connector.Error as error:
      print(f"ERROR: {error}")
      pass

