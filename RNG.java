import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RNG extends JFrame {
    private static final String FILE_NAME = "numbers.txt";
    private static final int MINIMUM_PERCENTAGE = 30; // Minimum window size 
    private Set<Integer> generatedNumbers;
    private Random random;
    private int min;
    private int max;
    private JTextField minField;
    private JTextField maxField;
    private JLabel resultLabel;
    private JTextArea outputArea;
    private JLabel minLabel;
    private JLabel maxLabel;
    private JButton menuButton;
    private JButton generateButton;
    private JButton resetButton;

    public RNG(int width, int height) {
        generatedNumbers = loadGeneratedNumbers();
        random = new Random();

        setTitle("Unique Random Number Generator");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Calculate font size based on window seze
        int fontSize = width / 40;
        Font font = new Font("Arial", Font.PLAIN, fontSize);

        // Initialize components
        minField = new JTextField(10);
        maxField = new JTextField(10);
        generateButton = new JButton("Generate Unique Number");
        resetButton = new JButton("Start Fresh");
        resultLabel = new JLabel("Please enter min and max values, then press 'Generate'");
        outputArea = new JTextArea(5, 20);

        // Menu button
        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("Arial", Font.PLAIN, fontSize));

        // Min and Max
        minLabel = new JLabel("Min:");
        maxLabel = new JLabel("Max:");
        minLabel.setFont(font);
        maxLabel.setFont(font);

        // font scaling
        minField.setFont(font);
        maxField.setFont(font);
        generateButton.setFont(font);
        resetButton.setFont(new Font("Arial", Font.PLAIN, fontSize - 2)); // Slightly smaller for reset button
        resultLabel.setFont(font);
        outputArea.setFont(new Font("Arial", Font.BOLD, fontSize + 10));
        outputArea.setEditable(false);

        // Add components
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Menu, Min, Max, and Start Fresh button
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(menuButton, gbc);

        gbc.gridx = 1;
        add(minLabel, gbc);
        gbc.gridx = 2;
        add(minField, gbc);

        gbc.gridx = 3;
        add(maxLabel, gbc);
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

        // Row 3: Output area
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(new JScrollPane(outputArea), gbc);

        // Generate button
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                    resultLabel.setText("Please enter both min and max values.");
                    return;
                }

                try {
                    min = Integer.parseInt(minField.getText());
                    max = Integer.parseInt(maxField.getText());

                    if (min >= max) {
                        resultLabel.setText("Min should be less than max.");
                        return;
                    }

                    int totalPossibleNumbers = (max - min) + 1;

                    if (generatedNumbers.size() >= totalPossibleNumbers) {
                        outputArea.setText("All possible numbers within the range have been generated.");
                        return;
                    }

                    int uniqueRandomNumber = generateUniqueRandomNumber();
                    outputArea.setText("Generated unique number: " + uniqueRandomNumber);
                    storeGeneratedNumber(uniqueRandomNumber);

                } catch (NumberFormatException ex) {
                    resultLabel.setText("Invalid input. Please enter integer values.");
                }
            }
        });

        // Reset button listener
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete the numbers file and clear generated numbers
                File file = new File(FILE_NAME);
                if (file.exists() && file.delete()) {
                    generatedNumbers.clear();
                    resultLabel.setText("All records cleared. Start fresh.");
                } else {
                    resultLabel.setText("Failed to delete numbers file.");
                }
                minField.setText("");
                maxField.setText("");
                outputArea.setText("");
            }
        });

        // Menu button listener
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu();

                JMenuItem returnToGenerator = new JMenuItem("Return to Generator");
                returnToGenerator.addActionListener(e1 -> outputArea.setText("")); // Clears output, returns to default view

                JMenuItem changeSize = new JMenuItem("Change Size");
                changeSize.addActionListener(e1 -> changeWindowSize());

                JMenu changeColor = new JMenu("Change Color");

                JMenuItem changeBackgroundColor = new JMenuItem("Change Background Color");
                changeBackgroundColor.addActionListener(e1 -> changeBackgroundColor());

                JMenuItem changeTextColor = new JMenuItem("Change Text Color");
                changeTextColor.addActionListener(e1 -> changeTextColor());

                JMenuItem changeButtonColor = new JMenuItem("Change Button Color & Opacity");
                changeButtonColor.addActionListener(e1 -> changeButtonColor());

                changeColor.add(changeBackgroundColor);
                changeColor.add(changeTextColor);
                changeColor.add(changeButtonColor);

                menu.add(returnToGenerator);
                menu.add(changeSize);
                menu.add(changeColor);

                // Add PopupMenuListener to handle dismissal of the menu
                menu.addPopupMenuListener(new PopupMenuListener() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        // No action needed when it becomes visible
                    }

                    @Override
                    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                        // Repaint the frame when the menu is closed to prevent ghosting
                        repaint();
                    }

                    @Override
                    public void popupMenuCanceled(PopupMenuEvent e) {
                        // Repaint the frame if the menu is canceled
                        repaint();
                    }
                });

                menu.show(menuButton, menuButton.getWidth(), menuButton.getHeight());
            }
        });
    }

    private void changeWindowSize() {
        String input = JOptionPane.showInputDialog(this, "Enter new size percentage (30-100):");
        try {
            int percentage = Integer.parseInt(input);
            if (percentage < MINIMUM_PERCENTAGE) {
                percentage = MINIMUM_PERCENTAGE;
                JOptionPane.showMessageDialog(this, "Minimum size is 30%. Setting to 30%.");
            }
            if (percentage > 100) {
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

    private void changeBackgroundColor() {
        Color selectedColor = selectColor("Select Background Color");
        if (selectedColor != null) {
            getContentPane().setBackground(selectedColor);
        }
    }

    private void changeTextColor() {
        Color selectedColor = selectColor("Select Text Color");
        if (selectedColor != null) {
            resultLabel.setForeground(selectedColor);
            outputArea.setForeground(selectedColor);
            minLabel.setForeground(selectedColor);
            maxLabel.setForeground(selectedColor);
            generateButton.setForeground(selectedColor);
            resetButton.setForeground(selectedColor);
            menuButton.setForeground(selectedColor);
        }
    }

    private void changeButtonColor() {
        Color selectedColor = selectColor("Select Button Color");
        if (selectedColor != null) {
            UIManager.put("Button.background", selectedColor);
            SwingUtilities.updateComponentTreeUI(this);
        }
    }

    private Color selectColor(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JColorChooser colorChooser = new JColorChooser();
        JSlider opacitySlider = new JSlider(0, 100, 100);
        opacitySlider.setMajorTickSpacing(20);
        opacitySlider.setPaintTicks(true);
        opacitySlider.setPaintLabels(true);

        JLabel opacityLabel = new JLabel("Opacity: 100%");
        opacitySlider.addChangeListener(e -> opacityLabel.setText("Opacity: " + opacitySlider.getValue() + "%"));

        panel.add(colorChooser, BorderLayout.CENTER);
        panel.add(opacityLabel, BorderLayout.NORTH);
        panel.add(opacitySlider, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            Color color = colorChooser.getColor();
            int opacity = opacitySlider.getValue();
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (opacity * 2.55)); // Convert opacity to 0-255 scale
        }
        return null;
    }

    private Set<Integer> loadGeneratedNumbers() {
        Set<Integer> generatedNumbers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                generatedNumbers.add(Integer.parseInt(line.trim()));
            }
        } catch (IOException e) {
            System.out.println("No existing numbers found. Starting fresh.");
        }
        return generatedNumbers;
    }

    private int generateUniqueRandomNumber() {
        int randomNumber;
        do {
            randomNumber = random.nextInt((max - min) + 1) + min;
        } while (generatedNumbers.contains(randomNumber));
        generatedNumbers.add(randomNumber);
        return randomNumber;
    }

    private void storeGeneratedNumber(int number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(number + "\n");
        } catch (IOException e) {
            System.out.println("Error storing the generated number: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int initialWidth = (int) (300 * 1.25);
            int initialHeight = (int) (150 * 1.25);

            JFrame initialFrame = new JFrame("Set Window Size Percentage");
            initialFrame.setSize(initialWidth, initialHeight);
            initialFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            initialFrame.setLayout(new FlowLayout());

            JLabel instructionLabel = new JLabel("<html>Enter window size as a percentage of screen (30-100%):</html>");
            JTextField percentageField = new JTextField(5);
            JButton openRNGButton = new JButton("Open RNG Generator");

            initialFrame.add(instructionLabel);
            initialFrame.add(percentageField);
            initialFrame.add(openRNGButton);

            openRNGButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int percentage = Integer.parseInt(percentageField.getText());
                        if (percentage < MINIMUM_PERCENTAGE) {
                            JOptionPane.showMessageDialog(initialFrame, "Minimum size is 30%. Setting to 30%.");
                            percentage = MINIMUM_PERCENTAGE;
                        }
                        if (percentage > 100) {
                            JOptionPane.showMessageDialog(initialFrame, "Maximum size is 100%. Setting to 100%.");
                            percentage = 100;
                        }

                        int width = (int) (screenSize.width * (percentage / 100.0));
                        int height = (int) (screenSize.height * (percentage / 100.0));

                        RNG rngFrame = new RNG(width, height);
                        rngFrame.setVisible(true);
                        initialFrame.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(initialFrame, "Please enter a valid integer for the percentage.");
                    }
                }
            });

            initialFrame.setVisible(true);
        });
    }
}
