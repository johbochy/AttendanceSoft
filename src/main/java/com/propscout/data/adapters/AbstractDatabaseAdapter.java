package com.propscout.data.adapters;

import java.sql.*;

public abstract class AbstractDatabaseAdapter {

    public static final String COURSES_TABLE = "courses";
    public static final String UNITS_TABLE = "units";
    public static final String STUDENTS_TABLE = "students";
    public static final String USERS_TABLE = "users";
    public static final String SCHEDULE_TABLE = "schedule";
    public static final String LECTURES_TABLE = "lectures";
    public static final String ATTENDANCE_TABLE = "attendance";
    public static final String SETTINGS_TABLE = "settings";

    /**
     * @var String CREATE_COURSES_TABLE_QUERY creates a table for courses if it does not exists already
     */
    public static final String CREATE_COURSES_TABLE_QUERY = "CREATE TABLE courses(\n" +
            "    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    alias VARCHAR(20),\n" +
            "    name VARCHAR(256) NOT NULL UNIQUE\n" +
            ");";

    //A string to create units table
    public static final String CREATE_UNITS_TABLE = "CREATE TABLE units(\n" +
            "    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    course_id BIGINT NOT NULL,\n" +
            "    user_id BIGINT NOT NULL,\n" +
            "    name VARCHAR(256) NOT NULL,\n" +
            "    code VARCHAR(10) NOT NULL UNIQUE,\n" +
            "    year MEDIUMINT NOT NULL,\n" +
            "    semester TINYINT NOT NULL,\n" +
            "    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (course_id) REFERENCES courses(id),\n" +
            "    FOREIGN KEY (user_id) REFERENCES users(id)\n" +
            ");";

    /**
     * @var String CREATE_STUDENTS_TABLE_QUERY creates a table for students if the table doesn't already exist
     */
    public static final String CREATE_STUDENTS_TABLE_QUERY = "CREATE TABLE students(\n" +
            "    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    reg_no VARCHAR(20) NOT NULL UNIQUE,\n" +
            "    name VARCHAR(256) NOT NULL UNIQUE,\n" +
            "    mobile VARCHAR(256) NOT NULL UNIQUE,\n" +
            "    course_id BIGINT NOT NULL,\n" +
            "    semester TINYINT NOT NULL,\n" +
            "    year MEDIUMINT NOT NULL,\n" +
            "    avatar VARCHAR(256) DEFAULT NULL,\n" +
            "    fingerprint LONGBLOB,\n" +
            "    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE\n" +
            ");";


