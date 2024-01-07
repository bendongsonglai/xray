package inext.ris.order.xray.hl7;

public class Segment {
private String msh;
private String evn;
private String pid;
private String pv1;
private String orc;
private String obr;
private String zds;
	public Segment() {
		// TODO Auto-generated constructor stub
	}
	public Segment(String msh, String evn, String pid, String pv1, String orc, String obr, String zds) {
		this.msh = msh;
		this.evn = evn;
		this.pid = pid;
		this.pv1 = pv1;
		this.orc = orc;
		this.obr = obr;
		this.zds = zds;
	}
	public String getMsh() {
		return msh;
	}
	public void setMsh(String msh) {
		this.msh = msh;
	}
	public String getEvn() {
		return evn;
	}
	public void setEvn(String evn) {
		this.evn = evn;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPv1() {
		return pv1;
	}
	public void setPv1(String pv1) {
		this.pv1 = pv1;
	}
	public String getOrc() {
		return orc;
	}
	public void setOrc(String orc) {
		this.orc = orc;
	}
	public String getObr() {
		return obr;
	}
	public void setObr(String obr) {
		this.obr = obr;
	}
	public String getZds() {
		return zds;
	}
	public void setZds(String zds) {
		this.zds = zds;
	}
}
