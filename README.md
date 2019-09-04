# ImageMachine
 
Image Machine app for ProSpace Android Technical Test

Things that is functional (for now):
- Add new machine (image hasn't been implemented yet)
- SQLite for database
- QR Code Scanner
- DatePicker

Database Schema:

DB_NAME = machines

TABLE_COLUMNS:
-ID (INTEGER PRIMARY KEY) >> auto increment by SQLite
-machineID (INTEGER)
-name (TEXT)
-type (TEXT)
-qr (INTEGER)
-date (DATE)
