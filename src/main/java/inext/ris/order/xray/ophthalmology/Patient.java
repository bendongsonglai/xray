package inext.ris.order.xray.ophthalmology;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Patient {
    private String no;
    private String iD;
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String age;
    private String dOB;
    private String nameJ1;
    private String nameJ2;
    private String memo;
}
