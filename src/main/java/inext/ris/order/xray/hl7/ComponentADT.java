package inext.ris.order.xray.hl7;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ComponentADT {
    public String getMSH(List<String> segment) {
        // TODO Auto-generated method stub
        String MSHmessage = null;
        MSHmessage = segment.get(0).replaceAll("[\\#\\~\\\r\\n]", "");
        MSHmessage = MSHmessage.replace("|^", "|^~");
        MSHmessage = MSHmessage.replace("\\S\\", "^");
        MSHmessage = MSHmessage.replace("^ADT_A01", "");
        return MSHmessage;
    }

    public String getEVN(List<String> segment) {
        // TODO Auto-generated method stub
        String EVNmessage = null;
        EVNmessage = segment.get(1).replaceAll("[\\#\\~\\\r\\n]", "");
        EVNmessage = EVNmessage.replace("\\S\\", "^");
        return EVNmessage;
    }


    public String getPID(List<String> segment) {
        // TODO Auto-generated method stub
        String PIDmessage = null;
        PIDmessage = segment.get(2).replaceAll("[\\#\\~\\\r\\n]", "");
        PIDmessage = PIDmessage.replace("\\S\\", "^");
        return PIDmessage;
    }


    public String getPV1(List<String> segment) {
        // TODO Auto-generated method stub
        String PV1message = null;
        PV1message = segment.get(3).replaceAll("[\\#\\~\\\r\\n]", "");
        PV1message = PV1message.replace("\\S\\", "^");
        return PV1message;
    }
}
