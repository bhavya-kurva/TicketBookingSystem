package com.railway.ui;

import com.railway.dao.BookingDAO;
import com.railway.model.Booking;
import com.railway.model.Passenger;
import com.railway.model.Train;
import com.railway.model.User;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

public class BookingPanel extends JPanel {

    private User currentUser;
    private Train selectedTrain;
    private JTextField dateField;
    private JSpinner passengerSpinner;
    private JTable passengerTable;
    private DefaultTableModel passengerTableModel;
    private JLabel trainInfoLabel, fareLabel, totalLabel;
    private BookingDAO bookingDAO;

    public BookingPanel(User user) {
        this.currentUser = user;
        this.bookingDAO = new BookingDAO();
        initUI();
    }

    public void setSelectedTrain(Train train) {
        this.selectedTrain = train;
        updateTrainInfo();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel header = new JLabel("Book Your Ticket", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(0, 102, 204));
        add(header, BorderLayout.NORTH);

        // Main Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        updatePassengerTable();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Train Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204)),
                "Train Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(0, 102, 204)));
        trainInfoLabel = new JLabel("  Please select a train from Search");
        trainInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(trainInfoLabel, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(infoPanel, gbc);

        // Journey Date
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(new JLabel("Journey Date:"), gbc);

        dateField = new JTextField(LocalDate.now().plusDays(1).toString());
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Number of Passengers
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Number of Passengers:"), gbc);

        passengerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 6, 1));
        passengerSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        passengerSpinner.setPreferredSize(new Dimension(80, 30));
        passengerSpinner.addChangeListener(e -> updatePassengerTable());
        gbc.gridx = 1;
        panel.add(passengerSpinner, gbc);

        // Passenger Details Table
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Passenger Details"));

