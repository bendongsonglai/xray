package inext.ris.order.xray.hl7;

public class SegmentMessage {
    private String msh;
    private String evn;
    private String pid;

    private String txa;

    private String obx;

    public SegmentMessage() {
        // TODO Auto-generated constructor stub
    }
    public SegmentMessage(String msh, String evn, String pid, String txa, String obx) {
        this.msh = msh;
        this.evn = evn;
        this.pid = pid;
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
