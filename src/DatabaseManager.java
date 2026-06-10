import java.sql.*;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./data/university_db;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";


    public static void initializeDatabase() {
        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS Students (
                id INT AUTO_INCREMENT PRIMARY KEY,
                full_name VARCHAR(100) NOT NULL,
                student_id INTEGER UNIQUE NOT NULL,
                gender VARCHAR(10) NOT NULL
            );
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = connection.createStatement()) {

            stmt.execute(createStudentsTable);
            System.out.println("🔄 Database tables initialized successfully.");

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

            System.out.println(EnrollmentSystem.registeredStudents.size() + "Students are loaded from Database.");

        } catch (SQLException e) {
            System.out.println("❌ There is Database Error: " + e.getMessage());
        }
    }
}
