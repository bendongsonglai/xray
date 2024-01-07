package inext.ris.order.xray.ophthalmology;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class List {
    private String no;
    private NUM num;
    private CD cd;
    private AVG avg;
    private SD sd;
    private CV cv;
    private MAX max;
    private MIN min;
    private HEX hex;
    private CT ct;
    private FIX fix;
    private Area0 area0;
    private Area100 area100;
    private Area200 area200;
    private Area300 area300;
    private Area400 area400;
    private Area500 area500;
    private Area600 area600;
    private Area700 area700;
    private Area800 area800;
    private Area900 area900;
    private Apex3 apex3;
    private Apex4 apex4;
    private Apex5 apex5;
    private Apex6 apex6;
    private Apex7 apex7;
    private Apex8 apex8;
    private Apex9 apex9;
    private Apex10 apex10;
    private Image image;
}
