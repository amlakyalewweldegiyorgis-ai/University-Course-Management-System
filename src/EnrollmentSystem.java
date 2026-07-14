import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class EnrollmentSystem {
    public static List<Student> registeredStudents = new ArrayList<>();
    public static List<Course> availableCourses = new ArrayList<>();
    private static final List<Course> selectedCourses = new ArrayList<>();

    // Tempo testing code
    public static void addAvailableCourse(Course course) {
        availableCourses.add(course);
    }

    // Add registered students
    public static void addRegisteredStudent(Student student) {
        registeredStudents.add(student);
    }

    // Day 08 : writing helper method to check prerequisites
    private static boolean checkPrerequisites(Student student, Course course) {
        List<Course> completedCourses = student.getCompletedCourse();

        boolean exists = availableCourses.stream()
                .anyMatch(c -> c.getCourseCode().equalsIgnoreCase(course.getCourseCode()));
        if (!exists) return false;

        if (course.getPrerequisites().isEmpty()) {
            return true;
        }

        for (Course prereq : course.getPrerequisites()) {
            boolean completed = completedCourses.stream()
                    .anyMatch(c -> c.getCourseCode().equalsIgnoreCase(prereq.getCourseCode()));
            if (!completed) {
                return false;
            }
        }
        return true;
    }

    // Day 09: Enrollment method
    public static void enroll(Student student, Course course) {
        if (checkPrerequisites(student, course)){
            if (!checkCourseRegistered(student, course)) {
                selectedCourses.add(course);
                checkOut(student);
            } else {
                System.out.println("The student has already registered for " +  course + "course, please select another course.");
            }
        } else {
            System.out.print("Sorry, you can't enroll for this " +  course.getCourseName() +  "  " + course.getCourseCode() + " course,\n prerequisite is not matched.\n");
            System.out.println(course.getPrerequisites());
        }
    }

    // Helper to check for duplicate registration
    private static boolean checkCourseRegistered(Student student, Course course) {
        boolean isCurrent = student.getCurrentCourse().stream()
                .anyMatch(c -> Objects.equals(c.getCourseCode(), course.getCourseCode()));
        boolean isCompleted = student.getCompletedCourse().stream()
                .anyMatch(c -> Objects.equals(c.getCourseCode(), course.getCourseCode()));

        return isCurrent || isCompleted;
    }

    // Day 10 : Fee calculation
    private static double totalFee() {
        double totalFee = 0.0;

        for (Course c : selectedCourses) {
            totalFee += c.getCourseFee();
        }
        return totalFee;
    }

    // Day 11: Checkout method
    public static void checkOut(Student student) {
        Scanner input = new Scanner(System.in);

        if (!selectedCourses.isEmpty()) {
            System.out.println("\uD83E\uDE99 Total fee is: " + totalFee());
            System.out.println("\uD83C\uDFE6 Pay using this address => GORP-International-Bank: 12345BA-26-PL");

            System.out.println("----------- Confirming Payment ----------- ");
            System.out.print("Enter TransactionID: ");
            String transactionId = input.nextLine();

            if (Payment.isValidTransactionId(transactionId)) {
                Payment studentSlip = new Payment(transactionId, student.getId(), totalFee());
                Payment.addPaymentData(studentSlip);
                DatabaseManager.savePayment(studentSlip);

                for (Course c : selectedCourses) {
                    student.addCurrentCourses(c);
                    DatabaseManager.saveEnrollment(student.getId(), c.getCourseCode(), "CURRENT");
                }

                System.out.println();
                System.out.println("----------- Student " + student.getFullName() + " have successfully enrolled -----------");
                System.out.println("Name: " + student.getFullName());
                System.out.println("TransactionID: " + transactionId);
                System.out.println("Total fee is: " + totalFee());
                System.out.print("----------- Enrolled courses: \n");

                for (Course c : selectedCourses) {
                    System.out.print(c.getCourseName() + ", " + "\n");
                }
                System.out.println();
                selectedCourses.clear();
            } else {
                selectedCourses.clear();
                System.out.println("Invalid Transaction ID. Please, enter a valid transaction Id.");
            }
        } else {
            System.out.println("You haven't select any course, please select from available courses.");
        }
    }
}