    /**
     * @var String CREATE_USERS_TABLE_QUERY creates a table for users if the table doesn't already exist
     */
    public static final String CREATE_USERS_TABLE_QUERY = "CREATE TABLE users(\n" +
            "    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    name VARCHAR(256) NOT NULL UNIQUE,\n" +
            "    username VARCHAR(20) NOT NULL UNIQUE,\n" +
            "    email VARCHAR(64) NOT NULL UNIQUE,\n" +
            "    mobile VARCHAR(20) NOT NULL UNIQUE,\n" +
            "    role ENUM('Admin', 'Lecturer') NOT NULL,\n" +
            "    avatar VARCHAR(256) DEFAULT NULL,\n" +
            "    fingerprint LONGBLOB,\n" +
            "    password TEXT NOT NULL,\n" +
            "    login_count INT NOT NULL DEFAULT 0,\n" +
            "    last_login TIMESTAMP,\n" +
            "    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ");";

    /**
     * @var String CREATE_SUPER_USER_QUERY creates the super users to oversee the admin section
     */
    public static final String CREATE_SUPER_USER_QUERY = "INSERT INTO users(name, username, email, mobile, role, avatar, fingerprint, password)\n" +
            "VALUES('Super Admin', 'root', 'admin@propscout.co.ke', '+254700000000', 'Admin', null, null, 'password');";

    /**
     * @var CREATE_SCHEDULE_TABLE creates a table for schedule if it does not exists yet
     */
    public static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE schedule(\n" +
            "    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    unit_id BIGINT NOT NULL,\n" +
            "    start_time TIME NOT NULL,\n" +
            "    duration_hrs TINYINT NOT NULL,\n" +
            "    week_day ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'),\n" +
            "    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (unit_id) REFERENCES units(id)\n" +
            ");";

    /**
     * @var CREATE_LECTURES_TABLE creates a table for lectures if it does not exist yet
     */
    public static final String CREATE_LECTURES_TABLE = "CREATE TABLE lectures(\n" +
            "    id BIGINT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "    lecture_number TINYINT NOT NULL DEFAULT 0,\n" +
            "    schedule_id BIGINT NOT NULL,\n" +
            "    commenced_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (schedule_id) REFERENCES schedule(id)\n" +
            ");";

    /**
     * @var CREATE_ATTENDANCE_TABLE creates a table for attendance if it does not already exist
     */
    public static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE attendance(\n" +
            "    lecture_id BIGINT UNSIGNED NOT NULL,\n" +
            "    reg_no VARCHAR(20) NOT NULL,\n" +
            "    arrived_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "    FOREIGN KEY (lecture_id) REFERENCES lectures(id),\n" +
            "    PRIMARY KEY(lecture_id, reg_no)\n" +
            ");";

    private static final String CREATE_SETTINGS_TABLE = "CREATE TABLE settings(\n" +
            "id INT(1) NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
            "commence_date DATE NOT NULL,\n" +
            "end_date DATE NOT NULL,\n" +
            "semester TINYINT(1) NOT NULL\n" +
            ")";

    /**
     * Single accessible DatabaseHandler instance
     *
     * @var dbh DatabaseHandler instance
     */
    protected DatabaseHandler dbh = null;

    protected Connection conn = null;

    public static class DatabaseHandler {

        /**
         * MySQL Database Source Name
         *
         * @var String DB_URL
         */
        private static final String DB_URL = "jdbc:mysql://localhost:3306/attendancejfx";

        /**
         * Authorized database user
         *
         * @var String DB_USER
         */
        private static final String DB_USER = "job";

        /**
         * Password for the above user
         *
         * @var String DB_PASS
         */
        private static final String DB_PASS = "job";

        /**
         * Connection instance to the database
         */
        private Connection connection;

        /**
         * Only instance of this class
         *
         * @var instance
         */
        private static DatabaseHandler instance = null;

        private DatabaseHandler() {
            connection = getConnection();

            createTables();

            closeConnection();
        }

        private void createTables() {

            try {

                DatabaseMetaData dmd = connection.getMetaData();

                checkAndCreateUsersTable(dmd);

                checkAndCreateCoursesTable(dmd);

                checkAndCreateUnitsTable(dmd);

                checkAndCreateStudentsTable(dmd);

                checkAndCreateScheduleTable(dmd);

                checkAndCreateLecturesTable(dmd);

                checkAndCreateAttendanceTable(dmd);

                checkAndCreateSettingsTable(dmd);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /**
         * Checks and create the settings table if does not already exist
         *
         * @param dmd current database connection metadata
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateSettingsTable(DatabaseMetaData dmd) throws SQLException {

            ResultSet rs = dmd.getTables(null, null, SETTINGS_TABLE, null);

            if (rs.next()) {
                System.out.println(String.format("%s table already exists", SETTINGS_TABLE));
            } else {

                try (Statement stmt = connection.createStatement()) {
                    System.out.println(String.format("Creating %s table ...", SETTINGS_TABLE));
                    stmt.execute(CREATE_SETTINGS_TABLE);
                }
            }
        }

        /**
         * Checks and create the attendance table if does not already exist
         *
         * @param dmd current database connection metadata
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateAttendanceTable(DatabaseMetaData dmd) throws SQLException {

            ResultSet rs = dmd.getTables(null, null, ATTENDANCE_TABLE, null);

            if (rs.next()) {
                System.out.println(String.format("%s table already exists", ATTENDANCE_TABLE));
            } else {

                try (Statement stmt = connection.createStatement()) {
                    System.out.println(String.format("Creating %s table ...", ATTENDANCE_TABLE));
                    stmt.execute(CREATE_ATTENDANCE_TABLE);
                }
            }
        }

        /**
         * Checks and create lectures table if it does not exist yet
         *
         * @param dmd current database connection metadata
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateLecturesTable(DatabaseMetaData dmd) throws SQLException {

            ResultSet resultSet = dmd.getTables(null, null, LECTURES_TABLE, null);

            if (resultSet.next()) {
                System.out.println(LECTURES_TABLE + " table already exists");
            } else {
                try (Statement statement = connection.createStatement()) {
                    System.out.println(String.format("Creating %s table ...", LECTURES_TABLE));
                    statement.execute(CREATE_LECTURES_TABLE);
                }
            }
        }

        /**
         * Checks and creates schedule table if it does  not already exist
         *
         * @param dmd current database connection metadata
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateScheduleTable(DatabaseMetaData dmd) throws SQLException {

            ResultSet resultSet = dmd.getTables(null, null, SCHEDULE_TABLE, null);

            if (resultSet.next()) {
                System.out.println(SCHEDULE_TABLE + " table already exists");
            } else {
                try (
                        Statement statement = connection.createStatement()
                ) {
                    System.out.println("Creating " + SCHEDULE_TABLE + " table ...");
                    statement.execute(CREATE_SCHEDULE_TABLE);
                }
            }
        }


        /**
         * Checks and create users table if it does not already exist
         *
         * @param dmd current database connection meta data
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateUsersTable(DatabaseMetaData dmd) throws SQLException {
            ResultSet resultSet = dmd.getTables(null, null, USERS_TABLE, null);

            if (resultSet.next()) {
                System.out.println(USERS_TABLE + " table already exists");
            } else {
                try (
                        Statement statement = connection.createStatement()
                ) {
                    System.out.println("Creating " + USERS_TABLE + " table ...");
                    statement.execute(CREATE_USERS_TABLE_QUERY);
                    statement.execute(CREATE_SUPER_USER_QUERY);
                }
            }
        }

        /**
         * Checks and create courses table if it does not exist
         *
         * @param dmd current database connection meta data
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateCoursesTable(DatabaseMetaData dmd) throws SQLException {
            ResultSet resultSet = dmd.getTables(null, null, COURSES_TABLE, null);

            if (resultSet.next()) {
                System.out.println(COURSES_TABLE + " table already exists");
            } else {
                try (
                        Statement statement = connection.createStatement()
                ) {
                    System.out.println("Creating " + COURSES_TABLE + " table ...");
                    statement.execute(CREATE_COURSES_TABLE_QUERY);
                }
            }
        }

        /**
         * Checks and create units table if it does not exist
         *
         * @param dmd current database connection meta data
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateUnitsTable(DatabaseMetaData dmd) throws SQLException {
            ResultSet resultSet = dmd.getTables(null, null, UNITS_TABLE, null);

            if (resultSet.next()) {
                System.out.println(UNITS_TABLE + " table already exists");
            } else {
                try (
                        Statement statement = connection.createStatement()
                ) {
                    System.out.println("Creating " + UNITS_TABLE + " table ...");
                    statement.execute(CREATE_UNITS_TABLE);
                }
            }
        }

        /**
         * Checks and create students table if it does not exist
         *
         * @param dmd current database connection meta data
         * @throws SQLException if a table creation error occurs
         */
        private void checkAndCreateStudentsTable(DatabaseMetaData dmd) throws SQLException {
            ResultSet resultSet = dmd.getTables(null, null, STUDENTS_TABLE, null);

            if (resultSet.next()) {
                System.out.println(STUDENTS_TABLE + " table already exists");
            } else {
                try (
                        Statement statement = connection.createStatement()
                ) {
                    System.out.println("Creating " + STUDENTS_TABLE + " table ...");
                    statement.execute(CREATE_STUDENTS_TABLE_QUERY);
                }
            }
        }

        /**
         * Singleton Implementation
         *
         * @return Instance of the connection class
         */
        public static DatabaseHandler getInstance() {

            //Lazy instantiation
            if (instance == null) {
                instance = new DatabaseHandler();
            }

            return instance;
        }

        /**
         * Gets database connection
         *
         * @return Connection instance to the database
         */
        private Connection getConnection() {

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");

                return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();

            }

            return null;
        }

        /**
         * Closes the connection to the database if it is not null
         */
        public void closeConnection() {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void open() {
        dbh = DatabaseHandler.getInstance();
        conn = dbh.getConnection();
    }

    public void close() {
        dbh.closeConnection();

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }
}
