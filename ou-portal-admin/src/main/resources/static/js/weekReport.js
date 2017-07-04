var id = 0;
var txnArr;
var complaintArr;
$(document).ready(function() {

	$("#selectedDate").datepicker({
	    orientation: "bottom left",
	    endDate: "current",
	    daysOfWeekDisabled: "0,2,3,4,5,6",
		autoclose:true
	});
	$("#complaintDate").datepicker({
	    orientation: "bottom left",
	    endDate: "current",
	    daysOfWeekDisabled: "0,2,3,4,5,6",
		autoclose:true
	});
	$("#segmentDate").datepicker({
	    orientation: "bottom left",
	    endDate: "current",
	    daysOfWeekDisabled: "0,2,3,4,5,6",
		autoclose:true
	});
	$('#divTxnReport').hide();
	$('#divComplaintReport').hide();
	$('#divSegmentReport').hide();

	$('#exportTable').hide();
	$('#exportcomplaintTable').hide();
	

	oTableTxnReport = $('#txnReportData').dataTable({
						//dom : 'Bfrtip',
						//buttons: ['excel'],
						//"aaSorting" : [ [ 2, "asc" ] ],
						//"orderCellsTop": true,
						//"bSortCellsTop":true,
						"paging" : true,
						"sPaginationType" : "full_numbers",
						"bJQueryUI" : true,
						//"select" : true,
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
	oTableComplaintReport = $('#complaintReportData').dataTable({
		//dom : 'Bfrtip',
		//buttons: ['excel'],
		//"aaSorting" : [ [ 2, "asc" ] ],
		//"orderCellsTop": true,
		//"bSortCellsTop":true,
		"paging" : true,
		"sPaginationType" : "full_numbers",
		"bJQueryUI" : true,
		//"select" : true,
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
	oTableSegmentReport = $('#segmentReportData').dataTable({
		//dom : 'Bfrtip',
		//buttons: ['excel'],
		//"aaSorting" : [ [ 2, "asc" ] ],
		//"orderCellsTop": true,
		//"bSortCellsTop":true,
		"paging" : true,
		"sPaginationType" : "full_numbers",
		"bJQueryUI" : true,
		//"select" : true,
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
})
function exportComplaintReport() {
	var inData = JSON.stringify({
		'bbpouName': complaintArr.bbpouName,
		'onUsoutstandingLastWeekCount': complaintArr.onUsoutstandingLastWeekCount,
		'onUsreceivedThisWeekCount': complaintArr.onUsreceivedThisWeekCount,
		'onUsTot': complaintArr.onUsTot,
		'offUsoutstandingLastWeekCount': complaintArr.offUsoutstandingLastWeekCount,
		'offUsreceivedThisWeekCount': complaintArr.offUsreceivedThisWeekCount,
		'offUsTot': complaintArr.offUsTot,
		'onUsResolvedCount': complaintArr.onUsResolvedCount,
		'offUsResolvedCount': complaintArr.offUsResolvedCount,
		'onUsPendingCount': complaintArr.onUsPendingCount,
		'offUsPendingCount': complaintArr.offUsPendingCount,
		'txnBasedCount' : complaintArr.txnBasedCount,
		'serviceBasedCount': complaintArr.serviceBasedCount
	});
	var form = document.createElement("form");
    form.method = "post";
    form.action = "/portal/weeklyReport/downloadComplaintReport";
    document.body.appendChild(form);
    var input = $("<input>").attr("type", "hidden").attr("name", "inData").val(inData);
    $(form).append($(input));
    $(form).target = "_self"
    $(form).submit();
    form.submit();
    document.body.removeChild(form);
}

function txnReport() {
	
	var selectedDate = $("#selectedDate").val();
	
	var inData = JSON.stringify({
		'selectedDate': selectedDate
	});
 
	var tbl = $('#txnReportData').serialize();
	$
			.ajax({
				url : "/portal/weeklyReport/viewTxnReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					$('#divTxnReport').show();
					$('#exportTable').show();

					txnArr = data.result;
					/*alert(arr.bbpouName);
					if (arr != null && arr.length > 0) {						
						loadDataTableTxnReport(arr);
					}*/
					loadDataTableTxnReport(txnArr);
					if (txnArr == null) {
						var html = '<tr>';
						html += '<td colspan="5" style="text-align:center">No Data Found</td></tr>';
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
function exportTxnReport() {
		
	var inData = JSON.stringify({
		'bbpouName': txnArr.bbpouName,
		'noOfAgentOutlets': txnArr.noOfAgentOutlets,
		'onUsTxnCount': txnArr.onUsTxnCount,
		'offUsTxnCount': txnArr.offUsTxnCount,
		'onUsTxnTot': txnArr.onUsTxnTot,
		'offUsTxnTot': txnArr.offUsTxnTot,
		'onUsFailedTxnCount': txnArr.onUsFailedTxnCount,
		'offUsFailedTxnCount': txnArr.offUsFailedTxnCount,
		'onUsFailedTxnTot': txnArr.onUsFailedTxnTot,
		'offUsFailedTxnTot': txnArr.offUsFailedTxnTot,
		'failureReason': txnArr.failureReason,
		'cashPaymentCount': txnArr.cashPaymentCount,
		'dcccpaymentCount': txnArr.dcccpaymentCount,
		'netBankingPaymentCount': txnArr.netBankingPaymentCount,
		'impspaymentCount': txnArr.impspaymentCount,
		'ppisPaymentCount': txnArr.ppisPaymentCount,
		'otherPaymentCount': txnArr.otherPaymentCount
	});
	var form = document.createElement("form");
    form.method = "post";
    form.action = "/portal/weeklyReport/downloadTxnReport";
    document.body.appendChild(form);
    var input = $("<input>").attr("type", "hidden").attr("name", "inData").val(inData);
    $(form).append($(input));
    $(form).target = "_self"
    $(form).submit();
    form.submit();
    document.body.removeChild(form);
}

function complaintReport() {
	
	var selectedDate = $("#complaintDate").val();
	
	var inData = JSON.stringify({
		'complaintDate': selectedDate
	});
 
	var tbl = $('#complaintReportData').serialize();
	$
			.ajax({
				url : "/portal/weeklyReport/viewComplaintReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					$('#divComplaintReport').show();
					$('#exportcomplaintTable').show();

					complaintArr = data.result;
					/*alert(arr.bbpouName);
					if (arr != null && arr.length > 0) {						
						loadDataTableTxnReport(arr);
					}*/
					loadDataTableComplaintReport(complaintArr);
					if (complaintArr == null) {
						var html = '<tr>';
						html += '<td colspan="5" style="text-align:center">No Data Found</td></tr>';
						$("#complaintListBody").html(html);
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
function segmentReport() {
	
	var selectedDate = $("#segmentDate").val();
	
	var inData = JSON.stringify({
		'segmentDate': selectedDate
	});
 
	var tbl = $('#segmentReportData').serialize();
	$
			.ajax({
				url : "/portal/weeklyReport/viewSegmentReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					$('#divSegmentReport').show();
					//$('#exportTable').show();

					var segmentArr = data.result;
					//alert(arr.bbpouName);
					if (segmentArr != null && segmentArr.length > 0) {						
					loadDataTableSegmentReport(segmentArr);
					}
					if (data.result.length == 0) {
						var html = '<tr>';
						html += '<td colspan="5" style="text-align:center">No Data Found</td></tr>';
						$("#segmentListBody").html(html);
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
function loadDataTableTxnReport(item) {
	oTableTxnReport.fnClearTable();// clear the DataTable
	oTableTxnReport.fnDraw();
	//$.each(arr, function(key, item) {
	
		
		oTableTxnReport.fnAddData([ 1, item.bbpouName,
				item.noOfAgentOutlets, item.onUsTxnCount, item.offUsTxnCount, item.onUsTxnTot, item.offUsTxnTot, item.onUsFailedTxnCount, item.offUsFailedTxnCount, 
				item.onUsFailedTxnTot, item.offUsFailedTxnTot, item.failureReason, item.cashPaymentCount, item.dcccpaymentCount, item.netBankingPaymentCount, item.impspaymentCount, item.ppisPaymentCount, item.otherPaymentCount ]);
		

	//});
	oTableTxnReport.fnDraw(false);
}
function loadDataTableComplaintReport(item) {
	oTableComplaintReport.fnClearTable();// clear the DataTable
	oTableComplaintReport.fnDraw();
	//$.each(arr, function(key, item) {
	
		
	oTableComplaintReport.fnAddData([ 1, item.bbpouName,
				item.onUsoutstandingLastWeekCount, item.onUsreceivedThisWeekCount, item.onUsTot, 
				item.offUsoutstandingLastWeekCount, item.offUsreceivedThisWeekCount, item.offUsTot, item.onUsResolvedCount, item.offUsResolvedCount, item.onUsPendingCount, item.offUsPendingCount, item.txnBasedCount, item.serviceBasedCount ]);
		

	//});
	oTableComplaintReport.fnDraw(false);
}
function loadDataTableSegmentReport(arr) {
	oTableSegmentReport.fnClearTable();// clear the DataTable
	oTableSegmentReport.fnDraw();
	$.each(arr, function(key, item) {
	
	id=id+1;
	oTableSegmentReport.fnAddData([ id, item.bbpouName,
	                               				item.blrCategory, item.blrName, item.onUsCount, item.offUsCount, item.onUsTot, item.offUsTot]);
		

	});
	oTableSegmentReport.fnDraw(false);
}