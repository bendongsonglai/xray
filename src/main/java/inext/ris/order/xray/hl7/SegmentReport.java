package inext.ris.order.xray.hl7;

public class SegmentReport {
    private String msh;
    private String evn;
    private String pid;
    private String pv1;
    private String orc;

    private String obrReport;

    private String txa;

    private String obx;

    public SegmentReport() {
        // TODO Auto-generated constructor stub
    }
    public SegmentReport(String msh, String evn, String pid, String pv1, String orc, String obrReport, String txa, String obx) {
        this.msh = msh;
        this.evn = evn;
        this.pid = pid;
        this.pv1 = pv1;
        this.orc = orc;
        this.obrReport = obrReport;
        this.txa = txa;
        this.obx = obx;
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

    public String getObrReport() {
        return obrReport;
    }

    public void setObrReport(String obrReport) {
        this.obrReport = obrReport;
    }

    public String getObx() {
        return obx;
    }

    public void setObx(String obx) {
        this.obx = obx;
    }

    public String getTxa() {
        return txa;
    }

    public void setTxa(String txa) {
        this.txa = txa;
    }
}
