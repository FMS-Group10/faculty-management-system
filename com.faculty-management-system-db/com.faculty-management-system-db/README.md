# Faculty Management System (Swing + JDBC)

This project follows an MVC folder structure and provides a **modern Swing UI** with **JDBC DAOs** (MySQL).

## 1) Project Structure

```
com.faculty-management-system-db/
├─ src/com/faculty/
│  ├─ model/
│  ├─ view/
│  ├─ controller/
│  ├─ dao/
│  ├─ util/
│  └─ main/
├─ database/
│  ├─ schema.sql
│  └─ sample_data.sql
├─ docs/
└─ demo/
```

## 2) Database Setup (MySQL)

1. Open `database/schema.sql` and run it.
2. Then run `database/sample_data.sql`.

Default DB name: `faculty_management_system`

### Configure connection
Edit:
- `src/com/faculty/util/DBConfig.java`

```java
public static final String URL = "jdbc:mysql://localhost:3306/faculty_management_system?useSSL=false&serverTimezone=UTC";
public static final String USER = "root";
public static final String PASSWORD = "";
```

## 3) JDBC Driver

Add **MySQL Connector/J** to your project classpath (IntelliJ/Eclipse):
- `mysql-connector-j` (8.x)

## 4) Run

Run:
- `src/com/faculty/main/App.java`

## 5) Demo Logins (from sample_data.sql)

- Admin: `admin / admin123`
- Student: `student1 / student123`
- Lecturer: `lecturer1 / lecturer123`

> Passwords are stored as plain text for simplicity (typical for coursework demos).
