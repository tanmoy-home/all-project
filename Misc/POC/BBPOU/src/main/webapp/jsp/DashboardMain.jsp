<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DashboardMain</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<!-- <script src="https://code.jquery.com/ui/1.8.14/jquery-ui.min.js"></script> -->
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>


<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/redmond/jquery-ui.css"  type="text/css" />
<link rel="stylesheet"  href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/customed.css" type="text/css" />

<!------------------------------ jqx widget ------------------------------------->
<link rel="stylesheet" href="resources/css/jqx.base.css" type="text/css" />
<link rel="stylesheet" href="resources/css/jqx.shinyblack.css" type="text/css" />
<link rel="stylesheet" href="resources/css/jqx.energyblue.css"  type="text/css" />
<link rel="stylesheet" href="resources/css/jqx.ui-redmond.css" type="text/css" />


<script type="text/javascript" src="resources/js/External/knockout-3.0.0.js"></script>
<script type="text/javascript" src="resources/js/External/jqxcore.js"></script>
<script type="text/javascript" src="resources/js/External/jqxdata.js"></script>
<script type="text/javascript" src="resources/js/External/jqxbuttons.js"></script>
<script type="text/javascript" src="resources/js/External/jqxscrollbar.js"></script>
<script type="text/javascript" src="resources/js/External/jqxmenu.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.selection.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.edit.js"></script>
<script type="text/javascript" src="resources/js/External/jqxknockout.js"></script>
<script type="text/javascript" src="resources/js/External/jqxcheckbox.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.pager.js"></script>
<script type="text/javascript" src="resources/js/External/jqxlistbox.js"></script>
<script type="text/javascript" src="resources/js/External/jqxdropdownlist.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.sort.js"></script>

<script type="text/javascript" src="resources/js/External/jqxdraw.js"></script>
<script type="text/javascript" src="resources/js/External/jqxchart.core.js"></script>
<script type="text/javascript" src="resources/js/External/json2.js"></script>

<script type="text/javascript" src="resources/js/External/demos.js"></script>
<script type="text/javascript" src="resources/js/External/jqxtabs.js"></script>
<script type="text/javascript" src="resources/js/External/jqxradiobutton.js"></script>
<script type="text/javascript" src="resources/js/External/jqxdata.export.js"></script>
<script type="text/javascript" src="resources/js/External/jqxgrid.export.js"></script>

<!-------------------------------- customed js ------------------------------------------>

<script type="text/javascript" src="resources/js/Customed/utility.js"></script>
<script type="text/javascript" src="resources/js/Customed/draw_mbaQtr_lineGraph.js"></script>
<script type="text/javascript" src="resources/js/Customed/draw_profitability_barGraph.js"></script>





<script type="text/javascript">

var chartId = [];
var chartName = [];
var chartType = [];
var chartURI = [];
var chartDesc = [];
var count =0;


var url_mbaQtrGraph = "${pageContext.request.contextPath}/displayGraph" ;
var url_showMerchStatusGrid = "${pageContext.request.contextPath}/displaygraphGrid";
var url_displayMerchInfo = "${pageContext.request.contextPath}/displayMerchInfoGrid" ;
var url_profitabilityGraph = "${pageContext.request.contextPath}/displayBarGraph";
var url_profitabilityGrid = "${pageContext.request.contextPath}/displayprofitabilityGrid" ;
var merchantOwnerInfo= "${pageContext.request.contextPath}/merchInfo";
var merchantDetailURL ="${pageContext.request.contextPath}/merchDetails";
var merchantPaymentMethod= "${pageContext.request.contextPath}/PmtMthod";
var merchantCreditCardType= "${pageContext.request.contextPath}/CCType";
var merchantstoreInfo =  "${pageContext.request.contextPath}/storeInfo";
var merchantterminalInfo = "${pageContext.request.contextPath}/TerminnalInfo";





function displayPane(indexNo) {
	$('#'+chartId[indexNo]).toggle();
	if($('#chk_' + chartId[indexNo]).is(":checked")) {
		
		if(chartId[indexNo]==2)
		{
			 draw_mbaQtr_lineGraph(chartId[indexNo], chartName[indexNo], chartType[indexNo], chartURI[indexNo],url_mbaQtrGraph);
		}
		else if(chartId[indexNo]==3) {
			draw_profitability_barGraph(chartId[indexNo], chartName[indexNo], chartType[indexNo], chartURI[indexNo],url_profitabilityGraph);
		}
	}
}




