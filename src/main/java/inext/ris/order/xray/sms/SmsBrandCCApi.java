package inext.ris.order.xray.sms;

/*import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import utils.Protocol;*/

/**
 * @author phucvm1
 * version 1.0 date: Nov 10, 2016
 */
public class SmsBrandCCApi {

 /*   public static void main(String[] args) {
        sendMT("smsbrand_ctymymy", "Mymy@123", "CTYMYMY", "PK_Victoria", "0367209442", "BN NGUYEN VAN TEST MA BN 073377", "0");
    }

    public static int sendMT(String username, String password, String cpCode,
                             String sender, String receiver, String content, String contentType) {
        int result = -1;
        long startTime = System.currentTimeMillis();

        String url = "http://ams.tinnhanthuonghieu.vn:8009/bulkapi?wsdl";

        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:impl=\"http://impl.bulkSms.ws/\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<impl:wsCpMt>"
                + "<User>" + username + "</User>"
                + "<Password>" + password + "</Password>"
                + "<CPCode>" + cpCode + "</CPCode>"
                + "<RequestID>" + "1" + "</RequestID>"
                + "<UserID>" + receiver + "</UserID>"
                + "<ReceiverID>" + receiver + "</ReceiverID>"
                + "<ServiceID>" + sender + "</ServiceID>"
                + "<CommandCode>" + "bulksms" + "</CommandCode>"
                + "<Content>" + content + "</Content>"
                + "<ContentType>" + contentType + "</ContentType>"
                + "</impl:wsCpMt>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";

        PostMethod post = null;
        try {
            Protocol protocol = new Protocol(url);
            HttpClient httpclient = new HttpClient();
            HttpConnectionManager conMgr = httpclient.getHttpConnectionManager();
            HttpConnectionManagerParams conPars = conMgr.getParams();
            conPars.setConnectionTimeout(20000);
            conPars.setSoTimeout(60000);
            post = new PostMethod(protocol.getUrl());

            RequestEntity entity = new StringRequestEntity(request, "text/xml", "UTF-8");
            post.setRequestEntity(entity);
            post.setRequestHeader("SOAPAction", "");
            httpclient.executeMethod(post);
            InputStream is = post.getResponseBodyAsStream();
            String response = null;
            if (is != null) {
                response = getStringFromInputStream(is);
            }
            System.out.println("Call sendMT response: " + response);

            if (response != null && !response.equals("")) {
                if (response.contains("<result>")) {
                    int start = response.indexOf("<result>") + "<result>".length();
                    int end = response.lastIndexOf("</result>");
                    String responseCode = response.substring(start, end);
                    if (responseCode.equalsIgnoreCase("1")) {
                        result = 0; //call success
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }

        System.out.println("Finish sendMT in " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    private static String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }

        return sb.toString();
    }*/
}