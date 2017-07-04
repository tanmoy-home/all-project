var participationtype = "";
var message = [
		{
			msgId : 1,
			msgErr : "Provided mobile number is not valid! Please enter valid 10 digit mobile number start with '7/8/9'."
		},
		{
			msgId : 2,
			msgErr : "Provided PAN number is not valid! PAN Card should be entered in [AAAAA9999A]"
		},
		{
			msgId : 3,
			msgErr : "Provided Email address is not valid!"
		},
		{
			msgId : 4,
			msgErr : "Please provide alpha-numeric value!"
		},
		{
			msgId : 5,
			msgErr : "Please provide numeric value!"
		},
		{
			msgId : 6,
			msgErr : "Please provide data in mandatory field!"
		},
		{
			msgId : 7,
			msgErr : "Please provide valid amount"
		},
		{
			msgId : 8,
			msgErr : "Please select a payment mode"
		},
		{
			msgId : 9,
			msgErr : "Provided Aadhar is not valid! Aadhar should not start with 0 or 1 and should be 12 digits"
		},
		{
			msgId : 10,
			msgErr : "Amount should be numeric or decimal."
		},
		{
			msgId : 11,
			msgErr : "Please select an Amount Option."
		} ];

function getErrMessage(msgId) {
	for (msg in message) {

		if (message[msg].msgId == msgId) {
			alert(message[msg].msgErr);
		}
	}
}

function hideAll() {
	$("#biller").hide();
	$("#subBiller").hide();
	$("#customerInfoArea").hide();
	$("#billInfoArea").hide();
	$("#errorbox").hide();

}

function showbiller(billerCat) {
	hideAll();

	if (billerCat.value != "") {

		$.ajax({
			url : "/AgentService/fetchBillerList",
			type : 'POST',
			async : false,
			data : billerCat.value,
			datatype : "text",
			contentType : "application/text",
			header : {
				"Access-Control-Allow-Origin" : "*",
				"Access-Control-Allow-Methods" : "POST"
			},
			success : function(results) {
				$("#BillerLists").html(results);
				$("#biller").show();
			}
		});
	}

}

function showSubBiller(billerId) {
	$("#errorbox").hide();
	if (billerId.value != "") {
		$.ajax({
			url : "/AgentService/fetchSubBillerList",
			type : 'POST',
			async : false,
			data : billerId.value,
			datatype : "text",
			contentType : "application/text",
			header : {
				"Access-Control-Allow-Origin" : "*",
				"Access-Control-Allow-Methods" : "POST"
			},
			success : function(results) {
				console.log(results);

				$("#SubBillerLists").html(results);
				$("#subBiller").show();
			}
		});
	} else {
		$("#subBiller").hide();
	}

}

function clearalldivInput(classval) {

	$('.' + classval).val('');
}

function showCustomerInfoArea(biller) {
	if (biller.value != "") {
		clearalldivInput('billinfobox');
		$("#customerInfoArea").hide();
		$("#billInfoArea").hide();
		$("#subBiller").hide();
		$("#btnFetchBill").removeAttr("disabled");
		var isParent = biller.options[biller.selectedIndex]
				.getAttribute('data-isparent');

		if (parseInt(isParent) == 0) {
			$("#customerInfoArea").show();
			fetchBillerInfo(biller)

		} else {
			showSubBiller(biller);
		}
	} else {
		$("#customerInfoArea").hide();
		$("#billInfoArea").hide();
		$("#subBiller").hide();
	}
}

function subBillerSelection(subbiller) {
	if (subbiller.value != "") {
		clearalldivInput('billinfobox');
		fetchBillerInfo(subbiller);
		$("#billInfoArea").hide();
		$("#customerInfoArea").show();
	} else {
		$("#customerInfoArea").hide();
		$("#billInfoArea").hide();
	}
}

function validateBillFetch(arr) {
	if ($("#inputMobile").val() == '') {
		getErrMessage(1);
		// document.getElementById(arr[ar][0]).focus();
		// $("#inputMobile").focus();
		return false;
	}
	if (!validateAadhar()) {
		getErrMessage(9);
		// $("#inputAadhaarNum").focus();
		return false;
	}
	for (ar in arr) {
		var Number = document.getElementById(arr[ar][0]).value
		if (arr[ar][2] == "false") {
			if (Number == "") {
				getErrMessage(6);
				// document.getElementById(arr[ar][0]).focus();
				return false;
			}
			if (arr[ar][1] == "ALPHANUMERIC") {

				if (!AlfanumericValidation(arr[ar][0]))
					return false;
			} else {
				if (arr[ar][1] == "NUMERIC") {

					if (!NumericValidation(arr[ar][0]))
						return false;
				}
			}
		} else {
			if (Number != "") {
				if (arr[ar][1] == "ALPHANUMERIC") {
					if (!AlfanumericValidation(arr[ar][0]))
						return false;
				} else {
					if (arr[ar][1] == "NUMERIC") {
						if (NumericValidation(arr[ar][0]))
							return false;
					}
				}
			}
		}
	}
	return true;
}