$(document).ready(function(){
	
	$("#btnsave").hide();
	
	var reportcount=0;
	var user_name = '<%= session.getAttribute("UserName") %>';
	var RoleID = '<%= session.getAttribute("RoleID") %>';
	var userId = '<%= session.getAttribute("user_id") %>';
	var ChxData=[];
	var UserData=[];
	var ChxRoleData=[];
    var ChxUserData=[];

	
	$("#btnsave").click(function(){
		var User = function(){
			this.user_id = "";
		};
		var Report = function(){
			this.reportId = "";
		};
		var usrReport = function() {
			this.user = new User();
			this.reports = new Array();
		};
		
		var open = new Array();
		var user = {};
		var reports = [];
		user['user_id']=parseInt(userId);
		open.push(user);
		var usrRpt = new usrReport();
		usrRpt.user = user;
		
		for(y in ChxRoleData ) {
		if($('#chk_' + ChxRoleData[y]).is(":checked")) {
			count ++;
			//alert(ChxRoleData[y]);
			 var rpt={};
			rpt["reportId"]=parseInt(ChxRoleData[y]);
			reports.push(rpt); 
			var report = new Report();
			report.reportId = parseInt(ChxRoleData[y]);
			usrRpt.reports.push(report);
		}
		
		}
		
		
		$.ajax ({
			
		url :  "${pageContext.request.contextPath}/saveReport",
		type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
		cache:false,
		//data : JSON.stringify({role_id :RoleID}),
		//data : JSON.stringify(report_value),
		data : JSON.stringify(usrRpt),
		 
		success: function (data) {
			
			alert(data[0].desc);
			
		},
		error: function(jqXHR, textStatus, errorThrown) {
			(errorThrown);
		
			}
		
		});
		
		
	});
	
	 //--------------------------------Role based list display-------------------------------------//
	 
	$.ajax ({
		
	//	url  : "http://localhost:8080/Customer360Interface/displayRolePanel",
		url :  "${pageContext.request.contextPath}/displayRolePanel",
		type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
	
	//	data: JSON.stringify({role_id:1}),
		data : JSON.stringify({role_id :RoleID}),
		
		cache:false, 
		success: function (data) {

		      for (i in data)
			   {
		    	  reportcount++;
		    	  ChxData[i]=data[i].reportName;
		    	  ChxRoleData[i]=data[i].reportId;
		    	  
		    	  chartId[i] = data[i].reportId;
		    	  chartName[i] = data[i].reportName;
		    	  chartType[i] = data[i].chartType;
		    	  chartURI[i] = data[i].chartURI;
		    	  chartDesc[i] = data[i].chartDesc;
			  
			  }
		
		    
		},
		error: function(jqXHR, textStatus, errorThrown) {
			(errorThrown);
		
			} 
			
		
	});
	
	//----------------------------------------left panel User list display---------------------------------------//
	
$.ajax ({
		
		// url  : "http://localhost:8080/Customer360Interface/displayUserPanel",
		url : "${pageContext.request.contextPath}/displayUserPanel",
		type: 'POST',
		datatype : "json",
		contentType: "application/json",
		async:false,
		data: JSON.stringify({username:user_name}),
		cache:false, 
		success: function (data) {
			
			/* alert(data);
			//alert(user_name);` */
			var count=0;
			 for (i in data)
			   { 
				 count++;
		    	  ChxUserData[i]=data[i].reportName;
		    	  UserData[i]=data[i].reportId;
		    	  
			  }
			 //alert(count);
			if(count==0) {
			//alert(count);
			
			 for (j in ChxData ) {
		    	  var checkBtnBox =  $('<label class = "lblcheckbox"><input type="checkbox" name="chx" id="chk_'+chartId[j]+'" onclick="displayPane('+j+')" />'+ ChxData[j]+'</label>'+'</br>' );
		    	  checkBtnBox.appendTo('#target');
		    	  
		    	//  $("#chbox"+j+"").prop("checked", true);
		    	  
		      }
		      $("input[type='checkbox']").attr("checked",true);
			}
			
			
			
			 else{
				 var lenRole= ChxRoleData.length;
				 var lenUser= UserData.length;
				 var totalarr=[];
				 
				 var lengthtotal;
				 
				 if (lenRole<lenUser) {
					 lengthtotal=lenRole;
				 }
				 
				 
				 lengthtotal= lenUser;
				 UserData.sort(function(a, b){return a-b});
				 ChxRoleData.sort(function(a, b){return a-b});
				 for(k=0 ; k<=lengthtotal ; k++){
					 
					 if(UserData[k]==ChxRoleData[k]) {
						 totalarr=UserData[k];
					 }
					 
				 }
				 
				 for (j in ChxData ) {
			    	  var checkBtnBox =  $('<label><input type="checkbox" name="chx" id="chk_'+ChxRoleData[j]+'" onclick="displayPane('+ j + ');"/>'+ ChxData[j]+'</label>'+'</br>' );
			    	  checkBtnBox.appendTo('#target');
			    	  
			      }
				
				 
				 for (w in UserData)
					 {
					 if(ChxRoleData.indexOf(UserData[w])!=-1) {
						 $("#chk_"+UserData[w]).prop("checked",true);
					 }
					 }
				
			} 
			
			var container="";
	 		for (x=0 ;x<reportcount;x++){
	 			container += '<div id='+ChxRoleData[x]+' class="col-sm-4" ><div class="panel panel-info" id="pnl_'+ChxRoleData[x]+'"><div class="panel-heading" >'+ChxData[x]+' </div><div class="panel-body"><div id="jqx_'+ChxRoleData[x]+'"   style="width: 100%; height: 300px; position: relative; left: 0px; top: 0px;" ></div> </div> </div></div></div>';
	 			
	 		}
	 		$("#showGraph").html(container);
	 		for (z=0 ;z<reportcount;z++){
	 			
	 			$('#'+ChxRoleData[z]).toggle();
				if($('#chk_' + chartId[z]).is(":checked")) {
					displayPane(z);
				}
	 		}
		}
		
});

	
	/* <--------------------------------zoom in -out-------------------------------------------> */
	
/* var currZoom = $('#'+ChxRoleData[x]).css("zoom");
if (currZoom == 'normal') currZoom = 1;

$('#zoomin_'+ChxRoleData[x]).click(function(event) 
		{			
	currZoom = 1;
  	currZoom *= 1.5;
   $('#'+ChxRoleData[x]).css("zoom", currZoom);
   $('#'+ChxRoleData[x]).css("-moz-transform", "Scale(" + currZoom + ")");
   $('#'+ChxRoleData[x]).css("-moz-transform-origin", "0 0");

       });


$('#zoomout_'+ChxRoleData[x]).click(function() 
		{
	
     $('#'+ChxRoleData[x]).css("zoom", 1);
     $('#'+ChxRoleData[x]).css("-moz-transform", "Scale(" + currZoom + ")");
     $('#'+ChxRoleData[x]).css("-moz-transform-origin", "0 0");

   }); */
   


   
//------------------------------------------check box check for 1 report---------------------------------------------//   
  var l,r, checkboxcount=0 , chkcount=0;
   
 $('input[type=checkbox]').each(function () {
   if (this.checked) {
   	checkboxcount++;
   }
   chkcount =checkboxcount;
}); 
 
  
$('input[type="checkbox"]').click(function(event){
	if($(this).prop("checked") == true || $(this).prop("checked") == false ){
	   	  $("#btnsave").show();
	    }
  
   for(r=reportcount;r>=0;r--) {
	  	 
 		if($('#chk_' + ChxRoleData[r-1]).is(":checked") == true) {
 			checkboxcount--;
    
 		 	if (checkboxcount==1){  
 	      	  $('#chk_' + ChxRoleData[r-1]).attr("disabled", true);     	
 	      		l = ('#chk_' + ChxRoleData[r-1]);
  	      		break;
 		 	} 
 		 	
 		}
	   
 		 else {
 			 $(l).attr("disabled", false);
 			checkboxcount=chkcount;
 		 }  
 		
     }
});
  


}); //end of document.ready(function)

