
from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker

mssql_username = 'sa'
mssql_password = '...'
mssql_host = '192.168.32.3'
mssql_port = 1433
mssql_database = 'testdb000'
mssql_driver = 'ODBC+Driver+18+for+SQL+Server'

sqlalchemy_url = (
    f"mssql+pyodbc://{mssql_username}:{mssql_password}@{mssql_host}:{mssql_port}/{mssql_database}" +
    f"?driver={mssql_driver}" +
    f"&TrustServerCertificate=yes"
)

engine = create_engine(sqlalchemy_url)

Session = sessionmaker(bind=engine)
session = Session()

rows = session.execute(text("SELECT * FROM [sch000].[book]")).fetchall()
for row in rows:
    print(row)

# [author], [title]



