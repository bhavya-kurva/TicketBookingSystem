package com.railway.ui;

import com.railway.dao.UserDAO;
import com.railway.model.User;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initUI();
    }
    
    private void initUI() {
        setTitle("Railway Ticket Booking System");
        setSize(450, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 51, 102),
                        0, getHeight(), new Color(0, 102, 204));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Railway Ticket Booking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255, 240));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints lgbc = new GridBagConstraints();
        lgbc.insets = new Insets(10, 10, 10, 10);
        
        // Username
        lgbc.gridx = 0;
        lgbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), lgbc);
        
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lgbc.gridx = 1;
        loginPanel.add(usernameField, lgbc);
        
        // Password
        lgbc.gridx = 0;
        lgbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), lgbc);
        
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        lgbc.gridx = 1;
        loginPanel.add(passwordField, lgbc);
        
        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> doLogin());
        
        lgbc.gridx = 0;
        lgbc.gridy = 2;
        lgbc.gridwidth = 2;
        loginPanel.add(loginButton, lgbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(loginPanel, gbc);
        
        add(mainPanel);
        
        // Enter key triggers login
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter username and password!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        loginButton.setText("Logging in...");
        loginButton.setEnabled(false);
        
        User user = userDAO.login(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this,
                "Welcome " + user.getFullName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            new MainDashboard(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password!\n\nTry: admin / admin123",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
            loginButton.setText("Login");
            loginButton.setEnabled(true);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {}
        
        new LoginFrame().setVisible(true);
    }
}