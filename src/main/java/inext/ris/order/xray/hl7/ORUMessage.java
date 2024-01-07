package inext.ris.order.xray.hl7;

import java.util.List;

public interface ORUMessage {
	public String getOBR();
	public String getOBR_REPORT();
	public String getOBX_REPORT(Integer total);

	public String getOBR_MESSAGE();
	public String getOBX_MESSAGE(Integer total);
}
