package com.railway.ui;

import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import com.railway.model.User;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class SearchPanel extends JPanel {
    
    private JComboBox<String> fromCombo, toCombo;
    private JTable trainTable;
    private DefaultTableModel tableModel;
    private final TrainDAO trainDAO; 
    
    // Indian railway stations
    private final String[] stations = {"Delhi", "Mumbai", "Bangalore", "Chennai", 
                                 "Kolkata", "Hyderabad", "Ahmedabad", "Pune",
                                 "Jaipur", "Lucknow", "Patna", "Bhopal"};
    
    public SearchPanel(User user) {
        this.trainDAO = new TrainDAO();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search form panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
        
        // Results table panel
        JPanel resultsPanel = createResultsPanel();
        add(resultsPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)), 
            "Search Trains", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 102, 204)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // From Station
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("From Station:"), gbc);
        
        fromCombo = new JComboBox<>(stations);
        fromCombo.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panel.add(fromCombo, gbc);
        
        // To Station
        gbc.gridx = 2;
        panel.add(new JLabel("To Station:"), gbc);
        
        toCombo = new JComboBox<>(stations);
        toCombo.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 3;
        panel.add(toCombo, gbc);
        
        // Search Button
        JButton searchBtn = new JButton("🔍 Search Trains");
        searchBtn.setBackground(new Color(0, 102, 204));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> searchTrains());
        
        gbc.gridx = 4;
        panel.add(searchBtn, gbc);
        
        // Swap Button
        JButton swapBtn = new JButton("⇄ Swap");
        swapBtn.setBackground(new Color(108, 117, 125));
        swapBtn.setForeground(Color.WHITE);
        swapBtn.addActionListener(e -> {
            String from = (String) fromCombo.getSelectedItem();
            String to = (String) toCombo.getSelectedItem();
            fromCombo.setSelectedItem(to);
            toCombo.setSelectedItem(from);
        });
        
        gbc.gridx = 5;
        panel.add(swapBtn, gbc);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Trains"));
        
        // Table columns
        String[] columns = {"Train No.", "Train Name", "Departure", "Arrival", 
                           "Seats Available", "Fare (₹)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        trainTable = new JTable(tableModel);
        trainTable.setRowHeight(30);
        trainTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        trainTable.getTableHeader().setBackground(new Color(0, 102, 204));
        trainTable.getTableHeader().setForeground(Color.WHITE);
        
        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < trainTable.getColumnCount(); i++) {
            trainTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(trainTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        JLabel statusLabel = new JLabel("Enter source and destination to search trains");
        statusLabel.setForeground(new Color(108, 117, 125));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void searchTrains() {
        String from = (String) fromCombo.getSelectedItem();
        String to = (String) toCombo.getSelectedItem();
        
        if (from.equals(to)) {
            JOptionPane.showMessageDialog(this, 
                "Source and destination cannot be same!",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Show loading cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Search trains
        List<Train> trains = trainDAO.searchTrains(from, to);
        
        setCursor(Cursor.getDefaultCursor());
        
        if (trains.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No trains found from " + from + " to " + to + "\nPlease try different route.",
                "No Results",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Add rows to table
        for (Train train : trains) {
            Object[] row = {
                train.getTrainNumber(),
                train.getTrainName(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getSeatsAvailable(),
                String.format("₹%.2f", train.getFare())
            };
            tableModel.addRow(row);
        }
        
        // Show success message
        JOptionPane.showMessageDialog(this,
            "Found " + trains.size() + " train(s) from " + from + " to " + to,
            "Search Results",
            JOptionPane.INFORMATION_MESSAGE);
    }
}