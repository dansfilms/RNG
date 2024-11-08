// Main.java
import gui.RNGGenerator;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final int MINIMUM_PERCENTAGE = 30;
    private static final int MAXIMUM_PERCENTAGE = 100;

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

            openRNGButton.addActionListener(e -> {
                try {
                    int percentage = Integer.parseInt(percentageField.getText());
                    if (percentage < MINIMUM_PERCENTAGE) {
                        JOptionPane.showMessageDialog(initialFrame, "Minimum size is 30%. Setting to 30%.");
                        percentage = MINIMUM_PERCENTAGE;
                    } else if (percentage > MAXIMUM_PERCENTAGE) {
                        JOptionPane.showMessageDialog(initialFrame, "Maximum size is 100%. Setting to 100%.");
                        percentage = MAXIMUM_PERCENTAGE;
                    }

                    int width = (int) (screenSize.width * (percentage / 100.0));
                    int height = (int) (screenSize.height * (percentage / 100.0));

                    RNGGenerator rngFrame = new RNGGenerator(width, height);
                    rngFrame.setVisible(true);
                    initialFrame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(initialFrame, "Please enter a valid integer for the percentage.");
                }
            });

            initialFrame.setVisible(true);
        });
    }
}
