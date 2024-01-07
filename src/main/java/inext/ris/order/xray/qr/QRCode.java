package inext.ris.order.xray.qr;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class QRCode {
    public String build(String secretKey, String dir, String acc, String content)throws WriterException, IOException,
            NotFoundException {
        String filedes=dir+acc+".png";
        String token = AES.encrypt(acc, secretKey);
        String qrCodeData = content+token;
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        createQRCode(qrCodeData, filedes, charset, hintMap, 256, 256);
        return null;
    }

    public String buildWithMrn(String secretKey, String dir, String mrn, String acc, String content)throws WriterException, IOException,
            NotFoundException {
        String filedes=dir+acc+".png";
        String qrCodeData = content+AES.encrypt(mrn, secretKey);
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        createQRCode(qrCodeData, filedes, charset, hintMap, 256, 256);
        return null;
    }

    public String buildWithMRNDES(String secretKey, String dir, String mrn, String content)throws WriterException, IOException,
            NotFoundException {
        String filedes=dir+mrn+".png";
        String qrCodeData = content+AES.encrypt(mrn, secretKey);
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        createQRCode(qrCodeData, filedes, charset, hintMap, 256, 256);
        return null;
    }

    @SuppressWarnings("deprecation")
    public static void createQRCode(String qrCodeData, String filePath,
                                    String charset, Map<EncodeHintType, ErrorCorrectionLevel> hintMap, int qrCodeheight, int qrCodewidth)
            throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
        MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                .lastIndexOf('.') + 1), new File(filePath));
    }

    public static String readQRCode(String filePath, String charset, Map<DecodeHintType, ?> hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }
    public static void main(String[] args) {
        QRCode qrCode = new QRCode();
        try {
            qrCode.build("bvtwcantho123456","D:\\Disk_D\\","21187904.254874","http://cdha.bvtwct.vn/iDiVi-QR/?token=");
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
