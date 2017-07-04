function displayMerchantdetails(columnData_pin,merchantDetailURL){
            $('#jqxTabs').jqxTabs({theme: "ui-redmond", width: 1220, position: 'top',  collapsible: true });
         
            $.ajax ({
        		
            	
        		//url :  "${pageContext.request.contextPath}/merchDetails",
            	url:merchantDetailURL,
        		type: 'POST',
        		datatype : "json",
        		contentType: "application/json",
        		async:false,
        		cache:false,
        		
        		data : JSON.stringify({appPIN:columnData_pin}),
        		 
        		success: function (data) {
        			
        			
        			//alert(data.appPIN);
        			//alert(data.merchantName);
        			//alert(data.salesAgent);
        		//-----merchant info---------//	
        			$("#appPin").html("");
        			$("#appPin").html(data.appPIN);
        			
        			$("#merchantName").html("");
        			$("#merchantName").html(data.merchantName);
        			
        			$("#merchantGroup").html("");
        			$("#merchantGroup").html(data.merchantGroup);
        			
        			$("#MCC").html("");
        			$("#MCC").html(data.MCC);
        			
        			$("#salesAgent").html("");
        			$("#salesAgent").html(data.salesAgent);
        			
        			$("#firstName").html("");
        			$("#firstName").html(data.firstName);
        			
        			$("#lastName").html("");
        			$("#lastName").html(data.lastName);
        			
        			$("#email").html("");
        			$("#email").html(data.email);
        			
        			$("#phone").html("");
        			$("#phone").html(data.phone);
        	//-------------------progress details------------------------	//	
        	
        			$("#initiationDate").html("");
        			$("#initiationDate").html(data.initiationDate);
        			
        			$("#boardingDate").html("");
        			$("#boardingDate").html(data.boardingDate);
        			
        			$("#busOpenDate").html("");
        			$("#busOpenDate").html(data.busOpenDate);
        			
        			$("#locInspectDate").html("");
        			$("#locInspectDate").html(data.locInspectDate);
        			
        			$("#approvalDate").html("");
        			$("#approvalDate").html(data.approvalDate);
        			
        			$("#rejectionDate").html("");
        			$("#rejectionDate").html(data.rejectionDate);
        	//----------------identity-----------------------------------//
        			$("#typeofOwnerShip").html("");
        			$("#typeofOwnerShip").html(data.typeofOwnerShip);
        			
        			$("#merchantFederalITaxId").html("");
        			$("#merchantFederalITaxId").html(data.merchantFederalITaxId);
        			
        			$("#merchantSSN").html("");
        			$("#merchantSSN").html(data.merchantSSN);
        			
        			$("#mthsOwnedBusniess").html("");
        			$("#mthsOwnedBusniess").html(data.mthsOwnedBusniess);
        			
        			$("#cardHolderDesc").html("");
        			$("#cardHolderDesc").html(data.cardHolderDesc);
        			
        			$("#websiteURL").html("");
        			$("#websiteURL").html(data.websiteURL);
        			
        			$("#insLName").html("");
        			$("#insLName").html(data.insLName);
        			
        			$("#salesMethod").html("");
        			$("#salesMethod").html(data.salesMethod);
        			
        			$("#delivery").html("");
        			$("#delivery").html(data.delivery);
        			
        			$("#insFName").html("");
        			$("#insFName").html(data.insFName);
        			
        			$("#status").html("");
        			$("#status").html(data.status);
        			
        			$("#processor").html("");
        			$("#processor").html(data.processor);
        			
        			//--------validation------------------//
        		
	            if (data.validGuarantor == true) {
					
					$('#validGuarantor').prop('checked', true);
				} else if (data.validGuarantor == false) {
					
					$('#validGuarantor').prop('checked', false);
				}
        			
	            if (data.validMerchantBank == true) {
					
					$('#validMerchantBank').prop('checked', true);
				} else if (data.validMerchantBank == false) {
					
					$('#validMerchantBank').prop('checked', false);
				}
	            
	            if (data.validOwnerDetail == true) {
					
					$('#validOwnerDetail').prop('checked', true);
				} else if (data.validOwnerDetail == false) {
					
					$('#validOwnerDetail').prop('checked', false);
				}
	            
	            if (data.locationInspected == true) {
					
					$('#locationInspected').prop('checked', true);
				} else if (data.locationInspected == false) {
					
					$('#locationInspected').prop('checked', false);
				}
	            
	            if (data.terroristCheck == true) {
					
					$('#terroristCheck').prop('checked', true);
				} else if (data.terroristCheck == false) {
					
					$('#terroristCheck').prop('checked', false);
				}
	            
	            if (data.validSSN == true) {
					
					$('#validSSN').prop('checked', true);
				} else if (data.validSSN == false) {
					
					$('#validSSN').prop('checked', false);
				}
	            
	            if (data.idverified == true) {
					
					$('#idverified').prop('checked', true);
				} else if (data.idverified == false) {
					
					$('#idverified').prop('checked', false);
				}
	            
	            if (data.creditCheck == true) {
					
					$('#creditCheck').prop('checked', true);
				} else if (data.creditCheck == false) {
					
					$('#creditCheck').prop('checked', false);
				}

			},
			error : function(jqXHR, textStatus, errorThrown) {
				(errorThrown);
				alert(errorThrown);

			}

		});

	}