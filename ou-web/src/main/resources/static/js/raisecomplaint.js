
var message = [ {msgId:1, msgErr:"Provided mobile number is not valid! Please enter valid mobile number start with '7/8/9'."}, 
						{msgId:2, msgErr:"Provided PAN number is not valid! PAN Card should be entered in [AAAAA9999A]"},
						{msgId:3, msgErr:"Provided Email address is not valid!"},
						{msgId:4, msgErr:"Please provide alpha-numeric value!"},
						{msgId:5, msgErr:"Please provide numeric value!"},
						{msgId:6, msgErr:"Please provide data in mandatory field!"},
						{msgId:7, msgErr:"Please provide valid amount"},
						{msgId:8, msgErr:"Please select a payment mode"},
						{msgId:9, msgErr:"Provided Aadhar is not valid! Aadhar should not start with 0 or 1 and should be 12 digits"}
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
$(document).ready(function() {

			$('input[type="radio"]').click(function(){
			    if ($(this).is(':checked'))
			    {
			     //alert($(this).val());
			      if($(this).val()=='transaction'){
			    	  hideAllServiceComplaintDiv();
			    	  complainttype='Transaction';
			    	  $("#transactionType").show();
			    	  $('#txnmobile').val('');
			      }
			      else if($(this).val()=='service'){
			    	  hideAllTransactionComplaintDiv();
			    	  complainttype='Service';
			    	  $("#serviceType").show();
			      }else{
			    	  $("#registerservicecomplaint").show();
			    	  if($(this).val()=='agent'){
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
			    		  if($(this).val()=='biller'){
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
			     }
      		  });
			
});

function hideAllServiceComplaintDiv(){
    $("#msg").hide();
	  $("#serviceType").hide();
	  $("#registerservicecomplaint").hide();
	  $("#timeoutmsgdiv").hide();
	  $("#errormsg").hide();  
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
			
			 var form = document.createElement("form");
			 form.method = "POST";
			 form.action = "/ComplaintService/"+document.getElementById("tenantId").value+"/raiseservicecomplaint";
			 
			    var element2 = document.createElement("input"); 
			    element2.value=servReason;
			    element2.name="servReason";
			    var element3 = document.createElement("input"); 
			    element3.value=participationType;
			    element3.name="participationType";
			    var element4 = document.createElement("input");
			    element4.value=participationId;
			    element4.name="participationId";
			    var element5 = document.createElement("input"); 
			    element5.value=desc;
			    element5.name="description";
			    var element6 = document.createElement("input"); 
			    element6.value=name;
			    element6.name="typeName";
			    
//			    form.appendChild(element1);
			    form.appendChild(element2);
			    form.appendChild(element3);
			    form.appendChild(element4);
			    form.appendChild(element5);
			    form.appendChild(element6);
			    document.body.appendChild(form);
			    
			 form.submit();
			 
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


function transactionList(){
	//alert("Search Transaction List.....");		
	var mobile =$("#txnmobile").val();
	var IndNum = /^[0]?[789]\d{9}$/;
	if(IndNum.test(mobile)){
		$("#msg").hide();
		$("#errormsg").hide();
		$("#txnlist").hide();
//		$("#loader").show();
		$("#registertxncomplaint").hide();
		$.ajax({
			url : "/ComplaintService/"+document.getElementById("tenantId").value+"/transactionlist/"+mobile,
			type: 'GET',
			async :false,
			header:{   "Access-Control-Allow-Origin" : "*",
						"Access-Control-Allow-Methods": "GET"},
			success: function (results) {
				//alert("Found Result"+results);
				if(results=="")
					{
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
					$("#txnlist").show();
					$("#searchresult").html(results);
				}
					}
			},
			error: function (textStatus, errorThrown) {
				$("#loader").hide();
				$("#errormsg").show();
			}		
		});
	}
	else{
		getErrMessage(1);
		$("#txnmobile").focus();
	}
	
}

function generateOtp(){
		var mobile =$("#txnmobile").val();
		$.ajax({
			url : "/OTPService/generateOTP",
			async :false,
			data: mobile,
			datatype : "text",
			contentType: "application/text",
			type: 'POST',
			header:{   "Access-Control-Allow-Origin" : "*",
						"Access-Control-Allow-Methods": "POST"},
			success: function () {
				alert("Success");
			},
			error: function () {
				alert("Failure");
			}		
		});
}

function validateOtp(){
	var otpNumber =$("#otpNumber").val();
	var mobile =$("#txnmobile").val();
	var inputData = JSON.stringify({
		'otpNumber' : otpNumber,
		'mobileNo' : mobile
	});
	$.ajax({
		url : "/OTPService/validateOTP",
		async :false,
		data: inputData,
		datatype : "json",
		contentType: "application/json; charset=utf-8",
		type: 'POST',
		header:{   "Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods": "POST"},
		success: function () {
			alert("Success");
		},
		error: function () {
			alert("Failure");
		}		
	});
}


function raiseTransactionComplaint(){
	//alert("Raise Complaint.....");
var val=$('input[name=txnlist]:checked').val();
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
	var mob=document.getElementById("txnmobile").value;
	if(isEmpty(disp)||isEmpty(desc)){
		alert("Please fill all the fields.");
	}else{
		
		var form = document.createElement("form");
		 form.method = "POST";
		 form.action = "/ComplaintService/"+document.getElementById("tenantId").value+"/txncomplaint";
		 
		    var element2 = document.createElement("input"); 
		    element2.value=txnId;
		    element2.name="txnRefId";
		    var element3 = document.createElement("input"); 
		    element3.value=disp;
		    element3.name="disposition";
		    var element4 = document.createElement("input");txnmobile
		    element4.value=desc;
		    element4.name="description";
		    
		    var element5 = document.createElement("input");
		    element4.value=mob;
		    element4.name="mob";
		   
	
		    form.appendChild(element2);
		    form.appendChild(element3);
		    form.appendChild(element4);
		
		    document.body.appendChild(form);
		    
		 form.submit();
		
		
		
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
      }
      else if(selctedValue.value=='service'){
    	  hideAllTransactionComplaintDiv();
    	  complainttype='Service';
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
