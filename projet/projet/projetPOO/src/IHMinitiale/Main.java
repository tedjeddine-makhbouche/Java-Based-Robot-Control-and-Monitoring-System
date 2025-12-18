package IHMinitiale;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                new IHM();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