//-----------------------------------------------*********--------------------------------------------------//


function toggleChevron(e) {
    $(e.target)
        .prev('.panel-heading')
        .find("i.indicator")
        .toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
    
    $('#accordion').on('hidden.bs.collapse', toggleChevron);
  $('#accordion').on('shown.bs.collapse', toggleChevron);
}


        
 //--------------------------------------****************-----------------------------------------------------//
        
/* $(document).ajaxStart($.blockUI({ message: '<img src="resources/images/gif-load.gif" />',
    css: { width: '30%', border:'0px solid #FFFFFF',cursor:'progress',backgroundColor:'#FFFFFF'},
    overlayCSS:  { backgroundColor: '#FFFFFF',opacity:0.0,cursor:'progress'}                                     
  })).ajaxStop($.unblockUI);  */

  
  
  
  
  
  
  
</script>

</head>
<body bgcolor="#E6E6FA">

	<div class="myhome">

		<div class="row">
			<div class="col-lg-12">

				<h4 class="page-header bg-primary img-rounded  custom-header">
					<b>&nbsp;&nbsp; Customer360 Dashboard</b>
				</h4>

			</div>
		</div>



		<div class="row">

			<div class="col-sm-3">

				<div class="panel panel-primary">
					<div class="panel-heading">
						<b>Preferences</b>
					</div>
					<div class="panel-body">
						<div id="target"></div>
						<!-- <input type="button" class="btn btn-default" value="Edit" id="Edit">-->
						<input type="submit" class="btn btn-default" id="btnsave"
							value="Save" id="Save">
					</div>
				</div>

			</div>

			<!--             <div id='jqxChart' style="width: 400px; height: 400px; position: relative; left: 0px; top: 0px;"> -->
			<!-- </div> -->


			<div class="col-sm-8">

				<div class="container" id="showGraph"></div>

				<!-- <modal start> -->
				<div class="modal fade" id="myModal" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">Merchant Status</h4>
							</div>
							<div class="modal-body" id="modal-body"></div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
							</div>
						</div>

					</div>
				</div>
				<!-- <modal end> -->

				<!-- < modal start> -->

				<div class="modal fade" id="mrchDetails" role="dialog">
					<div class="modal-dialog modal-lg">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">Detailed Merchant Information</h4>
							</div>
							<div class="modal-body">

								<!-- -label before accordian start -->

								<div id='jqxWidget'>
									<div style='float: left;' id='jqxTabs'>
										<ul style="margin-left: 30px;">
											<li>General Information</li>
											<li>Progress Details</li>
											<li>Validation</li>
											<li>Identity</li>

										</ul>
										<br /> 
										<div class="container-fluid" id="tab1">
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
													&nbsp;&nbsp;	<label for="appPin" >Application PIN:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="appPinData" id="appPin"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="MerchantName">Merchant Name:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="MerchantNameData" id="merchantName"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
													&nbsp;&nbsp;	<label for="MerchantGroup">Merchant Group:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="MerchantGroupData" id="merchantGroup"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="MCC">MCC:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="MCCdata" id="MCC"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
													&nbsp;&nbsp;	<label for="salesAgent">Sales Agent:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="SalesAgentData" id="salesAgent"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="firstName">First Name:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="FirstNameData" id="firstName"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
													&nbsp;&nbsp;	<label for="LastName">Last Name:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="LastNameData" id="lastName"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="Email">Email:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="EmailData" id="Email"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
												&nbsp;&nbsp;		<label for="phone">Phone No.:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="phoneData" id="phone"></label>

													</div>
												</div>
												
												
											</div>


										</div>
										
										<!-- -2nd tab -->

										<div class="container-fluid" id="tab2">
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="approvalDate">Approval Date</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="approvalDateData" id="approvalDate"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="rejectionDate">Rejection Date:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="rejectionDateData" id="rejectionDate"></label>

													</div>
												</div>

											</div>
											
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														 <label for="locInspectDate">location Inspection Date:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="locInspectDateData" id="locInspectDate"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="initiationDate">Initiation Date:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="initiationDate" id="initiationDate"></label>

													</div>
												</div>

											</div>
											
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														&nbsp;&nbsp; <label for="busOpenDate">Bus Open Date:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="busOpenDateData" id=""busOpenDate></label>

													</div>
												</div>
												

											</div>
											
										</div>
										
										<!-- -tab3 -->

										<div class="container-fluid" id="tab3">
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="isValidGuarantor">Guarantor is Valid?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isValidGuarantorData"><input type="checkbox" value="checkbox1" id="validGuarantor"></label>
                                                         
													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="isValidMerchantBank">Merchant Bank is Valid?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isValidMerchantBankData"><input type="checkbox" value="checkbox2" id=""validMerchantBank ></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														 <label for="isValidOwnerDetail">Owner Detail is Valid?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isValidOwnerDetailData"><input type="checkbox" value="checkbox3" id="validMerchantBank" ></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="isValidSSN">SSN is Valid?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isValidSSNdate"><input type="checkbox" value="checkbox4" id="validSSN" ></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														 <label for="isLocationInspected">Location is Inspected?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isLocationInspectedData"><input type="checkbox" value="checkbox5" id="locationInspected"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="isIDVerified">ID is Verified?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isIDVerifiedData"><input type="checkbox" value="checkbox6" id="idverified"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														 <label for="isCreditCheck">Credit id Checked?</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isCreditCheckdata"><input type="checkbox" value="checkbox7" id="creditCheck"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="isTerroristCheck">Terrorist Check</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="isTerroristCheckData"><input type="checkbox" value="checkbox8" id="terroristCheck"></label>

													</div>
												</div>

											</div>
										</div>

                                    <!-- -tab4 -->
										<div class="container-fluid" id="tab4">
											
										
										
										
										
										
										<div class="container-fluid" id="tab4">
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="TypeofOwnerShip">Type of OwnerShip:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="TypeofOwnerShipdata" id="typeofOwnerShip"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="merchantFederalITaxId">Merchant FederalI TaxId:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="merchantFederalITaxId" id="merchantFederalITaxId"></label>

													</div>
												</div>

											</div>
										
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="merchantSSN">Merchant SSN:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="merchantSSNdata" id="merchantSSN"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="mthsOwnedBusniess">mths Owned Busniess</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="mthsOwnedBusniessData" id="mthsOwnedBusniess"></label>

													</div>
												</div>

											</div>
											
											<div class='row'>
												<!-- rem -->
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="delivery">Delivery:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="deliveryData" id="delivery"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="cardHolderDesc">Card Holder Description</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="cardHolderDesc" id="cardHolderDesc"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="websiteURL">Website URL:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="websiteURLdata" id="websiteURL"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="salesMethod">Sales Method:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="salesMethodData" id="salesMethod"></label>

													</div>
												</div>

											</div>
											
												<!-- rem -->
												
											
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="insLName">ins Last Name:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="insLNameData" id="insLName"></label>

													</div>
												</div>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="status">Status:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="statusData" id="status"></label>

													</div>
												</div>

											</div>
											<div class='row'>
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="Processor">Processor:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="ProcessorData" id="processor"></label>

													</div>
												</div>
												
												<div class='col-sm-2'>
													<div class='form-group'>
														<label for="insFName">ins First Name:</label>

													</div>
												</div>
												<div class='col-sm-3'>
													<div class='form-group'>
														<label for="insFNameData" id="insFName"></label>

													</div>
												</div>
												
												
												
												</div>
											</div>
										</div>

									</div>

								</div>
								<br /> <br /> <br />
								<!-- -label before accordian end -->


								<!-- -accordian start -->
								<br />
								 <br /> 
								 <br />
								<div class="panel-group" id="accordion">

									<div class="panel panel-info">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a class="accordion-toggle" data-toggle="collapse"
													data-parent="#accordion" href="#collapseTwo">
													Owner Information of Merchant </a><i
													class="indicator glyphicon glyphicon-chevron-up  pull-right"></i>
											</h4>
										</div>
										<div id="collapseTwo" class="panel-collapse collapse">
											<div class="panel-body" id="mrchInfo"><div id='jqxWidget'><div id="gridMrchInfo"></div></div>
											</div>
										</div>
									</div>
									<div class="panel panel-info">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a class="accordion-toggle" data-toggle="collapse"
													data-parent="#accordion" href="#collapseThree">
													Payment Method of Merchant</a><i
													class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
											</h4>
										</div>
										<div id="collapseThree" class="panel-collapse collapse">
											<div class="panel-body" id="PaymentMthod"></div>
										</div>
									</div>

									<div class="panel panel-info">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a class="accordion-toggle" data-toggle="collapse"
													data-parent="#accordion" href="#collapseFour">
													Type of Credit Card used by Merchant </a><i
													class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
											</h4>
										</div>
										<div id="collapseFour" class="panel-collapse collapse">
											<div class="panel-body" id="CreditCardType"></div>
										</div>
									</div>

									<div class="panel panel-info">
										<div class="panel-heading">
											<h4 class="panel-title">
												<a class="accordion-toggle" data-toggle="collapse"
													data-parent="#accordion" href="#collapseFive">
													Store Information of Merchant </a><i
													class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
											</h4>
										</div>
										<div id="collapseFive" class="panel-collapse collapse">
											<div class="panel-body" id="StroreInfo"><div id="gridStroreInfo"></div><br/><div id="SubgridStroreInfo"></div></div>
										</div>
									</div>

									


								</div>



								<!-- -accordian end -->

							</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal" id="btnCloseModal">Close</button>
							</div>
						</div>
					</div>
				</div>


				<!-- <modal end> -->
				<!-- <modal for profitability> -->
				<!-- <modal start> -->
				<div class="modal fade" id="modalprftbility" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header" id = "prfmodalheader">
								<!-- <button type="button" class="close" data-dismiss="modal">&times;</button> -->
								<!-- <h4 class="modal-title">Profitability </h4> -->
							</div>
							<div class="modal-body" id="modal-body-prf"></div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
							</div>
						</div>

					</div>
				</div>
				<!-- <modal end> -->
			</div>



		</div>
	</div>




</body>
</html>