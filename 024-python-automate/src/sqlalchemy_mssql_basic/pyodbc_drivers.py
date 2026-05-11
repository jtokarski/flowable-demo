
import pyodbc

#
# The way to check if ODBC driver (for MS SQL Server) is installed in the system.
#
print(pyodbc.drivers())
