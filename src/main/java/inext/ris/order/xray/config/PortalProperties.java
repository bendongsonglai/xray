package inext.ris.order.xray.config;

import java.io.FileInputStream;
import java.util.Properties;

public class PortalProperties {
    private String portalUrl = "127.0.0.1";
    Properties prop = new Properties();

    public void LoadProperties() {
        try (FileInputStream fileInputStream = new FileInputStream("/usr/xray/xray.properties")) {
            prop.load(fileInputStream);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Properties getProperties() {
        return prop;
    }

    public String getPortalURL() {
        try {
            portalUrl = prop.getProperty("portal.url");// Portal URL
        } catch (Exception e) {
            e.printStackTrace();
        }
        return portalUrl;
    }
}
