
import sys
import platform



def assert_windows():
  """Assert that script is running on Windows OS

  Uses two methods (redundantly) to verify OS platform on which the script is running: sys.platform
  and platform.system(). If it's not Windows, exits process.
  This my first comment that follows 'docstrings' conventions defined in PEP 257 (the closest equivalent to Javadoc)
  """
  if sys.platform != "win32":
    print(f"Error: This script is designed for Windows. Detected: {sys.platform}")
    sys.exit(1)

  if platform.system() != "Windows":
    print(f"Error: This script is designed for Windows. Detected: {sys.platform}")
    sys.exit(1)
