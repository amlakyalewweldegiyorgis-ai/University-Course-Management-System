import java.util.ArrayList;
import java.util.List;

public class Course implements java.io.Serializable {

    // Course private instance members
    private String courseName;
    private String courseCode;
    private double courseFee;
    private List<Course> prerequisite;

    // Course constructor
    public Course(String courseName, String courseCode, double courseFee) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseFee = courseFee;
        this.prerequisite = new ArrayList<>();
    }

    // To string
    public String toString() {
        return String.format("  %-20s %-12s %-8s", this.getCourseName(), this.getCourseCode(), this.getCourseFee());
    }

    // Getters
    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public double getCourseFee() {
        return this.courseFee;
    }

    public void addPrerequisite(Course course) {
        this.prerequisite.add(course);
    }

    public List<Course> getPrerequisites() {
        return this.prerequisite;
    }

    // Setters
    public void setPrerequisite(List<Course> prerequisite) {
        this.prerequisite = (prerequisite != null) ? prerequisite : new ArrayList<>();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseFee(double courseFee) {
        this.courseFee = courseFee;
    }

    // Helper method to update the course
    public void updateCourse(String courseName, String courseCode, double courseFee) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseFee = courseFee;
    }

}
