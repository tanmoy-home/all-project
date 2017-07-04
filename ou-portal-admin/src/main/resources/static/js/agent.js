var checkBoxFlag = null;
var checkBoxFlag_pchannel = null;
var fromPage = "HOME";

var sPaymentmode;
var sPaymentchannel;

var oTable;



$(document)
		.ready(
				function() {
					$('.input-daterange').datepicker({
						autoclose : true,
	                    startDate: "current"

					});
					
					/*
					 * $('#agentDtlList').DataTable( { "lengthMenu": [[10, 25,
					 * 50, -1], [10, 25, 50, "All"]] } );
					 */
				
					
					$('#agent-view-buttons , #datagrid').hide();

					$('#data-table-close').click(function() {
						$('#datagrid').hide();
					});

					/*
					 * $("#agentMobile").keypress(function(event) { return
					 * /\d/.test(String.fromCharCode(event.keyCode)); });
					 */

					$('#agentGeoCode, #agentMobile')
							.bind(
									'keypress',
									function(e) {
										return (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) ? false
												: true;
									});
					$("#agentGeoCode").focusin(
							function(evt) {
								$(this).keypress(
										function() {
											content = $(this).val();
											content1 = content.replace(/\./g,
													'');
											length = content1.length;
											if (length === 2) {
												$('#agentGeoCode').val(
														$('#agentGeoCode')
																.val()
																+ '.');
											}
										});
							});

				

					
					/**
					 * **************************Agent Scheme
					 * Start******************************
					 */
					oTableScheme = $('#agentSchemeLists').dataTable({
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
						}
					});

					jQuery('.dataTable').wrap(
							'<div class="dataTables_scroll"/>');

					/**
					 * **************************Agent Scheme
					 * End******************************
					 */
					
					$('#agentState')
							.change(
									function() {
										
										var state = $(
												"#agentState option:selected")
												.val();

										$
												.ajax({
													type : 'POST',
													dataType : 'json',
													url : "/portal/agentInstitute/cityList/"
															+ state,
													success : function(data) {
														$("#city").empty();
														$("#city")
																.append(
																		$(
																				"<option></option>")
																				.val(
																						0)
																				.html(
																						"--Select City--"));
														$
																.each(
																		data.result.cities,
																		function(
																				key,
																				value) {
																			$(
																					"#city")
																					.append(
																							$(
																									"<option></option>")
																									.val(
																											value.cityId)
																									.html(
																											value.cityName));
																		});
													},
													error: function(err){
														$('.alert').removeClass('alert-success');
														$('.alert').addClass('alert-danger');
														$('.alert').html('Error');
														$('.alert').show();
													}
												});

									});

					$('#city')
							.change(
									function() {
										var city = $("#city option:selected")
												.val();
										$
												.ajax({
													type : 'POST',
													dataType : 'json',
													url : "/portal/agentInstitute/postalList/"
															+ city,
													success : function(data) {
														$('#pincode').empty();
														$('#pincode')
																.append(
																		$(
																				"<option></option>")
																				.val(
																						0)
																				.html(
																						"--Select PinCode--"));
														$
																.each(
																		data.result.postals,
																		function(
																				key,
																				value) {
																			$(
																					'#pincode')
																					.append(
																							$(
																									"<option></option>")
																									.val(
																											value.postalCode)
																									.html(
																											value.postalCode));
																		});
													},
													error: function(err){
														$('.alert').removeClass('alert-success');
														$('.alert').addClass('alert-danger');
														$('.alert').html('Error');
														$('.alert').show();
													}
												});
									});
					
					// Data Table serach placeholder text dataTables_filter
					$('.dataTables_filter input').attr("placeholder", "Enter to Search");
					$('.dataTables_filter input').attr("maxlength",15);
				});



