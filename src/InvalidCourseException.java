/** Thrown when course data fails validation (empty field, bad code, bad unit, duplicate...). */
public class InvalidCourseException extends Exception {
    public InvalidCourseException(String message) {
        super(message);
    }
}