function validatePANCard() {
	var Number = $("#inputPAN").val();
	var IndNum = /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/;

	if (IndNum.test(Number) || Number == "") {
		return;
	} else {
		getErrMessage(2);
		// $("#inputPAN").focus();
		$("#inputPAN").val('');
	}
}

function validateAadhar() {
	var Number = $("#inputAadhaarNum").val();
	if (Number === ''
			|| (NumericValidation('inputAadhaarNum') && Number.length === 12
					&& Number[0] != '1' && Number[0] != '0')) {
		return true;
	} else {
		return false;
	}
}

function validateEmail() {

	var Number = $("#inputEmail").val();
	var IndNum = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	if (IndNum.test(Number) || Number == "") {
		return;
	}

	else {
		getErrMessage(3);
		// $("#inputEmail").focus();
		$("#inputEmail").val('');
	}
}

function validateMobileNumber() {

	var Number = $("#inputMobile").val();
	var IndNum = /^[0]?[789]\d{9}$/;

	if (IndNum.test(Number)) {
		return;
	}

	else {
		// $("#inputMobile").focus();
		getErrMessage(1);
		$("#inputMobile").val('');

	}

}

function validateAmount(amountInput) {
	var amount = amountInput.value;
	var IndNum = /^\d*\.?\d*$/;
	if (IndNum.test(amount)) {
		return;
	} else {
		getErrMessage(10);
		amountInput.value='';
	}
}

