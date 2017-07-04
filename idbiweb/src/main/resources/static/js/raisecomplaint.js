
var message = [ {msgId:1, msgErr:"Provided mobile number is not valid! Please enter valid 10 digit mobile number start with '7/8/9'."}, 
						{msgId:2, msgErr:"Provided PAN number is not valid! PAN Card should be entered in [AAAAA9999A]"},
						{msgId:3, msgErr:"Provided Email address is not valid!"},
						{msgId:4, msgErr:"Please provide alpha-numeric value!"},
						{msgId:5, msgErr:"Please provide numeric value!"},
						{msgId:6, msgErr:"Please provide data in mandatory field!"},
						{msgId:7, msgErr:"Please provide valid amount"},
						{msgId:8, msgErr:"Please select a payment mode"},
						{msgId:9, msgErr:"Provided Aadhar is not valid! Aadhar should not start with 0 or 1 and should be 12 digits"},
						{msgId:10, msgErr:"Please provide a mobile number to generate OTP."}
					];

function getErrMessage(msgId) {
	for(msg in message) {
		if(message[msg].msgId == msgId) {
			alert(message[msg].msgErr);
		}
	}
}

function isEmpty(str){
	return str===null || str.match(/^ *$/) !== null;
}

function enableView(rdo_btn)
{
	$("#complainttxnid").val($('input[name=txnlist]:checked').val());
	$("#viewTranDtl").removeAttr("disabled" );	
}

function viewTransactionDetails()
{
	var txnID=$('input[name=txnlist]:checked').val();
	var mobNo = $('#txnmobile').val();
	$('#txnId').html($('#'+txnID+'_txnId').html());
	$('#blrId').html($('#'+txnID+'_billerId').html());
	$('#amnt').html($('#'+txnID+'_amount').html());
	$('#stats').html($('#'+txnID+'_status').html());
	$('#txndt').html($('#'+txnID+'_dt').html());
	//$('#mbNo').html(mobNo);
}
$(document).ready(function() {
	
    $('#fromdt').datepicker({
        autoclose: true,
        format:'dd-mm-yyyy',
        endDate: "current",
        defaultDate: new Date(),
        orientation: "bottom left",
        todayHighlight: true
    });

    $('#todt').datepicker({
        autoclose: true,
        format:'dd-mm-yyyy',
        defaultDate: new Date(),
       	endDate: "current",
        orientation: "bottom left",
        todayHighlight: true
        	
    });
    
    $('#todt').datepicker('setDate', 'today');
    $('#fromdt').datepicker('setDate', 'today');
    
    $('#fromdt, #todt').focus(function(){
    	var dtAlign= $('.datepicker-orient-bottom').css('top');
    	dtAlign=dtAlign.slice(0,-2);
    	$('.datepicker-orient-bottom').css('top',parseInt(dtAlign)+85);
    	});

  
   

//			$('input[type="radio"]').click(function(){
//				 
//			    if ($(this).is(':checked'))
//			    {
//			     alert($(this).val());
//			      if($(this).val()=='transaction'){
//			    	  hideAllServiceComplaintDiv();
//			    	  complainttype='Transaction';
//			    	  $("#transactionType").show();
//			    	  $('#txnmobile').val('');
//			      }
//			      else if($(this).val()=='service'){
//			    	  hideAllTransactionComplaintDiv();
//			    	  complainttype='Service';
//			    	  $("#serviceType").show();
//			      }else{
//			    	  $("#registerservicecomplaint").show();
//			    	  if($(this).val()=='agent'){
//			    		 // alert($(this).val());
//			    		  participationtype='agent';
//			    		  $("#divbillerid").hide();
//			    		  $("#divagentid").show();
//			    		  $("#timeoutmsgdiv").hide();
//				    	  $("#errormsg").hide(); 
//				    	  $("#msg").hide();
//			    	  }
//			    	  else{
//			    		 // alert($(this).val());
//			    		  $("#divagentid").hide();
//			    		  $("#divbillerid").show();
//			    		  if($(this).val()=='biller'){
//			    			  participationtype='biller';
//			    			  $("#timeoutmsgdiv").hide();
//					    	  $("#errormsg").hide(); 
//					    	  $("#msg").hide();
//			    		  }
//			    		  else{
//			    			//  alert($(this).val());
//			    			  participationtype='system';
//			    			  $("#timeoutmsgdiv").hide();
//					    	  $("#errormsg").hide(); 
//					    	  $("#msg").hide();
//			    		  }
//			    	  }
//			    	  raiseServiceComplaint(participationtype);	             
//			      }
//			     }
//      		  });
			
});

