import client.MainWindowView;
import client.ResultTO;
import numberPlateSegmentation.imageProcessing.impl.SegmentationResultRetrievingServiceImpl;
import numberPlateSegmentation.imageProcessing.interfaces.SegmentationResultRetrievingService;
import numberPlateSegmentation.machineLearning.impl.LicensePlateRecognition;
import numberPlateSegmentation.machineLearning.impl.SvmClassifier;
import numberPlateSegmentation.machineLearning.interfaces.BasicAlgorithmService;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import utils.ImageUtils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Wojciech Trefon on 12.11.2016.
 */
public class Application {
    private static final int BREAK_AFTER_OPEN = 1500;
    private static final int BREAK_AFTER_ALARM = 1500;
    private MainWindowView window = new MainWindowView();
    private static final int SLEEP_TIME = 20;
    private BasicAlgorithmService algorithm;
    private SegmentationResultRetrievingService segmentationResultRetrievingService = new SegmentationResultRetrievingServiceImpl();
    private Set<String> allowedPlates = new HashSet<>(Arrays.asList("DW6950E", "NO69435", "WWL33KK"));
    private Set<String> blackList = new HashSet<>(Arrays.asList( "KR4J788"));
    private VideoCapture camera;

    private static final String POST_OPEN_URL = "https://teamzero.azurewebsites.net/api/open";
    private static final String POST_ALARM_URL = "https://teamzero.azurewebsites.net/api/onalarm";


    public Application() {
        try {
            algorithm = SvmClassifier.load("SUPPORT_VECTOR_MACHINE.ser");
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void runApp() {
        camera = new VideoCapture(0);
        Mat frame = new Mat();
        camera.read(frame);

        if (!camera.isOpened()) {
            System.out.println("Error");
        } else {
            closeCameraOnClosing();
            while (true) {

                if (camera.read(frame)) {
                    doRecognition(frame);
                }
            }
        }
        camera.release();

    }

    private void closeCameraOnClosing() {
        window.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                camera.release();
                super.windowClosing(e);
            }
        });
    }

    private void doRecognition(Mat image) {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_RGB2GRAY);
        ResultTO resultTO = segmentationResultRetrievingService.segmentSigns(ImageUtils.mat2BufferedImage(gray));


        BufferedImage color = ImageUtils.mat2BufferedImage(image);
        ImageUtils.displayImageOnFrame(color, window.getPanelVideo(), window.getLabelVideo());

        List<String> recognizePlates = LicensePlateRecognition.recognizePlate(resultTO.getSegmentedSigns(),
                algorithm, 20, 20);
        for (String plate : recognizePlates) {
            window.getLblRecognizedPlate1().setText("");
            if(plateIsValid(plate)) {
                if (checkIfPlateIsAllowed(plate)) {
                    window.getLblRecognizedPlate1().setText(plate);
                    markImageWithColour(image, new Scalar(0, 255, 0));
                    openGate();
                } else if (blackList.contains(plate)) {
                    window.getLblRecognizedPlate1().setText(plate);
                    markImageWithColour(image, new Scalar(0, 0, 255));
                    startAlarm();
                }
            }
        }
    }

    private void openGate() {
        try {
            sendPost(POST_OPEN_URL);
            Thread.sleep(BREAK_AFTER_OPEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAlarm() {
        try {
            sendPost(POST_ALARM_URL);
            Thread.sleep(BREAK_AFTER_ALARM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfPlateIsAllowed(String plate) {
        return allowedPlates.contains(plate);
    }

    private boolean plateIsValid(String plate) {
        return plate != null && !plate.contains("#");
    }

    private void markImageWithColour(Mat image, Scalar colour) {
        Mat rect = new Mat();
        image.copyTo(rect);
        rect.setTo(colour);
        Core.addWeighted(rect, 0.3, image, 0.7, 0.0, rect);
        ImageUtils.displayImageOnFrame(ImageUtils.mat2BufferedImage(rect), window.getPanelVideo(), window.getLabelVideo());
    }

    private void sendPost(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request not worked");
        }
    }
}