function calculateCCFQuickPay() {
	var paymentmethod = $("#quickPaymentmethod").val();
	var billerLists = document.getElementById('BillerLists');
	var billerId;
	if (paymentmethod == "") {
		document.getElementById('billerAmountOptions').selectedIndex = 0;
		alert("Please select Payment Method .")
		return false;
	} else if ($("#quickPayAmount").val() != "") {

		var isParent = BillerLists.options[BillerLists.selectedIndex]
				.getAttribute('data-isparent');

		if (parseInt(isParent) == 1) {
			billerId = $("#SubBillerLists").val();
		} else {
			billerId = $("#BillerLists").val();
		}

		// var splitRowObject = $("#billerAmountOptions").val().split('|')

		// alert(splitRowObject[0]);

		/*
		 * url : "/AgentService/calculateCCF/" + $("#BillerLists").val() + "/" +
		 * $("#quickPayAmount").val() + "/" + paymentmethod + "/" +
		 * document.getElementById('agentId').value + "/" +
		 * document.getElementById('quickPaymentChannel').value,
		 */

		$.ajax({
			url : "/AgentService/calculateCCF",
			type : 'POST',
			async : false,
			data : JSON
					.stringify({
						billerId : billerId,
						billAmount : $("#quickPayAmount").val(),
						agentId : document.getElementById('agentId').value,
						payMentChannel : document
								.getElementById('quickPaymentChannel').value,
						paymentMode : paymentmethod
					}),
			datatype : "json",
			contentType : "application/json",
			header : {
				"Access-Control-Allow-Origin" : "*",
				"Access-Control-Allow-Methods" : "POST"
			},
			success : function(resp) {
				obj = JSON.parse(resp);
				if (obj.statusCode == 'OK') {

					var tot = parseFloat(obj.body)
							+ parseFloat($("#quickPayAmount").val());

					if ($("#quickPayAmount").val() != "") {
						$("#quickPayCCF").val(parseFloat(obj.body).toFixed(2));

						$("#quickPayTotal").val(tot.toFixed(2));
						$("#btn_quick_pay").removeAttr("disabled");
					} else {
						$("#quickPayCCF").val("");
						$("#quickPayTotal").val("");
						$("#btn_quick_pay").attr("disabled", "disabled");
					}
				} else {
					$("#quickPayCCF").val("");
					$("#quickPayTotal").val("");
					$("#btn_quick_pay").attr("disabled", "disabled");
					alert('Internal Server Error !');
				}
				/*
				 * $("#CCF_^_+_^_Tax(s)").attr('disabled', true);
				 * $("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', true);
				 */
			}
		});
	} else {
		$("#quickPayCCF").val("");
		$("#quickPayTotal").val("");
		$("#btn_quick_pay").attr("disabled", "disabled");
	}

}
function calculateCCF() {
	var paymentmethod = $("#paymentmethod").val();
	var billerId;
	
	if($('#billerexactness').val()!='EXACT')
	{
	amnt=$("#billerexactness_amnt").val()
	if(amnt=='')
		{
		$("#inputCCFTax").val("");
		$("#inputTotalAmtToPaid").val("");
		}
	}
	else
		{
		if($("#billerAmountOptions").val()=='')
			{
			$("#inputCCFTax").val("");
			$("#inputTotalAmtToPaid").val("");
			return;
			}
		var splitRowObject = $("#billerAmountOptions").val().split('|');
		amnt=splitRowObject[0];
		}
	
	if (paymentmethod == "") {
		document.getElementById('billerAmountOptions').selectedIndex = 0;
		alert("Please select Payment Method .")
		return false;
	} else if (amnt != "") {

		
//		var refId = $("#refId").val();

		var isParent = BillerLists.options[BillerLists.selectedIndex]
				.getAttribute('data-isparent');

		if (parseInt(isParent) == 1) {
			billerId = $("#SubBillerLists").val();
		} else {
			billerId = $("#BillerLists").val();
		}
		
			// alert(splitRowObject[0]);
			

			$.ajax({
				url : "/AgentService/calculateCCF",
				type : 'POST',
				async : false,
				data : JSON.stringify({
					billerId : billerId,
					billAmount : amnt,
					agentId : document.getElementById('agentId').value,
					payMentChannel : document
							.getElementById('inputPaymentChannel').value,
					paymentMode : paymentmethod
				}),
				datatype : "json",
				contentType : "application/json",
				success : function(resp) {
					var obj = JSON.parse(resp);
					if (obj.statusCode == 'OK') {

						var tot = parseFloat(obj.body)
								+ parseFloat(amnt);

						/*
						 * $("#CCF_^_+_^_Tax(s)").attr('disabled', false);
						 * $("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled',
						 * false);
						 */
						if ($("#billerAmountOptions").val() != "") {
							$("#inputCCFTax").val(
									parseFloat(obj.body).toFixed(2));

							$("#inputTotalAmtToPaid").val(tot.toFixed(2));
							// $("#totamount").val(tot);
							// $("#ccf").val(results);

							/*
							 * $("#billerAmountOptions").nextAll().remove();
							 * 
							 * $('#billerAmountOptions').after("</br><label>Total
							 * CCF Plus Tax Amount</label><input type='text'
							 * class='form-control' id='ccf' value='"+results+"'
							 * disabled='true'> <label>Total Amount</label><input
							 * type='text' class='form-control' id='tamount'
							 * value='"+tot+"' disabled='true'>");
							 */
						} else {
							$("#inputCCFTax").val("");

							$("#inputTotalAmtToPaid").val("");
						}
					} else {
						$("#inputCCFTax").val("");

						$("#inputTotalAmtToPaid").val("");
						alert('Internal Server Error !');
					}
					/*
					 * $("#CCF_^_+_^_Tax(s)").attr('disabled', true);
					 * $("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled',
					 * true);
					 */
				}
			});
	}

}

function fetchBillerInfo(biller) {
	$("#errorbox").hide();
	billerId = biller.value;
	blr = billerId;

	hideshowQuickBox(0);
	var isAcceptAdhoc = biller.options[biller.selectedIndex]
			.getAttribute('data-accept-adhoc');
	var fetch_Req = biller.options[biller.selectedIndex]
			.getAttribute('data-fetch-req');
	$("#btn_quick_pay").hide();
	$("#btnFetchBill").hide();
	$
			.ajax({
				url : "/AgentService/fetchCustomParam",
				type : 'POST',
				async : false,
				data : billerId,
				datatype : "text",
				contentType : "application/text",
				header : {
					"Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods" : "POST"
				},
				success : function(resp) {
					var obj = JSON.parse(resp);
					var custinfo = "";
					// alert(obj.statusCode)
					// $("#billFetchInfoForm").html(results1);customerinfobox
					if (obj.statusCode.toUpperCase() == "OK".toUpperCase()) {

						$(".custparam").remove();
						for (var i = 0; i < obj.body.customParams.length; i++) {
							var custparam = obj.body.customParams[i];
							custinfo += '<div class="col-sm-3 custparam"><div class="form-group"> <label>'
									+ custparam.name;

							if (custparam.isMandatory.toString().toUpperCase() == "true"
									.toUpperCase()) {
								custinfo += '<span style="color:red">*</span>';
							}
							custinfo += '</label>';
							custinfo += ' <input type="text" onchange="enableFetchbtn();" class="form-control custinfo" data-dataType="'
									+ custparam.type
									+ '" data-name="'
									+ custparam.name
									+ '"  data-req="'
									+ custparam.isMandatory
									+ '"  onchange="enableFetchbtn();">';
							custinfo += ' </div> </div>';
						}
						// $("#customerinfobox").html(custinfo);

						$("#inputMobile").attr("maxlength", "10");
						$("#inputMobile").attr("placeholder", "+91");
						$(custinfo).insertBefore($("#quickPayChannel"));
						if (fetch_Req != "NOT_SUPPORTED") {
							$("#btnFetchBill").show();
						}

						if (parseInt(isAcceptAdhoc) == 1) {

							hideshowQuickBox(1);

						}
						$("#customerinfobox").show();
						// $("#inputPAN").attr("maxlength", "10");
						// $("#inputAadhaarNum").attr("maxlength", "12");

						// $("#inputMobile").focusout(function(){
						// validateMobileNumber();
						// });

					} else {
						alert(obj.statusCode);
					}

					// $("#inputAadhaarNum").focusout(function(){
					// validateAadhar();
					// });
					//
					//			
					// $("#inputPAN").focusout(function(){
					// validatePANCard();
					// });
					//
					// $("#inputEmail").focusout(function(){
					// validateEmail();
					// });

				}
			});
}

