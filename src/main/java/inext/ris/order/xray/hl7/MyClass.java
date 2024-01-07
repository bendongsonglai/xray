package inext.ris.order.xray.hl7;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
public class MyClass {
    public static void main(String args[]) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        //tried this as well  new SimpleDateFormat("EEE',' d MMM YYYY HH:mm:ss z",Locale.US);
        String strDate = date.toString();
        System.out.println(strDate);// Tue Sep 19 10:31:40 GMT 2017
        // i want this --> Tue, Sep 19 2017 10:31:40 GMT 

    }
}