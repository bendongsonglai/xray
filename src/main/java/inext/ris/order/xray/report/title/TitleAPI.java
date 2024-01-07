package inext.ris.order.xray.report.title;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/title")
@Slf4j
@RequiredArgsConstructor
public class TitleAPI {
    @Autowired
    TitleService titleService;

    @GetMapping("/default/{xray_code}")
    public ResponseEntity<?> findByDefault(@PathVariable String xray_code) {
        Title title;
        try {
            title  = titleService.FindTitleReportByCode(xray_code);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Not exist default Title with XRAY CODE!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(title, HttpStatus.OK);
    }
}