function hideshowQuickBox(val) {
	if (val == 1) {
		$('.quickpayboxes').show();
		$("#btn_quick_pay").show();
	} else {
		$('.quickpayboxes').hide();
		$("#btn_quick_pay").hide();
	}
}

function validateCustomerInfo() {
	if ($("#inputMobile").val() == '') {
		getErrMessage(6);
		return false;
	}

	var customparams = document.getElementsByClassName('custinfo');
	for (var i = 0; i < customparams.length; i++) {
		var param = customparams[i];
		if (param.getAttribute('data-req') == 'true' && param.value == "") {
			getErrMessage(6);
			return false;
		} else if (param.getAttribute('data-req') == 'true'
				&& param.value != "") {
			if (param.getAttribute('data-datatype').toUpperCase() == "NUMERIC") {
				if (!NumericValidation(param)) {
					alert('Please provide numeric value for '
							+ param.getAttribute('data-name') + ' !');
					return false;
				}
			} else if (param.getAttribute('data-datatype').toUpperCase() == "ALPHANUMERIC") {

				if (!AlfanumericValidation(param)) {
					alert('Please provide alpha-numeric value for '
							+ param.getAttribute('data-name') + ' !');
					return false;
				}

			}

		} else if (param.getAttribute('data-req') == 'false'
				&& param.value != "") {
			if (param.getAttribute('data-datatype').toUpperCase() == "NUMERIC") {
				if (!NumericValidation(param)) {
					alert('Please provide numeric value for '
							+ param.getAttribute('data-name') + ' !');
					return false;
				}
			} else if (param.getAttribute('data-datatype').toUpperCase() == "ALPHANUMERIC") {

				if (!AlfanumericValidation(param)) {
					alert('Please provide alpha-numeric value for '
							+ param.getAttribute('data-name') + ' !');
					return false;
				}

			}
		}
	}

	return true;
}
function validateAmountOption(paymentChannel, paymentMethod, amount, billerId,isQuickPay,amountOption,basebillAmount) {
	$("#errorbox").hide();
	var validate_res = false;

	$.ajax({
		url : "/AgentService/validateAmount",
		type : 'POST',
		async : false,
		data : JSON.stringify({
			paymentChannel : paymentChannel,
			paymentMethod : paymentMethod,
			amount : amount,
			basebillAmount : basebillAmount,
			billerId : billerId,
			isQuickPay : isQuickPay,
			amountOption : amountOption
		}),
		'processData' : false,
		'contentType' : 'application/json',
		success : function(results) {
			validate_res = results.isValidated;
			if (!validate_res) {

				$("#erormsg").html(results.errorMsg);
				$("#errorbox").show();
			}
		}
	});
	return validate_res;
}
function quickPay() {

	$("#errorbox").hide();
	if (!validateCustomerInfo()) {
		return false;
	}

	var billerId = '';
	var billerName = '';
	var billers = document.getElementById('BillerLists');
	var isParent = billers.options[billers.selectedIndex]
			.getAttribute('data-isparent')
	if (parseInt(isParent) == 0) {
		billerId = billers.options[billers.selectedIndex].value;
		billerName = billers.options[billers.selectedIndex].innerHTML;
	} else {
		var subbiller = document.getElementById('SubBillerLists');
		billerId = subbiller.options[subbiller.selectedIndex].value;
		billerName = subbiller.options[subbiller.selectedIndex].innerHTML;
	}
	var customparams = document.getElementsByClassName('custinfo');
	var customparam = "";
	for (var i = 0; i < customparams.length; i++) {
		var param = customparams[i];
		if (param.value != "") {
			customparam += param.getAttribute('data-name') + ":" + param.value;
			if (i < customparams.length - 1) {
				customparam += "@";
			}
		}
	}
	var isAmountValidated = validateAmountOption($('#quickPaymentChannel')
			.val(), $('#quickPaymentmethod').val(), $('#quickPayAmount').val(),
			billerId,true,"","");
	
	
	if (isAmountValidated) {
	var form = document.getElementById("quickpayform");
	var element2 = document.createElement("input");
	element2.setAttribute("type", "hidden");
	element2.value = customparam;
	element2.name = "quickcustomparam";
	var element3 = document.createElement("input");
	element3.setAttribute("type", "hidden");
	element3.value = billerId;
	element3.name = "quickbillerId";
	var element4 = document.createElement("input");
	element4.setAttribute("type", "hidden");
	element4.value = document.getElementById('agentId').value;
	element4.name = "quickagentId";
	var element5 = document.createElement("input");
	element5.setAttribute("type", "hidden");
	element5.value = document.getElementById('inputEmail').value;
	element5.name = "quickinputEmail";
	var element6 = document.createElement("input");
	element6.setAttribute("type", "hidden");
	element6.value = document.getElementById('inputMobile').value;
	element6.name = "quickinputMobile";

	form.appendChild(element2);
	form.appendChild(element3);
	form.appendChild(element4);
	form.appendChild(element5);
	form.appendChild(element6);
	form.submit();
	}

}

