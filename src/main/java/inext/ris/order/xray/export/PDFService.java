package inext.ris.order.xray.export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PDFService {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static void main(String[] args) throws IOException, DocumentException, URISyntaxException {
        /*Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("E:\\WORK\\workspace victoria\\iTextTable.pdf"));
        document.setPageSize(PageSize.A4);
        document.setMargins(100, 100, 5, 5);
        document.open();
        //PdfPTable table = new PdfPTable(3);
        //addTableHeader(table);
        //addRows(table);
        //addCustomRows(table);

        //document.add(table);
        addHeader(writer);
        addPatient(writer);
        addTitle(writer);
        addTitleTable(writer);
        List<String> urls = new ArrayList<>();
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.327820356868223561199647969937327854013&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.102727464763597665284480183905056915987&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");
        urls.add("http://172.16.60.57/Pathology/ImageStream.do?serverURL=http://localhost:80/wado&study=1.2.20221207051233.5297&series=556353.20221207051233.575870&object=2.25.269532127435892490129520815521693618262&sopClassUID=1.2.840.10008.5.1.4.1.1.6.1&seriesDesc=&images=6&modality=US&contentType=image/jpeg");

        addContentTable(writer, urls);
        document.close();*/
        File initialFile = new File("E:\\WORK\\workspace victoria\\iTextTable.pdf");
        InputStream targetStream = FileUtils.openInputStream(initialFile);
        File targetFile = new File("E:\\WORK\\workspace victoria\\demo1.pdf");
        copyInputStreamToFile(targetStream, targetFile);
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

    private static void addHeader(PdfWriter writer){
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{10, 16});
            header.setTotalWidth(800);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            // add image
            Image logo = Image.getInstance("E:\\WORK\\workspace victoria\\logo.png");
            header.addCell(logo);
            logo.scaleToFit(PageSize.A4);
            // add text
            PdfPCell text = new PdfPCell();
            text.setPaddingBottom(15);
            text.setPaddingLeft(10);
            text.setBorder(Rectangle.NO_BORDER);
            header.addCell(text);
            // write content
            header.writeSelectedRows(0, -1, 100, 840, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (MalformedURLException e) {
            throw new ExceptionConverter(e);
        } catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    private static void addPatient(PdfWriter writer){
        PdfPTable header = new PdfPTable(3);
        try {
            // set defaults
            header.setWidths(new int[]{10, 12, 4});
            header.setTotalWidth(400);
            header.setLockedWidth(true);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // add Date
            PdfPCell date = new PdfPCell();
            date.setBorder(Rectangle.NO_BORDER);

            date.addElement(new Phrase("Date : 14/12/2022", new Font(Font.FontFamily.HELVETICA, 10)));
            header.addCell(date);
            // add Patient Name
            PdfPCell patientName = new PdfPCell();
            patientName.setBorder(Rectangle.NO_BORDER);

            patientName.addElement(new Phrase("Patient Name: PHAM NGOC TUYET", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            header.addCell(patientName);
            // add Patient Name
            PdfPCell patientID = new PdfPCell();
            patientID.setBorder(Rectangle.NO_BORDER);

            patientID.addElement(new Phrase("ID: 434140", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            header.addCell(patientID);

            // write content
            header.writeSelectedRows(0, -1, 100, 800, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private static void addTitle(PdfWriter writer){
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{1, 16});
            header.setTotalWidth(600);
            header.setLockedWidth(true);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // add Date
            PdfPCell title = new PdfPCell();
            title.setBorder(Rectangle.NO_BORDER);

            title.addElement(new Phrase("Title :", new Font(Font.FontFamily.HELVETICA, 10)));
            header.addCell(title);
            // add Patient Name
            PdfPCell space = new PdfPCell();
            space.setBorder(Rectangle.NO_BORDER);

            space.addElement(new Phrase("ENDOSCOPY REPORT", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            header.addCell(space);

            // write content
            header.writeSelectedRows(0, -1, 100, 780, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private static void addTitleTable(PdfWriter writer){
        PdfPTable header = new PdfPTable(1);
        try {
            // set defaults
            header.setWidths(new int[]{1});
            header.setTotalWidth(300);
            header.setLockedWidth(true);
            header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            header.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            header.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            // add Patient Name
            PdfPCell space = new PdfPCell();
            space.setBorder(Rectangle.NO_BORDER);
            byte[] fontByte = IOUtils
                    .toByteArray(PDFService.class.getResourceAsStream("/font/Helvetica-Bold.ttf"));
            Font f = new Font(BaseFont.createFont("Helvetica-Bold.ttf"
                    , BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontByte, null));
            space.addElement(new Phrase("COLONOSCOPY REPORT (Nội Soi Đại Tràng)", f));
            space.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            space.setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
            header.addCell(space);

            // write content
            header.writeSelectedRows(0, -1, 150, 740, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void addContentTable(PdfWriter writer, List<String> urls){
        Integer total = urls.size();
        Integer size = 0;
        if (total % 2 == 0) {
            size = total / 2;
        } else {
            size = (total + 1) / 2;
        }
        try {
            for (int i = 0; i< size; i++) {
                PdfPTable header = new PdfPTable(2);
                header.setWidths(new int[]{1, 1});
                header.setTotalWidth(300);
                header.setSpacingBefore(10);
                header.setSpacingAfter(10);
                header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                header.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_MIDDLE);
                header.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
                header.getDefaultCell().setFixedHeight(150);
                try {
                    Image image = Image.getInstance(getImage(new URL(urls.get(i))));
                    image.setScaleToFitHeight(true);
                    header.addCell(image);
                    if ((i + 2) < total) {
                        Image image1 = Image.getInstance(getImage(new URL(urls.get(i + 2))));
                        header.addCell(image1);
                    } else {
                        PdfPCell space = new PdfPCell();
                        space.setBorder(Rectangle.NO_BORDER);
                        space.addElement(new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                        header.addCell(space);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    PdfPCell space = new PdfPCell();
                    space.setBorder(Rectangle.NO_BORDER);
                    space.addElement(new Phrase("", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                    header.addCell(space);
                }
                header.writeSelectedRows(0, -1, 150, 710 - i * 150, writer.getDirectContent());
            }
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell();
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("column header 1", "column header 2", "column header 3")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private static void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get("E:\\WORK\\workspace victoria\\logo.png");
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
    public static byte[] getImage(URL url) throws IOException {
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.connect();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), baos);

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
