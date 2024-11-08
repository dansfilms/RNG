// gui/MenuHandler.java
package gui;

import utils.ColorUtils;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

public class MenuHandler {

    public static void showMenu(RNGGenerator rngFrame, JButton menuButton) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem returnToGenerator = new JMenuItem("Return to Generator");
        returnToGenerator.addActionListener(e -> rngFrame.getOutputArea().setText("")); // Use getter for outputArea

        JMenuItem changeSize = new JMenuItem("Change Size");
        changeSize.addActionListener(e -> rngFrame.changeWindowSize()); // Call changeWindowSize method

        JMenu changeColor = new JMenu("Change Color");

        JMenuItem changeBackgroundColor = new JMenuItem("Change Background Color");
        changeBackgroundColor.addActionListener(e -> ColorUtils.changeBackgroundColor(rngFrame));

        JMenuItem changeTextColor = new JMenuItem("Change Text Color");
        changeTextColor.addActionListener(e -> ColorUtils.changeTextColor(rngFrame));

        JMenuItem changeButtonColor = new JMenuItem("Change Button Color & Opacity");
        changeButtonColor.addActionListener(e -> ColorUtils.changeButtonColor(rngFrame));

        changeColor.add(changeBackgroundColor);
        changeColor.add(changeTextColor);
        changeColor.add(changeButtonColor);

        menu.add(returnToGenerator);
        menu.add(changeSize);
        menu.add(changeColor);

        menu.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { rngFrame.repaint(); }
            public void popupMenuCanceled(PopupMenuEvent e) { rngFrame.repaint(); }
        });

        menu.show(menuButton, menuButton.getWidth(), menuButton.getHeight());
    }
}
