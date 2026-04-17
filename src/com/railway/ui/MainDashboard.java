package com.railway.ui;

import com.railway.model.User;
import java.awt.*;
import javax.swing.*;

public class MainDashboard extends JFrame {
    
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    public MainDashboard(User user) {
        this.currentUser = user;
        initUI();
    }
    
    private void initUI() {
        setTitle("Railway Booking System - Welcome " + currentUser.getFullName());
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        
        createMenuBar();
        
        
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 245, 245));
        
        
        SearchPanel searchPanel = new SearchPanel(currentUser);
        BookingPanel bookingPanel = new BookingPanel(currentUser);
        
        contentPanel.add(searchPanel, "search");
        contentPanel.add(bookingPanel, "booking");
        
        add(contentPanel, BorderLayout.CENTER);
        
        
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(44, 62, 80));
        statusBar.setPreferredSize(new Dimension(getWidth(), 30));
        
        JLabel statusLabel = new JLabel("Connected to Railway Database  |  User: " + currentUser.getFullName());
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusBar.add(statusLabel);
        
        add(statusBar, BorderLayout.SOUTH);
        
        
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
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        
        
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(52, 73, 94));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userName = new JLabel(currentUser.getFullName());
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userRole = new JLabel("Passenger");
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
        
        
        JButton searchBtn = createMenuButton("SEARCH TRAINS", "search");
        JButton bookBtn = createMenuButton("BOOK TICKET", "booking");
        JButton logoutBtn = createMenuButton("LOGOUT", null);
        
        sidebar.add(searchBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(bookBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);
        
        
        searchBtn.addActionListener(e -> cardLayout.show(contentPanel, "search"));
        bookBtn.addActionListener(e -> cardLayout.show(contentPanel, "booking"));
        logoutBtn.addActionListener(e -> logout());
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setBackground(new Color(0, 70, 140));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(24, 24, 24));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(34, 34, 34));
            }
        });
        
        return btn;
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "RAILWAY TICKET BOOKING SYSTEM\n\n" +
            "Version: 1.0\n" +
            "Developer: Railway Team\n\n" +
            "Features:\n" +
            "- User Registration & Login\n" +
            "- Train Search\n" +
            "- Ticket Booking\n" +
            "- Passenger Details\n" +
            "- PNR Generation\n\n" +
            "© 2024 All Rights Reserved",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
       