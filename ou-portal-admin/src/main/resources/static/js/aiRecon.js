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
	                    endDate: "current"
					});

					/*
					 * $('#agentDtlList').DataTable( { "lengthMenu": [[10, 25,
					 * 50, -1], [10, 25, 50, "All"]] } );
					 */

					$('#agent-view-buttons , #datagrid').hide();

					$('#data-table-close').click(function() {
						$('#datagrid').hide();
					});

					/**
					 * **************************Agent Reconciliation
					 * Start******************************
					 */
					$('#divAgentReconSummary').hide();
					$('#divAgentRecon').hide();
					oTableReconSummary = $('#agentReconSummary').dataTable({
						dom : 'Bfrtip',
						buttons: {
					        buttons: [
					            { extend: 'csv', className: 'btn btn-default'},
					            { extend: 'excel', className: 'btn btn-default' }
					        ]
					    },
						"aaSorting" : [ [ 2, "asc" ] ],
						"paging" : true,
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
						}
					});

					jQuery('.dataTable').wrap(
							'<div class="dataTables_scroll"/>');

					oTableRecon = $('#agentRecon').dataTable({
						dom : 'Bfrtip',
						buttons: {
					        buttons: [
					            { extend: 'csv', className: 'btn btn-default'},
					            { extend: 'excel', className: 'btn btn-default' }
					        ]
					    },
						"aaSorting" : [ [ 2, "asc" ] ],
						"paging" : true,
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
						}
					});

					jQuery('.dataTable').wrap(
							'<div class="dataTables_scroll"/>');

					/**
					 * **************************Agent Reconciliation
					 * End******************************
					 */
					
					$('.dataTables_filter input').attr("placeholder", "Enter to Search");
					$('.dataTables_filter input').attr("maxlength",15);
				});

function agentRecon() {
	var stDate = $("#datepickerEffctvFrom").val();
	var endDate = $("#datepickerEffctvTo").val();
	var inData = JSON.stringify({
		'startDt' : stDate,
		'endDt' : endDate
	});
	// alert(inData);
	var tbl = $('#agentReconSummary').serialize();
	$
			.ajax({
				url : "/portal/agentInstitute/agentReconReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					$('#divAgentReconSummary').show();
					var arr = data.result;
					if (arr != null && arr.length > 0) {
						loadDataTableRecon(arr);
					}
					if (arr.length == 0) {
						var html = '<tr>';
						html += '<td colspan="6" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				},
				error : function(err) {
					$('.alert').removeClass('alert-success');
					$('.alert').addClass('alert-danger');
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
}

function loadDataTableRecon(arr) {
	oTableReconSummary.fnClearTable();// clear the DataTable
	oTableReconSummary.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var notInOUCount = 0;
						var notInCUCount = 0;
						var mismatchedCount = 0;
						var pendingCount = 0;
						var broughtForwardCount = 0;
						var strType = '';

						if (item.notInOUCount > 0) {
							strType = 'notInOU';
							notInOUCount = "<button class=\"msgCheckBox\"  name=\"notInOUCount\" id=\"notInOUCount\" value=\""
									+ item.notInOUCount
									+ "\" >"
									+ item.notInOUCount + "</button>";
						} else {
							notInOUCount = item.notInOUCount;
						}

						if (item.notInCUCount > 0) {
							strType = 'notInCU';
							notInCUCount = "<button class=\"msgCheckBox\"  name=\"notInCUCount\" id=\"notInCUCount\" value=\""
									+ item.notInCUCount
									+ "\" >"
									+ item.notInCUCount + "</button>";
						} else {
							notInCUCount = item.notInCUCount;
						}

						if (item.mismatchedCount > 0) {
							strType = 'mismatched';
							mismatchedCount = "<button class=\"msgCheckBox\"  name=\"mismatchedCount\" id=\"mismatchedCount\" value=\""
									+ item.mismatchedCount
									+ "\" >"
									+ item.mismatchedCount + "</button>";
						} else {
							mismatchedCount = item.mismatchedCount;
						}

						if (item.pendingCount > 0) {
							strType = 'pending';
							pendingCount = "<button class=\"msgCheckBox\"  name=\"pendingCount\" id=\"pendingCount\" value=\""
									+ item.pendingCount
									+ "\" >"
									+ item.pendingCount + "</button>";
						} else {
							pendingCount = item.pendingCount;
						}

						if (item.broughtForwardCount > 0) {
							strType = 'broughtForward';
							broughtForwardCount = "<button class=\"msgCheckBox\"  name=\"broughtForwardCount\" id=\"broughtForwardCount\" value=\""
									+ item.broughtForwardCount
									+ "\" >"
									+ item.broughtForwardCount + "</button>";
						} else {
							broughtForwardCount = item.broughtForwardCount;
						}

						oTableReconSummary.fnAddData([ item.reconId,
								notInOUCount, notInCUCount, mismatchedCount,
								pendingCount, broughtForwardCount ]);
						$('.msgCheckBox').click(function() {
							agentReconDtl(strType, item.reconId);
						})

					});
	oTableReconSummary.fnDraw(false);
}

function agentReconDtl(str, reconId) {

	var stDate = $("#datepickerEffctvFrom").val();
	var endDate = $("#datepickerEffctvTo").val();
	var inData = JSON.stringify({
		'startDt' : stDate,
		'endDt' : endDate
	});

	var tbl = $('#agentRecon').serialize();
	$
			.ajax({
				url : "/portal/agentInstitute/agentReconReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					var arr = data.result;
					if (arr != null && arr.length > 0) {
						$('#divAgentRecon').show();
						$
								.each(
										arr,
										function(key, item) {
											console
													.log(item.notInOuLists.length);
											if (item.reconId == reconId) {
												if (str == "notInOU") {
													loadDataTableReconDtl(item.notInOuLists);
												} else if (str == "notInCu") {
													loadDataTableReconDtl(item.notInCuLists);
												} else if (str == "mismatched") {
													loadDataTableReconDtl(item.mismatchedLists);
												} else if (str == "pending") {
													loadDataTableReconDtl(item.pendingLists);
												} else if (str == "broughtForward") {
													loadDataTableReconDtl(item.broughtForwardLists);
												}
											}
										});
					}
					if (arr.length == 0) {
						var html = '<tr>';
						html += '<td colspan="5" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				},
				error : function(err) {
					$('.alert').removeClass('alert-success');
					$('.alert').addClass('alert-danger');
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
}

function loadDataTableReconDtl(arr) {
	oTableRecon.fnClearTable();// clear the DataTable
	oTableRecon.fnDraw();
	$.each(arr, function(key, item) {
		oTableRecon.fnAddData([ item.txnRefId, item.agentId, item.billerId,
				item.reconDescription, item.reconStatus ]);

	});
	oTableRecon.fnDraw(false);
}

/**
 * **************************Agent Reconciliation
 * End******************************
 */
