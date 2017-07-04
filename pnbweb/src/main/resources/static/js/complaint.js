var oTable;
$(document).ready(function() {
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

});
$(function() {
	/*
	 * $("#getAllAgent").autocomplete( { serviceUrl : "getAllAgentsList",
	 * onSelect : function(suggestion) { alert('You selected: '); } });
	 */
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
});

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
			"Access-Control-Allow-Methods" : "GET, PUT, POST, DELETE, OPTIONS"
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