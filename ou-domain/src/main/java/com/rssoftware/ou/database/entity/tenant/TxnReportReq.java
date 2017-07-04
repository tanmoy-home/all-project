package com.rssoftware.ou.database.entity.tenant;
/**
<?xml version="1.0" encoding="UTF-8"?>
<TxnReportReq>
<hrColonMins>18:27</hrColonMins>
<day>6</day>
</TxnReportReq>
 */
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = ""/*, propOrder = {
        "ouId",
        "transactions"
}*/)
@XmlRootElement(name = "TxnReportReq")
public class TxnReportReq implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1896632033489118005L;
/*    @XmlAttribute(name = "hrColonMins")
*/    private String hrColonMins;
/*    @XmlAttribute(name = "day")
*/    private String day;
	public String getHrColonMins() {
		return hrColonMins;
	}
	public void setHrColonMins(String hrColonMins) {
		this.hrColonMins = hrColonMins;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
    
}