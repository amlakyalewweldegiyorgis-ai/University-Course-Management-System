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
                student_id VARCHAR(50) UNIQUE NOT NULL,
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
}
