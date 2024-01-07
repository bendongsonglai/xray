package inext.ris.order.xray.convertDicom;

import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceXray;
import inext.ris.order.xray.ophthalmology.Ophthalmology;
import inext.ris.order.xray.pacs.PacsModel;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/convert")
@Slf4j
public class ConvertAPI {
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> fetchDICOM(@Valid @RequestBody InstanceModel instance) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            if (instance.getType().equals("jpeg")) {
                String studyDate = instance.getStudyDate();
                String year = studyDate.substring(0, 4);
                String month = studyDate.substring(4, 6);
                String day = studyDate.substring(6, 8);
                String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/jpeg/" + year
                        + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                String srcFileMetadata = null;
                String nondicomfile = null;
                String dicomfile = null;
                try {
                    createDirectory(directory);
                    srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/metadata.xml";
                    nondicomfile = directory + "/" + instance.getInstanceUID() + ".jpg";
                    String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(dicomDir);
                    dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createNonDicom(instance.getContent(), nondicomfile);
                List<Integer> dimension = dimensionImage(nondicomfile);
                MetadataService metadataService = new MetadataService();
                String metadatafile = directory+"/"+instance.getInstanceUID()+".xml";
                try {
                    metadataService.generateMetadata(srcFileMetadata, metadatafile,
                            instance, dimension.get(1), dimension.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ToolkitService toolkitService = new ToolkitService();
                try {
                    toolkitService.convertjpg2dicom(metadatafile, nondicomfile, dicomfile);
                    toolkitService.clearCache();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("Error!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Created!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/uploadpdf")
    public ResponseEntity<String> pdf2dicom(@Valid @RequestBody InstanceModel instance) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            if (instance.getType().equals("pdf")) {
                String studyDate = instance.getStudyDate();
                String year = studyDate.substring(0, 4);
                String month = studyDate.substring(4, 6);
                String day = studyDate.substring(6, 8);
                String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/pdf/" + year
                        + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                String srcFileMetadata = null;
                String nondicomfile = null;
                String dicomfile = null;
                try {
                    createDirectory(directory);
                    srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/metadata.xml";
                    nondicomfile = directory + "/" + instance.getInstanceUID() + ".pdf";
                    String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(dicomDir);
                    dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                createNonDicompdf(instance.getContent(), nondicomfile);
                MetadataService metadataService = new MetadataService();
                String metadatafile = directory+"/"+instance.getInstanceUID()+".xml";
                try {
                    metadataService.generateMetadata(srcFileMetadata, metadatafile,
                            instance, 3508, 2480 );
                    log.info("Create metadata {} completed!", metadatafile);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Create metadata {} error!", metadatafile);
                }
                ToolkitService toolkitService = new ToolkitService();
                try {
                    toolkitService.convertpdf2dicom(metadatafile, nondicomfile, dicomfile);
                    toolkitService.clearCache();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("PDF Convert Error!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("PDF Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Created!", HttpStatus.CREATED);
    }


    @PostMapping(value = "/pdf")
    public ResponseEntity<String> convertpdf(@Valid @RequestBody InstanceModel instance) {
      WebClientConfig webClientConfig = new WebClientConfig();
      WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
      MetadataService metadataService = new MetadataService();
      try {
          PacsModel pacsModel = webServiceXray.getPacs();
          if (instance.getType().equals("pdf")) {
              String studyDate = instance.getStudyDate();
              String year = studyDate.substring(0, 4);
              String month = studyDate.substring(4, 6);
              String day = studyDate.substring(6, 8);
              String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality();
              String srcFileMetadata = null;
              String nondicomfile = null;
              String dicomfile = null;
              String metadatafile = null;
              try {
                  srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/MetaPDF/" + instance.getModality() + "/metadata.xml";
                  nondicomfile = directory + "/" + instance.getAccession() + ".pdf";
                  String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                          + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                  createDirectory(dicomDir);
                  String metaDir = pacsModel.getLocal_archive_ris() + "nondicom/MetaPDF/" + instance.getModality() + "/pdf/" + year
                          + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                  createDirectory(metaDir);
                  dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                  metadatafile = metaDir +"/"+instance.getInstanceUID()+".xml";
              } catch (Exception e) {
                  e.printStackTrace();
              }
              try {
                  metadataService.generateMetadata(srcFileMetadata, metadatafile,
                          instance, 3508, 2480 );
                  log.info("Create metadata {} completed!", metadatafile);
              } catch (Exception e) {
                  e.printStackTrace();
                  log.error("Create metadata {} error!", metadatafile);
              }
              ToolkitService toolkitService = new ToolkitService();
              try {
                  toolkitService.convertpdf2dicom(metadatafile, nondicomfile, dicomfile);
                  toolkitService.clearCache();
              } catch (Exception e) {
                  e.printStackTrace();
                  return new ResponseEntity<>("PDF Convert Error!", HttpStatus.BAD_REQUEST);
              }
          }
      } catch (Exception e) {
          e.printStackTrace();
          return new ResponseEntity<>("PDF Error!", HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>("PDF Created!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/ophthalmology/right/{count}")
    public ResponseEntity<String> convertOphthalmologyRight(@Valid @RequestBody Ophthalmology ophthalmology, @PathVariable String count) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        MetaOphthalmologyService metaOphthalmologyService = new MetaOphthalmologyService();
        InstanceModel instance = ophthalmology.getInstanceModel();
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            int totalImage = Integer.parseInt(count);
            instance.setTotalInstances(totalImage);
            if (instance.getType().equals("jpg")) {
                String studyDate = instance.getStudyDate();
                String year = studyDate.substring(0, 4);
                String month = studyDate.substring(4, 6);
                String day = studyDate.substring(6, 8);
                String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality()+ "/jpeg/" + year
                        + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                createDirectory(directory);
                String srcFileMetadata = null;
                String nondicomfile = null;
                String dicomfile = null;
                String metadatafile = null;
                ToolkitService toolkitService = new ToolkitService();
                try {
                    String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(dicomDir);
                    for (int i = 0; i < totalImage; i++) {
                        instance.setInstanceNumber(i+1);
                        instance.setInstanceUID(generateSOPUID());
                        srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/metadata.xml";
                        nondicomfile = directory + "/" + instance.getAccession()+ "_" + Integer.toString(i) + ".jpg";
                        dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                        metadatafile = directory + "/" + instance.getInstanceUID() + ".xml";
                        try {
                            metaOphthalmologyService.generateMetadata(srcFileMetadata, metadatafile,
                                    instance, 3508, 2480 , "Right", ophthalmology.getMeasure());
                            log.info("Create metadata {} completed!", metadatafile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("Create metadata {} error!", metadatafile);
                        }
                        try {
                            toolkitService.convertjpg2dicom(metadatafile, nondicomfile, dicomfile);
                            toolkitService.clearCache();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new ResponseEntity<>("JPG Ophthalmology Right Convert Error!", HttpStatus.BAD_REQUEST);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("JPG Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("JPG Created!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/ophthalmology/left/{count}")
    public ResponseEntity<String> convertOphthalmologyLeft(@Valid @RequestBody Ophthalmology ophthalmology, @PathVariable String count) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        MetaOphthalmologyService metaOphthalmologyService = new MetaOphthalmologyService();
        InstanceModel instance = ophthalmology.getInstanceModel();
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            int totalImage = Integer.parseInt(count);
            instance.setTotalInstances(totalImage);
            if (instance.getType().equals("jpg")) {
                String studyDate = instance.getStudyDate();
                String year = studyDate.substring(0, 4);
                String month = studyDate.substring(4, 6);
                String day = studyDate.substring(6, 8);
                String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality()+ "/jpeg/" + year
                        + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                createDirectory(directory);
                String srcFileMetadata = null;
                String nondicomfile = null;
                String dicomfile = null;
                String metadatafile = null;
                ToolkitService toolkitService = new ToolkitService();
                try {
                    String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(dicomDir);
                    for (int i = 0; i < totalImage; i++) {
                        instance.setInstanceNumber(i+1);
                        instance.setInstanceUID(generateSOPUID());
                        srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality() + "/metadata.xml";
                        nondicomfile = directory + "/" + instance.getAccession()+ "_" + Integer.toString(i) + ".jpg";
                        dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                        metadatafile = directory + "/" + instance.getInstanceUID() + ".xml";
                        try {
                            metaOphthalmologyService.generateMetadata(srcFileMetadata, metadatafile,
                                    instance, 3508, 2480 , "Left", ophthalmology.getMeasure());
                            log.info("Create metadata {} completed!", metadatafile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("Create metadata {} error!", metadatafile);
                        }
                        try {
                            toolkitService.convertjpg2dicom(metadatafile, nondicomfile, dicomfile);
                            toolkitService.clearCache();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new ResponseEntity<>("JPG Ophthalmology Left Convert Error!", HttpStatus.BAD_REQUEST);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("JPG Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("JPG Created!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/jpg")
    public ResponseEntity<String> convertjpg(@Valid @RequestBody InstanceModel instance) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        MetadataService metadataService = new MetadataService();
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            if (instance.getType().equals("jpg")) {
                String studyDate = instance.getStudyDate();
                String year = studyDate.substring(0, 4);
                String month = studyDate.substring(4, 6);
                String day = studyDate.substring(6, 8);
                String directory = pacsModel.getLocal_archive_ris() + "nondicom/" + instance.getModality();
                String srcFileMetadata = null;
                String nondicomfile = null;
                String dicomfile = null;
                String metadatafile = null;
                try {
                    srcFileMetadata = pacsModel.getLocal_archive_ris() + "nondicom/MetaJPG/" + instance.getModality() + "/metadata.xml";
                    nondicomfile = directory + "/" + instance.getAccession() + ".jpg";
                    String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(dicomDir);
                    String metaDir = pacsModel.getLocal_archive_ris() + "nondicom/MetaJPG/" + instance.getModality() + "/jpg/" + year
                            + "/" + month + "/" + day + "/" + instance.getStudyUID() + "/" + instance.getSeriesUID();
                    createDirectory(metaDir);
                    dicomfile = dicomDir + "/" + instance.getInstanceUID() + ".dcm";
                    metadatafile = metaDir +"/"+instance.getInstanceUID()+".xml";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    metadataService.generateMetadata(srcFileMetadata, metadatafile,
                            instance, 3508, 2480 );
                    log.info("Create metadata {} completed!", metadatafile);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Create metadata {} error!", metadatafile);
                }
                ToolkitService toolkitService = new ToolkitService();
                try {
                    toolkitService.convertpdf2dicom(metadatafile, nondicomfile, dicomfile);
                    toolkitService.clearCache();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<>("JPG Convert Error!", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("JPG Error!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("JPG Created!", HttpStatus.CREATED);
    }

    @PostMapping(value = "/sendpacs")
    public ResponseEntity<String> sendDICOM(@RequestParam(required = false) String studyUID, @RequestParam(required = false) String studyDate) {
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceXray webServiceXray = new WebServiceXray(webClientConfig.localApiClient());
        try {
            PacsModel pacsModel = webServiceXray.getPacs();
            String year = studyDate.substring(0, 4);
            String month = studyDate.substring(4, 6);
            String day = studyDate.substring(6, 8);
            String dicomDir = pacsModel.getLocal_archive_ris() + "dicom/" + year
                    + "/" + month + "/" + day + "/" + studyUID;
            String host = pacsModel.getHost();
            String port = pacsModel.getPort();
            String aetitle = pacsModel.getAetitle();
            String srcae = "BKRIS";
            ToolkitService toolkitService = new ToolkitService();
            toolkitService.sendDicom(host, port,aetitle, srcae, dicomDir);
            toolkitService.clearCache();
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error send PACS!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Send PACS Success!", HttpStatus.CREATED);
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

    public void createNonDicom(String rawData, String destination) {
        try {
            String separator = ",";
            if (rawData.contains(separator)) {
                // use this when the decoded string contains "," separator, like data:image/png;base64,
                String encoded = rawData.split(separator)[1];
                byte[] decodedByte = Base64.getDecoder().decode(encoded.getBytes(StandardCharsets.UTF_8));

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedByte));
                File file = new File(destination);
                ImageIO.write(bufferedImage, "jpg", file);
                log.info("File has been Written as " + destination);
            } else {
                log.info("i haven't think about it yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createNonDicompdf(String rawData, String destination) {
        File file = new File(destination);
        try {
            String separator = ",";
            if (rawData.contains(separator)) {
                String encoded = rawData.split(separator)[1];
                try (FileOutputStream fos = new FileOutputStream(file);) {
                    byte[] decoder = Base64.getDecoder().decode(encoded);
                    fos.write(decoder);
                    log.info("PDF File Saved");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("File Output Stream Failed!");
                }

                log.info("File has been Written as " + destination);
            } else {
                log.info("i haven't think about it yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Integer> dimensionImage (String filepath) throws IOException {
        List<Integer> dimension = new ArrayList<>();
        try {
            File file = new File(filepath);
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(iis, true);
                dimension.add(reader.getWidth(0));
                dimension.add(reader.getHeight(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dimension;
    }

    private static String generateSOPUID() {
        String stamp = getStamp();
        Integer random_id_max = 2;
        String before = generateRandomString(random_id_max);
        String after = generateRandomString(random_id_max);
        String sopUID = before + "." + stamp + "." + after;
        return sopUID;
    }

    private static String getStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }

    private static String generateRandomString(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length); // bound is exclusive
        Random random = new Random();
        return Integer.toString(random.nextInt(max - min) + min);
    }


}
