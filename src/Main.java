public class Main{

    public static void main(String[] args) {

//   Loading existing files
        EnrollmentSystem.availableCourses = FileManager.loadFromFile("Courses.dat");
        EnrollmentSystem.registeredStudents = FileManager.loadFromFile("Students.dat");
        Payment.payments = FileManager.loadFromFile("Payments.dat");
        DatabaseManager.initializeTableStudentOnDatabase();
        DatabaseManager.loadStudents();


//    Dummy data to test the system -----------------------------------------
        if (EnrollmentSystem.availableCourses.isEmpty()) {
            Course course1 = new Course("Java", "J12", 500);
            Course course2 = new Course("HTML", "H21", 400);
            Course course3 = new Course("React", "R001", 900);
            Course course4 = new Course("JavaScript", "JS88", 500);
            Course course5 = new Course("Css", "C90", 300);

            EnrollmentSystem.addAvailableCourse(course1);
            EnrollmentSystem.addAvailableCourse(course2);
            EnrollmentSystem.addAvailableCourse(course3);
            EnrollmentSystem.addAvailableCourse(course4);
            EnrollmentSystem.addAvailableCourse(course5);
        }
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
                    FileManager.saveToFile(EnrollmentSystem.registeredStudents, "Students.dat");
                    FileManager.saveToFile(Payment.payments, "Payments.dat");
                } else if (studentChoice == 3) {
                    String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
                    Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
                } else if (studentChoice == 4) {
                    Menu.removeRegisteredStudent();
                    FileManager.saveToFile(EnrollmentSystem.registeredStudents, "Students.dat");
                } else if (studentChoice == 5) {
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

//        Saving data to the files b4 leaving the system
                FileManager.saveToFile(EnrollmentSystem.availableCourses, "Courses.dat");
                FileManager.saveToFile(EnrollmentSystem.registeredStudents, "Students.dat");
                FileManager.saveToFile(Payment.payments, "Payments.dat");

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
