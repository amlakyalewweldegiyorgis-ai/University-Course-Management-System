import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    static Scanner sc = new Scanner(System.in);

    // Main Menu
    public static int mainMenu() {
        System.out.println();
        System.out.println();
        System.out.println("     \uD83D\uDDC3\uFE0F Welcome to University Course Management System \uD83D\uDDC3\uFE0F");

        System.out.println("\uD83D\uDCC1 _________________________________________________________ \uD83D\uDCC1");
        System.out.println("\uD83D\uDDD2\uFE0F Main Menu:");
        System.out.println("    1. Manage Students \uD83D\uDC68\u200D\uD83C\uDF93 \uD83E\uDDD1\u200D\uD83C\uDF93");
        System.out.println("    2. Manage Available Courses \uD83D\uDCDA");
        System.out.println("    3. Manage Payments \uD83D\uDCDA");
        System.out.println("    4. Exit \uD83C\uDF42");
        System.out.print("* Please choose an option (number): ");
        return validateNumberChoice(4);
    }


    //    --------------------------------------------------------------------------------------------
    // Students Management Menu:
    public static int manageStudentsMenu() {
        System.out.println("\uD83D\uDC68\u200D\uD83C\uDF93 ------------------ Managing Students ------------------ \uD83D\uDC69\u200D\uD83C\uDF93");
        System.out.println("    1. Register new student");
        System.out.println("    2. Enroll student");
        System.out.println("    3. List registered students");
        System.out.println("    4. Remove registered student");
        System.out.println("    5. Update course status");
        System.out.println("    6. See student's details");
        System.out.println("    7. Quit");
        System.out.print("* Please choose an option: ");
        return validateNumberChoice(7);
    }

    //  1  Registering new student:
    public static void registerNewStudent() {

        System.out.println("*** ------------------ Registering New Student ------------------ ***");
        System.out.print("    .Enter student's full name: ");
        String fullName = validateFullName();

        System.out.print("    .Enter student's gender (M/F): ");
        String gender = validateGender();

        System.out.print("    .Enter student's id: ");
        int id = validateNumberChoiceForId();

        Student student = new Student(fullName, id, gender);
        DatabaseManager.saveStudent(student);
        EnrollmentSystem.addRegisteredStudent(student);
        System.out.println("\uD83C\uDF89 Student '" + student.getFullName() + "' has been registered successfully!");
    }

    // Helper methods for registering new student
    public static String validateFullName() {
        while (true) {
            String fullName = sc.nextLine();

            if (fullName == null || fullName.trim().isEmpty()) {
                System.out.println("Name can't be empty. Please, enter a valid name.");
                sc.nextLine();
                continue;
            }

            fullName = fullName.trim();

            // I have used regular expression to validate the name
            // ^[a-zA-Z]+ checks whether starts with letters
            // \s+        checks whether followed by one or more spaces
            // [a-zA-Z]+$ checks whether ends with letters
            String namePattern = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)+$";

            if  (fullName.matches(namePattern) && fullName.length() >= 4) {
                return fullName;
            } else {
                System.out.println("Invalid name format, please try again.");
            }
        }
    }

    public static String validateGender() {
        while (true) {
            String gender = sc.next();
            sc.nextLine();
            if (gender.equals("M") || gender.equals("F")) {
                return gender;
            } else {
                System.out.println("    ! Invalid Gender value, please use M for 'Male' or F for 'Female'");
            }
        }
    }

    public static int validateNumberChoiceForId() {
        while (true) {
            try {
                int select = sc.nextInt();
                sc.nextLine();
                if (checkIdDuplication(select)) {
                    return select;
                } else {
                    System.out.println("Provided ID is not unique (already assigned), please, provide another value.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please, use numbers.");
                sc.nextLine();
            }
        }
    }

    private static boolean checkIdDuplication(int id) {
        for (Student student : EnrollmentSystem.registeredStudents){
            if (student.getId() == id){
                return false;
            }
        } return true;
    }

    // 2 Enrolling student
    public static void enrollStudent() {
        System.out.println("*** ------------------ Enrolling Student ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
            System.out.print("* Choose the student to enroll from the list: ");
            int chosenStudent = validateNumberChoice(EnrollmentSystem.registeredStudents.size());

            System.out.println("Student " + chosenStudent + " is chosen . . . ");
            int j = 1;

            for (Student student :  EnrollmentSystem.registeredStudents) {
                if (j == chosenStudent) {
                    enrollCourse(student);
                    break;
                }
                j++;
            }
    }

    private static void enrollCourse(Student student) {
            List<Course> tempCourses = new ArrayList<>();
            boolean continue_enrollment = true;


            while (continue_enrollment) {
                System.out.println("*** ------------------ Selecting Course for student ------------------ ***");
                String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
                Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
                System.out.print("* Select the course to enroll from the list: ");
                int chosenCourse = sc.nextInt();
                sc.nextLine();

                System.out.println(chosenCourse + " is chosen . . . ");
                int j = 1;

                for (Course c :  EnrollmentSystem.availableCourses) {
                    if (j == chosenCourse) {
                        tempCourses.add(c);
                        System.out.print("Do you want to continue enrolling another course? [any key for Yes or n for No]: ");
                        String choice = Menu.sc.nextLine();
                        if (choice.equalsIgnoreCase("N")) {
                            continue_enrollment = false;
                        }
                    }
                    j++;
                }
            }

            for (Course c : tempCourses) {
                EnrollmentSystem.enroll(student, c);
            }
    }

    //  3 List registered students
    // Handled by Menu.java

    //  4  Remove registered student
    public static void removeRegisteredStudent() {
        System.out.println("*** ------------------ Removing Registered Student ------------------ ***");
        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students found.");

        if(EnrollmentSystem.registeredStudents.isEmpty()) return;

        System.out.print("* Choose student index to remove: ");
        int chosenStudent = sc.nextInt();
        sc.nextLine();

        if (chosenStudent > 0 && chosenStudent <= EnrollmentSystem.registeredStudents.size()) {
            Student student = EnrollmentSystem.registeredStudents.remove(chosenStudent - 1);
            DatabaseManager.removeStudentFromDatabase(student.getId());
            System.out.println("\uD83D\uDFE2 Student '" + student.getFullName() + "' has been successfully removed.");
        } else {
            System.out.println("❌ Invalid index selection.");
        }
    }





    // 5 Update Course Status
    public static void updateCourseStatusMenu() {
        System.out.println("*** ------------------ Updating Course Status for Registered Student ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
        System.out.print("* Choose the student to update course status from the list: ");
        int chosenStudent = validateNumberChoice(EnrollmentSystem.registeredStudents.size());

        System.out.print("Enter course code: ");
        String chosenCourseCode = sc.nextLine();

        System.out.print("Enter new course status ['CURRENT', 'FAILED', 'COMPLETED']: ");
        String chosenCourseStatus = sc.nextLine();

        int j = 1;

        for (Student student :  EnrollmentSystem.registeredStudents) {
            if (j == chosenStudent) {
                DatabaseManager.updateCourseStatus(student.getId(), chosenCourseCode, chosenCourseStatus);
                break;
            }
            j++;
        }
    }





    // 6 See student's details
    public static void seeStudentsDetails() {
        System.out.println("*** ------------------ Showing Students Detail ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
            System.out.print("* Choose the student to see more detail: ");
            int chosenStudent = validateNumberChoice(EnrollmentSystem.registeredStudents.size());

            System.out.println("Student " + chosenStudent + " is chosen . . . ");
            int j = 1;

            for (Student student :  EnrollmentSystem.registeredStudents) {
                if (j == chosenStudent) {
                    listCurrentCourses(student);
                    break;
                }
                j++;
            }
    }







    private static void listCurrentCourses(Student student) {
        System.out.println("________________________________________________________");
        System.out.println("Name: " + student.getFullName() + "   Id:" + student.getId());
        System.out.println("Currently Enrolled courses . . . ");

        if (student.getCurrentCourse().isEmpty()) {
            System.out.println("      No Enrolled course list found.");
            listTakenCourses(student);
        } else {
            int i = 1;

            System.out.printf("    %-4s %-21s %-13s %-9s%n", "No.", "Course Name", "Code", "Fee");
            for (Course c : student.getCurrentCourse()) {
                System.out.println("    " + i + ". " + c.toString());
                i++;
            }
            listTakenCourses(student);
        }

    }

    private static void listTakenCourses(Student student) {
        System.out.println("Taken courses . . . ");
        if (student.getCompletedCourse().isEmpty()) {
            System.out.println("      No Taken course list found.");
        } else {
        int i = 1;
        System.out.printf("    %-4s %-21s %-13s %-9s%n", "No.", "Course Name", "Code", "Fee");

        for (Course c : student.getCompletedCourse()) {
            System.out.println("    " + i + ". " + c.toString());
            i++;
        }
        }
    }





    //    --------------------------------------------------------------------------------------------
    // Courses Management Menu:
    public static int manageAvailableCoursesMenu() {
        System.out.println("*** ------------------ Managing Available Courses ------------------ ***");
        System.out.println("    1. Add new course");
        System.out.println("    2. Update available course");
        System.out.println("    3. Delete available course");
        System.out.println("    4. Add prerequisite");
        System.out.println("    5. Show prerequisite");
        System.out.println("    6. List available courses");
        System.out.println("    7. Quit");
        System.out.print("* Please choose an option: ");
        return validateNumberChoice(7);
    }

    // Helper method to add course
    public static Course addCourse() {

        System.out.print("    .Enter course name: ");
        String name = sc.nextLine();

        System.out.print("    .Enter course code: ");
        String code = sc.nextLine();

        System.out.print("    .Enter course fee: ");
        double fee = sc.nextDouble();
        sc.nextLine();

        return new Course(name, code, fee);
    }

    //  1  Add new course
    public static void addNewCourseMenu() {
        System.out.println("*** ------------------ Adding New Course ------------------ ***");
        Course course = addCourse();

        boolean match = EnrollmentSystem.availableCourses.stream()
                .anyMatch(c -> c.getCourseCode().equalsIgnoreCase(course.getCourseCode()));

        if (!match) {
            EnrollmentSystem.availableCourses.add(course);
            DatabaseManager.saveCourse(course);
            System.out.println("\uD83C\uDF89 Course '" + course.getCourseName() + "' is added successfully!");
        } else {
            System.out.println("❌ Registration Failed: Course Code already exists.");
        }
    }


    // 2 Update existing course
    public static void updateExistingCourseMenu() {
        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
        System.out.print("* Choose the course to update from the list: ");
        int chosenCourse = sc.nextInt();
        sc.nextLine();

        System.out.println(chosenCourse + " is chosen . . . ");
        Course c = EnrollmentSystem.availableCourses.get(chosenCourse - 1);

        System.out.print("    .Enter updated course name: ");
        String name = sc.nextLine();

        System.out.print("    .Enter updated course code: ");
        String code = sc.nextLine();

        System.out.print("    .Enter updated course fee: ");
        double fee = sc.nextDouble();
        sc.nextLine();

        c.updateCourse(name, code, fee);
        System.out.println("\uD83C\uDF89 Chosen course '" + chosenCourse + "' is updated to '"+ c.getCourseName()+ "', successfully!");
    }

    // 3 Delete existing course
    public static void deleteExistingCourseMenu() {
        System.out.println("*** ------------------ Deleting Existing Course ------------------ ***");

        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");

        if (EnrollmentSystem.availableCourses.isEmpty()) return;

        System.out.print("* Choose the course index to delete from the list: ");
        int chosenCourse = sc.nextInt();
        sc.nextLine();

        if (chosenCourse > 0 && chosenCourse <= EnrollmentSystem.availableCourses.size()) {
            Course removed = EnrollmentSystem.availableCourses.remove(chosenCourse - 1);
            System.out.println("\uD83C\uDF89 Chosen course '" + removed.getCourseName() + "' is deleted, successfully!");
        } else {
            System.out.println("❌ Invalid course selection index.");
        }
    }


    // 4 Adding prerequisite
    public static void addPrerequisiteMenu() {
        System.out.println("*** ------------------ Adding Prerequisite ------------------ ***");
        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No courses registered.");

        if(EnrollmentSystem.availableCourses.isEmpty()) return;

        System.out.print("Choose index of course requiring a prerequisite: ");
        int targetIdx = sc.nextInt();

        System.out.print("Choose index of the required prerequisite course: ");
        int prereqIdx = sc.nextInt();
        sc.nextLine();

        int size = EnrollmentSystem.availableCourses.size();
        if (targetIdx > 0 && targetIdx <= size && prereqIdx > 0 && prereqIdx <= size) {
            if (targetIdx == prereqIdx) {
                System.out.println("❌ A course cannot be a prerequisite of itself!");
                return;
            }
            Course main = EnrollmentSystem.availableCourses.get(targetIdx - 1);
            Course prereq = EnrollmentSystem.availableCourses.get(prereqIdx - 1);

            main.addPrerequisite(prereq);
            DatabaseManager.savePrerequisites(main, prereq);
            System.out.println("✅ Link established: " + prereq.getCourseCode() + " must be taken before " + main.getCourseCode());
        } else {
            System.out.println("❌ Invalid selection limits.");
        }
    }


    // 5 Show Prerequisite
    public static void showPrerequisiteMenu() {
        System.out.println("*** ------------------ Showing Prerequisite ------------------ ***");
        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");

        System.out.print("Choose the course you want to see prerequisite for (Use Number): ");
        int selectedCourse = validateNumberChoice(EnrollmentSystem.availableCourses.size());

        System.out.println(selectedCourse + " is chosen . . . ");
        int j = 1;

        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == selectedCourse) {
                if (!c.getPrerequisites().isEmpty()) {
                    System.out.println("#Showing list of prerequisite for course '" + c.getCourseName() + "'");
                    System.out.printf("  %-20s %-12s %-8s%n", "Name", "Code", "Fee");
                    for (Course course : c.getPrerequisites()) {
                        System.out.println(course.toString());
                    }
                    break;
                } else {
                    System.out.println("No prerequisite for this course.");
                }
            }
            j++;
        }
    }





    //    --------------------------------------------------------------------------------------------
    // Payments Management Menu:
    public static int managePaymentsMenu() {
        System.out.println("*** ------------------ Managing Payments ------------------ ***");
        System.out.println("    1. List Payment Slips");
        System.out.println("    2. Quit");
        System.out.print("* Please choose an option: ");
        return validateNumberChoice(2);
    }

    // 1 List payment slips
    public static void listPaymentSlips() {
        int i = 1;
        System.out.println("________________________________________________________");

        if (!Payment.payments.isEmpty()) {
            System.out.println("#Payments slip list --- ");
            for (Payment payment :  Payment.payments) {
                System.out.println("    " + i + ". " + payment.toString());
                i++;
            }
            System.out.println("________________________________________________________");
        } else {
            System.out.println("Currently, no registered payment slips found in the server. Please, register.");
        }
    }

    //    --------------------------------------------------------------------------------------------
    // Helper function
    public static int validateNumberChoice(int options) {
        while (true) {
            try {
                int select = sc.nextInt();
                sc.nextLine();
                if (select < 1 || select > options) {
                    System.out.print("Invalid choice, please choose between 1 - " + options + ": ");
                    continue;
                }
                return select;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please, use numbers: ");
                sc.nextLine();
            }
        }
    }

    public static void exitNow() {
        System.exit(0);
    }

    public static <T> void listData(List<T> container, String introPrompt, String headerPrompt, String errorPrompt) {
        if (container == null || container.isEmpty()) {
            System.out.println(errorPrompt);
        } else {
            int i = 1;
            System.out.println("________________________________________________________");
            System.out.println(introPrompt);
            System.out.println(headerPrompt);

            for (T item : container) {
                System.out.println("    " + i + ". "+ item.toString());
                i++;
            }
            System.out.println("________________________________________________________");
        }
    }
}
