package inext.ris.order.xray.hl7;

public class ZMessage {
	public static String getZDS(String acessionNumber) {
		String message= null;
		message = "ZDS|"+acessionNumber+"|||||||||";
		return message;
	}
}
