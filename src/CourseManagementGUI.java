import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class CourseManagementGUI extends JFrame {

    // Stores all course objects
    private final ArrayList<Course> courses = new ArrayList<>();

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
        setSize(950, 650);
        setMinimumSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Heading
        JLabel heading = new JLabel(
            "STUDENT COURSE MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(25, 55, 100));

        mainPanel.add(heading, BorderLayout.NORTH);

        // Course registration form
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 8));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
            BorderFactory.createTitledBorder("Register a Course")
        );

        codeField = new JTextField();
        titleField = new JTextField();
        unitField = new JTextField();

        JButton addButton = new JButton("Add Course");

        formPanel.add(new JLabel("Course Code"));
        formPanel.add(new JLabel("Course Title"));
        formPanel.add(new JLabel("Course Unit"));
        formPanel.add(new JLabel(""));

        formPanel.add(codeField);
        formPanel.add(titleField);
        formPanel.add(unitField);
        formPanel.add(addButton);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(18);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchPanel.add(new JLabel("Search Course Code:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Top section
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 247, 250));
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Course table
        String[] columns = {
            "Course Code",
            "Course Title",
            "Course Unit"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(28);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 14));

        courseTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane = new JScrollPane(courseTable);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
            BorderFactory.createTitledBorder("Registered Courses")
        );

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom action buttons
        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 10)
        );

        bottomPanel.setBackground(new Color(245, 247, 250));

        JButton totalButton = new JButton("Calculate Total Units");
        JButton recursiveButton = new JButton("Recursive Total");
        JButton saveButton = new JButton("Save Courses");
        JButton loadButton = new JButton("Load Courses");

        totalLabel = new JLabel("Total Units: 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));

        bottomPanel.add(totalButton);
        bottomPanel.add(recursiveButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(loadButton);
        bottomPanel.add(totalLabel);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button actions
        addButton.addActionListener(e -> addCourse());

        searchButton.addActionListener(e -> searchCourse());

        showAllButton.addActionListener(e -> refreshTable());

        totalButton.addActionListener(e -> calculateTotalUnits());

        recursiveButton.addActionListener(e -> calculateRecursiveTotal());

        saveButton.addActionListener(e -> saveCourses());

        loadButton.addActionListener(e -> loadCourses());
    }

    // ADD COURSE
    private void addCourse() {

        String code = codeField.getText().trim();
        String title = titleField.getText().trim();
        String unitText = unitField.getText().trim();

        // Validate empty fields
        if (code.isEmpty() || title.isEmpty() || unitText.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please complete all course fields."
            );

            return;
        }

        try {

            int unit = Integer.parseInt(unitText);

            if (unit <= 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Course unit must be greater than zero."
                );

                return;
            }

            // Create a new Course object
            Course course = new Course();

            course.courseCode = code;
            course.courseTitle = title;
            course.courseUnit = unit;

            courses.add(course);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                this,
                "Course added successfully!"
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this,
                "Invalid course unit. Please enter a number."
            );
        }
    }

    // DISPLAY ALL COURSES
    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Course course : courses) {

            Object[] row = {
                course.courseCode,
                course.courseTitle,
                course.courseUnit
            };

            tableModel.addRow(row);
        }

        updateTotalLabel();
    }

    // SEARCH FOR A COURSE
    private void searchCourse() {

        String searchCode = searchField.getText().trim();

        if (searchCode.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please enter a course code."
            );

            return;
        }

        tableModel.setRowCount(0);

        boolean found = false;

        for (Course course : courses) {

            if (course.courseCode.equalsIgnoreCase(searchCode)) {

                Object[] row = {
                    course.courseCode,
                    course.courseTitle,
                    course.courseUnit
                };

                tableModel.addRow(row);

                found = true;
                break;
            }
        }

        if (!found) {

            JOptionPane.showMessageDialog(
                this,
                "Course not found."
            );

            refreshTable();

        } else {

            updateTotalLabel();
        }
    }

    // CALCULATE TOTAL UNITS USING A LOOP
    private void calculateTotalUnits() {

        int total = 0;

        for (Course course : courses) {
            total += course.courseUnit;
        }

        totalLabel.setText("Total Units: " + total);
    }

    // CALCULATE TOTAL UNITS USING RECURSION
    private void calculateRecursiveTotal() {

        int total = calculateTotalUnitsRecursively(courses, 0);

        totalLabel.setText("Recursive Total: " + total);
    }

    // RECURSIVE METHOD
    private int calculateTotalUnitsRecursively(
            ArrayList<Course> courses, int index) {

        // Base case
        if (index == courses.size()) {
            return 0;
        }

        // Recursive case
        return courses.get(index).courseUnit
            + calculateTotalUnitsRecursively(courses, index + 1);
    }

    // SAVE COURSES TO FILE
    private void saveCourses() {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("courses.txt"))) {

            for (Course course : courses) {

                writer.write(
                    course.courseCode + "|" +
                    course.courseTitle + "|" +
                    course.courseUnit
                );

                writer.newLine();
            }

            JOptionPane.showMessageDialog(
                this,
                "Courses saved successfully!"
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                this,
                "Error saving courses: " + e.getMessage()
            );
        }
    }

    // LOAD COURSES FROM FILE
    private void loadCourses() {

        ArrayList<Course> loadedCourses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader("courses.txt"))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] details = line.split("\\|", -1);

                if (details.length == 3) {

                    try {

                        int unit = Integer.parseInt(details[2]);

                        if (details[0].trim().isEmpty()
                                || details[1].trim().isEmpty()
                                || unit <= 0) {
                            continue;
                        }

                        Course course = new Course();

                        course.courseCode = details[0];
                        course.courseTitle = details[1];
                        course.courseUnit = unit;

                        loadedCourses.add(course);

                    } catch (NumberFormatException e) {

                        System.out.println(
                            "Skipping invalid course record."
                        );
                    }
                }
            }

            courses.clear();
            courses.addAll(loadedCourses);

            refreshTable();

            JOptionPane.showMessageDialog(
                this,
                "Courses loaded successfully!\nTotal courses: "
                    + courses.size()
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                this,
                "Error loading courses: " + e.getMessage()
            );
        }
    }

    // CLEAR INPUT FIELDS
    private void clearFields() {

        codeField.setText("");
        titleField.setText("");
        unitField.setText("");
    }

    // UPDATE TOTAL DISPLAY
    private void updateTotalLabel() {

        int total = 0;

        for (Course course : courses) {
            total += course.courseUnit;
        }

        totalLabel.setText("Total Units: " + total);
    }

    // APPLICATION ENTRY POINT
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CourseManagementGUI window =
                new CourseManagementGUI();

            window.setVisible(true);
        });
    }
}