
var blr;

var oTable;


var message = [ {msgId:1, msgErr:"Provided mobile number is not valid! Please enter valid mobile number start with '7/8/9'."}, 
						{msgId:2, msgErr:"Provided PAN number is not valid! PAN Card should be entered in [AAAAA9999A]"},
						{msgId:3, msgErr:"Provided Email address is not valid!"},
						{msgId:4, msgErr:"Please provide alpha-numeric value!"},
						{msgId:5, msgErr:"Please provide numeric value!"},
						{msgId:6, msgErr:"Please provide data in mandatory field!"},
						{msgId:7, msgErr:"Please provide valid amount"}
					];
		
		function getErrMessage(msgId) {
			for(msg in message) {
				if(message[msg].msgId == msgId) {
					alert(message[msg].msgErr);
				}
			}
		}
function calculateCCFQuickPay() {
			
			var splitRowObject = $("#quickPayAmount").val();
			var refId = $("#refId").val();
			if(splitRowObject.length > 0)
			   // alert(splitRowObject[0]);
			
			$.ajax({
				url : "/resource/calculate-ccf-element/" + document.getElementById("tenantId").value +"?billerId="+blr+"&billAmount="+splitRowObject+"&refId="+refId,
				type: 'GET',
        
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
                            "Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					
				var tot=	 parseFloat(results)+parseFloat(splitRowObject);
				
						/*$("#CCF_^_+_^_Tax(s)").attr('disabled', false);
						$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', false);*/
						if(results !="") {
							$("#ccfT").val(results);
							
							$("#totalAm").val(tot);
							$("#totamount").val(tot);
							$("#ccf").val(results);
							
					
					/*$("#billerAmountOptions").nextAll().remove();
					
					$('#billerAmountOptions').after("</br><label>Total CCF Plus Tax Amount</label><input type='text' class='form-control' id='ccf' value='"+results+"' disabled='true'> <label>Total  Amount</label><input type='text' class='form-control' id='tamount' value='"+tot+"' disabled='true'>");*/
						}
						else {
							$("#ccfT").val("");
							
							$("#totalAm").val("");
						}
						/*}*/
						/*$("#CCF_^_+_^_Tax(s)").attr('disabled', true);
						$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', true);*/
				}
			});
			
		}
		function calculateCCF() {
			
			var splitRowObject = $("#billerAmountOptions").val().split('|');
			var refId = $("#refId").val();
			if(splitRowObject.length > 0)
			   // alert(splitRowObject[0]);
			
			$.ajax({
				url : "/resource/calculate-ccf-element/" + document.getElementById("tenantId").value +"?billerId="+blr+"&billAmount="+splitRowObject[0]+"&refId="+refId,
				type: 'GET',
        
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
                            "Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					
				var tot=	 parseFloat(results)+parseFloat(splitRowObject[0]);
				
						/*$("#CCF_^_+_^_Tax(s)").attr('disabled', false);
						$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', false);*/
						if($("#billerAmountOptions").val() !="") {
							$("#ccfT").val(results);
							
							$("#totalAm").val(tot);
							$("#totamount").val(tot);
							$("#ccf").val(results);
							
					
					/*$("#billerAmountOptions").nextAll().remove();
					
					$('#billerAmountOptions').after("</br><label>Total CCF Plus Tax Amount</label><input type='text' class='form-control' id='ccf' value='"+results+"' disabled='true'> <label>Total  Amount</label><input type='text' class='form-control' id='tamount' value='"+tot+"' disabled='true'>");*/
						}
						else {
							$("#ccfT").val("");
							
							$("#totalAm").val("");
						}
						/*$("#CCF_^_+_^_Tax(s)").attr('disabled', true);
						$("#Total_^_Amount_^_to_^_be_^_paid").attr('disabled', true);*/
				}
			});
			
		}
		
		
		function anchorcall(refId,billerId,requestType){
			
			window.location =  "/resource/html/" + document.getElementById("tenantId").value+"/"+"tenantBillPayAgHTML"+"?refId="+refId+"&billerId="+billerId+"&requestType="+requestType;
		
		}
		
		
		
		$(document).ready(function() {
			loadDetails();
			var cataId;
			var billerId;
			var FbillerIdViewdata;
			/*
			*//********************Transaction History Start************************//*
			
			//test();
			
			oTable = $('#Trxnlist').dataTable({
				"aaSorting" : [ [ 2, "asc" ] ],
				"sPaginationType" : "full_numbers",
				"bJQueryUI" : true,
				"aoColumnDefs" : [ {
					"bSortable" : false,
					"bSearchable" : false,
					"aTargets" : [ 0 ]
				}, {
					"bVisible" : false,
					"bSortable" : false,
					"bSearchable" : false
				} ],
				"oLanguage" : {
					"sLoadingRecords" : "Please wait - loading...",
					"sEmptyTable" : "No data found.",
					"sZeroRecords" : "No data found."
				},
			});

			jQuery('.dataTable').wrap('<div class="dataTables_scroll"/>');
			
			//bind();
			
			*//********************Transaction History End************************//*
			
			*//********************Complaint Start************************//*
			 $("#divType").show();
	$("#divChkStatus").hide();
	$("#divcomplaintShown").hide();
	$('input[type="radio"]').click(function() {
		$("#divType").hide();
		if ($(this).attr("value") == "complaintShown") {
			$("#divcomplaintShown").show();
			$("#divChkStatus").hide();

			// within complaint show
			$("#divTranType").show();
			$("#divAgentTBCR").hide();
			$("#divAgentSBCR").hide();
		}
		if ($(this).attr("value") == "checkStatus") {
			$("#divcomplaintShown").hide();
			$("#divChkStatus").show();
		}

	});

	$('input:radio[name=serviceType]').click(function() {
		var stype = $(this).attr("value");
		if ($(this).attr("value") == "AGENT") {
			// change the name of the label
			$('#lblAllName').text("AGENT NAME");
		} else if ($(this).attr("value") == "BILLER") {
			$('#lblAllName').text("BILLER NAME");
		} else if ($(this).attr("value") == "SYSTEM") {
			$('#lblAllName').text("BILLER NAME");
		}
		// fill drop-down serviceTagAgent
		fillService(stype);
	});
	
	$("[id$=txtSearch]").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "getAllAgentsList",
				data : "{ 'prefix': '" + $("#txtSearch").val() + "'}",
				dataType : "json",
				type : "POST",
				contentType : "application/json; charset=utf-8",
				success : function(data) {
					response($.map(data.result, function(item) {
						return {
							data : item.agentId,
							value : item.agentName
						}
					}))
				},
				error : function(response) {
					alert(response.responseText);
				},
				failure : function(response) {
					alert(response.responseText);
				}
			});
		},
		select : function(e, i) {
			$("[id$=txtSearch]").val(i.item.value);
			$("[id$=agentBuzId]").val(i.item.data);
		},
		minLength : 1
	});
			*//******************Complaint End***************************//*
*/			
			$("#billerPaymentModes").on("change", function(event) {
				
				$.ajax({
					url : "/agent/biller-payment-modes/" + document.getElementById("tenantId").value+"?billerPaymentMode="+$("#billerPaymentModes").val(),
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, POST"},
					success: function (results) {
						
						
						
						var arr = results.split('/');
						 var options = $("#billerPaymentChannels");
						
						    $.each(arr, function(item) {
						        options.append($("<option />").val(item).text(item));
						    });
						    
						    
						    
						    //for payment information
						    
						    
						    
						    
						    $.ajax({
								url : "/agent/biller-payment-biller-information/" + document.getElementById("tenantId").value+"?billerPaymentMode="+$("#billerPaymentMode").val(),
								type: 'GET',
				        
								async :false,
								header:{   "Access-Control-Allow-Origin" : "*",
				                            "Access-Control-Allow-Methods": "GET, POST"},
								success: function (results) {
									$("#paymentinformation").html(results);
								}
							});
						    
						    
						    
					}
				});
				
				
			});
			
			
			
			
			
	$("#billerPaymentChannels").on("change", function(event) {
				
				$.ajax({
					url : "/agent/biller-payment-channels/" + document.getElementById("tenantId").value+"?billerPaymentChannel="+$("#billerPaymentChannels").val(),
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, POST"},
					success: function (results) {
						$("#paymenttag").html(results);
					}
				});
				
				
			});
		
			$("#searchId").click(function(){
			    
				
				$.ajax({
					url : "/agent/biller-transaction-details/" + document.getElementById("tenantId").value+"?mobnum="+$("#inputmobileNum").val(),
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, POST"},
					success: function (results) {
						//alert(results);
						$("#tabpay").html(results);
					}
				});
	

		});
			$("#agentheader").load("/resource/html/" + document.getElementById("tenantId").value +"/agentTenantHeader");
			$("#header").load("/resource/html/" + document.getElementById("tenantId").value +"/tenantHeader");
			
			hideAllArea();
			$.ajax({
				url : "/resource/biller-category-list/" + document.getElementById("tenantId").value +"?paymentChannels=INT&paymentModes=Internet_Banking",
				type: 'GET',
        
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
                            "Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					$("#catagoryId").html(results);
					//$("#billerCategories").val(document.getElementById("catagory").value);
				var element =  document.getElementById('catagory');
					
					if(typeof(element) != 'undefined' && element != null){
						
						$("#billerCategories option").each(function() {
							  if($(this).text() == document.getElementById("catagory").value) {
							    $(this).attr('selected', 'selected'); 
							    cataId= $(this).val();
								//$("#billerCategories").change();

							  }                        
							});
					}
					
					
				}
			});
			
			function user(){
				document.getElementById("userHeader").value=document.getElementById("user").value;
			}
			$.ajax({
					url : document.getElementById("user").value,
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, POST"},
					success: function (results) {
						//alert(results);
						document.getElementById("user").value = results;
						
						//document.getElementById("usr").type = 'show';
					}
				});
				$.ajax({
					url : document.getElementById("balance").value,
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, POST"},
					success: function (results) {
						//alert(results);
						document.getElementById("balance").value = results;
						
						//document.getElementById("usr").type = 'show';
					}
				});
		
				$("#refresh").click(function(){
					$.ajax({
						url : "/resource/fetchBalance/"+document.getElementById("tenantId").value,
						type: 'GET',
		        
						async :false,
						header:{   "Access-Control-Allow-Origin" : "*",
		                            "Access-Control-Allow-Methods": "GET, POST"},
						success: function (results) {
							//alert(results);
							document.getElementById("balance").value = results;
							
							//document.getElementById("usr").type = 'show';
						}
					});
		

			});
					var element1 =  document.getElementById('billerlstview');
					if(typeof(element1) != 'undefined' && element1 != null){
				
				$.ajax({
					url : "/resource/biller-list/" + document.getElementById("tenantId").value +"?billerCategory="+cataId+"&paymentChannels=INT&paymentModes=Internet_Banking",
					type: 'GET',
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
								"Access-Control-Allow-Methods": "GET, POST"},
					success: function (results1) {
						$('#biller').show();
						$("#billerlst").html(results1);
						
						$("#billerList").val(document.getElementById("billerlstview").value);
						billerId=$("#billerList").val();
						//var index = billerId.indexOf('_^P');
						var suffix = billerId.substr(billerId.length-3);
						if(suffix == '_^P') {
							var billerIdViewdata = billerId.substr(0,billerId.length - 3); 
							FbillerIdViewdata=billerIdViewdata;
							fetchBillerInfo(FbillerIdViewdata);
						}
						else{
							FbillerIdViewdata=billerId;
							($('#billFetchInfoArea')).show();
							($('#customerInfoArea')).show();
							($('#btnBillFetchArea')).show();
							fetchBillerInfo(FbillerIdViewdata);
							
						}
						//billerId=$(this).val();
						
					}
				});
			}
					var element2 =  document.getElementById('subbillerlstview');
					if(typeof(element2) != 'undefined' && element2 != null){
					
					
					$.ajax({
						url : "/resource/sub-biller-list/" + document.getElementById("tenantId").value +"?parentBillerId="+billerId+"&billerCategory=&paymentChannels=INT&paymentModes=Internet_Banking",
						type: 'GET',
						async :false,
						header:{   "Access-Control-Allow-Origin" : "*",
									"Access-Control-Allow-Methods": "GET, POST"},
						success: function (results1) {
							$("#subbiller").show();
							$("#subbillerlst").html(results1);
							$("#subBillerList").val(document.getElementById("subbillerlstview").value);
							
							($('#billFetchInfoArea')).show();
							($('#customerInfoArea')).show();
							($('#btnBillFetchArea')).show();
							
							FbillerIdViewdata=document.getElementById("subbillerlstview").value;
							fetchBillerInfo(FbillerIdViewdata);
						}
					});
					
					}

					
					
			
			//Bind change event
			$('#billerCategories').on("change", function(event) {
				var carId = $(this).val();//($('#billerCategories :selected').val());
				//selBiller();
				hideAllArea();

				if(carId != '') {
					$.ajax({
						url : "/resource/biller-list/" + document.getElementById("tenantId").value +"?billerCategory="+carId+"&paymentChannels=INT&paymentModes=Internet_Banking",
						type: 'GET',
						async :false,
						header:{   "Access-Control-Allow-Origin" : "*",
									"Access-Control-Allow-Methods": "GET, POST"},
						success: function (results1) {
							$('#biller').show();
							$("#billerlst").html(results1);
							
							//$("#billerList").val(document.getElementById("biller").value);
						}
					});

					//($('#biller')).show();

					$('#billerList').on("change", function(event) {
						var billerId = $(this).val();
						//selSubBiller();
						hideAllArea();
						$("#biller").show();
						
						if(billerId != '') {
							if(selBiller()) {
								$.ajax({
									url : "/resource/sub-biller-list/" + document.getElementById("tenantId").value +"?parentBillerId="+billerId+"&billerCategory=&paymentChannels=INT&paymentModes=Internet_Banking",
									type: 'GET',
									async :false,
									header:{   "Access-Control-Allow-Origin" : "*",
												"Access-Control-Allow-Methods": "GET, POST"},
									success: function (results1) {
										$("#subbillerlst").html(results1);
									}
								});

								$('#subBillerList').on("change", function(event) {
									var subBillerId = $(this).val();
									if(subBillerId == '') {
										//($('#subBillerList').val(''));
										//selSubBiller();
										hideAllArea();
										$("#biller").show();
										$("#subbiller").show();
									}
									else {
										selSubBiller();
										fetchBillerInfo(subBillerId);
										($('#subbiller')).show();
									}
									return;
								});								
							}
						}
						return;
					});
				}
				return;
			});
		});
		
		
		
		
		
		
		
		
		
		
		
		function fetchBillerInfo(billerId) {
			blr = billerId;
			$.ajax({
				url : "/resource/biller-fetch-form/" + document.getElementById("tenantId").value +"?billerId="+billerId,
				type: 'GET',
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
							"Access-Control-Allow-Methods": "GET, POST"},
				success: function (results1) {
					$("#billFetchInfoForm").html(results1);
					$("#inputMobile").attr("maxlength", "10");
					$("#inputMobile").attr("placeholder", "+91");
					$("#inputPAN").attr("maxlength", "10");
					
					var ele =  document.getElementById('mobile');
					if(typeof(ele) != 'undefined' && ele != null && $("#mobile").val()!="null" ){
						$("#inputMobile").val($("#mobile").val());
					}
					
					var ele1 =  document.getElementById('pan');
					if(typeof(ele1) != 'undefined' && ele1 != null && $("#pan").val()!="null"){
						$("#inputPAN").val($("#pan").val());
					}
					
					var ele2 =  document.getElementById('email');
					if(typeof(ele2) != 'undefined' && ele2 != null  && $("#email").val()!="null"){
						$("#inputEmail").val($("#email").val());
					}
					
					var ele3 =  document.getElementById('aadhaar');
					if(typeof(ele3) != 'undefined' && ele3 != null && $("#aadhaar").val()!="null"){
						$("#inputAadhaarNum").val($("#aadhaar").val());
					}
					/*$("#inputMobile").val($("#mobile").val());
					$("#inputPAN").val($("#pan").val());
					$("#inputEmail").val($("#email").val());
					$("#inputAadhaarNum").val($("#aadhaar").val());*/
					
					
					$("#inputMobile").focusout(function(){
						validateMobileNumber();
					});

					$("#inputPAN").focusout(function(){
						validatePANCard();
					});

					$("#inputEmail").focusout(function(){
						validateEmail();
					});

				}
			});			
		}
		
		function hideAllArea() {
			($('#biller')).hide();
			($('#subbiller')).hide();
			($('#billFetchInfoArea')).hide();
			($('#customerInfoArea')).hide();
			($('#btnBillFetchArea')).hide();
			($('#billInfoArea')).hide();
			($('#payArea')).hide();
			($('#btnPayArea')).hide();		
		}
		
		function selBiller() {
			var selectId = ($('#billerList :selected').val());
			if(selectId == '') {
				($('#subBillerList').val(''));
				($('#subbiller')).hide();
				hideAllArea();
				($('#biller')).show();
			}
			else {
				hideAllArea();
				($('#biller')).show();
				var suffix = selectId.substr(selectId.length-2);
				if(suffix == '^P') {
					var billerId = selectId.substr(1,selectId.length - 3);
					if(billerId != '') {
						($('#subbiller')).show();
						return true;
					}
				}
				else {
					fetchBillerInfo(selectId);
					($('#billFetchInfoArea')).show();
					($('#customerInfoArea')).show();
					($('#btnBillFetchArea')).show();
					return false;
				}
			}
			return false;
		}
		
		function selSubBiller()  {
			var subBillerId = ($('#subBillerList :selected').val());
			
			if(subBillerId != '') {
				($('#billFetchInfoArea')).show();
				($('#customerInfoArea')).show();
				($('#btnBillFetchArea')).show();
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

		function AlfanumericValidation(item) {
			//var target = $( event.target );

			var Number =document.getElementById(item).value
			var IndNum = /^[0-9a-zA-Z]+$/;  

			if(!(IndNum.test(Number))){
				getErrMessage(4);
				document.getElementById(item).focus();
				return false;
			}
			return true;
		}
		
		function NumericValidation(item) {			
			var Number =document.getElementById(item).value
			if(isNaN(Number) ||Number=="" ) {
				getErrMessage(5);
				document.getElementById(item).focus();
				return false;
			}
			return true;
		}
		
		function validateAndQuickPay(arr) {
			if(validateBillFetch(arr)) {
				if($("#quickPayAmount").val()!="") {
					
					if(NumericValidation("quickPayAmount")) {
						document.billFetchForm.action='/agent/' + document.getElementById("tenantId").value+ '/pay';
						document.billFetchForm.submit();
						//alert(document.getElementById("quickPayAmount").value);
						//document.getElementById('billPayForm').action='/agent/'+document.getElementById('tenantId').value+'pay?txnAmount=' + document.getElementById("quickPayAmount").value;

						//document.billPayForm.submit();
					}
					
					
				} else {
					getErrMessage(7);
					$("#quickPayAmount").focus();
					return false;
					
					
				}
			}
		}

		function validateAndFetchBill(arr) {
			if(validateBillFetch(arr)) {
				var fetchForm = new Object();
				for(ar in arr) {
					fetchForm[arr[ar][0]] = document.getElementById(arr[ar][0]).value;
				}
				
				fetchForm['billerId'] = document.getElementById('billerId').value;
				fetchForm['mobileNum'] = document.getElementById('inputMobile').value;
				fetchForm['aadhaarNum'] = document.getElementById('inputAadhaarNum').value;
				fetchForm['pan'] = document.getElementById('inputPAN').value;
				fetchForm['emailId'] = document.getElementById('inputEmail').value;
				
				var ele =  document.getElementById('agent');
				if(typeof(ele) != 'undefined' && ele != null && $("#agent").val()!="null" ){
					//$("#inputMobile").val($("#mobile").val());
					fetchForm['agent'] = document.getElementById('agent').value;
				}
				
				
				
				
				var formStr = JSON.stringify(fetchForm); 
				var jsonObj = JSON.parse(formStr);
				$.post( "/resource/biller-fetch-form-post/" + document.getElementById("tenantId").value, jsonObj)
				  .done(function( data ) {
					  $("#billInfoForm").html(data);
					  $("#billInfoArea").show();
				  });
				
				return true;
			}
			return false;
		}

		
		function validateBillFetch( arr) {
			if($("#inputMobile").val()=='') {
				getErrMessage(1);
				//document.getElementById(arr[ar][0]).focus();
				
				$("#inputMobile").focus();
				return false;
			}
			for(ar in arr) {
				var Number =document.getElementById(arr[ar][0]).value
				if(arr[ar][2]=="false") {
					if(Number == "") {
						getErrMessage(6);
						document.getElementById(arr[ar][0]).focus();
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
				$("#inputPAN").focus();
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
				$("#inputEmail").focus();
			}
		}
		
		function validateAndSubmitBillPayment() {
			
			var paymentAmount = $("#billerAmountOptions").val();
			if (paymentAmount == null || paymentAmount == ""){
				getErrMessage(7);
				$("#billerAmountOptions").focus();
				return false;
			}
			else {
				
				var ele =  document.getElementById('IP');
				if(typeof(ele) != 'undefined' && ele != null && $("#IP").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('APP').value+" />")
				     .attr("id", "IP")
				     .attr("name", "IP")
				     .appendTo("#billPayForm");
				}
				
				
				var ele1 =  document.getElementById('MAC');
				if(typeof(ele1) != 'undefined' && ele1 != null && $("#MAC").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('MAC').value+" />")
				     .attr("id", "MAC")
				     .attr("name", "MAC")
				     .appendTo("#billPayForm");
				}
				
				var ele2 =  document.getElementById('OS');
				if(typeof(ele2) != 'undefined' && ele2 != null && $("#OS").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('OS').value+" />")
				     .attr("id", "OS")
				     .attr("name", "OS")
				     .appendTo("#billPayForm");
				}
				
				var ele3 =  document.getElementById('APP');
				if(typeof(ele3) != 'undefined' && ele3 != null && $("#APP").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('APP').value+" />")
				     .attr("id", "APP")
				     .attr("name", "APP")
				     .appendTo("#billPayForm");
				}
				
				var ele4 =  document.getElementById('TERMINAL_ID');
				if(typeof(ele4) != 'undefined' && ele4 != null && $("#TERMINAL_ID").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('TERMINAL_ID').value+" />")
				     .attr("id", "TERMINAL_ID")
				     .attr("name", "TERMINAL_ID")
				     .appendTo("#billPayForm");
				}
				
				var ele5 =  document.getElementById('MOBILE');
				if(typeof(ele5) != 'undefined' && ele5 != null && $("#MOBILE").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('MOBILE').value+" />")
				     .attr("id", "MOBILE")
				     .attr("name", "MOBILE")
				     .appendTo("#billPayForm");
				}
				
				var ele6 =  document.getElementById('GEOCODE');
				if(typeof(ele6) != 'undefined' && ele6 != null && $("#GEOCODE").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('GEOCODE').value+" />")
				     .attr("id", "GEOCODE")
				     .attr("name", "GEOCODE")
				     .appendTo("#billPayForm");
				}
				
				var ele7 =  document.getElementById('POSTAL_CODE');
				if(typeof(ele7) != 'undefined' && ele7 != null && $("#POSTAL_CODE").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('POSTAL_CODE').value+" />")
				     .attr("id", "POSTAL_CODE")
				     .attr("name", "POSTAL_CODE")
				     .appendTo("#billPayForm");
				}
				
				var ele8 =  document.getElementById('IFSC');
				if(typeof(ele8) != 'undefined' && ele8 != null && $("#IFSC").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('IFSC').value+" />")
				     .attr("id", "IFSC")
				     .attr("name", "IFSC")
				     .appendTo("#billPayForm");
				}
				
				var ele9 =  document.getElementById('Remarks');
				if(typeof(ele9) != 'undefined' && ele9 != null && $("#Remarks").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('Remarks').value+" />")
				     .attr("id", "Remarks")
				     .attr("name", "Remarks")
				     .appendTo("#billPayForm");
				}
				
				var ele10 =  document.getElementById('AccountNo');
				if(typeof(ele10) != 'undefined' && ele10 != null && $("#AccountNo").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('AccountNo').value+" />")
				     .attr("id", "AccountNo")
				     .attr("name", "AccountNo")
				     .appendTo("#billPayForm");
				}
				var ele11 =  document.getElementById('CardNum');
				if(typeof(ele11) != 'undefined' && ele11 != null && $("#CardNum").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('CardNum').value+" />")
				     .attr("id", "CardNum")
				     .attr("name", "CardNum")
				     .appendTo("#billPayForm");
				}
				var ele12 =  document.getElementById('AuthCode');
				if(typeof(ele12) != 'undefined' && ele12 != null && $("#AuthCode").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('AuthCode').value+" />")
				     .attr("id", "AuthCode")
				     .attr("name", "AuthCode")
				     .appendTo("#billPayForm");
				}
				
				var ele13 =  document.getElementById('MobileNo');
				if(typeof(ele13) != 'undefined' && ele13 != null && $("#MobileNo").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('MobileNo').value+" />")
				     .attr("id", "MobileNo")
				     .attr("name", "MobileNo")
				     .appendTo("#billPayForm");
				}
				
				var ele14 =  document.getElementById('MMID');
				if(typeof(ele14) != 'undefined' && ele14 != null && $("#MMID").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('MMID').value+" />")
				     .attr("id", "MMID")
				     .attr("name", "MMID")
				     .appendTo("#billPayForm");
				}
				var ele15 =  document.getElementById('WalletName');
				if(typeof(ele15) != 'undefined' && ele15 != null && $("#WalletName").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('WalletName').value+" />")
				     .attr("id", "WalletName")
				     .attr("name", "WalletName")
				     .appendTo("#billPayForm");
				
				}
				
				var ele16 =  document.getElementById('VPA');
				if(typeof(ele16) != 'undefined' && ele16 != null && $("#VPA").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('VPA').value+" />")
				     .attr("id", "VPA")
				     .attr("name", "VPA")
				     .appendTo("#billPayForm");
				
			}
				var ele17 =  document.getElementById('billerPaymentModes');
				if(typeof(ele17) != 'undefined' && ele17 != null && $("#billerPaymentModes").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('billerPaymentModes').value+" />")
				     .attr("id", "billerPaymentModes")
				     .attr("name", "billerPaymentModes")
				     .appendTo("#billPayForm");
		
				}
				
				var ele18 =  document.getElementById('billerPaymentChannels');
				if(typeof(ele18) != 'undefined' && ele18 != null && $("#billerPaymentChannels").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('billerPaymentChannels').value+" />")
				     .attr("id", "billerPaymentChannels")
				     .attr("name", "billerPaymentChannels")
				     .appendTo("#billPayForm");
		
				}
				
				var ele19 =  document.getElementById('IMEI');
				if(typeof(ele16) != 'undefined' && ele16 != null && $("#IMEI").val()!="null" ){
					 $("<input type='hidden' value="+document.getElementById('IMEI').value+" />")
				     .attr("id", "IMEI")
				     .attr("name", "IMEI")
				     .appendTo("#billPayForm");
				
			}
				//alert(document.getElementById("totamount").value);

				document.getElementById('billPayForm').action='/agent/'+document.getElementById("tenantId").value+'/pay';

				document.billPayForm.submit();
				return true;
			}
			
			
			
		}
		
		function loadDetails(){
			alert("entering!");

			//alert("Hiiiiiiiiiii");
			$.ajax({
				url : "/agent/" + document.getElementById("tenantId").value +"/dashboard",
				type: 'GET',
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
							"Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					$("#dashboard").show();
					$("#dashboarddet").show();
					$("#details").html(results);
					/*$("#totalAmountCollected").show();
					$("#totalBillFetch").show();
					$("#totalBillPayment").show();*/
					/*$("#billPayLabel").hide();
					$("#billerDetails").hide();*/
					
				}
			});
			showTxnList();
		}
		
		function loadBillForm(){
			alert("entering!");
			$.ajax({
				url : "/agent/" + document.getElementById("tenantId").value +"/bill",
				type: 'GET',
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
							"Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					$("#page-wrapper").html('');
					$("#page-wrapper").html($.parseHTML(results));
					
					bind();
					
				}
			});
		}
		
		function showTxnList(){
			//alert("inside showTxnList");
			$("#txnList").show();
			$("#links").show();
			$.ajax({
				url : "/agent/" + document.getElementById("tenantId").value +"/dashboard",
				type: 'GET',
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
							"Access-Control-Allow-Methods": "GET, POST"},
				success: function (results) {
					$("#links").show();
					$("#dashboarddet").show();
					$("#details").html(results);
					/*$("#totalAmountCollected").show();
					$("#totalBillFetch").show();
					$("#totalBillPayment").show();*/
					$("#billPayLabel").hide();
					$("#billerDetails").hide();
					
				}
			});
		}
				
	
	/********************Transaction History Start************************/
	function tranxHist(){
		$.ajax({
			url : "/agent/" + document.getElementById("tenantId").value +"/transactionHistory",
			type: 'GET',
			async :false,
			header:{   "Access-Control-Allow-Origin" : "*",
						"Access-Control-Allow-Methods": "GET, POST"},
			success: function (results) {
				$("#page-wrapper").html('');
				$("#page-wrapper").html($.parseHTML(results));
				
				bind();
			}
		});
	}
	
	function bind(){
		var tbl = $("#datagrid").serialize();
		$.ajax({
			url : "/agent/" + document.getElementById("tenantId").value + "/agent-transaction-details",
			type : "get",
			success : function(result) {
				var json = JSON.parse(JSON.stringify(result));
				if (json["code"] == 1) {
					var arr = json["result"];
					if (arr != null && arr.length > 0) {
						loadDataTable(arr);
					}
					if (arr.length == 0) {
						var html = '<tr>';
						html += '<td colspan="5" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				}
			}
		});
	}
	
	function loadDataTable(arr) {
		//load_js("/js/jquery.dataTables.min.js");
		load_css("/css/jquery.dataTables.min.css");
		//test();
		oTable.fnClearTable();// clear the DataTable
		oTable.fnDraw();
		$.each(arr, function(key, item) {
			
			oTable.fnAddData([ item.bill_amount, item.bill_date,
					item.reconDescription, item.transactionType, item.bill_amount ]);
			/*var html = '<tr>';
			html += '<td style="text-align:center">' + item.txnRefId + '</td>';
			html += '<td style="text-align:center">' + item.bill_date + '</td>';
			html += '<td style="text-align:center">' + item.reconDescription + '</td>';
			html += '<td style="text-align:center">' + item.transactionType + '</td>';
			html += '<td style="text-align:center">' + item.bill_amount + '</td>';			
			html +='</tr>';
			$("#listBody").html(html);*/
		});
		oTable.fnDraw(false);
	}
	
	function load_js(path)
	   {
	      var head= document.getElementsByTagName('head')[0];
	      var script= document.createElement('script');
	      script.type= 'text/javascript';
	      script.src= path;
	      head.appendChild(script);
	   }
	
	function load_css(path)
	   {
	      var head= document.getElementsByTagName('head')[0];
	      var link= document.createElement("link");
	      link.setAttribute("rel","stylesheet");
	      link.setAttribute("type", "text/css");
	      link.setAttribute("href",path);
	      head.appendChild(link);
	   }
	
	/********************Transaction History End************************/
	
	/********************Complaint Start************************/
	
	function loadComplaint(){
		$.ajax({
			url : "/" + document.getElementById("tenantId").value +"/complaint/raiseComplaint",
			type: 'GET',
			async :false,
			header:{   "Access-Control-Allow-Origin" : "*",
						"Access-Control-Allow-Methods": "GET, POST"},
			success: function (results) {
				$("#page-wrapper").html('');
				$("#page-wrapper").html($.parseHTML(results));
			}
		});
	}
	
	function fillService(stype) {
		var tbl = $("#agentServList input").serialize();
		$.ajax({
			contentType : "application/json; charset=utf-8",
			url : "agentServiceReasonList/" + stype,
			type : "post",
			data : tbl,
			dataType : "json",
			header : {
				"Access-Control-Allow-Origin" : "*",
				"Access-Control-Allow-Methods" : "GET, POST"
			},
			success : function(result) {
				$("#serviceTagAgent").empty();
				$("#serviceTagAgent").append(
						$("<option></option>").val('').html(
								' --- Select Service Reason --- '));

				$.each(result.result, function(key, value) {
					$("#serviceTagAgent").append(
							$("<option></option>").val(value.reasonCode).html(
									value.reasonName));
				});
			}
		});
	}

	function cmsFunction(val) {
		// $("#divTranType").hide();
		if (val == 'TBC') {
			$("#divAgentTBCR").show();
			$("#divtxnList").hide();
			$("#divComplaint").hide();
			$("#divAgentSBCR").hide();
			$("#complaintTypeCd").val("TRANSACTION");
		}
		if (val == 'SBC') {
			// $("#strComplaintId").prop("disabled", false);
			$("#divAgentSBCR").show();
			$("#divAgentTBCR").hide();
			$("#complaintTypeCd").val("SERVICE");
		}
		//alert($("#complaintTypeCd").val());
	}

	function trnxClick() {
		$("#divtxnList").show();
		var stype = $("#mobNo").val();
		if (stype.length < 10) {
			stype = $("#transactionId").val();
		}
		var tbl = $("#tblDataGridForm").serialize();
		$.ajax({
					// contentType : "application/json; charset=utf-8",
					url : "TransactionLists/" + stype,
					type : "get",
					// data : tbl,
					// dataType : "json",
					/*
					 * header : { "Access-Control-Allow-Origin" : "*",
					 * "Access-Control-Allow-Methods" : "GET, PUT, POST, DELETE,
					 * OPTIONS" },
					 */
					success : function(result) {
						var json = JSON.parse(JSON.stringify(result));
						if (json["code"] == 1) {
							var arr = json["result"];
							if (arr != null && arr.length > 0) {
								loadDataTable(arr);
							}
							if (arr.length == 0) {
								var html = '<tr>';
								html += '<td colspan="7" style="text-align:center">No Data Found</td></tr>';
								$("#listBody").html(html);
							}
						}
					}
				});
	}

	function loadDataTable(arr) {
		oTable.fnClearTable();// clear the DataTable
		oTable.fnDraw();
		$
				.each(
						arr,
						function(key, item) {
							var idstr = item.agentId;
							// alert(idstr);

							var agentSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"agentSelected\" id=\"agentSelected\" value=\""
									+ idstr + "\" onchange=\"radioClick();\"/>";
							oTable.fnAddData([ agentSelected, item.txnReferenceId,
									item.agentId, item.txnDate, item.amount,
									item.txnStatus ]);
						});
	}

	function radioClick() {
		if ($('input:radio[name="agentSelected"]:checked', '#tblDataGridForm')
				.val()) {
			var sts = null;
			$('input:radio[name="agentSelected"]:checked').each(function() {
				$("#divComplaint").show();
				var $row = $(this).parents('tr');
				sts = ($row.find('td:eq(1)').html());
				$('#txnRefId').val(sts);
			});
		}
	}

	function ckeckvalidation() {
		var compDesc = $.trim($("#compDesc").val());
		if (compDesc != "") {
			$("#compDescMsg").html("");
			return true
		} else {
			$("#compDescMsg").html("Please provide valid details");
			return false;
		}
	}
	
	/********************Complaint End************************/
	
	
				
		
