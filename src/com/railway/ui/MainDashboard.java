package com.railway.ui;

import com.railway.model.User;
import java.awt.*;
import javax.swing.*;

public class MainDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private final User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public MainDashboard(User user) {
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setTitle("Railway Ticket Booking System - Welcome " + currentUser.getFullName());
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // Create menu bar
        createMenuBar();
        
        // Create sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Add panels
        contentPanel.add(new SearchPanel (currentUser), "search");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Show search panel by default
        cardLayout.show(contentPanel, "search");
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        // Add welcome message
        JLabel welcomeLabel = new JLabel(" Welcome, " + currentUser.getFullName() + " | Role: " + currentUser.getRole());
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(welcomeLabel);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        
        // User info panel
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(52, 73, 94));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userName = new JLabel(currentUser.getFullName());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userRole = new JLabel(currentUser.getRole().toUpperCase());
        userRole.setForeground(new Color(189, 195, 199));
        userRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userRole.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        userPanel.add(userIcon);
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(userName);
        userPanel.add(Box.createVerticalStrut(5));
        userPanel.add(userRole);
        
        sidebar.add(userPanel);
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu buttons
        JButton searchBtn = createMenuButton("Search Trains", "search");
        
        sidebar.add(searchBtn);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });
        
        btn.addActionListener(e -> cardLayout.show(contentPanel, cardName));
        
        return btn;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            """
            Railway Ticket Booking System
            Version 1.0
            
            A comprehensive railway reservation system
            Developed for academic project
            
            © 2024 All Rights Reserved""",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
}