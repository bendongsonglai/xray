package inext.ris.order.xray.convertDicom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
@Service
public class ToolkitService {
    public void convertjpg2dicom(String dirMeta, String input, String output){
        try {
            String cmd = "/usr/archive_ris/toolkit/dcm4che-5.26.1/bin/jpg2dcm -f ";
            cmd += dirMeta;
            cmd += " ";
            cmd += input;
            cmd += " ";
            cmd += output;
            log.info("Comand exec {}", cmd);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            log.info("Start convert!");
            while ((line=buf.readLine())!=null) {
                log.info(line);
            }
            log.info("Finish convert!");
        }catch (Exception e) {
            log.error("Convert jpeg to dicom failed!");
            e.printStackTrace();
        }
    }

    public void convertpdf2dicom(String dirMeta, String input, String output){
        try {
            String cmd = "/usr/archive_ris/toolkit/dcm4che-5.26.1/bin/pdf2dcm -f ";
            //String cmd = "E:\\nondicom\\archive_ris\\toolkit\\dcm4che-5.26.1\\bin\\pdf2dcm -f ";
            cmd += dirMeta;
            cmd += " ";
            cmd += input;
            cmd += " ";
            cmd += output;
            System.out.println(cmd);
            log.info("Comand exec {}", cmd);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            log.info("Start convert!");
            while ((line=buf.readLine())!=null) {
                log.info(line);
            }
            log.info("Finish convert!");
        }catch (Exception e) {
            log.error("Convert pdf to dicom failed!");
            e.printStackTrace();
        }
    }

    public void sendDicom(String host, String port, String aetitle, String srcae, String directory){
        try {
            String cmd = "/usr/archive_ris/toolkit/dcm4che-5.26.1/bin/storescu -b ";
            cmd += srcae;
            cmd += " ";
            cmd += "-c ";
            cmd += aetitle;
            cmd += "@";
            cmd += host+":"+port;
            cmd += " ";
            cmd += directory;
            log.info("Comand exec {}", cmd);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            log.info("Start sendDicom!");
            while ((line=buf.readLine())!=null) {
                log.info(line);
            }
            log.info("Finish send!");
        }catch (Exception e) {
            log.error("sendDicom to PACS failed!");
            e.printStackTrace();
        }
    }

    public void clearCache() {
        try {
            //String cmd = "sync; echo 1 > /proc/sys/vm/drop_caches";
            String cmd = "sudo /usr/inext/scripts/clearcached.sh";
            log.info("Comand exec {}", cmd);
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            log.info("Start Clear Cache!");
            while ((line=buf.readLine())!=null) {
                log.info(line);
            }
            log.info("Finish Clear Cache!");
        }catch (Exception e) {
            log.error("Clear Cache failed!");
            e.printStackTrace();
        }
    }
}