/*
 * function fetchBill(fetchbt) {
 * 
 * $("#errorbox").hide(); if (!validateCustomerInfo()) { return; }
 * fetchbt.disabled = true; var billerId = ''; var billerName = ''; var billers =
 * document.getElementById('BillerLists'); var isParent =
 * billers.options[billers.selectedIndex] .getAttribute('data-isparent') if
 * (parseInt(isParent) == 0) { billerId =
 * billers.options[billers.selectedIndex].value; billerName =
 * billers.options[billers.selectedIndex].innerHTML; } else { var subbiller =
 * document.getElementById('SubBillerLists'); billerId =
 * subbiller.options[subbiller.selectedIndex].value; billerName =
 * subbiller.options[subbiller.selectedIndex].innerHTML; } var customparams =
 * document.getElementsByClassName('custinfo'); var customparam = ""; for (var i =
 * 0; i < customparams.length; i++) { var param = customparams[i]; if
 * (param.value != "") { customparam += param.getAttribute('data-name') + ":" +
 * param.value; if (i < customparams.length - 1) { customparam += "@"; } } } $
 * .ajax({ url : "/AgentService/fetchBill?billerId=" + billerId + "&mobNo=" +
 * document.getElementById('inputMobile').value + "&custumparam=" +
 * encodeURIComponent(customparam) + "&agentId=" +
 * document.getElementById('agentId').value + "&paymentChannel=INT", type :
 * 'GET', async : false, header : { "Access-Control-Allow-Origin" : "*",
 * "Access-Control-Allow-Methods" : "GET" }, success : function(resp) { var obj =
 * JSON.parse(resp); fetchbt.disabled = true; var billResponseTags; if
 * (obj.fetchResponse.errorMessages.length == 0 && obj.amountOption != "") {
 * $("#billInfoArea").show(); $('#refid').val(obj.fetchResponse.refId);
 * inputBillerName $('#inputCustomerName').val(
 * obj.fetchResponse.billerResponse.customerName);
 * $('#inputBillerName').val(billerName); $('#inputBillDate').val(
 * obj.fetchResponse.billerResponse.billDate); $('#inputBillNumber').val(
 * obj.fetchResponse.billerResponse.billNumber); $('#inputBillPeriod').val(
 * obj.fetchResponse.billerResponse.billPeriod); $('#inputBillAmount').val(
 * obj.fetchResponse.billerResponse.amount); $('#inputBillDate').val(
 * obj.fetchResponse.billerResponse.dueDate); $('#inputDueDate').val(
 * obj.fetchResponse.billerResponse.dueDate);
 * 
 * $('#inputMeterNo').val( obj.fetchResponse.additionalInfo.tags[0].value);
 * $('#inputMeterReadingPresent').val(
 * obj.fetchResponse.additionalInfo.tags[1].value);
 * $('#inputMeterReadingPast').val(
 * obj.fetchResponse.additionalInfo.tags[2].value);
 * 
 * 
 * var insertionElement = ""; for (i = 0; i <
 * obj.fetchResponse.additionalInfo.tags.length; i++) {
 * 
 * insertionElement += '<div class="col-sm-3"><div class="form-group"><label>' +
 * obj.fetchResponse.additionalInfo.tags[i].name + '</label> <input type="text"
 * class="form-control" readonly="readonly" id="inputDueDate" value="' +
 * obj.fetchResponse.additionalInfo.tags[i].value + '" name="inputDueDate">
 * </div></div>'; } $(insertionElement).insertAfter($("#insertionPoint"));
 * 
 * $('#inputCCFTax').val(''); $('#inputTotalAmtToPaid').val('');
 * $('#billerAmountOptions').html(obj.amountOption);
 * 
 * var incomingTags = ""; for (i = 0; i <
 * obj.fetchResponse.billerResponse.tags.length; i++) { var param =
 * obj.fetchResponse.billerResponse.tags[i]; if (param.value != "") {
 * incomingTags += param.name + ":" + param.value; if (i <
 * obj.fetchResponse.billerResponse.tags.length - 1) { incomingTags += "@"; } } }
 * $('#billerResponseTags').val(incomingTags); } else {
 * $("#btnFetchBill").removeAttr("disabled"); $("#billInfoArea").hide(); var
 * errormsg = "" for (var i = 0; i < obj.fetchResponse.errorMessages.length;
 * i++) { errormsg += obj.fetchResponse.errorMessages[i].errorCd + " " +
 * obj.fetchResponse.errorMessages[i].errorDtl + "<br><br>"; }
 * $("#erormsg").html(errormsg); $("#errorbox").show();
 *  } } });
 * 
 * $("#btnFetchBill").removeAttr("disabled"); }
 */

