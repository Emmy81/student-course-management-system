
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            ArrayList<Course> courses = new ArrayList<>();

            boolean running = true;

            while (running) {

                System.out.println("\n===== STUDENT COURSE MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Course");
                System.out.println("2. Display All Courses");
                System.out.println("3. Search for a Course");
                System.out.println("4. Calculate Total Units (Using Loop)");
                System.out.println("5. Save Courses");
                System.out.println("6. Load Courses");
                System.out.println("7. Exit");
                System.out.println("8. Calculate Total Units (Using Recursion)");

                System.out.print("Choose an option: ");

                String choice = scanner.nextLine();

                switch (choice) {

                    // ADD COURSE
                    case "1":

                        System.out.print("Enter course code: ");
                        String courseCode = scanner.nextLine();

                        System.out.print("Enter course title: ");
                        String courseTitle = scanner.nextLine();

                        int courseUnit;

                        try {
                            System.out.print("Enter course unit: ");
                            courseUnit = Integer.parseInt(scanner.nextLine());

                            if (courseUnit <= 0) {
                                System.out.println(
                                    "Course unit must be greater than zero."
                                );
                                break;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println(
                                "Invalid input! Please enter a number."
                            );
                            break;
                        }

                        Course course = new Course();

                        course.courseCode = courseCode;
                        course.courseTitle = courseTitle;
                        course.courseUnit = courseUnit;

                        courses.add(course);

                        System.out.println("Course added successfully!");

                        break;

                    // DISPLAY ALL COURSES
                    case "2":

                        System.out.println("\n--- Registered Courses ---");

                        if (courses.isEmpty()) {
                            System.out.println(
                                "No courses have been added yet."
                            );

                        } else {

                            for (Course currentCourse : courses) {
                                currentCourse.displayCourse();
                                System.out.println();
                            }
                        }

                        break;

                    // SEARCH FOR A COURSE
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

                    // CALCULATE TOTAL UNITS USING A LOOP
                    case "4":

                        int totalUnits = 0;

                        for (Course currentCourse : courses) {
                            totalUnits += currentCourse.courseUnit;
                        }

                        System.out.println(
                            "Total units: " + totalUnits
                        );

                        break;

                    // SAVE COURSES
                    case "5":

                        try (BufferedWriter writer = new BufferedWriter(
                                new FileWriter("courses.txt"))) {

                            for (Course currentCourse : courses) {

                                writer.write(
                                    currentCourse.courseCode + "|" +
                                    currentCourse.courseTitle + "|" +
                                    currentCourse.courseUnit
                                );

                                writer.newLine();
                            }

                            System.out.println(
                                "Courses saved successfully!"
                            );

                        } catch (IOException e) {

                            System.out.println(
                                "Error saving courses: " + e.getMessage()
                            );
                        }

                        break;

                    // LOAD COURSES
                    case "6":

                        ArrayList<Course> loadedCourses = new ArrayList<>();

                        try (BufferedReader reader = new BufferedReader(
                                new FileReader("courses.txt"))) {

                            String line;

                            while ((line = reader.readLine()) != null) {

                                String[] details = line.split("\\|", -1);

                                if (details.length == 3) {

                                    try {

                                        Course loadedCourse = new Course();

                                        loadedCourse.courseCode = details[0];
                                        loadedCourse.courseTitle = details[1];
                                        loadedCourse.courseUnit =
                                            Integer.parseInt(details[2]);

                                        loadedCourses.add(loadedCourse);

                                    } catch (NumberFormatException e) {

                                        System.out.println(
                                            "Skipping invalid course record."
                                        );
                                    }
                                }
                            }

                            courses.clear();
                            courses.addAll(loadedCourses);

                            System.out.println(
                                "Courses loaded successfully!"
                            );

                            System.out.println(
                                "Total courses loaded: " + courses.size()
                            );

                        } catch (IOException e) {

                            System.out.println(
                                "Error loading courses: " + e.getMessage()
                            );
                        }

                        break;

                    // EXIT
                    case "7":

                        running = false;

                        System.out.println(
                            "Exiting the system. Goodbye!"
                        );

                        break;

                    // CALCULATE TOTAL UNITS USING RECURSION
                    case "8":

                        int recursiveTotal =
                            calculateTotalUnitsRecursively(courses, 0);

                        System.out.println(
                            "Total units (using recursion): " + recursiveTotal
                        );

                        break;

                    // INVALID MENU OPTION
                    default:

                        System.out.println(
                            "Invalid option. Please try again."
                        );
                }
            }
        }
    }

    // RECURSIVE METHOD TO CALCULATE TOTAL COURSE UNITS
    static int calculateTotalUnitsRecursively(
            ArrayList<Course> courses, int index) {

        // BASE CASE: Stop when there are no more courses.
        if (index == courses.size()) {
            return 0;
        }

        // RECURSIVE CASE: Add the current course units
        // and call this method again for the next course.
        return courses.get(index).courseUnit
                + calculateTotalUnitsRecursively(courses, index + 1);
    }
}
