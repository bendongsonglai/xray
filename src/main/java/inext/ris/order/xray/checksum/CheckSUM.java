package inext.ris.order.xray.checksum;

import inext.ris.order.xray.hl7.msh.MSH;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceXray;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.Base64;

@RestController
@RequestMapping("/inext/report/verification")
@Slf4j
@RequiredArgsConstructor
public class CheckSUM {
    static Charset windows1252 = Charset.forName("windows-1252");
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseEntity<?> checkVerify(@Valid @RequestBody ReportCheckModel reportCheckModel) throws Exception {
        String filePath = null;
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());

        MSH msh = webServiceXray.getMSH();
        String dir = msh.getDirectory();
        //String dir = "D:\\Disk_D\\Workspace Victoria\\report\\";
        try {
            String fileName = reportCheckModel.getFilename();
            String content = reportCheckModel.getData();
            String datetime = fileName.split("_")[1];
            String year = datetime.substring(0, 4);
            String month = datetime.substring(4, 6);
            String day = datetime.substring(6, 8);
            filePath = dir.replace("reporttemp", "hl7_report_backup") + "/" + year
                    + "/" + month + "/" + day + "/" + fileName + ".hl7";
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
            String checksumSrc = new BigInteger(1, hash).toString(16);
            byte[] decoder = Base64.getDecoder().decode(content);
            byte[] hashTarget= MessageDigest.getInstance("MD5").digest(decoder);
            String checksumTarget = new BigInteger(1, hashTarget).toString(16);
            if (checksumTarget.equals(checksumSrc)) {
                return new ResponseEntity<>("Verified!", HttpStatus.OK);
            } else {
                String directory_error = dir.replace("reporttemp", "hl7_report_error") + "/" + year
                        + "/" + month + "/" + day;
                createDirectory(directory_error);
                String convertedString = new String (decoder, windows1252);
                createFile(directory_error+"/"+ fileName + "_ERROR.hl7", convertedString);
                return new ResponseEntity<>("Verification Failed!", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Find Report file by file name {} not existed!", filePath);
            return new ResponseEntity<>("File not found!", HttpStatus.BAD_REQUEST);
        }
    }
    private static void createDirectory(String dir) {
        try {
            Path path = Paths.get(dir);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                log.info("Create directory {} success!", dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Failed to create directory!" + e.getMessage());
        }
    }

    private static void createFile (String path, String data) {
        try{
            FileOutputStream fileOut = new FileOutputStream(path);
            //Instantiating the DataOutputStream class
            DataOutputStream outputStream = new DataOutputStream(fileOut);
            //Writing UTF data to the output stream
            outputStream.write(data.getBytes(windows1252));
            outputStream.flush();
            log.info("Successfully wrote to the file.");

        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }
    }

}
