// gui/RNGGenerator.java
package gui;

import utils.ColorUtils;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class RNGGenerator extends JFrame {
    private JTextField minField;
    private JTextField maxField;
    private JTextArea outputArea;
    private JLabel resultLabel;
    private JButton menuButton, generateButton, resetButton;
    private Set<Integer> generatedNumbers;
    private int min, max;

    public RNGGenerator(int width, int height) {
        generatedNumbers = FileUtils.loadGeneratedNumbers();

        setTitle("Unique Random Number Generator");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int fontSize = width / 40;
        Font font = new Font("Arial", Font.PLAIN, fontSize);

        // Initialize components
        minField = new JTextField(10);
        maxField = new JTextField(10);
        generateButton = new JButton("Generate Unique Number");
        resetButton = new JButton("Start Fresh");
        resultLabel = new JLabel("Please enter min and max values, then press 'Generate'");
        outputArea = new JTextArea(5, 20);
        menuButton = new JButton("Menu");

        // Configure components and add them to the layout
        configureComponents(font, gbc);
        addComponentsToLayout(gbc);

        // Set listeners for buttons
        addMenuButtonListener();
        setGenerateButtonListener();
        setResetButtonListener();

        // Ensure the frame is visible after components are added
        setVisible(true);
    }

    // Provide public access to outputArea
    public JTextArea getOutputArea() {
        return outputArea;
    }

    // Implement changeWindowSize 
    public void changeWindowSize() {
        String input = JOptionPane.showInputDialog(this, "Enter new size percentage (30-100):");
        try {
            int percentage = Integer.parseInt(input);
            if (percentage < 30) {
                percentage = 30;
                JOptionPane.showMessageDialog(this, "Minimum size is 30%. Setting to 30%.");
            } else if (percentage > 100) {
                percentage = 100;
                JOptionPane.showMessageDialog(this, "Maximum size is 100%. Setting to 100%.");
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screenSize.width * (percentage / 100.0));
            int height = (int) (screenSize.height * (percentage / 100.0));
            setSize(width, height);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter an integer.");
        }
    }

    private void configureComponents(Font font, GridBagConstraints gbc) {
        minField.setFont(font);
        maxField.setFont(font);
        generateButton.setFont(font);
        resetButton.setFont(new Font("Arial", Font.PLAIN, font.getSize() - 2));
        resultLabel.setFont(font);
        outputArea.setFont(new Font("Arial", Font.BOLD, font.getSize() + 10));
        outputArea.setEditable(false);

        gbc.insets = new Insets(5, 5, 5, 5);
    }

    private void addComponentsToLayout(GridBagConstraints gbc) {
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Menu, Min, Max and Reset
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(menuButton, gbc);

        gbc.gridx = 1;
        add(new JLabel("Min:"), gbc);
        gbc.gridx = 2;
        add(minField, gbc);

        gbc.gridx = 3;
        add(new JLabel("Max:"), gbc);
        gbc.gridx = 4;
        add(maxField, gbc);

        gbc.gridx = 5;
        gbc.fill = GridBagConstraints.NONE;
        add(resetButton, gbc);

        // Row 1: Generate 
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(generateButton, gbc);

        // Row 2: Result 
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        add(resultLabel, gbc);

        // Row 3: Output area for displaying the generated number
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(new JScrollPane(outputArea), gbc);
    }

    private void addMenuButtonListener() {
        menuButton.addActionListener(e -> MenuHandler.showMenu(this, menuButton));
    }

    private void setGenerateButtonListener() {
        generateButton.addActionListener(e -> {
            if (minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                resultLabel.setText("Please enter both min and max values.");
                return;
            }
            try {
                min = Integer.parseInt(minField.getText());
                max = Integer.parseInt(maxField.getText());
                int totalPossibleNumbers = (max - min) + 1;

                if (generatedNumbers.size() >= totalPossibleNumbers) {
                    outputArea.setText("All possible numbers within the range have been generated.");
                    return;
                }

                int uniqueRandomNumber = FileUtils.generateUniqueRandomNumber(min, max, generatedNumbers);
                outputArea.setText("Generated unique number: " + uniqueRandomNumber);
                FileUtils.storeGeneratedNumber(uniqueRandomNumber);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input. Please enter integer values.");
            }
        });
    }

    private void setResetButtonListener() {
        resetButton.addActionListener(e -> {
            if (FileUtils.clearGeneratedNumbers()) {
                generatedNumbers.clear();
                resultLabel.setText("All records cleared. Start fresh.");
                minField.setText("");
                maxField.setText("");
                outputArea.setText("");
            } else {
                resultLabel.setText("Failed to delete numbers file.");
            }
        });
    }
}
