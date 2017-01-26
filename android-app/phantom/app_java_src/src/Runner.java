import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by @nowami on 05/10/15.
 */
public class Runner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                ScanConversion scanConversion = new ScanConversion();
                try {
                    scanConversion.compute_interpolation();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(scanConversion);
                frame.setSize(new Dimension(512, 512));
                frame.setVisible(true);
            }
        });
    }
}
