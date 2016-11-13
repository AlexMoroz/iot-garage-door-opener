package client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wojci on 12.11.2016.
 */
public class MainWindowView {
    private JFrame frame;
    private JPanel panelVideo;
    private JLabel lblRecognizedPlate1;
    private JLabel lblRecognizedPlate2;
    private JLabel lblRecognizedPlate3;
    private JLabel labelVideo;
    /**
     * Test view look.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainWindowView window = new MainWindowView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainWindowView() {
        initialize();
    }

    private void initialize() {
        initializeFrame();
        addPanelVideo();
        addVideoLabel();
        addRecognizedPlates();
        frame.setVisible(true);
    }

    private void addVideoLabel(){
        labelVideo = new JLabel();
        panelVideo.add(labelVideo);
    }


    private void addRecognizedPlates() {
        lblRecognizedPlate1 = new JLabel();
        lblRecognizedPlate2 = new JLabel();
        lblRecognizedPlate3 = new JLabel();

        lblRecognizedPlate1.setBounds(900, 200, 400, 80);
        lblRecognizedPlate2.setBounds(1210, 460, 150, 40);
        lblRecognizedPlate3.setBounds(1210, 520, 150, 40);

        lblRecognizedPlate1.setFont(new Font("Tahoma", Font.BOLD, 38));
        lblRecognizedPlate2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblRecognizedPlate3.setFont(new Font("Tahoma", Font.BOLD, 14));

        frame.add(lblRecognizedPlate1);
        frame.add(lblRecognizedPlate2);
        frame.add(lblRecognizedPlate3);
    }

    private void addPanelVideo() {
        panelVideo = new JPanel();
        panelVideo.setBounds(24, 25, 731, 537);
        frame.getContentPane().add(panelVideo);
    }

    private void initializeFrame() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1395, 803);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }


    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanelVideo() {
        return panelVideo;
    }


    public JLabel getLabelVideo() {
        return labelVideo;
    }

    public JLabel getLblRecognizedPlate1() {
        return lblRecognizedPlate1;
    }


}