function hideAllServiceComplaintDiv(){
    $("#msg").hide();
	  $("#serviceType").hide();
	  $("#registerservicecomplaint").hide();
	  $("#timeoutmsgdiv").hide();
	  $("#errormsg").hide(); 
	  $("#srvc_otpBox").hide();
	  $("#srvc_otp_input").hide();
}

function hideAllTransactionComplaintDiv(){
    $("#msg").hide();
	  $("#txnlist").hide();
	  $("#registertxncomplaint").hide();
	  $("#transactionType").hide();
	  $("#timeoutmsgdiv").hide();
	  $("#errormsg").hide();  
}

function raiseServiceComplaint(pt){
	$("#complaintServtype").val('Service');
	
	if (pt=='agent'){
		$("#participatetype").val("AGENT");
		showServiceReasonList(pt);
		showAgentList();
	}
	else if (pt=='biller'){
		$("#participatetype").val("BILLER");
		showServiceReasonList(pt);
		showBillerList();
	}
	else if (pt=='system'){
		$("#participatetype").val("SYSTEM");
		showServiceReasonList(pt);
		showBillerList();
	}
}

function showServiceReasonList(pt){
	$.ajax({
		url :"/ComplaintService/"+document.getElementById("tenantId").value+"/servicereason/"+pt,
		type: 'GET',
		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
				   "Access-Control-Allow-Methods": "GET"},
		success: function (results) {
			$("#servicereason").html(results);
		}	
	});
}

function showAgentList(){
	$.ajax({

		url : "/ComplaintService/"+document.getElementById("tenantId").value+"/agentslist",
		type: 'GET',
		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
				   "Access-Control-Allow-Methods": "GET"},
		success: function (results) {
			$("#complaintagentid").html(results);
		}	
	});
}
function showBillerList(){
	$.ajax({

		url : "/ComplaintService/"+document.getElementById("tenantId").value+"/billerslist",
		type: 'GET',
		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
				   "Access-Control-Allow-Methods": "GET"},
		success: function (results) {
//			alert(results);
			$("#complaintbillerid").html(results);
		}	
	});
}

