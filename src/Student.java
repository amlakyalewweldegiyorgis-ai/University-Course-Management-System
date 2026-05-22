import java.util.ArrayList;
import java.util.List;

public class Student implements java.io.Serializable {
    private String fullName;
    private int id;
    private String gender;
    private List<Course> completedCourses;
    private List<Course> currentCourse;


    //    No-args constructor
    public Student() {
        this.completedCourses = new ArrayList<>();
        this.currentCourse = new ArrayList<>();
    }


    public Student(String fullName, int id,  String gender) {
        this();
        this.fullName = fullName;
        this.id = id;
        this.gender = gender;
    }

    public void addCompletedCourse(Course course) {
        this.completedCourses.add(course);
    }

    public void addCurrentCourses(Course course) {
        this.currentCourse.add(course);
    }

    public String toString() {
        return String.format("  %-20s  %-12s    %-8s", this.getFullName(), this.getId(), this.getGender());
    }

    //    Getters
    public String getFullName() {
        return this.fullName;
    }

    public List<Course> getCurrentCourse() {
        return currentCourse;
    }

    public List<Course> getCompletedCourse() {
        return this.completedCourses;
    }

    public int getId() {
        return this.id;
    }

    public String getGender() {
        return gender;
    }

    //    setters
    public void setCompletedCourses(List<Course> completedCourses) {
        this.completedCourses = completedCourses;
    }

    public void setCurrentCourse(List<Course> currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
