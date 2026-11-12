import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/** Stores the courses and performs every operation. Contains no GUI code. */
public class CourseManager {

    public static final String DATA_FILE = "courses.txt";

    // The internal structure that stores all courses
    private final ArrayList<Course> courses = new ArrayList<>();

    // Number of bad lines ignored during the last load
    private int skippedRecords = 0;

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public int getSkippedRecords() {
        return skippedRecords;
    }

    // ------------------------------------------------------------------
    // STRING PROCESSING (trimming, casing, validation)
    // ------------------------------------------------------------------

    /** Trims, converts to upper case and collapses extra spaces: " cos   201 " -> "COS 201". */
    private String cleanCode(String rawCode) {
        if (rawCode == null) {
            return "";
        }
        return rawCode.trim().toUpperCase().replaceAll("\\s+", " ");
    }

    /** Validates the three inputs and builds a Course. Throws InvalidCourseException if anything is wrong. */
    private Course buildCourse(String rawCode, String rawTitle, String rawUnit)
            throws InvalidCourseException {

        String code = cleanCode(rawCode);
        String title = (rawTitle == null) ? "" : rawTitle.trim().replaceAll("\\s+", " ");
        String unitText = (rawUnit == null) ? "" : rawUnit.trim();

        if (code.isEmpty() || title.isEmpty() || unitText.isEmpty()) {
            throw new InvalidCourseException("Please complete all course fields.");
        }

        if (!code.matches("[A-Z]{2,4} ?\\d{3}")) {
            throw new InvalidCourseException(
                "Invalid course code. Use a format like COS 201 (2-4 letters followed by 3 digits).");
        }

        if (title.contains("|")) {
            throw new InvalidCourseException("Course title cannot contain the '|' character.");
        }

        int unit;
        try {
            unit = Integer.parseInt(unitText);
        } catch (NumberFormatException e) {
            throw new InvalidCourseException("Invalid course unit. Please enter a whole number.");
        }

        if (unit < 1 || unit > 6) {
            throw new InvalidCourseException("Course unit must be between 1 and 6.");
        }

        return new Course(code, title, unit);
    }

    // ------------------------------------------------------------------
    // 1. ADD COURSE
    // ------------------------------------------------------------------
    public Course addCourse(String rawCode, String rawTitle, String rawUnit)
            throws InvalidCourseException {

        Course course = buildCourse(rawCode, rawTitle, rawUnit);

        if (searchByCode(course.getCourseCode()) != null) {
            throw new InvalidCourseException(
                "A course with code " + course.getCourseCode() + " has already been added.");
        }

        courses.add(course);
        return course;
    }

    // ------------------------------------------------------------------
    // 3. SEARCH COURSE BY CODE (uses RECURSION)
    // ------------------------------------------------------------------
    public Course searchByCode(String rawCode) {
        return searchRecursively(cleanCode(rawCode), 0);
    }

    /** RECURSIVE METHOD: checks one course, then calls itself for the next index. */
    private Course searchRecursively(String code, int index) {
        // Base case 1: reached the end of the list, course not found
        if (index >= courses.size()) {
            return null;
        }
        // Base case 2: course found
        if (courses.get(index).getCourseCode().equals(code)) {
            return courses.get(index);
        }
        // Recursive case: search the rest of the list
        return searchRecursively(code, index + 1);
    }

    // ------------------------------------------------------------------
    // 4. COMPUTE TOTAL UNITS (uses RECURSION)
    // ------------------------------------------------------------------
    public int computeTotalUnits() {
        return totalUnitsRecursively(0);
    }

    /** RECURSIVE METHOD: units of this course + total units of all the remaining courses. */
    private int totalUnitsRecursively(int index) {
        // Base case: no more courses
        if (index == courses.size()) {
            return 0;
        }
        // Recursive case
        return courses.get(index).getCourseUnit() + totalUnitsRecursively(index + 1);
    }

    // ------------------------------------------------------------------
    // 5. SAVE TO FILE
    // ------------------------------------------------------------------
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Course course : courses) {
                writer.write(course.toFileLine());
                writer.newLine();
            }
        }
    }

    // ------------------------------------------------------------------
    // 6. LOAD FROM FILE
    // ------------------------------------------------------------------
    /** Replaces the current list with the courses in the file. Returns how many were loaded. */
    public int loadFromFile() throws IOException {
        ArrayList<Course> loaded = new ArrayList<>();
        skippedRecords = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] details = line.split("\\|", -1);

                try {
                    if (details.length != 3) {
                        throw new InvalidCourseException("Wrong number of fields.");
                    }

                    Course course = buildCourse(details[0], details[1], details[2]);

                    if (containsCode(loaded, course.getCourseCode())) {
                        throw new InvalidCourseException("Duplicate course code.");
                    }

                    loaded.add(course);

                } catch (InvalidCourseException e) {
                    skippedRecords++;   // bad record: ignore it and keep reading
                }
            }
        }

        courses.clear();
        courses.addAll(loaded);
        return loaded.size();
    }

    private boolean containsCode(ArrayList<Course> list, String code) {
        for (Course course : list) {
            if (course.getCourseCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
