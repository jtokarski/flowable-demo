
from sqlalchemy import create_engine, text

mssql_username = 'sa'
mssql_password = '...'
mssql_host = '192.168.32.3'
mssql_port = 1433
mssql_driver = 'ODBC+Driver+18+for+SQL+Server'
sqlalchemy_url = (
    f"mssql+pyodbc://{mssql_username}:{mssql_password}@{mssql_host}:{mssql_port}/master" +
    f"?driver={mssql_driver}" +
    f"&TrustServerCertificate=yes"
)
engine = create_engine(url=sqlalchemy_url, isolation_level='AUTOCOMMIT')
with engine.begin() as conn:
    conn.execute(text("""
        DROP DATABASE IF EXISTS [testdb000];
    """))
    conn.execute(text("""
        CREATE DATABASE [testdb000];
    """))
    conn.execute(text("""
        USE [testdb000];
    """))
    conn.execute(text("""
        CREATE SCHEMA [sch000];
    """))
    conn.execute(text("""
        CREATE TABLE [sch000].[book] (
            [id] [bigint] IDENTITY(1,1) NOT NULL,
            [authorFirstName] [nvarchar](255),
            [authorLastName] [nvarchar](255),
            [title] [nvarchar](255)
        );
    """))
    conn.execute(text("""
        INSERT INTO [sch000].[book] ([authorFirstName], [authorLastName], [title]) VALUES
            ('Henryk', 'Sienkiewicz', 'Quo vadis'),
            ('Adam', 'Mickiew', 'Pan Tadeusz'),
            ('Henryk', 'Sienkiewicz', 'The knights of cross'),
            ('Aleksander', 'Fredro', 'Zemsta');
    """))

