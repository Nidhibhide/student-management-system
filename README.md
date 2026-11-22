# student-management-system

Maven Swing app for admin auth and student management.

## Run

```bash
cd student-app
mvn compile exec:java
```

Set the DB password before running:

```powershell
$env:SMS_DB_PASSWORD = "your-mysql-password"
```

Main class defaults to `frontend.SignUp` (see `student-app/pom.xml`).
