package inext.ris.order.xray.convertDicom;

import inext.ris.order.xray.ophthalmology.Measure;
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
public class MetaOphthalmologyService {
    public static void generateMetadata(String srcfile, String destination, InstanceModel instanceModel, int rows, int columns, String seriesDes, Measure measure) {
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
                    }

                    if ("SeriesDescription".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(seriesDes);
                                }
                            }
                        }
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
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
                        continue;
                    }

                    if ("PixelToArea".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getPixelToArea().getValue());
                                }
                            }
                        }
                        continue;
                    }
                    /* Right */
                    if ("R_NUM".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getNum().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_CD".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getCd().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_AVG".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getAvg().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_SD".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getSd().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_CV".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getCv().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_MAX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getMax().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_MIN".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getMin().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_HEX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getHex().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_CT".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getCt().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_FIX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getFix().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_FIX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getFix().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area0".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea0().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area100".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea100().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area200".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea200().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area300".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea300().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area400".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea400().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area500".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea500().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area600".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea600().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area700".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea700().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area800".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea800().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Area900".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getArea900().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex3".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex3().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex4".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex4().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex5".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex5().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex6".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex6().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex7".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex7().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex8".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex8().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex9".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex9().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("R_Apex10".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getR().getList().getApex10().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    /* Left */

                    if ("L_NUM".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getNum().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_CD".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getCd().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_AVG".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getAvg().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_SD".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getSd().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_CV".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getCv().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_MAX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getMax().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_MIN".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getMin().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_HEX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getHex().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_CT".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getCt().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_FIX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getFix().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_FIX".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getFix().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area0".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea0().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area100".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea100().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area200".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea200().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area300".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea300().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area400".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea400().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area500".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea500().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area600".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea600().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area700".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea700().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area800".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea800().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Area900".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getArea900().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex3".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex3().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex4".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex4().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex5".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex5().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex6".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex6().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex7".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex7().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex8".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex8().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex9".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex9().getValue());
                                }
                            }
                        }
                        continue;
                    }

                    if ("L_Apex10".equals(keyword.trim())) {
                        NodeList childNodes = dicomAttribute.getChildNodes();
                        for (int j = 0; j < childNodes.getLength(); j++) {
                            Node item = childNodes.item(j);
                            if (item.getNodeType() == Node.ELEMENT_NODE) {
                                if ("Value".equalsIgnoreCase(item.getNodeName())) {
                                    item.setTextContent(measure.getSm().getL().getList().getApex10().getValue());
                                }
                            }
                        }
                        continue;
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

}
