
//--------------------------------------draw MBA Qtr Graph --------------------------------------------------------//

function draw_mbaQtr_lineGraph(id, name, chartType, URI,url_mbaQtrGraph) {
	$.ajax({
		 
	
	 //	url : "${pageContext.request.contextPath}/displayGraph" ,
		url : url_mbaQtrGraph,
	 	
	 data : {URI_val:URI},
	 type: 'GET',
	  dataType: 'JSON',
	  async :false,
	  header:{   "Access-Control-Allow-Origin" : "*" ,
		          'Accept': 'application/json',
		 		 "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},

	  success: function (results) {
		  //alert("alert");
		  var labels = [];
	      var data1=[] , data2=[] , data3=[] ,data4=[] ;
	      var data5=[] , data6=[] , data7=[] ,data8=[];
	      
	      for (i in results)
		   {
	    	  
	    	  month_val={};
	    	  month_val["status"]=results[i].month_value;
			    
			    labels.push(month_val);
	    	  
		     item1={};
		    
		    item1["value"]=results[i].init_Cnt;
		    
		    data1.push(item1);
		    
  item2={};
		    
		    item2["value"]=results[i].appr_Cnt;
		    
		    data2.push(item2);
  item3={};
		    
		    item3["value"]=results[i].reject_Cnt;
		    
		    data3.push(item3);
  item4={};
		    
		    item4["value"]=results[i].board_Cnt;
		    
		    data4.push(item4); 
		    
		   data5[i]=results[i].init_status;
		   data6[i]=results[i].appr_status;
		   data7[i]=results[i].board_status;
		   data8[i]=results[i].reject_status;
		   
		   		   }
	   
	      var settings = {
	                title: "MBA Count - Last Qtr",
	                description: "Status of the last quater",
	                enableAnimations: true,
	                animationDuration: 2000,
	                showLegend: true,
	                showToolTips : true,
	              //  toolTipShowDelay : 500,
	                padding: { left: 5, top: 5, right: 40, bottom: 5 },
	                titlePadding: { left: 90, top: 0, right: 0, bottom: 10 },
	                source: labels,
	              
	                xAxis:
	                {
	                    dataField: 'status',
	                    gridLines: { visible: true },
	                    title: { text: 'Months' }
	                },
	                colorScheme: 'scheme02',
	                valueAxis:
	                {
	                    visible: true,
	                    title: { text: 'Merchant Status Count' }
	                },
	                seriesGroups:
	                    [
	                        {
	                            type: 'stackedline',
	                            source: data1,
	                            series: [
	                                  { dataField: 'value', displayText: 'Initiated' }
	                            ]
	                        },
	                        {
	                            type: 'stackedline',
	                            source: data2,
	                            series: [
	                                    { dataField: 'value', displayText: 'Approved'  }
	                            ]
	                        },
	                        
	                        
	                        {
	                            type: 'stackedline',
	                            source: data3,
	                            series: [
	                                    { dataField: 'value', displayText: 'Rejected' }
	                            ]
	                        },
	                        {
	                            type: 'stackedline',
	                            source: data4,
	                            series: [
	                                    { dataField: 'value', displayText: 'Boarded'  }
	                            ]
	                        }
	                    ]
	            };
	           
	      
	      function myEventHandler(event) {
	          var eventData = '<div><b>Last Event: </b>' + event.type + '<b>, Serie DataField: </b>' + event.args.serie.dataField + '<b>, Value: </b>' + event.args.elementValue + "</div>";
	        
	       
	        var  mnth =results[event.args.elementIndex].month_value;
	        alert(mnth);
	          
	          var month_value=monthID_generation(mnth);
	          showMrchStatusGrid(month_value,url_showMerchStatusGrid);
	      };
	    
	      $('#jqx_'+id).jqxChart(settings);
	     
	      
	      $('#jqx_'+id).on('click', function (event) {
	          if (event.args)
	              myEventHandler(event); 
	          
	          });
	  		
	          
	  }
		});
	
}



//--------------------------------------show Merchant Status Grid ------------------------------------------------------//


 function showMrchStatusGrid(monthID,url_showMerchStatusGrid){
	

	// var url = "status.json";
	var month_val = month_generation_grid(monthID);
	
	var url = url_showMerchStatusGrid ;
     var data_val;

     var GridModel = function () {
         this.items = ko.observableArray();
         var me = this;
         $.ajax({
             
          url: url,
          data : ({month:monthID,status:""}),
   		  type: 'GET',
   		  dataType: 'JSON',
   		  cache :false,
   		  async :false,
   		  header:{   "Access-Control-Allow-Origin" : "*" ,
   			          'Accept': 'application/json',
   			 		 "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},

             
         }).done(function (data) {
        	 var dt = data[0].date;
        	 date = new Date(dt);
        	 var year = date.getFullYear();
        	 
        	 
        	 $('#myModal').modal('show');
             $("#modal-body").html("");
             $("#modal-body").append('<div class ="caption" align="center"><label>Merchant Status for '+month_val+','+year+'</label></div>');
             $("#modal-body").append('<div id="tablegrid1"><div id="grid"></div></div><br/><div id = "tablegrid2"><div id="grid1"></div></div>');
            // $("#modal-body").append('<div><label>List of Merchant Status for '+month_val+','+year+'</label></div><div id="tablegrid1"><div id="grid"></div><br/></div><div id = "tablegrid2"><div id="grid1"></div></div>');
        	 
         	data_val=data;
         	 var jsonData =data;
             //var jsonData = $.parseJSON(data);
             me.items(jsonData);
            
         });
     };

     var model = new GridModel();

     // prepare the data
     var source =
     {
         datatype: "observablearray",
         datafields: [
             { name: 'date' },
             { name: 'init_Cnt' ,type : 'int'},
             { name: 'appr_Cnt' ,type : 'int'},
             { name: 'board_Cnt' ,type : 'int' },
             { name: 'reject_Cnt' ,type : 'int'},
         ],
         id: 'id',
         localdata: model.items
     };

     var dataAdapter = new $.jqx.dataAdapter(source);
     
     

     $("#grid").jqxGrid(
     {   theme: "energyblue",
         width: 550,
       //  height: 210,
         source: dataAdapter,
         pageable: true,
         autoheight: true,
         pagesize: 5,
       //  pagesizeoptions: [5],
         selectionmode: 'singlecell',
         columns: [
           { text: 'Date', datafield: 'date',cellsformat: 'd' ,width:150 },
           { text: 'INITIATED', datafield: 'init_Cnt',cellsalign: 'right',width:100 },
           { text: 'APPROVED', datafield: 'appr_Cnt' ,cellsalign: 'right',width:100},
           { text: 'BOARDED', datafield: 'board_Cnt', cellsalign: 'right',width:100},
           { text: 'REJECTED', datafield: 'reject_Cnt' ,cellsalign: 'right',width:100 }
         ]
     });

    // ko.applyBindings(model);
     
     $("#grid").on('cellclick', function (event) {
         var datafield = event.args.datafield;
         var value = event.args.value;
         
         var row = event.args.rowindex;
         var columntext = $("#grid").jqxGrid('getcolumn', event.args.datafield).text;
       // alert(columntext );
       //  alert("date  =" + data_val[row].date);
         
         var date = data_val[row].date;
         var status = columntext;
         
        // $("#log").html("A cell has been clicked:" + event.args.rowindex + ":" + event.args.datafield);
         
     
         
         if(columntext == "Date" || value == 0){
         	alert("wrong selection");
         	
         }
         else{
         	
         displayMerchInfo(date,status,url_displayMerchInfo);
         //	alert("success");
         }
     });
	
		
	
} 
 
 
//--------------------------display Merchchant Info Grid -------------------------------------------------//
 
 
 
 function displayMerchInfo(date,status,url_displayMerchInfo){
	
	
	
	var url = url_displayMerchInfo;
    var data_val;

    var GridModel = function () {
     this.items = ko.observableArray();
     var me = this;
     $.ajax({
         
      url: url,
      type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
		data: JSON.stringify({cDate:date , status: status}),
		cache:false, 

         
     }).done(function (data) {
    	 
     	$("#tablegrid2").html("");
     	$("#tablegrid2").append('<div class ="caption" align="center"><label>'+status+ '  Merchants on '+date+'</label></div>')
			$("#tablegrid2").append('<div id="grid1"></div>');
         
     	data_val=data;
     	 var jsonData =data;
         //var jsonData = $.parseJSON(data);
         me.items(jsonData);
        
     });
 };

 var model = new GridModel();

 // prepare the data
 var source =
 {
     datatype: "observablearray",
     datafields: [
         { name: 'appPIN' },
         { name: 'merchantName' }
         
     ],
     id: 'id',
     localdata: model.items
 };

 var dataAdapter = new $.jqx.dataAdapter(source);
 
 

 $("#grid1").jqxGrid(
 {   theme: "energyblue",
     width: 550,
   //  height: 210,
     source: dataAdapter,
     pageable: true,
     pagesize: 5,
     selectionmode: 'singlecell',
     
     autoheight: true,
     columns: [
       { text: 'Merchant Application Pin', datafield: 'appPIN',cellsalign: 'centre' ,width:275},
       { text: 'Merchant Name', datafield: 'merchantName',cellsalign: 'right',width:275 },
       
     ]
 });

//  ko.applyBindings(model);

$("#grid1").on('cellclick', function (event) {
      var datafield = event.args.datafield;
      var value = event.args.value;
      
      var row = event.args.rowindex;
      var columntext = $("#grid1").jqxGrid('getcolumn', event.args.datafield).text;
      var columnData_pin=  args.value;
      if (columntext == "Merchant Name")
     	 {
     	 
     	 }
     	 
      else {
     	
     	$("#mrchDetails").modal('show');
     	$("#btnCloseModal").click(function(){
     	    
      	   $("#SubgridStroreInfo").hide();
      	});
      	$('.panel-collapse.in').collapse('hide');
     	displayMerchantdetails(columnData_pin,merchantDetailURL);
    	OwnerInfo(columnData_pin,merchantOwnerInfo);
    	PaymentMthod(columnData_pin,merchantPaymentMethod) ;
    	CreditCardType(columnData_pin,merchantCreditCardType);
    	StoreInfo(columnData_pin,merchantstoreInfo);
     	
      }


});
}
 
 //----------------------------------------Detailed Merchant Information------------------------------------------------------//
 
 function displayMerchantdetails(columnData_pin,merchantDetailURL){
     $('#jqxTabs').jqxTabs({theme: "ui-redmond", width: 1220, position: 'top',  collapsible: true });
  
     $.ajax ({
 		
     	url:merchantDetailURL,
 		type: 'POST',
 		datatype : "json",
 		contentType: "application/json",
 		async:false,
 		cache:false,
 		
 		data : JSON.stringify({appPIN:columnData_pin}),
 		 
 		success: function (data) {
 			
 		//---------------------------------Tab merchant info----------------------------------------//	
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
 			
 	//------------------------------------------Tab progress details--------------------------------------	//	
 	
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
 			
 	//------------------------------------------Tab identity-----------------------------------------------//
 			
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
 			
 //---------------------------------------- Tab validation--------------------------------------//
 		
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
 
 //------------------------------------------Owner Info-------------------------------------------------------------//
 
 function OwnerInfo(columnData_pin,merchantOwnerInfo) 
 {
 
// var url = merchantOwnerInfo;
 var data_val;

 var GridModel = function () {
     this.items = ko.observableArray();
     var me = this;
     
     
     $.ajax({
         datatype: 'json',
         url: merchantOwnerInfo,
         type: 'POST',
 		//datatype : "json",
 		contentType: "application/json",
 		async:false,
 		cache:false,
         data : JSON.stringify({appPin:columnData_pin}),
         
     }).done(function (data) {
     	data_val=data;
     	 var jsonData =data;
         //var jsonData = $.parseJSON(data);
         me.items(jsonData);
        
     });
 };

 var model = new GridModel();

 // prepare the data
 var source =
 {
     datatype: "observablearray",
     datafields: [
         { name: 'ownerId' },
         { name: 'appPin'},
         { name: 'firstName'},
         { name: 'lastName'},
         { name: 'title'},
         { name: 'percentOwner' },
         { name: 'address'},
         { name: 'phone'},
         { name: 'fax'},
         { name: 'bankName' },
         { name: 'bankAddress' },
         { name: 'bankPhone'},
         { name: 'bankACType' },
         { name: 'bankACHOlder' },
         { name: 'bankACNo'},
         { name: 'transitNo' },
         { name: 'ssn'}
     ],
     id: 'id',
     localdata: model.items
 };

 var dataAdapter = new $.jqx.dataAdapter(source);
 
 

 $("#gridMrchInfo").jqxGrid(
 {
	 theme: "energyblue",
     width: 1200,
     //height: 150,
     source: dataAdapter,
     pageable: true,
     pagesize: 5,
     selectionmode: 'singlecell',
     sortable:true,
     
     autoheight: true,
     columns: [
               { text: 'Owner Id', datafield: 'ownerId'},
               { text: 'appPin', datafield: 'appPin' },
               { text: 'First Name', datafield: 'firstName'},
               { text: 'Last Name', datafield: 'lastName'},
               { text: 'Title', datafield: 'title'},
               { text: 'Percent Owner', datafield: 'percentOwner' },
               { text: 'Address', datafield: 'address' },
               { text: 'Phone No.', datafield: 'phone'},
               { text: 'Fax', datafield: 'fax' },
               { text: 'Bank Name', datafield: 'bankName'},
               { text: 'Bank Address', datafield: 'bankAddress'},
               { text: 'Bank Phone No.', datafield: 'bankPhone'},
               { text: 'Bank A/C Type', datafield: 'bankACType' },
               { text: 'Bank A/C Holder', datafield: 'bankACHOlder' },
               { text: 'Bank A/C No', datafield: 'bankACNo'},
               { text: 'Transit No', datafield: 'transitNo'},
               { text: 'SSN', datafield: 'ssn'}

     ]
 });

 ko.applyBindings(model);
 
 }    	   
 
 //--------------------------payment method---------------------------------------------//
 
 function PaymentMthod(columnData_pin,merchantPaymentMethod) 
 {
 
	 $.ajax ({
		
 	
		url : merchantPaymentMethod,
		type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
		cache:false,
		
		data : JSON.stringify({applicationPin:columnData_pin}),
		 
		success: function (data) {
			
			
			
			var PmtMethodDesc=[];
			var PmtMethodType=[];
			$("#PaymentMthod").html("");
			$("#PaymentMthod").append('<label><font color="blue">DESCRIPTION &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></label>');
			$("#PaymentMthod").append('<label><font color="blue">TYPE &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></label><br/><br/>');
			for(i in data){
				
				$("#PaymentMthod").append('<label id="lbl_'+i+'">'+data[i].pmtMethodDesc +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>');
				$("#PaymentMthod").append('<label id="lbl2_'+i+'">'+data[i].pmtMethodType +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><br/><br/>');
			//	PmtMethodDesc=data[i].pmtMethodDesc;
			//	PmtMethodType=data[i].pmtMethodType;
			
				
			}
		
		},
		error : function(jqXHR, textStatus, errorThrown) {
			(errorThrown);
			alert(errorThrown);

		}
 
		}); 
	
	 
 }

 //----------------------------------------Credit card type---------------------------------------------------------------//
 
 function CreditCardType(columnData_pin,merchantCreditCardType) 
 {
 
	 $.ajax ({
		
 	
		url : merchantCreditCardType,
		type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
		cache:false,
		
		data : JSON.stringify({appPIN:columnData_pin}),
		 
		success: function (data) {
			//alert(data);
			
			var CcTypeId=[];
			var CcTypeDesc=[];
			$("#CreditCardType").html("");
			$("#CreditCardType").append('<label><font color="blue">CREDIT CARD ID &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></label>');
			$("#CreditCardType").append('<label><font color="blue">DESCRIPTION &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font></label><br/><br/>');
			for(i in data){
				
				$("#CreditCardType").append('<label id="lblCC_'+i+'">'+data[i].ccTypeId +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>');
				$("#CreditCardType").append('<label id="lblCC2_'+i+'">'+data[i].ccTypeDesc +'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><br/><br/>');
				
			
				
			}
	
		},
		error : function(jqXHR, textStatus, errorThrown) {
			(errorThrown);
			alert(errorThrown);

		}
	 });
 }
 
 //-------------------------------------------------strore info--------------------------------------------------------------------------------------//
 
 
 function StoreInfo(columnData_pin,merchantstoreInfo) 
 {
	 $("#gridStroreInfo").html("");
	
	     var data_val;

	     var GridModel = function () {
	         this.items = ko.observableArray();
	         var me = this;
	         
	         
	         $.ajax({
	             datatype: 'json',
	             url: merchantstoreInfo,
	             type: 'POST',
	     		//datatype : "json",
	     		contentType: "application/json",
	     		async:false,
	     		cache:false,
	             data : JSON.stringify({appPin:columnData_pin}),
	             
	         }).done(function (data) {
	         	data_val=data;
	         	 var jsonData =data;
	             //var jsonData = $.parseJSON(data);
	             me.items(jsonData);
	            
	         });
	     };

	     var model = new GridModel();

	     // prepare the data
	     var source =
	     {
	         datatype: "observablearray",
	         datafields: [
	             { name: 'store_id'},
	             { name: 'storeName'},
	             { name: 'location_Address'},
	             { name: 'location_City'},
	             { name: 'location_State_Id' },
	             { name: 'state'},
	             { name: 'country_Id'},
	             { name: 'countryName'},
	             { name: 'shortName'},
	             { name: 'location_Zip'},
	             { name: 'appPin'}
	            
	             
	         ],
	         id: 'id',
	         localdata: model.items
	     };

	     var cellsrenderer = function (row, columnfield, value, defaulthtml, columnproperties, rowdata) {
	         
	         return '<span style="margin: 4px; float: ' + columnproperties.cellsalign + '; color: #0000FF;">' + value + '</span>';
	   
	 }
	     
	     var dataAdapter = new $.jqx.dataAdapter(source);
	     
	     

	     $("#gridStroreInfo").jqxGrid(
	     {
	    	 theme: "energyblue",
	         width: 1200,
	         height: 150,
	         source: dataAdapter,
	         pageable: true,
	         pagesize: 5,
	         selectionmode: 'singlecell',
	         sortable:true,
	         
	         autoheight: true,
	         columns: [
	                   { text: 'store_id', datafield: 'store_id',cellclassname: 'largeFont', cellsrenderer: cellsrenderer},
	                   { text: 'Store Name', datafield: 'storeName' },
	                   { text: 'Location Address', datafield: 'location_Address'},
	                   { text: 'Location City', datafield: 'location_City'},
	                   { text: 'Location State Id', datafield: 'location_State_Id'},
	                   { text: 'State', datafield: 'state' },
	                   { text: 'Country Id', datafield: 'country_Id'},
	                   { text: 'Country Name', datafield: 'countryName'},
	                   { text: 'Short Name', datafield: 'shortName'},
	                   { text: 'Location Zip', datafield: 'location_Zip' },
	                   { text: 'Application pin', datafield: 'appPin'}
	                  
	                  

	         ]
	     });

	  
	     
	     $("#gridStroreInfo").on('cellclick', function (event) {
	         var datafield = event.args.datafield;
	         var value = event.args.value;
	         
	         var row = event.args.rowindex;
	         var columntext = $("#gridStroreInfo").jqxGrid('getcolumn', event.args.datafield).text;
	       
	       //  var date = data_val[row].date;
	         var status = columntext;
	         var columnData_storeid=  args.value;
	         
	     
	         
	         if(columntext != "store_id"){
	         	alert("wrong selection");
	        	// alert("succces");
	         	
	         }
	         else {
	        	// alert("success");
	        	
	        	// $("#SubgridStroreInfo").html("");
	        	 $("#SubgridStroreInfo").show();    
	        	 TerminalInfo(columnData_storeid,merchantterminalInfo);
	        	      	 
	        	 
	         	//alert("success");
	         }
	     });
		 

 }
 
 
 
 //---------------------terminal info---------------------------------------------//
 
 function TerminalInfo(columnData_storeid,merchantterminalInfo) 
 {
 
	
	 
	 var data_val;

     var GridModel = function () {
         this.items = ko.observableArray();
         var me = this;
         
         
         $.ajax({
             datatype: 'json',
             url: merchantterminalInfo,
             type: 'POST',
     		
     		contentType: "application/json",
     		async:false,
     		cache:false,
             data : JSON.stringify({store_Id:columnData_storeid}),
             
         }).done(function (data) {
         	data_val=data;
         	 var jsonData =data;
            
             me.items(jsonData);
            
         });
     };

     var model = new GridModel();

     
     var source =
     {
         datatype: "observablearray",
         datafields: [
             { name: 'terminalID'},
             { name: 'terminal_Desc'},
             { name: 'terminal_Type'},
             { name: 'manufacturing_Date'},
             { name: 'store_Id' },
             { name: 'warrenty_Start'},
             { name: 'warrenty_End'}
            
             
         ],
         id: 'id',
         localdata: model.items
     };

     var dataAdapter = new $.jqx.dataAdapter(source);
     
     

     $("#SubgridStroreInfo").jqxGrid(
     {
    	 theme: "energyblue",
         width: 1200,
         height: 150,
         source: dataAdapter,
         pageable: true,
         pagesize: 5,
         selectionmode: 'singlecell',
         sortable:true,
         
         autoheight: true,
         columns: [
                   { text: 'Terminal ID', datafield: 'terminalID'},
                   { text: 'Terminal Description', datafield: 'terminal_Desc' },
                   { text: 'Terminal Type', datafield: 'terminal_Type'},
                   { text: 'Manufacturing Date', datafield: 'manufacturing_Date'},
                   { text: 'Store Id', datafield: 'store_Id'},
                   { text: 'Warranty Start Date', datafield: 'warrenty_Start' },
                   { text: 'Warranty End Date', datafield: 'warrenty_End'}
          
         ]
     });

     ko.applyBindings(model);
	 
	 
	 
 }


 