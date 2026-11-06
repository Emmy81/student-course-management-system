import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine();

            System.out.print("Enter course title: ");
            String courseTitle = scanner.nextLine();

            System.out.print("Enter course unit: ");
            int courseUnit = scanner.nextInt();

            Course course1 = new Course();
            course1.courseCode = courseCode;
            course1.courseTitle = courseTitle;
            course1.courseUnit = courseUnit;

            System.out.print("Enter another course code: ");
            scanner.nextLine(); // Consume the newline character
            String anotherCourseCode = scanner.nextLine();

            System.out.print("Enter another course title: ");
            String anotherCourseTitle = scanner.nextLine();

            System.out.print("Enter another course unit: ");
            int anotherCourseUnit = scanner.nextInt();

            Course course2 = new Course();
            course2.courseCode = anotherCourseCode;
            course2.courseTitle = anotherCourseTitle;
            course2.courseUnit = anotherCourseUnit;

            course1.displayCourse();
            course2.displayCourse();
        }
    }
}