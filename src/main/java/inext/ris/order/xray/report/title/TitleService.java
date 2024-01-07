package inext.ris.order.xray.report.title;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TitleService {
    @Autowired
    TitleRepository titleRepository;

    public Title FindTitleReportByCode(String xray_code) {
        return titleRepository.FindTitleReportByCode(xray_code);
    }
}
