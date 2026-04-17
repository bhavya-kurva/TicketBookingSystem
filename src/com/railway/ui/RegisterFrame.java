package com.railway.ui;

import com.railway.dao.UserDAO;
import com.railway.model.User;
import java.awt.*;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    
    private JTextField nameField, usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;
    
    public RegisterFrame() {
        userDAO = new UserDAO();
        initUI();
    }
    
    private void initUI() {
        setTitle("Register New Account");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
       
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
        
        
        JLabel title = new JLabel("CREATE NEW ACCOUNT");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);
        
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(10, 10, 10, 10);
        
        
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), fgbc);
        
        nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        fgbc.gridx = 1;
        formPanel.add(nameField, fgbc);
        
        
        fgbc.gridx = 0;
        fgbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), fgbc);
        
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        fgbc.gridx = 1;
        formPanel.add(usernameField, fgbc);
        
        
        fgbc.gridx = 0;
        fgbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), fgbc);
        
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        fgbc.gridx = 1;
        formPanel.add(passwordField, fgbc);
        
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        registerBtn.setBackground(new Color(0, 70, 140));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Arial", Font.BOLD, 12));
        registerBtn.setPreferredSize(new Dimension(100, 35));
        registerBtn.addActionListener(e -> doRegister());
        applyHover(registerBtn, new Color(0, 70, 140), new Color(0, 51, 102));
        
        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        cancelBtn.setBackground(new Color(0, 70, 140));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setPreferredSize(new Dimension(100, 35));
        cancelBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        applyHover(cancelBtn, new Color(0, 70, 140), new Color(0, 51, 102));
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        
        fgbc.gridx = 0;
        fgbc.gridy = 3;
        fgbc.gridwidth = 2;
        formPanel.add(buttonPanel, fgbc);
        
        gbc.gridy = 1;
        mainPanel.add(formPanel, gbc);
        
        add(mainPanel);
    }
    
    private void applyHover(JButton button, Color base, Color hover) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hover);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(base);
            }
        });
    }

    private void doRegister() {
        String fullName = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }
        
        User user = new User(username, password, fullName);
        
        if (userDAO.register(user)) {
            JOptionPane.showMessageDialog(this, 
                "Registration Successful!\nPlease login with your credentials.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Registration Failed!\nUsername may already exist.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
