package inext.ris.order.xray.hsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Location {
    private Integer visibleX;
    private Integer visibleY;
    private Integer visibleWidth;
    private Integer visibleHeight;
}