        String[] columns = { "S.No", "Name", "Age", "Gender" };
        passengerTableModel = new DefaultTableModel(columns, 1);
        passengerTable = new JTable(passengerTableModel);
        passengerTable.setRowHeight(35);
        passengerTable.setFont(new Font("Arial", Font.PLAIN, 13));
        passengerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        // Set column widths
        passengerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        passengerTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        passengerTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        passengerTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        // Gender combo box editor
        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        passengerTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(genderCombo));

        tablePanel.add(new JScrollPane(passengerTable), BorderLayout.CENTER);

        panel.add(tablePanel, gbc);

        // Fare Panel
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        JPanel farePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        farePanel.setBorder(BorderFactory.createTitledBorder("Fare Details"));
        farePanel.setBackground(Color.WHITE);

        JLabel fareLabelText = new JLabel("Fare per passenger:");
        fareLabelText.setFont(new Font("Arial", Font.BOLD, 13));
        fareLabel = new JLabel("Rs. 0.00");
        fareLabel.setFont(new Font("Arial", Font.BOLD, 13));
        fareLabel.setForeground(new Color(0, 102, 204));

        JLabel totalLabelText = new JLabel("Total Fare:");
        totalLabelText.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel = new JLabel("Rs. 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0, 153, 0));

        farePanel.add(fareLabelText);
        farePanel.add(fareLabel);
        farePanel.add(totalLabelText);
        farePanel.add(totalLabel);

        panel.add(farePanel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(Color.WHITE);

        JButton confirmBtn = new JButton("CONFIRM BOOKING");
        confirmBtn.setBackground(new Color(40, 167, 69));
        confirmBtn.setForeground(Color.BLUE);
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 14));
        confirmBtn.setPreferredSize(new Dimension(180, 45));
        confirmBtn.setFocusPainted(false);
        confirmBtn.addActionListener(e -> confirmBooking());

        JButton clearBtn = new JButton("CLEAR");
        clearBtn.setBackground(new Color(108, 117, 125));
        clearBtn.setForeground(Color.BLUE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.setPreferredSize(new Dimension(120, 45));
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> clearForm());

        panel.add(confirmBtn);
        panel.add(clearBtn);

        return panel;
    }

    private void updateTrainInfo() {
        if (selectedTrain != null) {
            String info = "<html>" +
                    "  <b>Train Number:</b> " + selectedTrain.getTrainNumber() + "<br>" +
                    "  <b>Train Name:</b> " + selectedTrain.getTrainName() + "<br>" +
                    "  <b>From:</b> " + selectedTrain.getFromStation() +
                    "  &nbsp;&nbsp; <b>To:</b> " + selectedTrain.getToStation() + "<br>" +
                    "  <b>Departure:</b> " + selectedTrain.getDepartureTime() +
                    "  &nbsp;&nbsp; <b>Fare:</b> Rs." + String.format("%.2f", selectedTrain.getFare()) +
                    "</html>";
            trainInfoLabel.setText(info);
            fareLabel.setText("Rs." + String.format("%.2f", selectedTrain.getFare()));
            updateTotal();
        }
    }

    private void updatePassengerTable() {
        int passengers = (Integer) passengerSpinner.getValue();
        passengerTableModel.setRowCount(0);

        for (int i = 1; i <= passengers; i++) {
            Object[] row = { i, "", "", "Male" };
            passengerTableModel.addRow(row);
        }

        updateTotal();
    }

    private void updateTotal() {
        if (selectedTrain != null) {
            int passengers = (Integer) passengerSpinner.getValue();
            double total = selectedTrain.getFare() * passengers;
            totalLabel.setText("Rs." + String.format("%.2f", total));
        }
    }

    private void confirmBooking() {
        // Validation
        if (selectedTrain == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a train from Search Trains first!",
                    "No Train Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate date
        String dateStr = dateField.getText().trim();
        Date journeyDate;
        try {
            journeyDate = Date.valueOf(dateStr);
            if (journeyDate.toLocalDate().isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this,
                        "Journey date cannot be in the past!",
                        "Invalid Date", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format! Use YYYY-MM-DD (Example: 2024-12-25)",
                    "Date Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Collect passenger details
        int passengerCount = (Integer) passengerSpinner.getValue();
        List<Passenger> passengers = new ArrayList<>();

        for (int i = 0; i < passengerCount; i++) {
            String name = (String) passengerTableModel.getValueAt(i, 1);
            String ageStr = (String) passengerTableModel.getValueAt(i, 2);
            String gender = (String) passengerTableModel.getValueAt(i, 3);

            // Validate name
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter name for passenger " + (i + 1),
                        "Missing Name", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate age
            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age < 1 || age > 120) {
                    JOptionPane.showMessageDialog(this,
                            "Age must be between 1 and 120 for passenger " + (i + 1),
                            "Invalid Age", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid age for passenger " + (i + 1),
                        "Invalid Age", JOptionPane.ERROR_MESSAGE);
                return;
            }

            passengers.add(new Passenger(name, age, gender));
        }

        double totalFare = selectedTrain.getFare() * passengerCount;

        // Create booking object
        Booking booking = new Booking();
        booking.setUserId(currentUser.getUserId());
        booking.setTrainId(selectedTrain.getTrainId());
        booking.setFromStation(selectedTrain.getFromStation());
        booking.setToStation(selectedTrain.getToStation());
        booking.setJourneyDate(journeyDate);
        booking.setTotalFare(totalFare);
        booking.setPassengers(passengerCount);

        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "---------------------------------------\n" +
                        "          CONFIRM BOOKING DETAILS\n" +
                        "---------------------------------------\n\n" +
                        "Train: " + selectedTrain.getTrainNumber() + " - " + selectedTrain.getTrainName() + "\n" +
                        "From: " + selectedTrain.getFromStation() + "\n" +
                        "To: " + selectedTrain.getToStation() + "\n" +
                        "Date: " + journeyDate + "\n" +
                        "Passengers: " + passengerCount + "\n" +
                        "Total Fare: Rs." + String.format("%.2f", totalFare) + "\n\n" +
                        "---------------------------------------\n" +
                        "Proceed with booking?",
                "Confirm Booking", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Show loading
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            boolean success = bookingDAO.createBooking(booking, passengers);

            setCursor(Cursor.getDefaultCursor());

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "---------------------------------------\n" +
                                "          BOOKING CONFIRMED!\n" +
                                "---------------------------------------\n\n" +
                                "PNR Number: " + booking.getPnr() + "\n\n" +
                                "Please save this PNR for future reference.\n" +
                                "Thank you for choosing Indian Railways!\n" +
                                "---------------------------------------",
                        "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Booking failed! Please try again.\n" +
                                "Check if seats are available.",
                        "Booking Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedTrain = null;
        trainInfoLabel.setText("  Please select a train from Search");
        fareLabel.setText("Rs. 0.00");
        totalLabel.setText("Rs. 0.00");
        dateField.setText(LocalDate.now().plusDays(1).toString());
        passengerSpinner.setValue(1);
        updatePassengerTable();
    }
}
