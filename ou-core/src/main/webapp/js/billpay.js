
var blr;		


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
		
		function calculateCCF() {
			
			var splitRowObject = $("#billerAmountOptions").val().split('|');
			if(splitRowObject.length > 0)
			   // alert(splitRowObject[0]);
			
			$.ajax({
				url : "/resource/calculate-ccf-element/" + document.getElementById("tenantId").value +"?billerId="+blr+"&ccfAmount="+splitRowObject[0],
				type: 'GET',
        
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
                            "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
				success: function (results) {
					
				var tot=	 parseInt(results)+parseInt(splitRowObject[0]);
				
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
			//alert("refId"+refId+"billerId"+billerId);
			
			 window.location =  "/resource/html/" + document.getElementById("tenantId").value+"/"+"tenantBillPayAgHTML"+"?refId="+refId+"&billerId="+billerId+"&requestType="+requestType;
			
		
			
			
		}
		$(document).ready(function() {
			var cataId;
			var billerId;
			var FbillerIdViewdata;
		//alert($("#catagory").val());
			//alert($("#billerlstview").val());
			//alert($("#mobile").val());
			
			$("#searchId").click(function(){
			    //alert("The paragraph was clicked.");
				//($("#inputmobileNum").val());
				
				$.ajax({
					url : "/agent/biller-transaction-details/" + document.getElementById("tenantId").value+"?mobnum="+$("#inputmobileNum").val(),
					type: 'GET',
	        
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
	                            "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
					success: function (results) {
						//alert(results);
						$("#tabpay").html(results);
					}
				});
	

		});

			$("#header").load("/resource/html/" + document.getElementById("tenantId").value +"/tenantHeader");
			
			hideAllArea();
			$.ajax({
				url : "/resource/biller-category-list/" + document.getElementById("tenantId").value +"?paymentChannels=INT&paymentModes=Internet_Banking",
				type: 'GET',
        
				async :false,
				header:{   "Access-Control-Allow-Origin" : "*",
                            "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
			
					var element1 =  document.getElementById('billerlstview');
					if(typeof(element1) != 'undefined' && element1 != null){
				
				$.ajax({
					url : "/resource/biller-list/" + document.getElementById("tenantId").value +"?billerCategory="+cataId+"&paymentChannels=INT&paymentModes=Internet_Banking",
					type: 'GET',
					async :false,
					header:{   "Access-Control-Allow-Origin" : "*",
								"Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
									"Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
									"Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
												"Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
							"Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},
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
						document.billFetchForm.action='/resource/bill-pay-request/' +document.getElementById("tenantId").value;
						document.billFetchForm.submit();
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
			
			
			return true;
		}
