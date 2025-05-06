package com.example.libapp.api;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.File;

public class BookScanner {

    static {
        try {
            String nativeLibPath = System.getProperty("user.dir") + File.separator + "lib-app" + File.separator + "native";
            String opencvLib = nativeLibPath + File.separator + System.mapLibraryName(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Loading native lib from: " + opencvLib);
            System.load(opencvLib);
            System.out.println("OpenCV library loaded successfully!");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Không thể load thư viện OpenCV: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int TIMEOUT = 15000; // 15 seconds timeout


    // ✅ Static để dùng được ở static context
    private static BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (mat.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        }

        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), buffer);
        return image;
    }

    public static void displayCameraFeed(ImageView imageView) {
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.err.println("Không thể mở webcam.");
            return;
        }

        Mat frame = new Mat();
        long startTime = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTime) < TIMEOUT) {
            if (camera.read(frame)) {
                if (frame.empty()) {
                    System.err.println("Khung hình bị rỗng.");
                    continue;
                }

                Mat resizedFrame = new Mat();
                Imgproc.resize(frame, resizedFrame, new Size(640, 480));
                Mat frameRGB = new Mat();
                Imgproc.cvtColor(resizedFrame, frameRGB, Imgproc.COLOR_BGR2RGB);

                try {
                    BufferedImage image = matToBufferedImage(frameRGB);
                    if (image != null) {
                        Image fxImage = SwingFXUtils.toFXImage(image, null);
                        Platform.runLater(() -> imageView.setImage(fxImage));
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi xử lý khung hình: " + e.getMessage());
                }
            }
        }

        camera.release();
    }
}
