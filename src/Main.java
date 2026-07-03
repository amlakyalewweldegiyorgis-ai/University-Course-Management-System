public class Main{

    public static void main(String[] args) {

        // Initialize DB
        DatabaseManager.initializeHelperTables();
        DatabaseManager.initializeTableStudentOnDatabase();
        DatabaseManager.initializeTableCourseOnDatabase();

        // Load from DB
        DatabaseManager.loadStudents();
        DatabaseManager.loadCourses();
        DatabaseManager.loadPrerequisites();
        DatabaseManager.loadStudentEnrollments();
        DatabaseManager.loadPayments();

//    -----------------------------------------

        System.out.println("\uD83D\uDDC3\uFE0F Welcome to University Course Management System \uD83D\uDDC3\uFE0F");

        while (true) {
            int choice = Menu.mainMenu();

//        Student Management
            if (choice == 1) {
                int studentChoice = Menu.manageStudentsMenu();

                if (studentChoice == 1) {
                    Menu.registerNewStudent();
                    FileManager.saveToFile(EnrollmentSystem.registeredStudents, "Students.dat");
                } else if (studentChoice == 2) {
                    Menu.enrollStudent();
                } else if (studentChoice == 3) {
                    String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
                    Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
                } else if (studentChoice == 4) {
                    Menu.removeRegisteredStudent();
                    FileManager.saveToFile(EnrollmentSystem.registeredStudents, "Students.dat");
                } else if (studentChoice == 5) {
                    Menu.updateCourseStatusMenu();
                } else if (studentChoice == 6) {
                    Menu.seeStudentsDetails();
                }


//       Courses Management
            } else if (choice == 2) {
                int innerChoice = Menu.manageAvailableCoursesMenu();

                if (innerChoice == 1) {
                    Menu.addNewCourseMenu();
                    FileManager.saveToFile(EnrollmentSystem.availableCourses, "Courses.dat");
                } else if (innerChoice == 2) {
                    Menu.updateExistingCourseMenu();
                    FileManager.saveToFile(EnrollmentSystem.availableCourses, "Courses.dat");
                } else if (innerChoice == 3) {
                    Menu.deleteExistingCourseMenu();
                    FileManager.saveToFile(EnrollmentSystem.availableCourses, "Courses.dat");
                } else if (innerChoice == 4) {
                    Menu.addPrerequisiteMenu();
                    FileManager.saveToFile(EnrollmentSystem.availableCourses, "Courses.dat");
                } else if (innerChoice == 5) {
                    Menu.showPrerequisiteMenu();
                } else if (innerChoice == 6) {
                    String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
                    Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
                }
            } else if (choice == 3) {
                int innerChoice = Menu.managePaymentsMenu();

                if (innerChoice == 1) {
                    String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
                    Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");

                    Menu.listPaymentSlips();
                }
            } else if (choice == 4) {
                System.out.println("\uD83D\uDDA5 Exiting the system, Thank you for using . . . ");
                break;
            }
        }

//    Homework:
//    1. Fix duplicate registration.
//    2. Only display if there is an error while saving the file.
//    3. Arrange the methods in order and using comments.
//       . File Manager, Student, Course, Payment, EnrollmentSystem,
//    4. Connect to the Database.
    }
}
