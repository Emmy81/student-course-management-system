import javax.swing.*;
import java.awt.*;

public class CourseManagementGUI extends JFrame {

    public CourseManagementGUI() {

        // Configure the main application window
        setTitle("Student Course Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        // Create the heading
        JLabel heading = new JLabel(
            "STUDENT COURSE MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        // Add heading to the main panel
        mainPanel.add(heading, BorderLayout.NORTH);

        // Add the panel to the window
        add(mainPanel);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CourseManagementGUI window =
                new CourseManagementGUI();

            window.setVisible(true);
        });
    }
}
