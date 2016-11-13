/**
 * Created by Wojciech on 12-11-16.
 */

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import java.io.File;
import java.io.IOException;

public class Main {

    public static final String OPENCV_PATH_WOJTEK = "C:\\Program Files\\Java\\opencv\\build\\java\\x64\\opencv_java2411.dll";
    public static final String OPENCV_PATH_ARNI = "C:\\opencv\\build\\java\\x64\\opencv_java2411.dll";

    public static void main(String[] args) throws IOException {
        System.load(OPENCV_PATH_WOJTEK);
        startApplication();
    }

    private static void startApplication() {
        new Application().runApp();

    }

    public static Mat getImageFromResources(String path) throws IOException {
        String canonicalPath = new File(".").getCanonicalPath();
        String replace = canonicalPath.replace("\\", "\\\\");
        return Highgui.imread(replace + "\\src\\main\\resources\\" + path, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
    }
}
