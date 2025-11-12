import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class WeeFitLoginUI {
    private static final HashMap<String, String> users = new HashMap<>();
    private static final String FILE_PATH = "users.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeeFitLoginUI().createAndShowGUI();
        });
    }

    public void createAndShowGUI() {
        loadUsersFromFile();

        JFrame frame = new JFrame("WeeFit Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to WeeFit!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(45, 45, 45));

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // Username field
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        panel.add(messageLabel, gbc);

        // Buttons
        gbc.gridy++;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton loginBtn = createButton("Login");
        JButton createBtn = createButton("Create Account");

        buttonPanel.add(loginBtn);
        buttonPanel.add(createBtn);
        panel.add(buttonPanel, gbc);

        // --- Button Actions ---
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(new Color(200, 0, 0));
                messageLabel.setText("Please enter both username and password.");
                return;
            }

            if (authenticate(username, password)) {
                messageLabel.setForeground(new Color(0, 153, 51));
                messageLabel.setText("Login successful! Welcome, " + username + ".");
            } else {
                messageLabel.setForeground(new Color(200, 0, 0));
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });

        createBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(new Color(200, 0, 0));
                messageLabel.setText("Please fill in both fields.");
                return;
            }

            if (users.containsKey(username)) {
                messageLabel.setForeground(new Color(200, 0, 0));
                messageLabel.setText("Username already exists. Try another.");
            } else {
                users.put(username, password);
                saveUserToFile(username, password);
                messageLabel.setForeground(new Color(0, 153, 51));
                messageLabel.setText("Account created! You can now log in.");
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(55, 125, 255));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private static void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing users file: " + e.getMessage());

        }
    }

    private static void saveUserToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error reading or writing users file: " + e.getMessage());

        }
    }
}