import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Simple Swing interface for the Student Course Management System. */
public class CourseManagementGUI extends JFrame {

    // Menu options (the 7 required operations)
    private static final int ADD_COURSE = 1;
    private static final int VIEW_ALL = 2;
    private static final int SEARCH_COURSE = 3;
    private static final int COMPUTE_TOTAL = 4;
    private static final int SAVE_FILE = 5;
    private static final int LOAD_FILE = 6;
    private static final int EXIT_PROGRAM = 7;

    // All course data and operations live in this object
    private final CourseManager manager = new CourseManager();

    // GUI components
    private JTextField codeField;
    private JTextField titleField;
    private JTextField unitField;
    private JTextField searchField;

    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;

    public CourseManagementGUI() {

        setTitle("Student Course Management System");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Closing the window behaves the same as the Exit option
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleMenuChoice(EXIT_PROGRAM);
            }
        });

        Color background = new Color(245, 247, 250);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(background);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Heading
        JLabel heading = new JLabel("STUDENT COURSE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(25, 55, 100));

        // Course registration form
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Register a Course"));

        codeField = new JTextField();
        titleField = new JTextField();
        unitField = new JTextField();

        formPanel.add(new JLabel("Course Code (e.g. COS 201)"));
        formPanel.add(new JLabel("Course Title"));
        formPanel.add(new JLabel("Course Unit (1-6)"));
        formPanel.add(new JLabel(""));

        formPanel.add(codeField);
        formPanel.add(titleField);
        formPanel.add(unitField);
        formPanel.add(makeButton("Add Course", ADD_COURSE));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(18);

        searchPanel.add(new JLabel("Search Course Code:"));
        searchPanel.add(searchField);
        searchPanel.add(makeButton("Search", SEARCH_COURSE));
        searchPanel.add(makeButton("View All Courses", VIEW_ALL));

        // Top section = heading + form + search
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(background);
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.setBackground(background);
        northPanel.add(heading, BorderLayout.NORTH);
        northPanel.add(topPanel, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Course table
        String[] columns = {"Course Code", "Course Title", "Course Unit"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(28);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Registered Courses"));
        tablePanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottomPanel.setBackground(background);

        totalLabel = new JLabel("Total Units: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));

        bottomPanel.add(makeButton("Compute Total Units", COMPUTE_TOTAL));
        bottomPanel.add(makeButton("Save to File", SAVE_FILE));
        bottomPanel.add(makeButton("Load from File", LOAD_FILE));
        bottomPanel.add(makeButton("Exit", EXIT_PROGRAM));
        bottomPanel.add(totalLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /** Creates a button that sends its menu number to handleMenuChoice. */
    private JButton makeButton(String text, int menuChoice) {
        JButton button = new JButton(text);
        button.addActionListener(e -> handleMenuChoice(menuChoice));
        return button;
    }

    /** MENU SELECTION: decides which operation to run. */
    private void handleMenuChoice(int choice) {
        switch (choice) {
            case ADD_COURSE:
                addCourse();
                break;
            case VIEW_ALL:
                displayAllCourses();
                break;
            case SEARCH_COURSE:
                searchCourse();
                break;
            case COMPUTE_TOTAL:
                computeTotalUnits();
                break;
            case SAVE_FILE:
                saveCourses();
                break;
            case LOAD_FILE:
                loadCourses();
                break;
            case EXIT_PROGRAM:
                exitProgram();
                break;
            default:
                showError("Invalid menu option.");
        }
    }

    // 1. ADD COURSE
    private void addCourse() {
        try {
            manager.addCourse(codeField.getText(), titleField.getText(), unitField.getText());

            displayAllCourses();
            clearFields();

            JOptionPane.showMessageDialog(this, "Course added successfully!");

        } catch (InvalidCourseException e) {
            showError(e.getMessage());
        }
    }

    // 2. VIEW ALL COURSES
    private void displayAllCourses() {
        tableModel.setRowCount(0);

        for (Course course : manager.getCourses()) {
            addRow(course);
        }

        updateTotalLabel();
    }

    // 3. SEARCH COURSE BY CODE
    private void searchCourse() {
        String searchCode = searchField.getText().trim();

        if (searchCode.isEmpty()) {
            showError("Please enter a course code to search for.");
            return;
        }

        Course found = manager.searchByCode(searchCode);

        if (found == null) {
            JOptionPane.showMessageDialog(this, "Course not found.");
            displayAllCourses();
        } else {
            tableModel.setRowCount(0);
            addRow(found);
        }
    }

    // 4. COMPUTE TOTAL UNITS
    private void computeTotalUnits() {
        updateTotalLabel();

        JOptionPane.showMessageDialog(
            this,
            "Total units: " + manager.computeTotalUnits()
        );
    }

    // 5. SAVE TO FILE
    private void saveCourses() {
        try {
            manager.saveToFile();

            JOptionPane.showMessageDialog(
                this,
                manager.getCourses().size() + " course(s) saved to " + CourseManager.DATA_FILE
            );

        } catch (IOException e) {
            showError("Error saving courses: " + e.getMessage());
        }
    }

    // 6. LOAD FROM FILE
    private void loadCourses() {
        try {
            int count = manager.loadFromFile();
            displayAllCourses();

            String message = count + " course(s) loaded successfully.";
            if (manager.getSkippedRecords() > 0) {
                message += "\n" + manager.getSkippedRecords() + " invalid record(s) were skipped.";
            }
            JOptionPane.showMessageDialog(this, message);

        } catch (FileNotFoundException e) {
            showError("No saved file found. Save some courses first.");
        } catch (IOException e) {
            showError("Error loading courses: " + e.getMessage());
        }
    }

    // 7. EXIT PROGRAM
    private void exitProgram() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit",
            JOptionPane.YES_NO_OPTION
        );

        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ---------- helper methods ----------

    private void addRow(Course course) {
        Object[] row = {
            course.getCourseCode(),
            course.getCourseTitle(),
            course.getCourseUnit()
        };
        tableModel.addRow(row);
    }

    private void clearFields() {
        codeField.setText("");
        titleField.setText("");
        unitField.setText("");
        codeField.requestFocus();
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total Units: " + manager.computeTotalUnits());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // APPLICATION ENTRY POINT
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseManagementGUI().setVisible(true));
    }
}
