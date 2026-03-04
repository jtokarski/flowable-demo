

#
# This script demonstrates show to check - from inside Python script - on which (virtual) environment
# it is executing.
#

import sys
import platform

print()

print("sys.version_info:          ", sys.version_info)
print("platform.python_version(): ", platform.python_version())

print()

print("sys.executable:    ", sys.executable)
print("sys.prefix:        ", sys.prefix)
print("sys.base_prefix:   ", sys.base_prefix)

print()

if sys.prefix != sys.base_prefix:
  print("sys.prefix != sys.base_prefix   -   linkely running inside virtualenv")
else:
  print("sys.prefix == sys.base_prefix   -   rather NOT running inside a virtualenv")

print()
