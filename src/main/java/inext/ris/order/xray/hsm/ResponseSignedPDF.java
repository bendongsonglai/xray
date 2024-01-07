package inext.ris.order.xray.hsm;
public class ResponseSignedPDF {
    private Integer status;
    private String msg;
    private String data;
    public ResponseSignedPDF(Integer status, String msg, String data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    public ResponseSignedPDF() {
        super();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
