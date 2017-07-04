package com.rssoftware.ou.cbsiso.service.impl;

//import com.rssoftware.upiint.common.ComponentNames;
//import com.rssoftware.ou.model.cbs.ProcessingMessage;
//import com.rssoftware.upiint.common.service.InMemorySchedulerService;
//import com.rssoftware.ou.schema.cbs.CBSISOConstants;
//import com.rssoftware.upiint.model.imps.ImpsConnectionStatus;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.ip.tcp.connection.DefaultTcpSocketSupport;


//import reactor.bus.Event;
//import com.rssoftware.upiint.common.EventBus;

public class CbsIsoTcpSocketSupport extends DefaultTcpSocketSupport {

//	@Autowired
//	InMemorySchedulerService inMemorySchedulerService;
	
//	@Autowired
//	private EventBus eventBus;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.integration.ip.tcp.connection.DefaultTcpSocketSupport#postProcessSocket(java.net.Socket)
	 */
	public void postProcessSocket(java.net.Socket socket){
		super.postProcessSocket(socket);
		
		//scheduleLogon();//logOnToIMPS();
		//scheduleEcho();
		//scheduleLogonOnCutOver();
		
	}
	
	/*private void scheduleLogon(){

			ProcessingMessage logonProvessingMessage = new ProcessingMessage();
			logonProvessingMessage.setImpsSignOn(true);
			//TODO: echoProcessor is non-existent - correct destination to be identified
			inMemorySchedulerService.unSchedule("scheduleLogon");
			inMemorySchedulerService.schedule("scheduleLogon", ComponentNames.IMPSADAPTER, logonProvessingMessage, "0 0/1 * * * ?");
			//unscheduled in IMPSServiceImpl on receipt of 810 response
			
	}*/
	
	/*
	 * Send logon message to IMPS Adapter
	 */
	/*public void logOnToIMPS(){
		//TODO: provide implementation
		ProcessingMessage signOnProcessingMessage = new ProcessingMessage();
		signOnProcessingMessage.setImpsSignOn(true);
		eventBus.notify(ComponentNames.IMPSADAPTER, Event.wrap(signOnProcessingMessage));
	}*/
	
	/*
	 * Schedule echo message to IMPS Adapter -  every 3 minute
	 */
//	private void scheduleEcho(){
//		
///*		if ((ImpsConnectionStatus.ON.equals(IMPSConstants.connectionStatus ))){
//			inMemorySchedulerService.unSchedule("scheduleLogon");
//		}	*/
//			ProcessingMessage echoProcessingMessage = new ProcessingMessage();
//			echoProcessingMessage.setImpsEcho(true);
//			//TODO: echoProcessor is non-existent - correct destination to be identified
//			inMemorySchedulerService.unSchedule("scheduleEcho");
//			inMemorySchedulerService.schedule("scheduleEcho", ComponentNames.IMPSADAPTER, echoProcessingMessage, "0 0/1 * * * ?");
//		
//		
//	}
//	
//	private void scheduleLogonOnCutOver(){
//		ProcessingMessage signOnProcessingMessage = new ProcessingMessage();
//		signOnProcessingMessage.setImpsSignOn(true);
//		//TODO:Need to know re-login time -  change and uncomment line below
//		//inMemorySchedulerService.schedule("scheduleEcho", destination, echoProcessingMessage, "");
//	}
}