function submitServiceComplaint(){
	var servReason=document.getElementById("servicereason").value;
	var participationType=document.getElementById("participatetype").value;
	var otp_svc_mobile=document.getElementById("otp_svc_mobile").value;
	var svc_otp=document.getElementById("svc_otp").value;
	var name="";
	if(participationType=='AGENT'){
		participationId=document.getElementById("complaintagentid").value;
		name=$("#complaintagentid option:selected").html();
	}
	else{
		participationId=document.getElementById("complaintbillerid").value;
		name=$("#complaintbillerid option:selected").html();
	}
	var desc=document.getElementById("complaintServdesc").value;
	if(isEmpty(participationId)||isEmpty(desc)||isEmpty(servReason)){
		alert("Plaese fill all the fields");
		}else{
			
			
			    var isvalidated=validateOTP(svc_otp,otp_svc_mobile);
				 if(isvalidated)
					 {
					 var form = document.createElement("form");
					 form.method = "POST";
					 form.action = "/ComplaintService/"+document.getElementById("tenantId").value+"/raiseservicecomplaint";
					 
					    var element2 = document.createElement("input"); 
					    element2.setAttribute("type", "hidden");
					    element2.value=servReason;
					    element2.name="servReason";
					    var element3 = document.createElement("input"); 
					    element3.setAttribute("type", "hidden");
					    element3.value=participationType;
					    element3.name="participationType";
					    var element4 = document.createElement("input");
					    element4.setAttribute("type", "hidden");
					    element4.value=participationId;
					    element4.name="participationId";
					    var element5 = document.createElement("input"); 
					    element5.setAttribute("type", "hidden");
					    element5.value=desc;
					    element5.name="description";
					    var element6 = document.createElement("input"); 
					    element6.setAttribute("type", "hidden");
					    element6.value=name;
					    element6.name="typeName";
					   
					    
//					    form.appendChild(element1);
					    form.appendChild(element2);
					    form.appendChild(element3);
					    form.appendChild(element4);
					    form.appendChild(element5);
					    form.appendChild(element6);
					    document.body.appendChild(form);
					 form.submit();
					 }
				 else
					 {
					 $("#svcOtp_errormsgcont").html("OTP mismatched.");
						$("#svcOtp_errormsg").show();
					 }
			
			 
//		$.ajax({
//			url : "/ComplaintService/"+document.getElementById("tenantId").value+"/raiseservicecomplaint/"+servReason+"/"+participationType+"/"+participationId+"/"+desc+"/"+name,
//			type: 'GET',
//			async :false,
//			header:{   "Access-Control-Allow-Origin" : "*",
//					   "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
//			success: function (results) {
//				if(results.indexOf("Error") > -1){
//					$("#timeoutmsgdiv").show();
//					$("#timeoutmsg").html(results);
//				}else{
//				$("#msg").hide();
//				$("#successmsg").html(results);
//				$("#msg").show();
//				}
//				$('#complaintServdesc').val('');
//				$('#servicereason').val('');
//				$('#complaintagentid').val('');
//				$('#complaintbillerid').val('');
//				//document.getElementById('servicecomplaintbtn').style.visibility = 'hidden';
//			}	
//		});
		
		
		}
}

function validateSearchTransaction()
{
	$('#srchtxn').removeAttr('data-target','.bs-OTP-modal-md');
	var mobile =$("#txnmobile").val();
	var fromdt =$("#fromdt").val();
	var todt =$("#todt").val();
	var txnrefid =$("#txnrefid").val();
	var srch_by="MOBILE";
	$("#errormsg").hide();
	if(mobile==''&&fromdt==''&&todt==''&&txnrefid=='')
		{
		alert('Please provide either Mobile Number ,From date and To Date or Transaction Ref Id.');
		return false;
		}
	else if(txnrefid=='' &&(mobile!=''||fromdt!=''||todt!=''))
		{
		if(mobile!='')
			{
		var IndNum = /^[0]?[789]\d{9}$/;
		if(!IndNum.test(mobile))
			{
			getErrMessage(1);
			$("#txnmobile").val('');	
			return false;
			}
			}
		else
			{
			alert('Please provide Mobile Number.');
			return false;
			}
		if(fromdt=='')
			{
			alert('Please provide From Date');
			return false;
			}
		
		if(todt=='')
		{
		alert('Please provide To Date');
		return false;
		}
		
		sendOTPReq(mobile);
		$("#tran_otp_label").html('Enter OTP delivered against XXXXXX'+mobile.substring(6, 10));
		$("#otp").val('');
		$("#otpBox").show();
//		$('#mobdigit').html(mobile.substring(6,10));
//		$('#srchtxn').attr('data-target','.bs-OTP-modal-md');
//		
		}
	else
		{
		
		transactionList();
		
		}
	}


function sendSvcOTPReq() {
	$("#srvc_otp_input").hide();
	var mobile = $("#otp_svc_mobile").val();
	if (mobile != '') {
		var IndNum = /^[0]?[789]\d{9}$/;
		if (!IndNum.test(mobile)) {
			getErrMessage(1);
			$("#txnmobile").val('');
			return false;
		}
	} else {
		alert('Please provide Mobile Number to continue.');
		return false;
	}
	sendOTPReq(mobile);
	$("#svc_otp_label").html('Enter OTP delivered against XXXXXX'+mobile.substring(6, 10));
	$("#srvcotp").val('');
	$("#srvc_otp_input").show();
}

