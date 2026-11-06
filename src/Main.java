public class Main {
    public static void main(String[] args) {

        Course course1 = new Course();

        course1.courseCode = "COS 201";
        course1.courseTitle = "Programming I";
        course1.courseUnit = 3;

        Course course2 = new Course();

        course2.courseCode = "COS 203";
        course2.courseTitle = "Discrete Structures";
        course2.courseUnit = 2;

        course1.displayCourse();
        course2.displayCourse();
    }
}