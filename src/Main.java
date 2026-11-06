
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            ArrayList<Course> courses = new ArrayList<>();

            boolean running = true;

            while (running) {
                System.out.println("1. Add Course");
                System.out.println("2. Display All Courses");
                System.out.println("3. Search for a Course");
                System.out.println("4. Calculate Total Units");
                System.out.println("5. Exit");

                String choice = scanner.nextLine();

                switch (choice) {

                    case "1":
                        System.out.print("Enter course code: ");
                        String courseCode = scanner.nextLine();

                        System.out.print("Enter course title: ");
                        String courseTitle = scanner.nextLine();

                        System.out.print("Enter course unit: ");
                        int courseUnit = Integer.parseInt(scanner.nextLine());

                        Course course = new Course();
                        course.courseCode = courseCode;
                        course.courseTitle = courseTitle;
                        course.courseUnit = courseUnit;

                        courses.add(course);

                        System.out.println("Course added successfully!");
                        break;

                    case "2":
                        System.out.println("\n--- Registered Courses ---");

                        if (courses.isEmpty()) {
                            System.out.println("No courses have been added yet.");
                        } else {
                            for (Course currentCourse : courses) {
                                currentCourse.displayCourse();
                                System.out.println();
                            }
                        }
                        break;

                    case "3":
                        System.out.print("Enter course code to search: ");
                        String searchCode = scanner.nextLine();

                        boolean found = false;

                        for (Course currentCourse : courses) {

                            if (currentCourse.courseCode.equalsIgnoreCase(searchCode)) {
                                System.out.println("\nCourse found!");
                                currentCourse.displayCourse();
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("Course not found.");
                        }
                        break;

                    case "4":
                        int totalUnits = 0;
                        for (Course currentCourse : courses) {
                            totalUnits += currentCourse.courseUnit;
                        }
                        System.out.println("Total units for all registered courses: " + totalUnits);
                        break;

                    case "5":
                        running = false;
                        System.out.println("Exiting the system. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}
