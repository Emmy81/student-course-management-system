import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            // Create an ArrayList to store courses
            ArrayList<Course> courses = new ArrayList<>();

            // Collect the first course
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine();

            System.out.print("Enter course title: ");
            String courseTitle = scanner.nextLine();

            System.out.print("Enter course unit: ");
            int courseUnit = Integer.parseInt(scanner.nextLine());

            // Create the first course object
            Course course1 = new Course();
            course1.courseCode = courseCode;
            course1.courseTitle = courseTitle;
            course1.courseUnit = courseUnit;

            // Add the first course to the ArrayList
            courses.add(course1);

            // Collect the second course
            System.out.print("Enter another course code: ");
            String anotherCourseCode = scanner.nextLine();

            System.out.print("Enter another course title: ");
            String anotherCourseTitle = scanner.nextLine();

            System.out.print("Enter another course unit: ");
            int anotherCourseUnit = Integer.parseInt(scanner.nextLine());

            // Create the second course object
            Course course2 = new Course();
            course2.courseCode = anotherCourseCode;
            course2.courseTitle = anotherCourseTitle;
            course2.courseUnit = anotherCourseUnit;

            // Add the second course to the ArrayList
            courses.add(course2);

            // Display all courses in the ArrayList
            System.out.println("\n--- Registered Courses ---");

            for (Course course : courses) {
                course.displayCourse();
                System.out.println();
            }
        }
    }
}