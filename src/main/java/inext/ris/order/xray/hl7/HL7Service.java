package inext.ris.order.xray.hl7;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class HL7Service implements HL7Repository {
	private static final int BUFFER_SIZE = 4096;
	public static final int DEFAULT_BUFFER_SIZE = 8192;
	static Charset windows1252 = Charset.forName("windows-1252");
	public void createFileHL7 (HL7Model hl7) {
		String msh = null;
		String pid = null;
		String evn = null;
		String pv1 = null;
		String orc = null;
		String obr = null;
		String zds = null;
		try {
			ADTMessageImpl adt = new ADTMessageImpl();
			ADTMessageImpl.setADT(hl7.getOrganization(), hl7.getPacs(), convertTimestampToString(hl7.getEncounterPeriodStart()), hl7.getBundleID(), convertTimestampToString(hl7.getServiceAuthoredOn())
					, hl7.getPatientID(), getPatientName(hl7.getPatientName()).get(0), getPatientName(hl7.getPatientName()).get(1), getPatientName(hl7.getPatientName()).get(2)
					, convertBirthDate(hl7.getPatientBirthDate()), convertGender(hl7.getPatientGender()), hl7.getPatientAddressLine(), hl7.getPatientAddressCountry(), hl7.getPatientTelecomNumber()
					, convertPatientClass(hl7.getEncounterClass()), hl7.getEncounterLocation(), hl7.getServiceRequesterIdentifierValue(), hl7.getServiceRequesterDisplay());

			ORMMessageImpl orm = new ORMMessageImpl();
			ORMMessageImpl.setOrder(convertTimestampToString(hl7.getServiceAuthoredOn()));
			ORUMessageImpl oru = new ORUMessageImpl();
			ORUMessageImpl.setOrder(hl7.getImagingIdentifier(), hl7.getServiceCodeCodingCode(), hl7.getServiceCodeText(), convertTimestampToString(hl7.getEncounterPeriodStart())
					, hl7.getServiceReasonCodeText(), hl7.getPacs(), hl7.getImagingModalityCode());


			Segment segment =  new Segment(adt.getMSH(), adt.getEVN(), adt.getPID(), adt.getPV1(), orm.getORC(), oru.getOBR(), ZMessage.getZDS(hl7.getServiceID()));
			msh = segment.getMsh();
			evn = segment.getEvn();
			pid = segment.getPid();
			pv1 = segment.getPv1();
			orc = segment.getOrc();
			obr = segment.getObr();
			zds = segment.getZds();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		writeFileHL7(hl7.getDestination()+hl7.getImagingIdentifier()+"_"+convertTimestampToString(hl7.getServiceAuthoredOn())+"_NW.hl7", msh, evn, pid, pv1, orc, obr, zds);
	}

	public void createReportHL7 (HL7ReportModel hl7) {
		String msh = null;
		String pid = null;
		String evn = null;
		String pv1 = null;
		String orc = null;
		String obr = null;
		String txa = null;
		String obx = null;
		try {
			ADTMessageImpl adt = new ADTMessageImpl();

			adt.setADTReport(hl7.getSend_app(), hl7.getSend_fac(), hl7.getRece_app(), hl7.getRece_fac(), hl7.getDateTimeOfMessage()
					, hl7.getAccessionNumber(), hl7.getAuthoredOn(), hl7.getPid(), hl7.getFamily(), hl7.getGiven()
					, hl7.getName(), hl7.getBirthdate(), hl7.getGender(), hl7.getRace(), hl7.getAddress()
					, hl7.getCountry(), hl7.getPhoneHome(), hl7.getPhoneBusiness(), hl7.getEthic(), VNCharacterUtils.removeAccent(hl7.getLocation()).toUpperCase()
					, hl7.getReferrer_code(), hl7.getReferrer_name(), hl7.getReferrer_lastname()
					, hl7.getProvider(), hl7.getConsult_name(), hl7.getConsult_lastname());

			ORMMessageImpl orm = new ORMMessageImpl();
			ORMMessageImpl.setOrderReport(hl7.getAuthoredOn(), hl7.getReferrer_code()
					, hl7.getReferrer_name(), hl7.getReferrer_lastname());

			DOCMessageImpl doc = new DOCMessageImpl();
			doc.setDoc(hl7.getDoc_code(), hl7.getDoc_type(), hl7.getActivityDateTime(), hl7.getProvider()
					, hl7.getConsult_name(), hl7.getConsult_lastname(), hl7.getConsult_position(), hl7.getAccessionNumber(), hl7.getOriginationDateTime()
					, hl7.getEditDateTime(), hl7.getCe_mp().replaceAll("[\\t\\n\\r]+", " "));

			ORUMessageImpl oru = new ORUMessageImpl();
			oru.setReport(hl7.getAccessionNumber(), hl7.getXray_code(), hl7.getProcedure(), hl7.getRecordedDateTime()
					, hl7.getReason(), hl7.getProvider(), hl7.getConsult_lastname(), hl7.getConsult_name(), hl7.getConsult_position(), hl7.getResultsDateTime()
					, hl7.getModality(),  hl7.getObxList());
			oru.setReportOBX(hl7.getAccessionNumber(), hl7.getXray_code(), hl7.getProcedure(), hl7.getRecordedDateTime()
					, hl7.getReason(), hl7.getProvider(), hl7.getConsult_lastname(), hl7.getConsult_name(), hl7.getConsult_position(), hl7.getResultsDateTime()
					, hl7.getModality(),  hl7.getObxList());

			SegmentReport segment =  new SegmentReport(adt.getMSH(), adt.getEVN(), adt.getPID()
					, adt.getPV1(), orm.getORCReport(), oru.getOBR_REPORT(), doc.getTXA(), oru.getOBX_REPORT(hl7.getObxList().size() + 1));
			msh = segment.getMsh();
			evn = segment.getEvn();
			pid = segment.getPid();
			pv1 = segment.getPv1();
			orc = segment.getOrc();
			obr = segment.getObrReport();
			txa = segment.getTxa();
			obx = segment.getObx();
		} catch (Exception e) {
			e.printStackTrace();
		}
		writeANSI(hl7.getDirectory()+hl7.getAccessionNumber()+"_"+hl7.getRecordedDateTime()+"_RE.hl7"
				, msh, evn, pid, pv1, orc, obr, txa, obx);

	}


	private static void writeFileReportHL7 (String fileName, String msh, String evn, String pid, String pv1, String orc, String obr, String txa) {
		BufferedWriter writer = null;
		try {
			//writer = new BufferedWriter(new FileWriter(fileName));
			//ANSI
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.append((msh));
			writer.newLine();
			writer.append((evn));
			writer.newLine();
			writer.append((pid));
			writer.newLine();
			writer.append((pv1));
			writer.newLine();
			writer.append((orc));
			writer.newLine();
			writer.append((txa));
			writer.newLine();
			writer.append((obr));
			writer.flush();
			writer.close();
		} catch (IOException ie) {
			log.error("Failed when create file hl7.");
			ie.fillInStackTrace();
		}
		log.info("File HL7 {} Created.", fileName);
	}

	public static void writeANSI(String fileName,  String msh, String evn, String pid, String pv1, String orc, String obr, String txa, String obx) {

		Path path = Paths.get(fileName);

		try (BufferedWriter writer = Files.newBufferedWriter(path, windows1252)) {
			writer.append((msh));
			writer.newLine();
			writer.append((evn));
			writer.newLine();
			writer.append((pid));
			writer.newLine();
			writer.append((pv1));
			writer.newLine();
			writer.append((orc));
			writer.newLine();
			writer.append((obr));
			writer.newLine();
			writer.append((txa));
			writer.newLine();
			writer.append((obx));
			//writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*try {
			String dir = fileName.substring(0, fileName.lastIndexOf("reporttemp")+10);
			//String file_copy = fileName.substring(fileName.lastIndexOf("reporttemp")+11, fileName.length());
			String file_name = fileName.substring(fileName.lastIndexOf("reporttemp")+11, fileName.lastIndexOf(".hl7"));
			String datetime = file_name.split("_")[1];
			String year = datetime.substring(0, 4);
			String month = datetime.substring(4, 6);
			String day = datetime.substring(6, 8);
			String directory = dir.replace("report", "hl7_report_backup") + "/" + year
					+ "/" + month + "/" + day;
			createDirectory(directory);
			//File initialFile = new File(fileName);
			//InputStream targetStream = FileUtils.openInputStream(initialFile);
			//File targetFile = new File(directory + "/" +file_copy);
			//copyInputStreamToFile(targetStream, targetFile);
		}catch (Exception e) {
			e.printStackTrace();
			log.error("Cant copy backup report file!");
		}*/

	}
	private static String convertANSI (String content) {
		byte[] bytes = content.getBytes();
		String newFormat = "";
		try {
			newFormat = new String(bytes, windows1252);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return newFormat;
	}
	private static void writeFileHL7 (String fileName, String msh, String evn, String pid, String pv1, String orc, String obr, String zds) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.append(msh);
			writer.newLine();
			writer.append(evn);
			writer.newLine();
			writer.append(pid);
			writer.newLine();
			writer.append(pv1);
			writer.newLine();
			writer.append(orc);
			writer.newLine();
			writer.append(obr);
			writer.newLine();
			writer.append(zds);
			writer.newLine();
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException ie) {
			log.error("Failed when create file hl7.");
			ie.fillInStackTrace();
		}
		log.info("File HL7 {} Created.", fileName);
	}
	private static String convertBirthDate(String birthdate) {
		String dob = null;
		String dp = "yyyy-MM-dd";

		SimpleDateFormat sdf = new SimpleDateFormat(dp);
		SimpleDateFormat newdf = new SimpleDateFormat("yyyyMMdd");
		Date d =  null;;
		try {
			d = sdf.parse(birthdate);
			dob = newdf.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dob;
	}
	private static String convertTimestampToString (String timestamp) {
		String datetime =  null;
		String dp = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(dp);
		SimpleDateFormat newdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date d =  null;;
		try {
			d = sdf.parse(timestamp);
			datetime = newdf.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datetime;
	}
	private static List<String> getPatientName (String patientName) {
		//patientName = Tools.removeAccentCode(patientName);
		patientName = patientName == null ? null :
				Normalizer.normalize(patientName, Normalizer.Form.NFD)
						.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		List<String> list = new ArrayList<String>();
		int start;
		int end;
		String family = null;
		String name = null;
		String given = null;
		if(patientName.contains(" ")) {
			start = patientName.indexOf(" ");
			end = patientName.lastIndexOf(" ");
			family = patientName.substring(0, start);
			name = patientName.substring(end, patientName.length());
			given = patientName.substring(start, end);
		} else {
			family = "";
			given = "";
			name = patientName;
		}
		list.add(family);
		list.add(given);
		list.add(name);
		return list;
	}
	private static String convertGender (String gender) {
		String gen = null;
		if (gender.equals("MALE")) {
			gen="M";
		} else if (gender.equals("FEMALE")) {
			gen="F";
		} else {
			gen="O";
		}
		return gen;
	}
	private static String convertPatientClass (String patientClass) {
		String pcCode = null;
		if (patientClass.equals("EMER")) {
			pcCode = "E";
		} else if (patientClass.equals("IMP")) {
			pcCode = "I";
		} else {
			pcCode = "O";
		}
		return pcCode;
	}

	private static void downloadFile (String urlLink, File fileLoc) throws IOException {
		URL url = new URL(urlLink);
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		FileOutputStream fis = new FileOutputStream(fileLoc);
		byte[] buffer = new byte[1024];
		int count=0;
		while((count = bis.read(buffer,0,1024)) != -1)
		{
			fis.write(buffer, 0, count);
		}
		fis.close();
		bis.close();
	}

	public static void copyInputStreamToFile(InputStream inputStream, File file)
			throws IOException {

		// append = false
		try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
			int read;
			byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
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

	public static void main(String[] args) throws IOException {
		//System.out.println(getPatientName("Nguyễn Thị Hớn"));
		//String name = "Nguyễn Thị Hớn";
		/*System.out.println(name.length());
		System.out.println(name == null ? null :
            Normalizer.normalize(name, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));*/
		//Please provide the correct URL of what you want to download, and the correct directory with a name and extension to save the downloaded file in.
		String link = "https://172.16.60.57/bkris/pathologypdf.php?ACCESSION=3612612";
		File fileLoc = new File("E:\\WORK\\workspace victoria\\demo.pdf");

		downloadFile(link, fileLoc);
	}
}