function fetchBill(fetchbt) {

	$("#errorbox").hide();
	if (!validateCustomerInfo()) {
		return;
	}
	fetchbt.disabled = true;
	var billerId = '';
	var billerName = '';
	var billers = document.getElementById('BillerLists');
	var isParent = billers.options[billers.selectedIndex]
			.getAttribute('data-isparent')
	if (parseInt(isParent) == 0) {
		billerId = billers.options[billers.selectedIndex].value;
		billerName = billers.options[billers.selectedIndex].innerHTML;
	} else {
		var subbiller = document.getElementById('SubBillerLists');
		billerId = subbiller.options[subbiller.selectedIndex].value;
		billerName = subbiller.options[subbiller.selectedIndex].innerHTML;
	}
	var customparams = document.getElementsByClassName('custinfo');
	var customparam = "";
	for (var i = 0; i < customparams.length; i++) {
		var param = customparams[i];
		if (param.value != "") {
			customparam += param.getAttribute('data-name') + ":" + param.value;
			if (i < customparams.length - 1) {
				customparam += "@";
			}
		}
	}
	var sendInfo = {
		billerId : billerId,
		mobNo : document.getElementById('inputMobile').value,
		custumparam : (customparam),
		agentId : document.getElementById('agentId').value,
		paymentChannel : "INTB"
	};

	//console.log(sendInfo);
	var fetchData = JSON.stringify(sendInfo);
	//console.log(fetchData);
	$
			.ajax({
				url : "/AgentService/fetchBill",
				data : fetchData,
				dataType : 'json',
				type : 'POST',
				async : false,
				contentType : 'application/json',
				header : {
					"Access-Control-Allow-Origin" : "*",
					"Access-Control-Allow-Methods" : "POST"
				},
				success : function(obj) {
					fetchbt.disabled = true;
					var billResponseTags;
					if (obj.fetchResponse.errorMessages.length == 0
							&& obj.amountOption != "") {
						$("#billInfoArea").show();
						$('#refid').val(obj.fetchResponse.refId);
						$('#billerexactness').val(obj.billerExactness);
						$('#inputCustomerName').val(
								obj.fetchResponse.billerResponse.customerName);
						$('#inputBillerName').val(billerName);
						$('#inputBillDate').val(
								obj.fetchResponse.billerResponse.billDate);
						$('#inputBillNumber').val(
								obj.fetchResponse.billerResponse.billNumber);
						$('#inputBillPeriod').val(
								obj.fetchResponse.billerResponse.billPeriod);
						$('#inputBillAmount').val(
								obj.fetchResponse.billerResponse.amount);
						$('#inputBillDate').val(
								obj.fetchResponse.billerResponse.dueDate);
						$('#inputDueDate').val(
								obj.fetchResponse.billerResponse.dueDate);
						var billerAmountOptions = document.getElementById("billerAmountOptions");
						$('#billerexactness_amnt').val('');
//						billerAmountOptions.onchange =  null;
						$("#billerAmountOptions").unbind("change");
						var billerexactness_amnt = document.getElementById("billerexactness_amnt");
//						billerexactness_amnt.onblur  =  calculateCCF;
						$("#billerexactness_amnt").bind("change", function(e) {
							return calculateCCF();});
						$('#exact_amount').show();
						if(obj.billerExactness==='EXACT')
							{
							$('#exact_amount').hide();
							$("#billerAmountOptions").bind("change", function(e) {
								return calculateCCF();});
							$("#billerexactness_amnt").unbind("blur");
//							billerexactness_amnt.onblur = null ;
							}
						/*
						 * $('#inputMeterNo').val(
						 * obj.fetchResponse.additionalInfo.tags[0].value);
						 * $('#inputMeterReadingPresent').val(
						 * obj.fetchResponse.additionalInfo.tags[1].value);
						 * $('#inputMeterReadingPast').val(
						 * obj.fetchResponse.additionalInfo.tags[2].value);
						 */

						var insertionElement = "";
						for (i = 0; i < obj.fetchResponse.additionalInfo.tags.length; i++) {

							insertionElement += '<div class="col-sm-3"><div class="form-group"><label>'
									+ obj.fetchResponse.additionalInfo.tags[i].name
									+ '</label> <input type="text" class="form-control" readonly="readonly" id="inputDueDate" value="'
									+ obj.fetchResponse.additionalInfo.tags[i].value
									+ '" name="inputDueDate"> </div></div>';
						}
						$(insertionElement).insertAfter($("#insertionPoint"));

						$('#inputCCFTax').val('');
						$('#inputTotalAmtToPaid').val('');
						$('#billerAmountOptions').html(obj.amountOption);

						var incomingTags = "";
						for (i = 0; i < obj.fetchResponse.billerResponse.tags.length; i++) {
							var param = obj.fetchResponse.billerResponse.tags[i];
							if (param.value != "") {
								incomingTags += param.name + ":" + param.value;
								if (i < obj.fetchResponse.billerResponse.tags.length - 1) {
									incomingTags += "@";
								}
							}
						}
						$('#billerResponseTags').val(incomingTags);
					} else {
						$("#btnFetchBill").removeAttr("disabled");
						$("#billInfoArea").hide();
						var errormsg = ""
						for (var i = 0; i < obj.fetchResponse.errorMessages.length; i++) {
							errormsg += obj.fetchResponse.errorMessages[i].errorCd
									+ " "
									+ obj.fetchResponse.errorMessages[i].errorDtl
									+ "<br><br>";
						}
						$("#erormsg").html(errormsg);
						$("#errorbox").show();

					}
				}
			});

	$("#btnFetchBill").removeAttr("disabled");
}

