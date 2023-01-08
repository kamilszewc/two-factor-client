package io.github.kamilszewc.twofactorclient;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class QrCodeScanner {

    public void scanScreen() throws IOException, AWTException {
        BinaryBitmap binaryBitmap = takeScreenshot();
        Optional<String> result = processQrCode(binaryBitmap);
        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("Did not found any QR code");
        }
    }

    private BinaryBitmap takeScreenshot() throws AWTException, IOException {

        File file = new File("/home/kamil/screenshot.jpg");
        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);

        ImageIO.write(bufferedImage, "jpg", file);

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        return bitmap;
    }

    private Optional<String> processQrCode(BinaryBitmap binaryBitmap) {
        MultiFormatReader mfr = new MultiFormatReader();
        try {
            Result result = mfr.decode(binaryBitmap);
            return Optional.of(result.getText());
        } catch (NotFoundException ex) {
            return Optional.empty();
        }
    }
}
