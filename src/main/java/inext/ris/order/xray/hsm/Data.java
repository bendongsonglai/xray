package inext.ris.order.xray.hsm;
import java.util.List;


public class Data {
    private List<ResponseContent> responseContentList;
    private String base64Certificate;
    public Data(List<ResponseContent> responseContentList, String base64Certificate) {
        this.responseContentList = responseContentList;
        this.base64Certificate = base64Certificate;
    }
    public Data () {
        super();
    }

    public List<ResponseContent> getResponseContentList() {
        return responseContentList;
    }

    public void setResponseContentList(List<ResponseContent> responseContentList) {
        this.responseContentList = responseContentList;
    }

    public String getBase64Certificate() {
        return base64Certificate;
    }

    public void setBase64Certificate(String base64Certificate) {
        this.base64Certificate = base64Certificate;
    }
}
