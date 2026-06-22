import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./data/university_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";


    public static void initializeHelperTables() {
//        -- Join table for Enrollments (Both current and completed)
        String createEnrollmentTable = """
                CREATE TABLE IF NOT EXISTS Enrollments (
                    student_id VARCHAR(50),
                    course_code VARCHAR(50),
                    status VARCHAR(20) NOT NULL, -- 'CURRENT' or 'COMPLETED'
                    PRIMARY KEY (student_id, course_code),
                    FOREIGN KEY (student_id) REFERENCES Students(student_id) ON DELETE CASCADE,
                    FOREIGN KEY (course_code) REFERENCES Courses(course_code) ON DELETE CASCADE
                );
                """;

//        -- Join table for Course Prerequisites
        String createPrerequisiteTable = """
                CREATE TABLE IF NOT EXISTS Prerequisites (
                    course_code VARCHAR(50),
                    prereq_course_code VARCHAR(50),
                    PRIMARY KEY (course_code, prereq_course_code),
                    FOREIGN KEY (course_code) REFERENCES Courses(course_code) ON DELETE CASCADE,
                    FOREIGN KEY (prereq_course_code) REFERENCES Courses(course_code) ON DELETE CASCADE
                );
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = connection.createStatement()) {

            stmt.execute(createEnrollmentTable);
            stmt.execute(createPrerequisiteTable);

        } catch (SQLException e) {
            System.out.println("❌ There is Database Initialization Error: " + e.getMessage());
        }
    }


    public static void loadPrerequisites() {
        String sql = "SELECT course_code, prereq_course_code FROM Prerequisites";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String courseCode = rs.getString("course_code");
                String prereqCode = rs.getString("prereq_course_code");

                // Find the main course and the prerequisite course in your memory list
                Course mainCourse = findCourseByCode(courseCode);
                Course prereqCourse = findCourseByCode(prereqCode);

                if (mainCourse != null && prereqCourse != null) {
                    // Assuming your Course class has a method to add a prerequisite object
                    mainCourse.getPrerequisites().add(prereqCourse);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error loading prerequisites: " + e.getMessage());
        }
    }

    // Helper to look up a course from memory
    private static Course findCourseByCode(String code) {
        return EnrollmentSystem.availableCourses.stream()
                .filter(c -> c.getCourseCode().equals(code))
                .findFirst()
                .orElse(null);
    }


    public static void loadStudentEnrollments() {
        String sql = "SELECT student_id, course_code, status FROM Enrollments";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int studentId = rs.getInt("student_id"); // Updated to getInt
                String courseCode = rs.getString("course_code");
                String status = rs.getString("status");

                Student student = findStudentById(studentId);
                Course course = findCourseByCode(courseCode);

                if (student != null && course != null) {
                    if ("COMPLETED".equalsIgnoreCase(status)) {
                        student.addCompletedCourse(course);
                    } else if ("CURRENT".equalsIgnoreCase(status)) {
                        student.addCurrentCourses(course);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error loading student enrollments: " + e.getMessage());
        }
    }

    // Helper to look up a student from memory using an int ID
    private static Student findStudentById(int id) {
        return EnrollmentSystem.registeredStudents.stream()
                .filter(s -> s.getId() == id) // Direct integer comparison
                .findFirst()
                .orElse(null);
    }


    public static void initializeTableStudentOnDatabase() {
        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS Students (
                id INT AUTO_INCREMENT PRIMARY KEY,
                full_name VARCHAR(100) NOT NULL,
                student_id INT UNIQUE NOT NULL,
                gender VARCHAR(10) NOT NULL
            );
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = connection.createStatement()) {

            stmt.execute(createStudentsTable);

        } catch (SQLException e) {
            System.out.println("❌ There is Database Initialization Error: " + e.getMessage());
        }
    }


    public static void saveStudent(Student st){
        String sql = "INSERT INTO Students (full_name, student_id, gender) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql)) {

             pst.setString(1, st.getFullName());
             pst.setInt(2, st.getId());
             pst.setString(3, st.getGender());
             pst.executeUpdate();

            System.out.println("Student saved in the database, successfully!");

        } catch (SQLException e) {
            System.out.println("❌ There is Database Error: " + e.getMessage());
        }
    }

    public static void loadStudents() {
        String sql = "SELECT full_name, student_id, gender FROM Students";

        EnrollmentSystem.registeredStudents.clear();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                String name = rs.getString("full_name");
                int id = rs.getInt("student_id");
                String gender = rs.getString("gender");

                Student student = new Student(name, id, gender);

                EnrollmentSystem.registeredStudents.add(student);
            }

        } catch (SQLException e) {
            System.out.println("❌ There is Database Error on saving this student: " + e.getMessage());
        }
    }

    public static boolean removeStudentFromDatabase(int studentId) {
        String sql = "DELETE FROM Students WHERE student_id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("💾 Student ID " + studentId + " permanently deleted from Database.");
                return true;
            } else {
                System.out.println("⚠️ Student ID " + studentId + " was not found in the database.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error while removing student: " + e.getMessage());
            return false;
        }
    }


//    Courses Database

    public static void initializeTableCourseOnDatabase() {
        String createCoursesTable = """
            CREATE TABLE IF NOT EXISTS Courses (
                id INT AUTO_INCREMENT PRIMARY KEY,
                course_name VARCHAR(100) NOT NULL,
                course_code VARCHAR(50) UNIQUE NOT NULL,
                course_fee DECIMAL(7,2) NOT NULL
            );
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = connection.createStatement()) {

            stmt.execute(createCoursesTable);

        } catch (SQLException e) {
            System.out.println("❌ There is Database Initialization Error: " + e.getMessage());
        }
    }

    public static void saveCourse(Course course){
        String sql = "INSERT INTO Courses (course_name, course_code, course_fee) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, course.getCourseName());
            pst.setString(2, course.getCourseCode());
            pst.setDouble(3, course.getCourseFee());
            pst.executeUpdate();

            System.out.println("Course saved in the database, successfully!");

        } catch (SQLException e) {
            System.out.println("❌ There is Database Error on saving this course: " + e.getMessage());
        }
    }

    public static void loadCourses() {
        String sql = "SELECT course_name, course_code, course_fee FROM Courses";

        EnrollmentSystem.availableCourses.clear();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                String course_name = rs.getString("course_name");
                String course_code = rs.getString("course_code");
                double course_fee = rs.getDouble("course_fee");

                Course course = new Course(course_name, course_code, course_fee);

                EnrollmentSystem.availableCourses.add(course);
            }

        } catch (SQLException e) {
            System.out.println("❌ There is Database Error on loading Courses: " + e.getMessage());
        }
    }

    public static void saveEnrollment(int studentId, String courseCode, String status) {
        String sql = "INSERT INTO Enrollments (student_id, course_code, status) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setInt(1, studentId);
            pst.setString(2, courseCode);
            pst.setString(3, status.toUpperCase());
            pst.executeUpdate();

            System.out.println("💾 Enrollment saved to database successfully.");

        } catch (SQLException e) {
            System.out.println("❌ Database Error on saving enrollment: " + e.getMessage());
        }
    }

    public static void updateCourseStatus(int studentId, String courseCode, String newStatus) {
        String sql = "UPDATE Enrollments SET status = ? WHERE student_id = ? AND course_code = ?";

        // 1. Update the permanent Database Storage
        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pst = connection.prepareStatement(sql)) {

            pst.setString(1, newStatus.toUpperCase());
            pst.setInt(2, studentId);
            pst.setString(3, courseCode);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("🔄 Database updated: Course " + courseCode + " status changed to " + newStatus);

                // 2. Sync the in-memory runtime objects
                syncMemoryStatus(studentId, courseCode, newStatus);
            } else {
                System.out.println("⚠️ No enrollment record found matching Student ID " + studentId + " and Course " + courseCode);
            }

        } catch (SQLException e) {
            System.out.println("❌ Database Error while updating course status: " + e.getMessage());
        }
    }


    private static void syncMemoryStatus(int studentId, String courseCode, String newStatus) {
        Student student = findStudentById(studentId);
        Course course = findCourseByCode(courseCode);

        if (student != null && course != null) {
            if ("COMPLETED".equalsIgnoreCase(newStatus)) {
                // Remove from current tracks if present
                student.getCurrentCourse().remove(course);

                // Add to completed track so prerequisites system recognizes it
                if (!student.getCompletedCourse().contains(course)) {
                    student.getCompletedCourse().add(course);
                }
            } else if ("CURRENT".equalsIgnoreCase(newStatus)) {
                student.getCompletedCourse().remove(course);
                student.addCurrentCourses(course);
            }
        }
    }
}
