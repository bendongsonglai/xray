package inext.ris.order.xray.hl7;

public interface ADTMessage {
	public String getMSH();
	public String getEVN();
	public String getPID();
	public String getPV1();
}