function resendOTPReq(comp_type)
{
	var mobile;
	if(comp_type=='tran')
		{
		comp_type='COMP_TRAN';
		 mobile =$("#txnmobile").val();
		}
	else if(comp_type=='svc')
		{
		comp_type='COMP_SVC';
		 mobile =$("#otp_svc_mobile").val();
		}
	
	sendOTPReq(comp_type,mobile);
	}

function validateAndFetch()
{
	var otp_data=$("#otp").val();
	var mobile =$("#txnmobile").val();
	if(otp_data=='')
		{
		alert('Please eneter OTP sent');
		}
	else
		{
		  var isvalidate=validateOTP(otp_data,mobile);
		  if(isvalidate)
			  {
			  $("#otpBox").hide();
			  transactionList();
			  }
		  else
			  {
			  $("#errormsgcont").html("OTP mismatched");
				$("#errormsg").show();
			  }
		}
	
}

function validateAndShow()
{
	var otp_data=$("#srvcotp").val();
	var mobile =$("#otp_svc_mobile").val();
	if(otp_data=='')
		{
		alert('Please eneter OTP sent');
		}
	else
		{
		  var isvalidate=validateOTP(otp_data,mobile);
		  if(isvalidate)
			  {
			  $("#srvc_otpBox").hide();
			  document.getElementById('service_com_type').selectedIndex = 0;
			  $("#serviceType").show();
			  }
		  else
			  {
			  $("#errormsgcont").html("OTP mismatched");
				$("#errormsg").show();
			  }
		}
}
function generateOTP(comp_type,genOTPbtn)
{
	var mobile;
	genOTPbtn.setAttribute('disabled','disabled');
if(comp_type=='tran')
	{
	comp_type='COMP_TRAN';
	//mobile=$("#txnmobile").val();
	mobile=$("#complaintmobile").val();
	if (mobile == "") {
		getErrMessage(10);
		genOTPbtn.removeAttribute('disabled');
		$("#complaintmobile").focus()
		return;
	}
	document.getElementById('tran_resend_otp').removeAttribute('disabled');
	document.getElementById('tran_comp_sub').removeAttribute('disabled');
	}
else
	{
	comp_type='COMP_SVC';
	mobile =$("#otp_svc_mobile").val();
	document.getElementById('svc_resend_otp').removeAttribute('disabled');
	document.getElementById('servicecomplaintbtn').removeAttribute('disabled');
	
	}
sendOTPReq(comp_type,mobile)
}
function enableSvcGenOTPBtn()
{
	
	var mobile = $("#otp_svc_mobile").val();
	if (mobile != '') {
		var IndNum = /^[0]?[789]\d{9}$/;
		if (!IndNum.test(mobile)) {
			getErrMessage(1);
			//$("#txnmobile").val('');
			$("#otp_svc_mobile").val('');
			$("#otp_svc_mobile").focus();
			return false;
		}
	document.getElementById('svc_gen_otp').removeAttribute('disabled');	
	}
}
function sendOTPReq(comp_type,mob_no)
{

	$.ajax({
		url : "/OTPService/generateOTP",
		type: 'POST',
		async :false,
		data: JSON.stringify({comp_type:comp_type, mobileNo:mob_no}),
		'processData': false,
	    'contentType': 'application/json',
		header:{   "Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods": "POST"},
		success: function (results) {
		
		}	
	});
}

function validateOTP(inputData,mob)
{
	var validate_res=false;

	$.ajax({
		url : "/OTPService/validateOTP",
		type: 'POST',
		async :false,
		 data: JSON.stringify({otpNumber:inputData, mobileNo:mob}),
		'processData': false,
	    'contentType': 'application/json',
		header:{   "Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods": "POST"},
		success: function (results) {
			validate_res= results;
		}	
	});
	return validate_res;
}

