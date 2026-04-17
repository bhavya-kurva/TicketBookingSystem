package com.railway.ui;

import com.railway.dao.TrainDAO;
import com.railway.model.Train;
import com.railway.model.User;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class SearchPanel extends JPanel {
    
    private User currentUser;
    private JComboBox<String> fromCombo, toCombo;
    private JTable trainTable;
    private DefaultTableModel tableModel;
    private TrainDAO trainDAO;
    private List<Train> searchResults = new ArrayList<>();
    private MainDashboard parent;
    
    private String[] stations = {"Delhi", "Mumbai", "Bangalore", "Chennai", 
                                 "Kolkata", "Hyderabad", "Pune", "Ahmedabad"};
    
    public SearchPanel(User user) {
        this.currentUser = user;
        this.trainDAO = new TrainDAO();
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
       
        JLabel header = new JLabel("Search Trains", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(0, 70, 140));
        add(header, BorderLayout.NORTH);
        
        
        JPanel searchPanel = createSearchForm();
        add(searchPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSearchForm() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 70, 140)), 
            "Journey Details", 
            TitledBorder.LEFT, 
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 70, 140)
        ));
        
        
        inputPanel.add(new JLabel("From:"));
        fromCombo = new JComboBox<>(stations);
        fromCombo.setPreferredSize(new Dimension(150, 35));
        fromCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(fromCombo);
        
        
        JButton swapBtn = new JButton("SWAP");
        swapBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        swapBtn.setBackground(new Color(0, 70, 140));
        swapBtn.setForeground(Color.WHITE);
        swapBtn.setFont(new Font("Arial", Font.BOLD, 14));
        swapBtn.setFocusPainted(false);
        swapBtn.setPreferredSize(new Dimension(70, 35));
        swapBtn.addActionListener(e -> swapStations());
        applyHover(swapBtn, new Color(0, 70, 140), new Color(0, 51, 102));
        inputPanel.add(swapBtn);
        
        
        inputPanel.add(new JLabel("To:"));
        toCombo = new JComboBox<>(stations);
        toCombo.setPreferredSize(new Dimension(150, 35));
        toCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(toCombo);
        
        
        JButton searchBtn = new JButton("SEARCH TRAINS");
        searchBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        searchBtn.setBackground(new Color(0, 70, 140));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("Arial", Font.BOLD, 14));
        searchBtn.setPreferredSize(new Dimension(150, 40));
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> searchTrains());
        applyHover(searchBtn, new Color(0, 70, 140), new Color(0, 51, 102));
        inputPanel.add(searchBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        
        
        JPanel resultsPanel = createResultsTable();
        panel.add(resultsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createResultsTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Trains"));
        
        
        String[] columns = {"Train No.", "Train Name", "Departure", "Arrival", 
                           "Duration", "Seats", "Fare (Rs.)", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        
        trainTable = new JTable(tableModel);
        trainTable.setRowHeight(40);
        trainTable.setUI(new javax.swing.plaf.basic.BasicTableUI());
        trainTable.setFont(new Font("Arial", Font.PLAIN, 13));
        trainTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        trainTable.getTableHeader().setBackground(new Color(0, 70, 140));
        
        
        
        trainTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        trainTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        trainTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        trainTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        trainTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        trainTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        trainTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        trainTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        
        trainTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        trainTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(trainTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void swapStations() {
        String from = (String) fromCombo.getSelectedItem();
        String to = (String) toCombo.getSelectedItem();
        fromCombo.setSelectedItem(to);
        toCombo.setSelectedItem(from);
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
    
    private void searchTrains() {
        String from = (String) fromCombo.getSelectedItem();
        String to = (String) toCombo.getSelectedItem();
        
        if (from.equals(to)) {
            JOptionPane.showMessageDialog(this, 
                "Source and destination cannot be same!",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        tableModel.setRowCount(0);
        
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        
        List<Train> trains = trainDAO.searchTrains(from, to);
        searchResults.clear();
        searchResults.addAll(trains);
        
        setCursor(Cursor.getDefaultCursor());
        
        if (trains.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No trains found for " + from + " to " + to,
                "No Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        
        for (Train train : trains) {
            Object[] row = {
                train.getTrainNumber(),
                train.getTrainName(),
                train.getDepartureTime(),
                train.getArrivalTime(),
                train.getDuration(),
                train.getSeatsAvailable(),
                "Rs." + String.format("%.2f", train.getFare()),
                "Book Now"
            };
            tableModel.addRow(row);
        }
        
        JOptionPane.showMessageDialog(this,
            "Found " + trains.size() + " trains!",
            "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setUI(new javax.swing.plaf.basic.BasicButtonUI());
            setOpaque(true);
            setBackground(new Color(0, 70, 140));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Book" : value.toString());
            return this;
        }
    }
    
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            button.setOpaque(true);
            button.setBackground(new Color(0, 70, 140));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Book" : value.toString();
            button.setText(label);
            isPushed = true;
            selectedRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                Train train = searchResults.get(selectedRow);
                
                
                Window window = SwingUtilities.getWindowAncestor(SearchPanel.this);
                if (window instanceof MainDashboard) {
                    MainDashboard dashboard = (MainDashboard) window;
                   
                    JPanel contentPanel = (JPanel) dashboard.getContentPane().getComponent(1);
                    Component[] components = contentPanel.getComponents();
                    for (Component comp : components) {
                        if (comp instanceof BookingPanel) {
                            ((BookingPanel) comp).setSelectedTrain(train);
                            break;
                        }
                    }
                    CardLayout cl = (CardLayout) contentPanel.getLayout();
                    cl.show(contentPanel, "booking");
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}