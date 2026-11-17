package com.mycompany.jacinto_campusconnectfinal;

/**
 *
 * @author Gail Jacinto
 */
import javax.swing.SwingUtilities;

public class CampusConnectMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CampusConnectMainFrame().setVisible(true);
        });
    }
}