function agentApprove(str) {
	
	var agentID = $('#agentId').val();

	/*
	 * var agentID = ''; if ($('input[name="agentSelected"]:checked').val()) {
	 * $('input[name="agentSelected"]:checked').each(function() { var $row =
	 * $(this).parents('tr'); agentID = ($row.find('td:eq(1)').html());
	 */
			$.ajax({
				url : "/portal/agentInstitute/agentApprove/" + agentID,
				type : 'POST',
				success : function(data) {			
					if (data.result == 'SUCCESS') {
						$('.alert').css('display','block');
						$('.alert').addClass('alert-success');						
						$('.alert').html(agentID+" is approved.");
						if(str === 'AI'){
							setInterval(function(){ window.location.replace("/portal/agentInstitute/agentView"); }, 3000);
						}else{
							setInterval(function(){ window.location.replace("/portal/COUAdmin/agentViewCOU"); }, 3000);
						}
					}
				},
				error: function(err){
					$('.alert').removeClass('alert-success');
					$('.alert').addClass('alert-danger');
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
		/*
		 * }); }
		 */
}

function agentReject(str) {
	var agentID = $('#agentId').val();
	var comment = $('#rejectComment').val();
	/*
	 * if ($('input[name="agentSelected"]:checked').val()) {
	 * $('input[name="agentSelected"]:checked').each(function() { var $row =
	 * $(this).parents('tr'); agentID = ($row.find('td:eq(1)').html());
	 */
			// alert(agentID);
	if(comment == ''){
		$('.cmtMsg').html('Please enter Rejection Reason');
		return false;
	}
			$.ajax({
				url : "/portal/agentInstitute/agentReject/" + agentID + "/" + comment,
				type : 'POST',
				success : function(data) {
					if (data.result == 'SUCCESS') {
						$('.alert').css('display','block');
						$('.alert').addClass('alert-danger');
						$('.alert').text(agentID+" is rejected.");
						
						if(str === 'AI'){							
							setInterval(function(){ window.location.replace("/portal/agentInstitute/agentView"); }, 3000);
						}else{
							setInterval(function(){ window.location.replace("/portal/COUAdmin//agentViewCOU"); }, 3000);
						}
					}
				},
				error: function(err){
					$('.alert').removeClass('alert-success');
					$('.alert').addClass('alert-danger');
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
		/*
		 * }); }
		 */
}

function removeRequired() {
	$("#agentMobile").removeAttr('required');
	$("#businessType").removeAttr('required');
	$("#adr1").removeAttr('required');

	$("#state").removeAttr('required');
	$("#pincode").removeAttr('required');

	$("#paymentMode").removeAttr('required');
	$("#paymentChannel").removeAttr('required');
	return true;
}
/**
 * **************************Agent Scheme List
 * Start******************************
 */
function ViewScheme() {
	$('#datagrid').show();
	var tbl = $('#agentSchemeList').serialize();
	$
			.ajax({
				url : "/portal/agentInstitute/agentScheme",
				type : "POST",
				success : function(data) {
					var arr = data.result;
					if (arr != null && arr.length > 0) {
						loadDataTableScheme(arr);
					}
					if (arr.length == 0) {
						var html = '<tr>';
						html += '<td colspan="8" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				},
				error: function(err){
					$('.alert').removeClass('alert-success');
					$('.alert').addClass('alert-danger');
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
}

function loadDataTableScheme(arr) {
	oTableScheme.fnClearTable();// clear the DataTable
	oTableScheme.fnDraw();

	$.each(arr, function(key, item) {
		oTableScheme.fnAddData([
				item.schemeName,
				item.schemeDesc,
				item.schemeEffctvFrom,
				item.schemeEffctvTo,
				item.commissionDetails[0].agentPercentFee,
				item.commissionDetails[0].agentFlatFee,
				item.commissionDetails[0].aiPercentFee,
				item.commissionDetails[0].tranAmtRangeMin + '-'
						+ item.commissionDetails[0].tranAmtRangeMax ]);
	});
	oTableScheme.fnDraw(false);
}

function getDateFormate() {

}
/**
 * **************************Agent Scheme List End******************************
 */


