// utils/ColorUtils.java
package utils;

import javax.swing.*;
import java.awt.*;

public class ColorUtils {

    public static void changeBackgroundColor(JFrame frame) {
        Color selectedColor = selectColor("Select Background Color");
        if (selectedColor != null) {
            frame.getContentPane().setBackground(selectedColor);
        }
    }

    public static void changeTextColor(JFrame frame) {
        Color selectedColor = selectColor("Select Text Color");
        if (selectedColor != null) {
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JLabel || component instanceof JTextArea || component instanceof JButton) {
                    component.setForeground(selectedColor);
                }
            }
        }
    }

    public static void changeButtonColor(JFrame frame) {
        Color selectedColor = selectColor("Select Button Color");
        if (selectedColor != null) {
            UIManager.put("Button.background", selectedColor);
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    private static Color selectColor(String title) {
        JColorChooser colorChooser = new JColorChooser();
        JPanel panel = new JPanel(new BorderLayout());
        JSlider opacitySlider = new JSlider(0, 100, 100);
        JLabel opacityLabel = new JLabel("Opacity: 100%");

        opacitySlider.addChangeListener(e -> opacityLabel.setText("Opacity: " + opacitySlider.getValue() + "%"));
        panel.add(colorChooser, BorderLayout.CENTER);
        panel.add(opacityLabel, BorderLayout.NORTH);
        panel.add(opacitySlider, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Color color = colorChooser.getColor();
            int opacity = opacitySlider.getValue();
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (opacity * 2.55));
        }
        return null;
    }
}
