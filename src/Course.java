/** Represents one course. Fields are private and accessed through getters. */
public class Course {
    private final String courseCode;
    private final String courseTitle;
    private final int courseUnit;

    public Course(String courseCode, String courseTitle, int courseUnit) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.courseUnit = courseUnit;
    }

    public String getCourseCode()  { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getCourseUnit()     { return courseUnit; }

    /** One line of the save file: CODE|TITLE|UNIT */
    public String toFileLine() {
        return courseCode + "|" + courseTitle + "|" + courseUnit;
    }
}