function transactionList(){
	//alert("Search Transaction List.....");
//	document.getElementById("viewTranDtl").setAttribute("disabled", "disabled");	
	
		var mobile =$("#txnmobile").val();
		var fromdt =$("#fromdt").val();
		var todt =$("#todt").val();
		var txnrefid =$("#txnrefid").val();
		var srch_by="MOBILE";
		var errormsg="Mobile no."
			if(mobile==''&&fromdt==''&&todt==''&&txnrefid=='')
			{
			alert('Please provide either Mobile Number ,From date and To Date or Transaction Ref Id.');
			return false;
			}
		else if(txnrefid=='' &&(mobile!=''||fromdt!=''||todt!=''))
			{
			if(mobile!='')
				{
			var IndNum = /^[0]?[789]\d{9}$/;
			if(!IndNum.test(mobile))
				{
				getErrMessage(1);
				$("#txnmobile").val('');	
				return false;
				}
				}
			else
				{
				alert('Please provide Mobile Number.');
				return false;
				}
			if(fromdt=='')
				{
				alert('Please provide From Date');
				return false;
				}
			
			if(todt=='')
			{
			alert('Please provide To Date');
			return false;
			}
			}
		if(txnrefid!='')
			{
			srch_by="TRAN_REF_ID";
			errormsg="Transaction Ref Id";
			}
			
		var querystring="?";
		if(srch_by=="MOBILE")
			{
			querystring+="mobno="+mobile+"&fromdt="+fromdt+"&todt="+todt;
		    }
		else if(srch_by=="TRAN_REF_ID")
			{
			querystring+="txnId="+txnrefid;
			}
		
		
			$("#msg").hide();
			$("#errormsg").hide();
			$("#txnlist").hide();
//			$("#loader").show();
			$("#registertxncomplaint").hide();
			$.ajax({
				url : "/ComplaintService/"+document.getElementById("tenantId").value+"/transactionlist/"+srch_by+querystring,
				type: 'GET',
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
							"Access-Control-Allow-Methods": "GET"},
				success: function (results) {
				
					if(results.error.length>0)
						{
						$("#errormsgcont").html(results.error);
						$("#errormsg").show();
						$("#txnlist").hide();
						}
					else
						{
                        $("#searchresult").html(results.txnlistAsString);
						$("#txnlist").show();
						}
					//alert("Found Result"+results);
					/*if(results=="")
						{
						
						$("#errormsgcont").html('No transaction found for this '+errormsg);
						$("#errormsg").show();
						$("#txnlist").hide();
						}
					else
						{
					if(results.indexOf("Error") > -1){
						$("#timeoutmsgdiv").show();
						$("#timeoutmsg").html(results);
					}
					else{
						
						$("#searchresult").html(results);
						
						$("#txnlist").show();
					}
						}*/
				},
				error: function (textStatus, errorThrown) {
					$("#loader").hide();
					$("#errormsg").show();
				}		
			});
		
		
	}

function setMinDate(fromDate)
{
if(fromDate.value=='')
	{
	document.getElementById('todt').removeAttribute('minDate');
	}
else
	{
	
    $('#todt').datepicker('setStartDate', fromDate.value);
    //$('#todt').datepicker('setDate', fromDate.value);//For testing...
//	 $( "#todt" ).datepicker( "option", "minDate", fromDate.value );
//	document.getElementById('todt').setAttribute('min',fromDate.value);
	}
}