function enableFetchbtn() {
	// alert('Changed');
	$("#btnFetchBill").removeAttr("disabled");
}

function AlfanumericValidation(item) {
	// var target = $( event.target );

	var Number = item.value
	var IndNum = /^[0-9a-zA-Z]+$/;

	if (!(IndNum.test(Number))) {
		//getErrMessage(4);
		// document.getElementById(item).focus();
		return false;
	}
	return true;
}

function NumericValidation(item) {
	var Number = item.value
	if (isNaN(Number) || Number == "") {
		//getErrMessage(5);
		// document.getElementById(item).focus();
		return false;
	}
	return true;
}

function appendPayForm() {
	
	if($('#billerAmountOptions').val() === "") {
		getErrMessage(11);
		return false;
	}
	
	var form = document.getElementById("paybillform");

	var customparams = document.getElementsByClassName('custinfo');
	var billerId = '';
	var billers = document.getElementById('BillerLists');
	var isParent = billers.options[billers.selectedIndex]
			.getAttribute('data-isparent')
	if (parseInt(isParent) == 0) {
		billerId = billers.options[billers.selectedIndex].value;

	} else {
		var subbiller = document.getElementById('SubBillerLists');
		billerId = subbiller.options[subbiller.selectedIndex].value;
	}

	var customparam = "";
	for (var i = 0; i < customparams.length; i++) {
		var param = customparams[i];
		if (param.value != "") {
			customparam += param.getAttribute('data-name') + ":" + param.value;
			if (i < customparams.length - 1) {
				customparam += "@";
			}
		}
	}
	var splitRowObject = $("#billerAmountOptions").val().split('|');
	var amountOpt='';
	var amnt=splitRowObject[0];
	var basebillamount='';
	if($('#billerexactness').val()!='EXACT')
		{
		basebillamount=$("#inputBillAmount").val();
		amnt=$("#billerexactness_amnt").val();
		if(amnt=='')
			{
			alert("Payment Exact Amount  can't be blank");
			return false;
			}
		amountOpt=$("#billerAmountOptions").val()
		}
	var isAmountValidated = validateAmountOption($('#inputPaymentChannel')
			.val(), $('#paymentmethod').val(),amnt,
			billerId,false,amountOpt,basebillamount);
	
	if (isAmountValidated) {
	var element2 = document.createElement("input");
	element2.setAttribute("type", "hidden");
	element2.value = customparam;
	element2.name = "customparam";
	var element3 = document.createElement("input");
	element3.setAttribute("type", "hidden");
	element3.value = billerId;
	element3.name = "billerId";
	var element4 = document.createElement("input");
	element4.setAttribute("type", "hidden");
	element4.value = document.getElementById('agentId').value;
	element4.name = "agentId";
	var element5 = document.createElement("input");
	element5.setAttribute("type", "hidden");
	element5.value = document.getElementById('inputEmail').value;
	element5.name = "inputEmail";
	var element6 = document.createElement("input");
	element6.setAttribute("type", "hidden");
	element6.value = document.getElementById('inputMobile').value;
	element6.name = "inputMobile";

	// form.appendChild(element1);
	form.appendChild(element2);
	form.appendChild(element3);
	form.appendChild(element4);
	form.appendChild(element5);
	form.appendChild(element6);
	
	return true;
	}
	return false;
}

