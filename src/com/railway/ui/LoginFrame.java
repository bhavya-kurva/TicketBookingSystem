package com.railway.ui;

import com.railway.dao.UserDAO;
import com.railway.model.User;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        initUI();
    }
    
    private void initUI() {
        setTitle("Railway Ticket Booking System");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel with Gradient Background
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
        JLabel titleLabel = new JLabel("RAILWAY TICKET BOOKING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitle = new JLabel("Book Your Train Tickets Online");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        mainPanel.add(subtitle, gbc);
        
        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(255, 255, 255, 240));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints lgbc = new GridBagConstraints();
        lgbc.insets = new Insets(10, 10, 10, 10);
        
        // Login Label
        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        loginLabel.setForeground(new Color(0, 51, 102));
        lgbc.gridx = 0;
        lgbc.gridy = 0;
        lgbc.gridwidth = 2;
        loginPanel.add(loginLabel, lgbc);
        
        // Username
        lgbc.gridwidth = 1;
        lgbc.gridy = 1;
        lgbc.gridx = 0;
        loginPanel.add(new JLabel("Username:"), lgbc);
        
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lgbc.gridx = 1;
        loginPanel.add(usernameField, lgbc);
        
        // Password
        lgbc.gridx = 0;
        lgbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), lgbc);
        
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        lgbc.gridx = 1;
        loginPanel.add(passwordField, lgbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.addActionListener(e -> doLogin());
        
        JButton registerButton = new JButton("REGISTER");
        registerButton.setBackground(new Color(34, 139, 34));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(100, 35));
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        lgbc.gridx = 0;
        lgbc.gridy = 3;
        lgbc.gridwidth = 2;
        loginPanel.add(buttonPanel, lgbc);
        
        gbc.gridy = 2;
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
        
        User user = userDAO.login(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(this,
                "Welcome " + user.getFullName() + "!",
                "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            new MainDashboard(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password!\nTry: raj / raj123",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        new LoginFrame().setVisible(true);
    }
}