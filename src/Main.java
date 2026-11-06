public class Main {
    public static void main(String[] args) {

        Course course1 = new Course();

        course1.courseCode = "COS 201";
        course1.courseTitle = "Programming I";
        course1.courseUnit = 3;

        System.out.println(course1.courseCode);
        System.out.println(course1.courseTitle);
        System.out.println(course1.courseUnit);
    }
}