function raiseTransactionComplaint(){
	//alert("Raise Complaint.....");
	document.getElementById('tran_resend_otp').removeAttribute('disabled');
var val=$('input[name=txnlist]:checked').val();
document.getElementById('tran_resend_otp').setAttribute('disabled','disabled');
document.getElementById('tran_comp_sub').setAttribute('disabled','disabled');
if(typeof val !="undefined"){
	$("#timeoutmsgdiv").hide();
	$("#errormsg").hide(); 
	$("#msg").hide();
	document.getElementById("complaintdesc").value="";
	$("#registertxncomplaint").show();
	$("#complainttype").val(complainttype);
	$("#complaintmobile").val(document.getElementById("txnmobile").value);
	$("#complainttxnid").val($('input[name=txnlist]:checked').val());
	$.ajax({
		url : "/ComplaintService/"+document.getElementById("tenantId").value+"/dispositionlist",
		type: 'GET',
		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
				   "Access-Control-Allow-Methods": "GET"},
		success: function (results) {
			$("#complaintdisp").html(results);
		}	
	});
}
else
	{
	alert("Please select transaction.")
	}
}


function submitTransactionComplaint() {
	var txnId=document.getElementById("complainttxnid").value;
	var disp=document.getElementById("complaintdisp").value;
	var desc=document.getElementById("complaintdesc").value;
	//var mob=document.getElementById("txnmobile").value;
	var mob=document.getElementById("complaintmobile").value;
	var otpInput=document.getElementById("otp").value;
	if(isEmpty(disp)||isEmpty(desc)||isEmpty(mob)){
		alert("Please fill all the fields.");
	}else{
		
		
//		    var element6 = document.createElement("hidden");
//		    element6.value=otpInput;
//		    element6.name="otpInput";
		   
	
		   
		 var isvalidated=validateOTP(otpInput,mob);
		 if(isvalidated)
			 {
			 var form = document.createElement("form");
			 form.method = "POST";
			 form.action = "/ComplaintService/"+document.getElementById("tenantId").value+"/txncomplaint";
			 
			    var element2 = document.createElement("input"); 
			    element2.setAttribute("type", "hidden");
			    element2.value=txnId;
			    element2.name="txnRefId";
			    var element3 = document.createElement("input"); 
			    element3.setAttribute("type", "hidden");
			    element3.value=disp;
			    element3.name="disposition";
			    var element4 = document.createElement("input");
			    element4.setAttribute("type", "hidden");
			    element4.value=desc;
			    element4.name="description";
			    
			    var element5 = document.createElement("input");
			    element5.setAttribute("type", "hidden");
			    element5.value=mob;
			    element5.name="mob";
			    
			    form.appendChild(element2);
			    form.appendChild(element3);
			    form.appendChild(element4);
			    form.appendChild(element5);
//			    form.appendChild(element6);
			    document.body.appendChild(form);
			    form.submit();
			 }
		 else
			 {
			 $("#tranOtp_errormsgcont").html("OTP mismatched.");
				$("#tranOtp_errormsg").show();
			 }
		    
		
		
		
		
//	$.ajax({
//		url : "/ComplaintService/"+document.getElementById("tenantId").value+"/txncomplaint/"+txnId+"/"+disp+"/"+desc,
//		type: 'GET',
//		async :false,
//		header:{   "Access-Control-Allow-Origin" : "*",
//				   "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
//		success: function (results) {
//			if(results.indexOf("Error") > -1){
//				$("#timeoutmsgdiv").show();
//				$("#timeoutmsg").html(results);
//			}else{
//			$("#msg").hide();
//			$("#successmsg").html(results);
//			$("#msg").show();
//			
//		          }
//			//	document.getElementById('txncomplaintbtn').style.visibility = 'hidden';
//			$('#complaintdisp').val('');
//			$('#complaintdesc').val('');
//		}	
//	});
	}
}
function validateMobileNumber(){

	var Number =$("#inputMobile").val();
	var IndNum = /^[0]?[789]\d{9}$/;

	if(IndNum.test(Number)){
		return;
	}

	else{
		getErrMessage(1);
		$("#inputMobile").focus();
	}

}

