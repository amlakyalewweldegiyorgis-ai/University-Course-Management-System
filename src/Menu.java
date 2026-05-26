import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {
    static Scanner sc = new Scanner(System.in);

    // Main Menu
    public static int mainMenu() {
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
        System.out.println("*** ------------------ Managing Students ------------------ ***");
        System.out.println("    1. Register new student");
        System.out.println("    2. Enroll student");
        System.out.println("    3. List registered students");
        System.out.println("    4. Remove registered student");
        System.out.println("    5. See student's details");
        System.out.println("    6. Quit");
        System.out.print("* Please choose an option: ");
        return validateNumberChoice(6);
    }

    //  1  Registering new student:
    public static void registerNewStudent() {
        Scanner input = new Scanner(System.in);

        System.out.println("*** ------------------ Registering New Student ------------------ ***");
        System.out.print("    .Enter student's full name: ");
        String fullName = validateFullName();

        System.out.print("    .Enter student's gender (M/F): ");
        String gender = validateGender();

        System.out.print("    .Enter student's id: ");
        int id = validateNumberChoiceForId();

        Student student = new Student(fullName, id, gender);
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
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Enrolling Student ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
            System.out.print("* Choose the student to enroll from the list: ");
            int chosenStudent = input.nextInt();
            input.nextLine();

            System.out.println("Student " + chosenStudent + " is choosen . . . ");
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
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Selecting Course for student ------------------ ***");

        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
        System.out.print("* Select the course to enroll from the list: ");
        int chosenCourse = input.nextInt();
        input.nextLine();

        System.out.println(chosenCourse + " is choosen . . . ");
        int j = 1;

        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == chosenCourse) {
                EnrollmentSystem.enroll(student, c);
                break;
            }
            j++;
        }
    }

    //  3 List registered students
    // Handled by Menu.java

    //  4  Remove registered student
    public static void removeRegisteredStudent() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Removing Registered Student ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
            System.out.print("* Choose the student to remove from the list: ");
            int chosenStudent = input.nextInt();
            input.nextLine();

            System.out.println(chosenStudent + " is choosen . . . ");
            int j = 1;

            for (Student student :  EnrollmentSystem.registeredStudents) {
                if (j == chosenStudent) {
                    EnrollmentSystem.registeredStudents.remove(student);
                    System.out.println("\uD83C\uDF89 Choosen student '" + j + "' has been removed, successfully!");
                    break;
                }
                j++;
            }
    }

    // 5 See student's details
    public static void seeStudentsDetails() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Showing Students Detail ------------------ ***");

        String student_list_header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Full Name", "Id", "Gender");
        Menu.listData(EnrollmentSystem.registeredStudents, "#Registered Students . . .", student_list_header, "No registered students are found, please register.");
            System.out.print("* Choose the student to see more detail: ");
            int chosenStudent = input.nextInt();
            input.nextLine();

            System.out.println("Student " + chosenStudent + " is choosen . . . ");
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

        if (!student.getCurrentCourse().isEmpty()) {
            int i = 1;

            System.out.println("Currently Enrolled courses . . . ");
            System.out.printf("    %-4s %-21s %-13s %-9s%n", "No.", "Course Name", "Code", "Fee");
            for (Course c : student.getCurrentCourse()) {
                System.out.println("    " + i + ". " + c.toString());
                i++;
            }
            listTakenCourses(student);
        } else {
            System.out.println("There is no detail to see.");
        }

    }

    private static void listTakenCourses(Student student) {
        System.out.println("Taken courses . . . ");
        if (student.getCurrentCourse().isEmpty()) {
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
        Scanner input = new Scanner(System.in);

        System.out.print("    .Enter course name: ");
        String name = input.nextLine();

        System.out.print("    .Enter course code: ");
        String code = input.nextLine();

        System.out.print("    .Enter course fee: ");
        double fee = input.nextDouble();

        return new Course(name, code, fee);
    }

    //  1  Add new course
    public static void addNewCourseMenu() {

        System.out.println("*** ------------------ Adding New Course ------------------ ***");
        Course course = addCourse();

        if (!EnrollmentSystem.availableCourses.contains(course)) {
            EnrollmentSystem.availableCourses.add(course);
            System.out.println("\uD83C\uDF89 Course '" + course.getCourseName() + "' is added successfully!");
        } else {
            System.out.println("Course already exists");
        }
    }

    // 2 Update existing course
    public static void updateExistingCourseMenu() {
        Scanner input = new Scanner(System.in);

        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
        System.out.print("* Choose the course to update from the list: ");
        int chosenCourse = input.nextInt();
        input.nextLine();

        System.out.println(chosenCourse + " is choosen . . . ");

        System.out.print("    .Enter updated course name: ");
        String name = input.nextLine();

        System.out.print("    .Enter updated course code: ");
        String code = input.nextLine();

        System.out.print("    .Enter updated course fee: ");
        double fee = input.nextDouble();

        int j = 1;
        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == chosenCourse) {
                c.updateCourse(name, code, fee);
                System.out.println("\uD83C\uDF89 Choosen course '" + j + "' is updated to '"+ c.getCourseName()+ "', successfully!");
                break;
            }
            j++;
        }
    }

    // 3 Delete existing course
    public static void deleteExistingCourseMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Deleting Existing Course ------------------ ***");

        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
        System.out.print("* Choose the course to delete from the list: ");
        int chosenCourse = input.nextInt();
        input.nextLine();

        System.out.println(chosenCourse + " is choosen . . . ");
        int j = 1;

        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == chosenCourse) {
                EnrollmentSystem.availableCourses.remove(c);
                System.out.println("\uD83C\uDF89 Choosen course '" + j + "' is deleted, successfully!");
                break;
            }
            j++;
        }
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");
    }

    // 4 Adding prerequisite
    public static void addPrerequisiteMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Adding Prerequisite ------------------ ***");
        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");

        System.out.print("Choose the course you want to add prerequisite for (Use Number): ");
        int selectedCourse = input.nextInt();

        System.out.println(selectedCourse + " is choosen . . . ");
        int j = 1;

        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == selectedCourse) {
                Course course = addCourse();
                c.addPrerequisite(course);
                System.out.println("\uD83C\uDF89 Adding prerequisite for selected course '" + c.getCourseName() + "' is done, successfully!");
                break;
            }
            j++;
        }
    }

    // 5 Show Prerequisite
    public static void showPrerequisiteMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("*** ------------------ Showing Prerequisite ------------------ ***");
        String header = String.format("    %-4s %-21s %-13s %-9s", "No.", "Course Name", "Code", "Fee");
        Menu.listData(EnrollmentSystem.availableCourses, "#Available Courses . . . ", header, "No registered courses are found, please register.");

        System.out.print("Choose the course you want to see prerequisite for (Use Number): ");
        int selectedCourse = input.nextInt();

        System.out.println(selectedCourse + " is choosen . . . ");
        int j = 1;

        for (Course c :  EnrollmentSystem.availableCourses) {
            if (j == selectedCourse) {
                if (!c.getPrerequisites().isEmpty()) {
                    System.out.println("#Showing list of prerequisite for course '" + c.getCourseName() + "'");
                    System.out.println("Name       Code         Fee");
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
                    System.out.println("Invalid choice, please choose between 1 - " + options + ".");
                    continue;
                }
                return select;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please, use numbers.");
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
