package com.example.libapp.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.image.BufferedImage;

public class ISBNScannerWindow extends Application {

    public static String resultISBN = null;
    private static final int TIMEOUT = 15000;
    private volatile boolean running = true;

    private ImageView imageView = new ImageView();
    private VideoCapture camera;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  // Nạp thư viện native của OpenCV
    }


    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 640, 480);

        primaryStage.setTitle("Quét mã ISBN...");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            running = false;
            if (camera != null) {
                camera.release();
            }
        });
        primaryStage.show();

        new Thread(this::runCamera).start();
    }

    private void runCamera() {
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.err.println("Không mở được webcam.");
            return;
        }

        Mat frame = new Mat();
        long startTime = System.currentTimeMillis();

        while (running && (System.currentTimeMillis() - startTime) < TIMEOUT) {
            if (!camera.read(frame) || frame.empty()) continue;

            Core.flip(frame, frame, 1); // ✅ Lật ảnh theo chiều ngang (mirror -> real-world view)

            Imgproc.resize(frame, frame, new Size(640, 480));
            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);

            WritableImage fxImage = matToWritableImage(frame);
            Platform.runLater(() -> imageView.setImage(fxImage));

            String isbn = tryDecode(frame);
            if (isbn != null) {
                System.out.println("📚 Quét thành công: " + isbn);
                resultISBN = isbn;
                running = false;
                Platform.runLater(() -> {
                    Stage stage = (Stage) imageView.getScene().getWindow();
                    if (stage != null) {
                        stage.close();
                    }
                });
                break;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {}
        }

        if (running) {
            Platform.runLater(() -> {
                Stage stage = (Stage) imageView.getScene().getWindow();
                if (stage != null) {
                    stage.close();
                }
            });
        }

        camera.release();
    }


    // Chuyển đổi từ Mat sang WritableImage (JavaFX)
    private WritableImage matToWritableImage(Mat mat) {
        int width = mat.width();
        int height = mat.height();
        byte[] data = new byte[width * height * mat.channels()];
        mat.get(0, 0, data);

        WritableImage writableImage = new WritableImage(width, height);
        javafx.scene.image.PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = data[(y * width + x) * 3] & 0xFF;
                int g = data[(y * width + x) * 3 + 1] & 0xFF;
                int b = data[(y * width + x) * 3 + 2] & 0xFF;
                pixelWriter.setColor(x, y, javafx.scene.paint.Color.rgb(r, g, b));
            }
        }
        return writableImage;
    }

    private String tryDecode(Mat frame) {
        try {
            BufferedImage bImage = matToBufferedImage(frame);
            LuminanceSource source = new BufferedImageLuminanceSource(bImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            return null;
        }
    }

    // Chuyển đổi Mat sang BufferedImage
    private BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if (mat.channels() == 1) type = BufferedImage.TYPE_BYTE_GRAY;
        byte[] data = new byte[mat.rows() * mat.cols() * mat.channels()];
        mat.get(0, 0, data);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    // Phương thức quét ISBN và trả về kết quả ISBN đã quét
    public static String launchAndScan() {
        resultISBN = null;

        // Chạy ứng dụng quét ISBN trong một thread riêng
        Platform.runLater(() -> {
            ISBNScannerWindow scannerWindow = new ISBNScannerWindow();
            scannerWindow.start(new Stage());  // Khởi động cửa sổ quét ISBN
        });

        long startTime = System.currentTimeMillis();
        while (resultISBN == null && (System.currentTimeMillis() - startTime) < TIMEOUT + 2000) {
            try {
                Thread.sleep(100); // Chờ trong khi quét
            } catch (InterruptedException ignored) {}
        }

        return resultISBN; // Trả về ISBN quét được, hoặc null nếu không quét được
    }
}