// $(document).ready(function() {
// $("#btn_quick_pay").click(function() {
// $("#paybillform").submit();
// });

// $("#qp_payment_channel").change(function() {
// var qp_payment_channel = $("#qp_payment_channel").val();
// if (qp_payment_channel == "") {
// resetQuickPayFields();
// } else {
// if ($("#qp_amount").val() != "") {
// fetchCCFAmountForQuickPay();
// }
// }
//
// });
//
// $("#qp_amount").blur(function() {
// fetchCCFAmountForQuickPay();
/*
 * var amount = this.value; var billerId = $("#BillerLists").val(); var
 * paymentmode = $("#qp_payment_mode").val(); var agentId = $("#agentId").val();
 * var paymentChannel = $("#qp_payment_channel").val(); var ccfURL =
 * "/AgentService/calculateCCF/"+billerId+"/"+amount+"/"+paymentmode+"/"+agentId+"/"+paymentChannel;
 * $("#qp_amount").val(parseFloat($("#qp_amount").val()).toFixed(2));
 * 
 * $.ajax({ url : ccfURL, type : 'GET', async : false, header : {
 * "Access-Control-Allow-Origin" : "*", "Access-Control-Allow-Methods" : "GET" },
 * success : function(resp) { setQuickPayCCF(resp); }, error : function() {
 * //alert("error"); } });
 */

// });
// });
// function fetchCCFAmountForQuickPay() {
// var amount = $("#qp_amount").val();
// var billerId = $("#BillerLists").val();
// var paymentmode = $("#qp_payment_mode").val();
// var agentId = $("#agentId").val();
// var paymentChannel = $("#qp_payment_channel").val();
// var ccfURL =
// "/AgentService/calculateCCF/"+billerId+"/"+amount+"/"+paymentmode+"/"+agentId+"/"+paymentChannel;
// $("#qp_amount").val(parseFloat($("#qp_amount").val()).toFixed(2));
//
// $.ajax({
// url : ccfURL,
// type : 'GET',
// async : false,
// header : {
// "Access-Control-Allow-Origin" : "*",
// "Access-Control-Allow-Methods" : "GET"
// },
// success : function(resp) {
// setQuickPayCCF(resp);
// },
// error : function() {
// //alert("error");
// }
// });
// }
//
// function setQuickPayCCF(resp) {
// var obj = JSON.parse(resp);
// if (obj.statusCode == 'OK') {
// $("#qp_ccf").val(parseFloat(obj.body).toFixed(2));
// $("#qp_ccf_view").val(parseFloat(obj.body).toFixed(2));
// var qp_amount = parseFloat($("#qp_amount").val());
// var qp_ccf = parseFloat($("#qp_ccf").val());
// $("#qp_total_amount").val((qp_amount+qp_ccf).toFixed(2));
// $("#qp_total_amount_view").val((qp_amount+qp_ccf).toFixed(2));
// } else {
// resetQuickPayFields();
// //alert('Internal Server Error !');
// }
// }
//
// function resetQuickPayFields() {
// $("#qp_amount").val("");
// $("#qp_ccf").val("");
// $("#qp_total_amount").val("");
// $("#qp_ccf_view").val("");
// $("#qp_total_amount_view").val("");
// }