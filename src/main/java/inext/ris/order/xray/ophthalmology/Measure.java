package inext.ris.order.xray.ophthalmology;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Measure {
    private String type;
    private PixelToArea pixelToArea;
    private SM sm;
}
