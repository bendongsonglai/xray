package inext.ris.order.xray.convertDicom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class MetadataService {
    public static void generateMetadata(String srcfile, String destination, InstanceModel instanceModel, int rows, int columns) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        log.info("src {} and des {}", srcfile, destination);
        try (InputStream is = new FileInputStream(srcfile)) {

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);

            NodeList listOfDicomAttribute = doc.getElementsByTagName("DicomAttribute");
            //System.out.println(listOfDicomAttribute.getLength()); // 2

            for (int i = 0; i < listOfDicomAttribute.getLength(); i++) {

                Node dicomAttribute = listOfDicomAttribute.item(i);
                if (dicomAttribute.getNodeType() == Node.ELEMENT_NODE) {
                    String keyword = dicomAttribute.getAttributes().getNamedItem("keyword").getTextContent();
                    if ("StudyDate".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyDate().substring(0, 8));
                                }
                            }
                        }
                    }

                    if ("ContentDate".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyDate().substring(0, 8));
                                }
                            }
                        }
                    }

                    if ("StudyTime".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyDate().substring(8, 14));
                                }
                            }
                        }
                    }

                    if ("ContentTime".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyDate().substring(8, 14));
                                }
                            }
                        }
                    }

                    if ("AccessionNumber".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getAccession());
                                }
                            }
                        }
                    }

                    if ("Modality".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getModality());
                                }
                            }
                        }
                    }

                    if ("Manufacturer".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getManufacturer());
                                }
                            }
                        }
                    }

                    if ("InstitutionName".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(VNCharacterUtils.removeAccent(instanceModel.getInstitutionName()));
                                }
                            }
                        }
                    }

                    if ("ReferringPhysicianName".equals(keyword.trim())) {
                        List<String> lists = getNameDetails(instanceModel.getReferringPhysicianName());
                        Node PersonName = dicomAttribute.getChildNodes().item(1);
                        Node Alphabetic = PersonName.getChildNodes().item(1);

                        NodeList childNodes = Alphabetic.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                                Node item = childNodes.item(j);
                                if (item.getNodeType() == Node.ELEMENT_NODE) {
                                    if ("FamilyName".equalsIgnoreCase(item.getNodeName())) {
                                        item.setTextContent(lists.get(0));
                                    }
                                    if ("GivenName".equalsIgnoreCase(item.getNodeName())) {
                                        item.setTextContent(lists.get(2));
                                    }
                                    if ("MiddleName".equalsIgnoreCase(item.getNodeName())) {
                                        item.setTextContent(lists.get(1));
                                    }
                                }
                        }
                    }

                    if ("StationName".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(VNCharacterUtils.removeAccent(instanceModel.getStationName()));
                                }
                            }
                        }
                    }

                    if ("ManufacturerModelName".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(VNCharacterUtils.removeAccent(instanceModel.getManufacturerModelName()));
                                }
                            }
                        }
                    }

                    if ("PatientName".equals(keyword.trim())) {
                        List<String> lists = getNameDetails(instanceModel.getPatientName());
                        Node PersonName = dicomAttribute.getChildNodes().item(1);
                        Node Alphabetic = PersonName.getChildNodes().item(1);

                        NodeList childNodes = Alphabetic.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("FamilyName".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(lists.get(0) + " " + lists.get(1));
                                }
                                if ("GivenName".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(lists.get(2));
                                }

                            }
                        }
                    }


                    if ("PatientID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getMrn());
                                }
                            }
                        }
                    }

                    if ("PatientBirthDate".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getDob());
                                }
                            }
                        }
                    }

                    if ("PatientSex".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getGender());
                                }
                            }
                        }
                    }

                    if ("SequenceOfUltrasoundRegions".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node ItemNode = childNodes.item(j);
                            NodeList DicomAttributes = ItemNode.getChildNodes();
                            for (int k = 0; k < DicomAttributes.getLength(); k++) {
                                Node item = DicomAttributes.item(k);
                                if (item.getNodeType() == Node.ELEMENT_NODE) {
                                    String keyword1 = item.getAttributes().getNamedItem("keyword").getTextContent();
                                    if ("RegionLocationMaxX1".equals(keyword1.trim())) {
                                        NodeList childNodeDeep = item.getChildNodes();
                                        for (int m = 0; m < childNodeDeep.getLength(); m++) {
                                            Node item1 = childNodeDeep.item(m);
                                            if (item1.getNodeType() == Node.ELEMENT_NODE) {
                                                if ("Value".equalsIgnoreCase(item1.getNodeName())) {
                                                    item1.setTextContent(Integer.toString(columns));
                                                }
                                            }
                                        }
                                    }
                                    if ("RegionLocationMaxY1".equals(keyword1.trim())) {
                                        NodeList childNodeDeep = item.getChildNodes();
                                        for (int m = 0; m < childNodeDeep.getLength(); m++) {
                                            Node item1 = childNodeDeep.item(m);
                                            if (item1.getNodeType() == Node.ELEMENT_NODE) {
                                                if ("Value".equalsIgnoreCase(item1.getNodeName())) {
                                                    item1.setTextContent(Integer.toString(rows));
                                                }
                                            }
                                        }
                                    }
                                    if ("ReferencePixelY0".equals(keyword1.trim())) {
                                        NodeList childNodeDeep = item.getChildNodes();
                                        for (int m = 0; m < childNodeDeep.getLength(); m++) {
                                            Node item1 = childNodeDeep.item(m);
                                            if (item1.getNodeType() == Node.ELEMENT_NODE) {
                                                if ("Value".equalsIgnoreCase(item1.getNodeName())) {
                                                    item1.setTextContent(Integer.toString(rows));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if ("StudyDescription".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyDescription());
                                }
                            }
                        }
                    }

                    if ("StudyInstanceUID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyUID());
                                }
                            }
                        }
                    }

                    if ("StudyID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getStudyUID());
                                }
                            }
                        }
                    }

                    if ("SeriesInstanceUID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getSeriesUID());
                                }
                            }
                        }
                    }

                    if ("InstanceNumber".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(Integer.toString(instanceModel.getInstanceNumber()));
                                }
                            }
                        }
                    }

                    if ("NumberofStudyRelatedInstances".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(Integer.toString(instanceModel.getTotalInstances()));
                                }
                            }
                        }
                    }

                    if ("NumberofSeriesRelatedInstances".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(Integer.toString(instanceModel.getTotalInstances()));
                                }
                            }
                        }
                    }

                    if ("Rows".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(Integer.toString(rows));
                                }
                            }
                        }
                    }

                    if ("Columns".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(Integer.toString(columns));
                                }
                            }
                        }
                    }

                    if ("MediaStorageSOPInstanceUID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getSOPInstanceUID());
                                }
                            }
                        }
                    }

                    if ("SOPInstanceUID".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getSOPInstanceUID());
                                }
                            }
                        }
                    }

                    if ("SOPClassesinStudy".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(instanceModel.getSOPClassesinStudy());
                                }
                            }
                        }
                    }

                }

            }


            try (FileOutputStream output =
                         new FileOutputStream(destination)) {
                writeXml(doc, output);
            }

        } catch (ParserConfigurationException | SAXException
                 | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getNameDetails(String name) {
        List<String> stringList = new ArrayList<>();
        try {
            name = VNCharacterUtils.removeAccent(name);
            int spaceCount = StringUtils.countOccurrencesOf(name, " ");
            String FamilyName = name.substring(0, name.indexOf(" "));
            String GivenName = name.substring(name.lastIndexOf(" ") + 1, name.length());
            String MiddleName;
            if (spaceCount == 1) {
                MiddleName = "";
            } else {
                MiddleName = name.substring(name.indexOf(" ") + 1, name.lastIndexOf(" "));
            }
            stringList.add(FamilyName);
            stringList.add(MiddleName);
            stringList.add(GivenName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringList;
    }

    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // add a xslt to remove the extra newlines
        Transformer transformer = transformerFactory.newTransformer();

        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }

    public static void main(String[] args) {
        MetadataService metadataService = new MetadataService();
        InstanceModel instanceModel = new InstanceModel();
        instanceModel.setType("jpeg");
        instanceModel.setManufacturer("Philip");
        instanceModel.setInstitutionName("BVNT");
        instanceModel.setReferringPhysicianName("Kỹ thuật viên Ninh Thuận");
        instanceModel.setStationName("US10");
        instanceModel.setManufacturerModelName("Philips-O001");
        instanceModel.setMrn("123456");
        instanceModel.setAccession("654321");
        instanceModel.setPatientName("Phạm Thành Luân");
        instanceModel.setDob("19900101");
        instanceModel.setGender("M");
        instanceModel.setModality("US");
        instanceModel.setStudyDescription("ABDOMINAL US");
        instanceModel.setStudyDate("20220802100500");
        instanceModel.setStudyUID("1.2.4236.4361");
        instanceModel.setSeriesUID("2.3.4571245.42541");
        instanceModel.setInstanceUID("1.2.3.34789.56257481");
        instanceModel.setInstanceNumber(2);
        instanceModel.setTotalInstances(3);
        metadataService.generateMetadata("E:\\nondicom\\US\\metadata.xml"
                ,"E:\\nondicom\\US\\jpeg\\1.2.3.34789.56257481.xml", instanceModel, 600, 70);
    }
}
