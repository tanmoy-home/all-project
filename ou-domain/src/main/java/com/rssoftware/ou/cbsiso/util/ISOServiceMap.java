package com.rssoftware.ou.cbsiso.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ISOServiceMap {
   @Value("${iso.engine:1234}") 
    private String isoEngine;
   
   public String getIsoEngine() {
	   return isoEngine;
   }
}



 