function statusCheckRequest(){
	//	alert("Checking status......");
		var complaintType=document.getElementById("complainttype_status").value;
		var complaintId=document.getElementById("complaintid_status").value;
		$("#timeoutmsgdiv").hide();
		  $("#msg").hide();
		  $("#errormsg").hide();
		if(complaintType==""||complaintId==""){
			alert("Please fill all the fields");
		}else{
		$.ajax({
			url : "/ComplaintService/"+document.getElementById("tenantId").value+"/complaintstatus/"+complaintType+"/"+complaintId,
			type: 'GET',
			async :false,
			header:{   "Access-Control-Allow-Origin" : "*",
					   "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
			success: function (results) {
				if(results!="")
					{
				if(results.indexOf("Error") > -1){
					$("#timeoutmsgdiv").show();
					$("#timeoutmsg").html(results);
				}else{
				  $("#msg").hide();	
				  $("#successmsg").html(results);
				  $("#msg").show();
				}
					}
				else
					{
					$("#timeoutmsgdiv").hide();
					  $("#msg").hide();	
					$("#errormsg").show();
					}
			},
			error: function (textStatus, errorThrown) {
				$("#errormsg").show();
				$("#timeoutmsgdiv").hide();
				  $("#msg").hide();	
			}	
		    });
		}
	}

function selectComplaintType(selctedValue)
{
	//alert(seelctedValue.value);
	 if(selctedValue.value=='transaction'){
    	  hideAllServiceComplaintDiv();
    	  complainttype='Transaction';
    	  $("#transactionType").show();
    	  $('#txnmobile').val('');
    	  $('#txnrefid').val('');
      }
      else if(selctedValue.value=='service'){
		    	  hideAllTransactionComplaintDiv();
		complainttype = 'Service';
		$("#otp_svc_mobile").val('');
		// $("#srvc_otpBox").show();

		document.getElementById('svc_resend_otp').setAttribute('disabled',
				'disabled');
		document.getElementById('servicecomplaintbtn').setAttribute('disabled',
				'disabled');
		document.getElementById('svc_gen_otp').setAttribute('disabled',
				'disabled');
		document.getElementById('service_com_type').selectedIndex = 0;
		$("#serviceType").show();
      }
      else
    	  {
    	  hideAllTransactionComplaintDiv();
    	  hideAllServiceComplaintDiv();
    	  }
}




function selectServiceCompType(serviceType)
{
	
	 document.getElementById('svc_resend_otp').setAttribute('disabled','disabled');
		document.getElementById('servicecomplaintbtn').setAttribute('disabled','disabled');
		document.getElementById('svc_gen_otp').setAttribute('disabled','disabled');
		 $("#otp_svc_mobile").val('');
		 $("#complaintServdesc").val('');
	if(serviceType.value!="")
		{
	$("#registerservicecomplaint").show();
	  if(serviceType.value=='agent'){
		 // alert($(this).val());
		  participationtype='agent';
		  $("#divbillerid").hide();
		  $("#divagentid").show();
		  $("#timeoutmsgdiv").hide();
    	  $("#errormsg").hide(); 
    	  $("#msg").hide();
	  }
	  else{
		 // alert($(this).val());
		  $("#divagentid").hide();
		  $("#divbillerid").show();
		  if(serviceType.value=='biller'){
			  participationtype='biller';
			  $("#timeoutmsgdiv").hide();
	    	  $("#errormsg").hide(); 
	    	  $("#msg").hide();
		  }
		  else{
			//  alert($(this).val());
			  participationtype='system';
			  $("#timeoutmsgdiv").hide();
	    	  $("#errormsg").hide(); 
	    	  $("#msg").hide();
		  }
	  }
	  raiseServiceComplaint(participationtype);	
		}
	else
		{
		$("#registerservicecomplaint").hide();
		}
}

function validateMobileNumber(inputFieldName) {

	var mobileNo = $("#" + inputFieldName).val();
	var pattern = /^[0]?[789]\d{9}$/;

	if (pattern.test(mobileNo)) {
		return;
	}

	else {
		getErrMessage(1);
		$("#" + inputFieldName).val('');
		$("#" + inputFieldName).focus();

	}

}
