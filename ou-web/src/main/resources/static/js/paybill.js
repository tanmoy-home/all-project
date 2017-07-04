var participationtype="";
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



function hideAll()
{
	 $("#biller").hide();
	 $("#subBiller").hide();
	 $("#customerInfoArea").hide();
	 $("#billInfoArea").hide();
	 $("#errorbox").hide();
	 
}
function showbiller(billerCat)
{
	console.log("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	hideAll();
	
	if(billerCat.value!="")
		{
		$("#biller").show();
		$.ajax({
			url :"/AgentService/fetchBillerList",
			type: 'POST',
			async :false,
			data: billerCat.value,
			datatype : "text",
			contentType: "application/text",
			header:{   "Access-Control-Allow-Origin" : "*",
					   "Access-Control-Allow-Methods": "POST"},
			success: function (results) {
			
				$("#BillerLists").html(results);
			}	
		});
		}
	 
}

function showSubBiller(billerId)
{
	 $("#errorbox").hide();
	if(billerId.value!="")
		{		
		$.ajax({
			url :"/AgentService/fetchSubBillerList",
			type: 'POST',
			async :false,
			data: billerId.value,
			datatype : "text",
			contentType: "application/text",
			header:{   "Access-Control-Allow-Origin" : "*",
					   "Access-Control-Allow-Methods": "POST"},
			success: function (results) {
				console.log(results);
				
				$("#SubBillerLists").html(results);
				$("#subBiller").show();
			}	
		});
		}
	else
		{
		$("#subBiller").hide();
		}
	 
}

function clearalldivInput(classval)
{
	
	$('.'+classval).val('');
}

function showCustomerInfoArea(biller)
{
	if(biller.value!="")
		{
		clearalldivInput('billinfobox');
	$("#customerInfoArea").hide();
	$("#billInfoArea").hide();
	$("#subBiller").hide();
	$("#btnFetchBill").removeAttr("disabled");
	  var isParent=biller.options[biller.selectedIndex].getAttribute('data-isparent')
		 if(parseInt(isParent)==0)
			 {
			 $("#customerInfoArea").show();
			 fetchBillerInfo(biller.value)
			 
			 }
		 else
			 {
			 showSubBiller(biller);
			 }
		}
	else
		{
		$("#customerInfoArea").hide();
		 $("#billInfoArea").hide();
		 $("#subBiller").hide();
		}
}

function subBillerSelection(subbiller)
{
	if(subbiller.value!="")
			{
			clearalldivInput('billinfobox');
		fetchBillerInfo(subbiller.value);
		 $("#billInfoArea").hide();
		 $("#customerInfoArea").show();
			}
	else
		{
		$("#customerInfoArea").hide();
		 $("#billInfoArea").hide();
		}
}


function validateBillFetch( arr) {
	if($("#inputMobile").val()=='') {
		getErrMessage(1);
		//document.getElementById(arr[ar][0]).focus();							
//		$("#inputMobile").focus();
		return false;
	}
	if(!validateAadhar()){
		getErrMessage(9);
//		$("#inputAadhaarNum").focus();
		return false;
	}
	for(ar in arr) {
		var Number =document.getElementById(arr[ar][0]).value
		if(arr[ar][2]=="false") {
			if(Number == "") {
				getErrMessage(6);
//				document.getElementById(arr[ar][0]).focus();
				return false;
			}
			if(arr[ar][1]=="ALPHANUMERIC") {
				
				if(!AlfanumericValidation(arr[ar][0]))
					return false;
			}
			else {
				if(arr[ar][1]=="NUMERIC") {
					
					if(!NumericValidation(arr[ar][0]))
						return false;
				}
			}
		}
		else {
			 if(Number!="") {
				 if(arr[ar][1]=="ALPHANUMERIC") {
					if(!AlfanumericValidation(arr[ar][0]))
						return false;
				}
				else {
					if(arr[ar][1]=="NUMERIC") {
						if(NumericValidation(arr[ar][0]))
							return false;
					}
				}
			}
		}
	}
	return true;
}
      
function validatePANCard () {
	var Number =$("#inputPAN").val();
	var IndNum = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;  
	
	if(IndNum.test(Number) || Number == ""){
		return;
	}
	else{
		getErrMessage(2);
//		$("#inputPAN").focus();
		$("#inputPAN").val('');
	}
}
    
function validateAadhar () {
	var Number =$("#inputAadhaarNum").val();
	if(Number === '' || (NumericValidation('inputAadhaarNum') && Number.length === 12 && Number[0] != '1' && Number[0] != '0')){
		return true;
	}			
	else{
		return false;
	}
}

function validateEmail () {

	var Number =$("#inputEmail").val();
	var IndNum = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/; 

	if(IndNum.test(Number) || Number == ""){
		return;
	}

	else{
		getErrMessage(3);
//		$("#inputEmail").focus();
		$("#inputEmail").val('');
	}
}

function validateMobileNumber(){

	var Number =$("#inputMobile").val();
	var IndNum = /^[0]?[789]\d{9}$/;

	if(IndNum.test(Number)){
		return;
	}

	else{
//		$("#inputMobile").focus();
		getErrMessage(1);
		$("#inputMobile").val('');
		
	}

}
/*function calculateCCF() {
	var paymentmethod=$("#paymentmethod").val();
	if(paymentmethod=="")
		{
		document.getElementById('billerAmountOptions').selectedIndex=0;
		alert("Please select Payment Method .")
		return false;
		}
	else if($("#billerAmountOptions").val()!="")
		{
		 
	var splitRowObject = $("#billerAmountOptions").val().split('|');
	var refId = $("#refId").val();
	if(splitRowObject.length > 0)
	   // alert(splitRowObject[0]);
	
	$.ajax({
		url : "/AgentService/calculateCCF/"+$("#BillerLists").val()+"/"+splitRowObject[0]+"/"+paymentmethod+"/"+document.getElementById('agentId').value+"/"+document.getElementById('inputPaymentChannel').value,
		type: 'POST',

		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
                    "Access-Control-Allow-Methods": "POST"},
		success: function (resp) {
			var obj = JSON.parse(resp);
			if(obj.statusCode=='OK')
				{
				
		         var tot=	 parseFloat(obj.body)+parseFloat(splitRowObject[0]);
		
				$("#CCF_^_+_^_Tax(s)").attr('disabled', false);
				$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', false);
				if($("#billerAmountOptions").val() !="") {
					$("#inputCCFTax").val(parseFloat(obj.body).toFixed(2));
					
					$("#inputTotalAmtToPaid").val(tot.toFixed(2));
					//$("#totamount").val(tot);
					//$("#ccf").val(results);
					
			
			$("#billerAmountOptions").nextAll().remove();
			
			$('#billerAmountOptions').after("</br><label>Total CCF Plus Tax Amount</label><input type='text' class='form-control' id='ccf' value='"+results+"' disabled='true'> <label>Total  Amount</label><input type='text' class='form-control' id='tamount' value='"+tot+"' disabled='true'>");
				}
				else {
					$("#inputCCFTax").val("");
					
					$("#inputTotalAmtToPaid").val("");
				}
				}
			else
				{
				$("#inputCCFTax").val("");
				
				$("#inputTotalAmtToPaid").val("");
				alert('Internal Server Error !');
				}
				$("#CCF_^_+_^_Tax(s)").attr('disabled', true);
				$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', true);
		}
	});
		}
	
}*/

function calculateCCF() {
	var paymentmethod=$("#paymentmethod").val();
	if(paymentmethod=="")
		{
		document.getElementById('billerAmountOptions').selectedIndex=0;
		alert("Please select Payment Method .")
		return false;
		}
	else if($("#billerAmountOptions").val()!="")
		{
		 
	var splitRowObject = $("#billerAmountOptions").val().split('|');
	var refId = $("#refId").val();
	if(splitRowObject.length > 0)
	   // alert(splitRowObject[0]);
		
		/*var ccfData = {
			BillerLists:$("#BillerLists").val(),
			splitRowObject:splitRowObject[0],
			agentId:document.getElementById('agentId').value,
			inputPaymentChannel:document.getElementById('inputPaymentChannel').value
			};*/
	
	$.ajax({
		url : "/AgentService/calculateCCF",
		type: 'POST',
		async :false,
		data: JSON.stringify({
			BillerLists:$("#BillerLists").val(),
			splitRowObject:splitRowObject[0],
			agentId:document.getElementById('agentId').value,
			inputPaymentChannel:document.getElementById('inputPaymentChannel').value
			
			}),
		datatype : "json",
		contentType: "application/json",
		header:{   "Access-Control-Allow-Origin" : "*",
                    "Access-Control-Allow-Methods": "POST"},
		success: function (resp) {
			var obj = JSON.parse(resp);
			if(obj.statusCode=='OK')
				{
				console.log("=======================: " + parseFloat(splitRowObject[0]));
		         var tot=	 parseFloat(obj.body)+parseFloat(splitRowObject[0]);
		
				/*$("#CCF_^_+_^_Tax(s)").attr('disabled', false);
				$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', false);*/
				if($("#billerAmountOptions").val() !="") {
					$("#inputCCFTax").val(parseFloat(obj.body).toFixed(2));
					
					$("#inputTotalAmtToPaid").val(tot.toFixed(2));
					//$("#totamount").val(tot);
					//$("#ccf").val(results);
					
			
			/*$("#billerAmountOptions").nextAll().remove();
			
			$('#billerAmountOptions').after("</br><label>Total CCF Plus Tax Amount</label><input type='text' class='form-control' id='ccf' value='"+results+"' disabled='true'> <label>Total  Amount</label><input type='text' class='form-control' id='tamount' value='"+tot+"' disabled='true'>");*/
				}
				else {
					$("#inputCCFTax").val("");
					
					$("#inputTotalAmtToPaid").val("");
				}
				}
			else
				{
				$("#inputCCFTax").val("");
				
				$("#inputTotalAmtToPaid").val("");
				alert('Internal Server Error !');
				}
				/*$("#CCF_^_+_^_Tax(s)").attr('disabled', true);
				$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', true);*/
		}
	});
		}
	
}

function fetchBillerInfo(billerId) {
	 $("#errorbox").hide();
	blr = billerId;
	$.ajax({
		url : "/AgentService/fetchCustomParam",
		type: 'POST',
		async :false,
		data: billerId,
		datatype : "text",
		contentType: "application/text",
		header:{   "Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods": "POST"},
		success: function (resp) {
			var obj = JSON.parse(resp);
			var custinfo="";
		//alert(obj.statusCode)
//			$("#billFetchInfoForm").html(results1);customerinfobox
			if(obj.statusCode.toUpperCase()=="OK".toUpperCase())
				{
				for( var i=0;i<obj.body.customParams.length;i++)
				{
				var custparam=obj.body.customParams[i];
				custinfo+='<div class="col-sm-3"><div class="form-group"> <label>'+custparam.name;
					
					if(custparam.isMandatory.toString().toUpperCase()=="true".toUpperCase())
						{
						custinfo+='<span style="color:red">*</span>';
						}
				custinfo+='</label>';  
				custinfo+=' <input type="text" onchange="enableFetchbtn();" class="form-control custinfo" data-dataType="'+custparam.type+'" data-name="'+custparam.name+'"  data-req="'+custparam.isMandatory+'"  onchange="enableFetchbtn();">';
					custinfo+=' </div> </div>';       
				}
				$("#customerinfobox").html(custinfo);
				$("#inputMobile").attr("maxlength", "10");
			    $("#inputMobile").attr("placeholder", "+91");
//			$("#inputPAN").attr("maxlength", "10");
//			$("#inputAadhaarNum").attr("maxlength", "12");
			
//			$("#inputMobile").focusout(function(){
//				validateMobileNumber();
//			});
			
			
				}
			else
				{
				alert(obj.statusCode);
				}

//			$("#inputAadhaarNum").focusout(function(){
//				validateAadhar();
//			});
//
//			
//			$("#inputPAN").focusout(function(){
//				validatePANCard();
//			});
//
//			$("#inputEmail").focusout(function(){
//				validateEmail();
//			});

		}
	});			
}

function validateCustomerInfo()
{
if($("#inputMobile").val()=='')
	{
	getErrMessage(6);
	return false;
	}

var customparams=document.getElementsByClassName('custinfo');
for(var i=0;i<customparams.length;i++)
	{
	var param=customparams[i];
	if(param.getAttribute('data-req')=='true'&&param.value=="")
		{
		getErrMessage(6);
		return false;
		}
	else if(param.getAttribute('data-req')=='true'&&param.value!="")
		{
		 if(param.getAttribute('data-datatype').toUpperCase()=="NUMERIC")
			 {
			 if(!NumericValidation(param))
				 {
				 alert('Please provide alpha-numeric value for '+param.getAttribute('data-name')+' !');
				 return false;
				 }
			 }
		 else if(param.getAttribute('data-datatype').toUpperCase()=="ALPHANUMERIC")
			 {
			 
			 if(!AlfanumericValidation(param))
				 {
				 alert('Please provide numeric value for '+param.getAttribute('data-name')+' !');
				 return false;
				 }
			 
			 }
		
		}
	else if(param.getAttribute('data-req')=='false'&&param.value!="")
		{
		if(param.getAttribute('data-datatype').toUpperCase()=="NUMERIC")
		 {
		 if(!NumericValidation(param))
			 {
			 alert('Please provide alpha-numeric value for '+param.getAttribute('data-name')+' !');
			 return false;
			 }
		 }
	 else if(param.getAttribute('data-datatype').toUpperCase()=="ALPHANUMERIC")
		 {
		 
		 if(!AlfanumericValidation(param))
			 {
			 alert('Please provide numeric value for '+param.getAttribute('data-name')+' !');
			 return false;
			 }
		 
		 }
		}
	}

return true;
}

function fetchBill(fetchbt)
{
	
	$("#errorbox").hide();
	if(!validateCustomerInfo())
	{
		return;
	}
	fetchbt.disabled = true;
	var billerId='';
	var billers=document.getElementById('BillerLists');
	  var isParent=billers.options[billers.selectedIndex].getAttribute('data-isparent')
		 if(parseInt(isParent)==0)
			 {
			billerId=billers.options[billers.selectedIndex].value;
			 
			 }
		 else
			 {
			 var subbiller=document.getElementById('SubBillerLists');
			 billerId=subbiller.options[subbiller.selectedIndex].value;
			 }
	var customparams=document.getElementsByClassName('custinfo');
	var customparam="";
	for(var i=0;i<customparams.length;i++)
		{
		var param=customparams[i];
		if(param.value!="")
			{
		customparam+=param.getAttribute('data-name')+":"+param.value;
		if(i<customparams.length-1)
			{
			customparam+="@";
			}
			}
		}
	$.ajax({
		url: "/AgentService/fetchBill?billerId/"+billerId+"/"+document.getElementById('inputMobile').value+"/"+encodeURIComponent(customparam)+"/"+document.getElementById('agentId').value+"/"+"paymentChannel=INT",
		type: 'POST',
		async :false,
		header:{   "Access-Control-Allow-Origin" : "*",
                    "Access-Control-Allow-Methods": "POST"},
		success: function (resp) {
			var obj = JSON.parse(resp);
			fetchbt.disabled = true;
			if(obj.fetchResponse.errorMessages.length==0&&obj.amountOption!="")
				{
			$("#billInfoArea").show();
			$('#refid').val(obj.fetchResponse.refId);
			$('#inputCustomerName').val(obj.fetchResponse.billerResponse.customerName);
       	    $('#inputBillDate').val(obj.fetchResponse.billerResponse.billDate);
       	    $('#inputBillNumber').val(obj.fetchResponse.billerResponse.billNumber);
       	    $('#inputBillPeriod').val(obj.fetchResponse.billerResponse.billPeriod);
       	    $('#inputBillAmount').val(obj.fetchResponse.billerResponse.amount);
       	    $('#inputBillDate').val(obj.fetchResponse.billerResponse.dueDate); 
       	    $('#inputDueDate').val(obj.fetchResponse.billerResponse.dueDate); 
       	    $('#inputMeterNo').val(obj.fetchResponse.additionalInfo.tags[0].value);
       	    $('#inputMeterReadingPresent').val(obj.fetchResponse.additionalInfo.tags[1].value);
       	    $('#inputMeterReadingPast').val(obj.fetchResponse.additionalInfo.tags[2].value); 
       	 $('#inputCCFTax').val('');
       	 $('#inputTotalAmtToPaid').val('');
       	 $('#billerAmountOptions').html(obj.amountOption);
       	 
				}
			else
				{
				$("#btnFetchBill").removeAttr("disabled");
				$("#billInfoArea").hide();
				var errormsg=""
				for(var i=0;i<obj.fetchResponse.errorMessages.length;i++)
					{
					errormsg+=obj.fetchResponse.errorMessages[i].errorCd+" "+obj.fetchResponse.errorMessages[i].errorDtl+"<br><br>";
					}
				$("#erormsg").html(errormsg);
				$("#errorbox").show();
				}
		}
	});
	 
	$("#btnFetchBill").removeAttr("disabled");
}
/*function fetchBill(fetchbt)
{
	
	$("#errorbox").hide();
	if(!validateCustomerInfo())
	{
		return;
	}
	fetchbt.disabled = true;
	var billerId='';
	var billers=document.getElementById('BillerLists');
	  var isParent=billers.options[billers.selectedIndex].getAttribute('data-isparent')
		 if(parseInt(isParent)==0)
			 {
			billerId=billers.options[billers.selectedIndex].value;
			 
			 }
		 else
			 {
			 var subbiller=document.getElementById('SubBillerLists');
			 billerId=subbiller.options[subbiller.selectedIndex].value;
			 }
	var customparams=document.getElementsByClassName('custinfo');
	var customparam="";
	for(var i=0;i<customparams.length;i++)
		{
		var param=customparams[i];
		if(param.value!="")
			{
		customparam+=param.getAttribute('data-name')+":"+param.value;
		if(i<customparams.length-1)
			{
			customparam+="@";
			}
			}
		}
	
	var fetchData = {
			encodeURI:encodeURIComponent(customparam),
			inputMobile:document.getElementById('inputMobile').value,
			agentId:document.getElementById('agentId').value,
			inputPaymentChannel:document.getElementById('inputPaymentChannel').value
			billerID:billerId
			};
	$.ajax({
		url: "/AgentService/fetchBill",
		type: 'POST',
		async :false,
		data:JSON.stringify({
			encodeURI:encodeURIComponent(customparam),
			inputMobile:document.getElementById('inputMobile').value,
			agentId:document.getElementById('agentId').value,
			inputPaymentChannel:document.getElementById('inputPaymentChannel').value, 
			billerID:billerId}
		),
		datatype: "json",
		contentType:"application/json",
		header:{   "Access-Control-Allow-Origin" : "*",
                    "Access-Control-Allow-Methods": "POST"},
		success: function (resp) {
			console.log("$$$$$$$$$$$$$$$$$: " + resp);
			var obj = JSON.parse(resp);
			fetchbt.disabled = true;
			if(obj.fetchResponse.errorMessages.length==0&&obj.amountOption!="")
				{
			$("#billInfoArea").show();
			$('#refid').val(obj.fetchResponse.refId);
			$('#inputCustomerName').val(obj.fetchResponse.billerResponse.customerName);
       	    $('#inputBillDate').val(obj.fetchResponse.billerResponse.billDate);
       	    $('#inputBillNumber').val(obj.fetchResponse.billerResponse.billNumber);
       	    $('#inputBillPeriod').val(obj.fetchResponse.billerResponse.billPeriod);
       	    $('#inputBillAmount').val(obj.fetchResponse.billerResponse.amount);
       	    $('#inputBillDate').val(obj.fetchResponse.billerResponse.dueDate); 
       	    $('#inputDueDate').val(obj.fetchResponse.billerResponse.dueDate); 
       	    $('#inputMeterNo').val(obj.fetchResponse.additionalInfo.tags[0].value);
       	    $('#inputMeterReadingPresent').val(obj.fetchResponse.additionalInfo.tags[1].value);
       	    $('#inputMeterReadingPast').val(obj.fetchResponse.additionalInfo.tags[2].value); 
       	 $('#inputCCFTax').val('');
       	 $('#inputTotalAmtToPaid').val('');
       	 $('#billerAmountOptions').html(obj.amountOption);
       	 
				}
			else
				{
				$("#btnFetchBill").removeAttr("disabled");
				$("#billInfoArea").hide();
				var errormsg=""
				for(var i=0;i<obj.fetchResponse.errorMessages.length;i++)
					{
					errormsg+=obj.fetchResponse.errorMessages[i].errorCd+" "+obj.fetchResponse.errorMessages[i].errorDtl+"<br><br>";
					}
				$("#erormsg").html(errormsg);
				$("#errorbox").show();
				}
		}
	});
	 
	$("#btnFetchBill").removeAttr("disabled");
}*/

function enableFetchbtn()
{
//	alert('Changed');
	$("#btnFetchBill").removeAttr("disabled");
}

function AlfanumericValidation(item) {
	//var target = $( event.target );

	var Number =item.value
	var IndNum = /^[0-9a-zA-Z]+$/;  

	if(!(IndNum.test(Number))){
		//getErrMessage(4);
		//document.getElementById(item).focus();
		return false;
	}
	return true;
}

function NumericValidation(item) {			
	var Number =item.value
	if(isNaN(Number) ||Number=="" ) {
		//getErrMessage(5);
		//document.getElementById(item).focus();
		return false;
	}
	return true;
}

