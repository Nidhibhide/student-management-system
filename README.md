# student-management-system

This repo contains two separate apps.

## student-app (Person A — Student Management)

Maven project for add / view / search students.

```bash
cd student-app
mvn compile exec:java
```

Set the DB password before running:

```powershell
$env:SMS_DB_PASSWORD = "your-mysql-password"
```

## auth-app (Person B — Authentication)

Swing Sign Up / Sign In UI.

1. Copy `auth-app/.env.example` to `auth-app/.env` and fill in your DB values.
2. Run `SignUp` or `SignIn` from `auth-app/src/frontend` with jars in `auth-app/lib` on the classpath.

Keep the two apps separate until you intentionally integrate them.
