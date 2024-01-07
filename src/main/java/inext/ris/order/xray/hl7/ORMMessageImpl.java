package inext.ris.order.xray.hl7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.group.ORM_O01_ORDER;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import ca.uhn.hl7v2.model.v25.segment.ORC;

public class ORMMessageImpl implements ORMMessage {
	private static ORM_O01 orm;
	private static ORM_O01_ORDER orderORC;
	private static List<String> segment = new ArrayList<String>();
	/*public static void main(String[] args) throws HL7Exception, IOException {
		ORMMessageImpl.setOrder();
		System.out.println(ORMMessageImpl.getTest());
	}*/
	public static void setOrder(String authoredOn) {
		ORMMessageImpl.orm = new ORM_O01();
		try {
			orm.initQuickstart("ORM", "O01", "P");
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ORMMessageImpl.orderORC = orm.getORDER();
    	// Populate the ORC Segment
    	try { 
        ORC orcSegment =  orderORC.getORC();
        orcSegment.getOrc1_OrderControl().setValue("NW");
        orcSegment.getOrc2_PlacerOrderNumber().getEntityIdentifier().setValue("");
        orcSegment.getOrc3_FillerOrderNumber().getEntityIdentifier().setValue("");
        orcSegment.getOrc4_PlacerGroupNumber().getEntityIdentifier().setValue("");
        orcSegment.getOrc5_OrderStatus().setValue("IP");
        orcSegment.getOrc6_ResponseFlag().setValue("");
        orcSegment.getOrc7_QuantityTiming(0).getStartDateTime().getTime().setValue(authoredOn);
        orcSegment.getOrc7_QuantityTiming(0).getText().setValue("#");
        orcSegment.getOrc8_ParentOrder();
        orcSegment.getOrc9_DateTimeOfTransaction().getTime().setValue("");
        orcSegment.getOrc10_EnteredBy(0).getMessage();
        orcSegment.getOrc11_VerifiedBy(0).getIDNumber().setValue("");
        orcSegment.getOrc12_OrderingProvider(0).getIDNumber().setValue("#");
        orcSegment.getOrc13_EntererSLocation();
        orcSegment.getOrc14_CallBackPhoneNumber();
        orcSegment.getOrc15_OrderEffectiveDateTime();
        orcSegment.getOrc16_OrderControlCodeReason();
        orcSegment.getOrc17_EnteringOrganization();
        orcSegment.getOrc18_EnteringDevice();
        orcSegment.getOrc19_ActionBy();
        orcSegment.getOrc20_AdvancedBeneficiaryNoticeCode();
        orcSegment.getOrc21_OrderingFacilityName();
        orcSegment.getOrc22_OrderingFacilityAddress();
        orcSegment.getOrc23_OrderingFacilityPhoneNumber();
        orcSegment.getOrc24_OrderingProviderAddress(0).getAddressRepresentationCode().setValue("");
        //orcSegment.insertRepetition(24, 0);
        // Now, let's encode the message and look at the output      
        segment =  Arrays.asList(orm.encode().split("\r"));
    	} catch (HL7Exception he) {
    		he.fillInStackTrace();
    	}
	}

    public static void setOrderReport(String authoredOn, String referrer_code, String referrer_name, String referrer_lastname) {
        ORMMessageImpl.orm = new ORM_O01();
        try {
            orm.initQuickstart("ORM", "O01", "P");
        } catch (HL7Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ORMMessageImpl.orderORC = orm.getORDER();
        // Populate the ORC Segment
        try {
            ORC orcSegment =  orderORC.getORC();
            orcSegment.getOrc1_OrderControl().setValue("RE");
            orcSegment.getOrc2_PlacerOrderNumber().getEntityIdentifier().setValue("");
            orcSegment.getOrc3_FillerOrderNumber().getEntityIdentifier().setValue("");
            orcSegment.getOrc4_PlacerGroupNumber().getEntityIdentifier().setValue("");
            orcSegment.getOrc5_OrderStatus();
            orcSegment.getOrc6_ResponseFlag();
            orcSegment.getOrc7_QuantityTiming();
            orcSegment.getOrc8_ParentOrder();
            orcSegment.getOrc9_DateTimeOfTransaction().getTime().setValue(authoredOn);
            orcSegment.getOrc10_EnteredBy(0).getMessage();
            orcSegment.getOrc11_VerifiedBy(0).getIDNumber().setValue("");
            orcSegment.getOrc12_OrderingProvider(0).getIDNumber().setValue(referrer_code);
            orcSegment.getOrc12_OrderingProvider(0).getFamilyName().getSurname().setValue(referrer_lastname);
            orcSegment.getOrc12_OrderingProvider(0).getGivenName().setValue(referrer_name);
            orcSegment.getOrc12_OrderingProvider(0).getPrefixEgDR().setValue("#");
            orcSegment.getOrc13_EntererSLocation();
            orcSegment.getOrc14_CallBackPhoneNumber();
            orcSegment.getOrc15_OrderEffectiveDateTime();
            orcSegment.getOrc16_OrderControlCodeReason();
            orcSegment.getOrc17_EnteringOrganization();
            orcSegment.getOrc18_EnteringDevice();
            orcSegment.getOrc19_ActionBy();
            orcSegment.getOrc20_AdvancedBeneficiaryNoticeCode().getIdentifier().setValue("#");
            /*orcSegment.getOrc21_OrderingFacilityName();
            orcSegment.getOrc22_OrderingFacilityAddress();
            orcSegment.getOrc23_OrderingFacilityPhoneNumber();
            orcSegment.getOrc24_OrderingProviderAddress();
            orcSegment.getOrc25_OrderStatusModifier();
            orcSegment.getOrc26_AdvancedBeneficiaryNoticeOverrideReason();
            orcSegment.getOrc27_FillerSExpectedAvailabilityDateTime();
            orcSegment.getOrc28_ConfidentialityCode();
            orcSegment.getOrc29_OrderType();
            orcSegment.getOrc30_EntererAuthorizationMode().getIdentifier().setValue("#");*/
            //orcSegment.insertRepetition(24, 0);
            // Now, let's encode the message and look at the output
            segment =  Arrays.asList(orm.encode().split("\r"));
        } catch (HL7Exception he) {
            he.fillInStackTrace();
        }
    }

	@Override
	public String getORC() {
		// TODO Auto-generated method stub
		String message = null;
		message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
		return message;
	}

    @Override
    public String getORCReport() {
        // TODO Auto-generated method stub
        String message = null;
        message = segment.get(1).replaceAll("[\\#\\~\\r\\n]", "");
        message = message.replace("\\S\\", "^");
        return message;
    }
}
