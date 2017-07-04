var checkBoxFlag = null;
var checkBoxFlag_pchannel = null;
var fromPage = "HOME";

var sPaymentmode;
var sPaymentchannel;

var oTable;

$(document)
		.ready(
				function() {
					$('.btn-Edit').hide();
					$('#agent-view-buttons , #datagrid').hide();

					/*$('#data-table-close').click(function() {
						$('#datagrid').hide();
					});*/
					
					oTableDtlList = $('#agentDtlList').dataTable({
						dom : 'Bfrtip',
						buttons: {
					        buttons: [
					            { extend: 'csv', className: 'btn btn-default'},
					            { extend: 'excel', className: 'btn btn-default' }
					        ]
					    },
						"aaSorting" : [ [ 1, "desc" ] ],
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

					agentDtlList();

					$('.dataTables_filter input').attr("placeholder", "Enter to Search");
					$('.dataTables_filter input').attr("maxlength",15);

				});


function agentDtlList() {
	var tbl = $('#agentDtlList').serialize();
	$
			.ajax({
				url : "/portal/agentInstitute/agentList",
				type : "POST",
				success : function(data) {
					var arr = data.result;
					if (arr != null && arr.length > 0) {
						loadDataTableDtlList(arr);
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

function loadDataTableDtlList(arr) {
	oTableDtlList.fnClearTable();// clear the DataTable
	oTableDtlList.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var idstr = item.agentId;
						var agentSelected = '';
						/* if (item.entityStatus == 'APPROVED') { */
						agentSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"agentSelected\" id=\"agentSelected\" value=\""
								+ idstr + "\" />";
						$(".msgCheckBox").click(function() {
							$('#agent-view-buttons').show();
							if(item.entityStatus == 'DRAFT'){
								$('.btn-Edit').show();
							}
						});

						/*
						 * } else { agentSelected = "<input
						 * class=\"msgCheckBox\" type=\"radio\"
						 * disabled=\"true\" name=\"agentSelected\"
						 * id=\"agentSelected\" value=\"" + idstr + "\" />";
						 * agentSelected = "<i class=\"fa fa-check fa-icon\"
						 * onclick=\"agentApprove('"+ idstr +"');\"></i>" + "<i
						 * class=\"fa fa-times fa-icon\"
						 * onclick=\"agentReject('"+ idstr +"');\"></i>" //"<button
						 * class=\"btn btn-default waves-effect waves-light\"
						 * type=\"button\" id=\"btnApprove\"
						 * onclick=\"agentApprove('"+ idstr +"');\">Approve</button>&nbsp;&nbsp;" +
						 * //"<button class=\"btn btn-danger waves-effect
						 * waves-light\" type=\"button\" id=\"btnReject\"
						 * onclick=\"agentReject();\">Reject</button>" }
						 */

						oTableDtlList
								.fnAddData([ agentSelected, item.agentId,
										item.agentName, item.agentMobileNo,
										item.agentRegisteredAdrline,
										item.entityStatus ]);

					});
	$(".msgCheckBox").click(function() {
		$('#agent-view-buttons').show();
	});

	oTableDtlList.fnDraw(false);
}

function agentDtl(str) {
	var agentID = '';
	// alert($('input[name="agentSelected"]:checked').val());
	if ($('input[name="agentSelected"]:checked').val()) {
		$('input[name="agentSelected"]:checked').each(
				function() {
					var $row = $(this).parents('tr');
					agentID = ($row.find('td:eq(1)').html());
					if (agentID != '') {
						window.location.replace("agentBoarding/" + str + "/"
								+ agentID);
					}
				});
	}
}