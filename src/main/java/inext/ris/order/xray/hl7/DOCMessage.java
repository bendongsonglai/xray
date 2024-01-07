package inext.ris.order.xray.hl7;

import java.util.List;

public interface DOCMessage {
    public String getTXA();
    public List<String> getOBX();
}
