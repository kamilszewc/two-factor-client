package io.github.kamilszewc.twofactorclient;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.computinglaboratory.totp.Totp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

public class QrCodeScanner {

    public Optional<Entry> scanScreen() throws IOException, AWTException, URISyntaxException {
        var bufferedImage = takeScreenshot();
        Optional<String> result = processQrCode(bufferedImage);
        if (result.isPresent()) {
            return Optional.of(parseTotpUrl(result.get()));
        } else {
            return Optional.empty();
        }
    }

    private BufferedImage takeScreenshot() throws AWTException, IOException {

        Robot robot = new Robot();
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        File file = new File("/home/kamil/screenshot.jpg");
        //return ImageReader.readImage(file.toURI());
        return bufferedImage;
    }

    private Optional<String> processQrCode(BufferedImage bufferedImage) {

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));

        var hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));

        var hints_pure = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        MultiFormatReader mfr = new MultiFormatReader();
        try {
            Result result = mfr.decode(bitmap, hints_pure);
            bufferedImage.flush();
            return Optional.of(result.getText());
        } catch (NotFoundException ex) {
            bufferedImage.flush();
            return Optional.empty();
        }
    }

    private Entry parseTotpUrl(String totpUrl) throws URISyntaxException {

        URI uri = new URI(totpUrl);

        String serviceName = uri.getPath().substring(1);
        Map<String, String> parameters = Arrays.stream(uri.getQuery().split("&"))
                .map(param -> {
                    String[] splitted = param.split("=");
                    String key = splitted[0];
                    String value = splitted[1];
                    var entry = Map.entry(key, value);
                    return entry;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String algorithm = Totp.HashFunction.HMACSHA1.toString();
        if (parameters.containsKey("algorithm")) {
            if (parameters.get("algorithm").equals("SHA256")) {
                algorithm = Totp.HashFunction.HMACSHA256.toString();
            }
            if (parameters.get("algorithm").equals("SHA512")) {
                algorithm = Totp.HashFunction.HMACSHA512.toString();
            }
        }

        Entry entry = Entry.builder()
                .serviceName(serviceName)
                .secret(parameters.get("secret"))
                .issuer(parameters.get("issuer"))
                .algorithm(algorithm)
                .build();

        return entry;
    }
}
