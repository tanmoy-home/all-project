var checkBoxFlag = null;
var checkBoxFlag_pchannel = null;
var fromPage = "HOME";

var sPaymentmode;
var sPaymentchannel;

var oTable;

$(document).ready(function() {

	$('.alert').hide();
	
	$('.input-daterange').datepicker({
		autoclose : true,
        endDate: "current"

	});
	/**
	 * **************************pg recon datatable
	 * Start******************************
	 */

	oTablePG = $('#pgReconList').dataTable({
		"aaSorting" : [ [ 1, "asc" ] ],
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

	jQuery('.dataTable').wrap('<div class="dataTables_scroll"/>');
	$('.dataTables_filter input').attr( 'maxlength', 50 );
	pgReconList();

	/**
	 * **************************pg recon datatable
	 * End******************************
	 */

	
	/**
	 * **************************biller recon datatable
	 * Start******************************
	 */
	oTableBlr = $('#blrReconList').dataTable({
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

	jQuery('.dataTable').wrap('<div class="dataTables_scroll"/>');

	blrReconList();
	
	/**
	 * **************************biller recon datatable
	 * End******************************
	 */
	
	
	/**
	 * **************************cu settlement datatable
	 * Start******************************
	 */
	oTableCUSetl = $('#cuSetlReconList').dataTable({
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

	jQuery('.dataTable').wrap('<div class="dataTables_scroll"/>');

	cuSetlList();
	
	/**
	 * **************************cu settlement datatable
	 * End******************************
	 */
	
	/**
	 * **************************Recon Summary datatable
	 * Start******************************
	 */
	$('#divReconSummary').hide();
	$('#divReconDetails').hide();
	oTableReconSumary = $('#reconSummary')
			.dataTable(
					{
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
	 * **************************Recon Summary datatable
	 * End******************************
	 */
	
	
	/**
	 * **************************Recon Details datatable
	 * Start******************************
	 */
	oTableReconDetails = $('#reconDetails').dataTable({
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
	
	
	
	$('.dataTables_filter input').attr("placeholder", "Enter to Search");
	$('.dataTables_filter input').attr("maxlength",15);

	/**
	 * **************************Recon Details datatable
	 * End******************************
	 */
});

function pgReconList() {
	var tbl = $('#pgReconList').serialize();
	$
			.ajax({
				url : "/portal/reconciliation/viewPGReconFiles",
				type : "POST",
				success : function(data) {
					if (data.result != null && data.result.length > 0) {

						loadDataTablePG(data.result);
					}
					if (data.result.length == 0) {
						var html = '<tr>';
						html += '<td colspan="3" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				}
			});
}
function loadDataTablePG(arr) {
	oTablePG.fnClearTable();// clear the DataTable
	oTablePG.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var formattedDate = new Date(item.date);
						var idstr = item.fileName;
						var reconSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"reconSelected\" id=\"reconSelected\" value=\""
								+ idstr + "\" />";
						oTablePG.fnAddData([ reconSelected, formattedDate,
								item.fileName ]);

					});
	oTablePG.fnDraw(false);
}


function blrReconList() {
	var tbl = $('#blrReconList').serialize();
	$
			.ajax({
				url : "/portal/reconciliation/viewBlrReconFiles",
				type : "POST",
				success : function(data) {
					if (data.result != null && data.result.length > 0) {
						loadDataTableBlr(data.result);
					}
					if (data.result.length == 0) {
						var html = '<tr>';
						html += '<td colspan="3" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				}
			});
}
function loadDataTableBlr(arr) {
	oTableBlr.fnClearTable();// clear the DataTable
	oTableBlr.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var formattedDate = new Date(item.date);
						var idstr = item.fileName;
						var reconSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"reconSelected\" id=\"reconSelected\" value=\""
								+ idstr + "\" />";
						oTableBlr.fnAddData([ reconSelected, formattedDate,
								item.fileName ]);

					});
	oTableBlr.fnDraw(false);
}



function cuSetlList() {
	var tbl = $('#cuSetlReconList').serialize();
	$
			.ajax({
				url : "/portal/settlement/viewCuSettlementFiles",
				type : "POST",
				success : function(data) {
					if (data.result != null && data.result.length > 0) {
						loadDataTableCUSetl(data.result);
					}
					if (data.result.length == 0) {
						var html = '<tr>';
						html += '<td colspan="3" style="text-align:center">No Data Found</td></tr>';
						$("#listBody").html(html);
					}
				}
			});
}
function loadDataTableCUSetl(arr) {
	oTableCUSetl.fnClearTable();// clear the DataTable
	oTableCUSetl.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var formattedDate = new Date(item.date);
						var idstr = item.fileName;
						var reconSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"reconSelected\" id=\"reconSelected\" value=\""
								+ idstr + "\" />";
						oTableCUSetl.fnAddData([ reconSelected, formattedDate,
								item.fileName ]);

					});
	oTableCUSetl.fnDraw(false);
}



function fileDtl(str) {
	$('.alert').removeClass('alert-success');
    $('.alert').removeClass('alert-danger');
    $('.alert').html('');
    $('.alert').hide();
	 if ($('input[name="reconSelected"]:checked').val()) {
         $('input[name="reconSelected"]:checked').each(function() {
                 var $row = $(this).parents('tr');
                 var fileName = ($row.find('td:eq(2)').html());
                 var fileType = str;
                 var inData = JSON.stringify({
                         'fileName' : fileName,
                         'fileType' : fileType
                 });
                
                 var form = document.createElement("form");
                 form.method = "post";
                 form.action = "/portal/reconciliation/download";
                 document.body.appendChild(form);
                 var input = $("<input>").attr("type", "hidden").attr("name", "inData").val(inData);
                 $(form).append($(input));
                 $(form).submit();
                 form.submit();
                 document.body.removeChild(form);
                

                 /*$.ajax({
                         url : "/portal/reconciliation/download",
                         type : "POST",
                         dataType : "json",
                         contentType : "application/json; charset=utf-8",
                         data : inData,
                         success : function(data) {
                                 var arr = data.result;
                                 var contentType = 'application/octet-stream';
                                 var blob = b64toBlob(arr, contentType);
                                 var blobUrl = URL.createObjectURL(blob);
                                 */
                                 //$('#fileView').attr('download', fileName);
                                 //$('#fileView').attr('href', blobUrl);
                                 /*var a = document.createElement("a");
                                 a.style = "display: none";
                                 document.body.appendChild(a);
                                 a.href = blobUrl;
                                 a.download = fileName;
                                 a.click();
                                 window.URL.revokeObjectURL(blobUrl);*/
                                
                                 /*var oIframe = document.createElement('iframe');
                               
                                 var $oIframe = $(oIframe).attr({
                                     src: blobUrl,
                                     style: 'display:none'
                                 });
                                 document.body.appendChild(oIframe);
                         }
                 });*/

         });
 }
 else{
      $('.alert').removeClass('alert-success');
             $('.alert').addClass('alert-danger');
             $('.alert').html('Please select a Report for download!');
             $('.alert').show();
             $(window).scrollTop(0);
 }
}

function b64toBlob(b64Data, contentType, chunkSize) {
	contentType = contentType || '';
	chunkSize = chunkSize || 1024;
	var byteCharacters = atob(b64Data);
	var byteArrays = [];

	for (var offset = 0; offset < byteCharacters.length; offset += chunkSize) {
		var chunk = byteCharacters.slice(offset, offset + chunkSize);
		var byteNumbers = new Array(chunk.length);
		for (var i = 0; i < chunk.length; i++) {
			byteNumbers[i] = chunk.charCodeAt(i);
		}

		var byteArray = new Uint8Array(byteNumbers);
		byteArrays.push(byteArray);
	}

	var blob = new Blob(byteArrays, {
		type : contentType
	});
	return blob;
}

function recon() {
	var stDate = $("#datepkerEffctvFrom").val();
	var endDate = $("#datepkerEffctvTo").val();
	var inData = JSON.stringify({
		'startDt': stDate,
		'endDt': endDate
	});
	//alert(inData);
	var tbl = $('#reconSummary').serialize();
	$
			.ajax({
				url : "/portal/reconciliation/reconDetailsReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {
					$('#divReconSummary').show();
					var arr = data.result;
					if (arr != null && arr.length > 0) {						
						loadDataTableReconSummary(arr);
					}
					if (arr.length == 0) {
						var html = '<tr>';
						html += '<td colspan="6" style="text-align:center">No Data Found</td></tr>';
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
function loadDataTableReconSummary(arr) {
	oTableReconSumary.fnClearTable();// clear the DataTable
	oTableReconSumary.fnDraw();
	$.each(arr, function(key, item) {
		var notInOUCount = 0;
		var notInCUCount = 0;
		var mismatchedCount = 0;
		var pendingCount = 0;
		var broughtForwardCount = 0;
		var strType = '';
		
		if(item.notInOUCount > 0){
			strType = 'notInOU';
			notInOUCount = "<button class=\"msgCheckBox\" data-reconId="+item.reconId+" data-strType="+strType+"  name=\"notInOUCount\" id=\"notInOUCount\" value=\""
				+ item.notInOUCount + "\" >" + item.notInOUCount + "</button>";
		}else{
			notInOUCount = item.notInOUCount;
		}
		
		if(item.notInCUCount > 0){
			strType = 'notInCU';
			notInCUCount = "<button class=\"msgCheckBox\" data-reconId="+item.reconId+" data-strType="+strType+" name=\"notInCUCount\" id=\"notInCUCount\" value=\""
				+ item.notInCUCount + "\" >" + item.notInCUCount + "</button>";
		}else{
			notInCUCount = item.notInCUCount;
		}
		
		if(item.mismatchedCount > 0){
			strType = 'mismatched';
			mismatchedCount = "<button class=\"msgCheckBox\" data-reconId="+item.reconId+" data-strType="+strType+" name=\"mismatchedCount\" id=\"mismatchedCount\" value=\""
				+ item.mismatchedCount + "\" >" + item.mismatchedCount + "</button>";
		}else{
			mismatchedCount = item.mismatchedCount;
		}
		
		if(item.pendingCount > 0){
			strType = 'pending';
			pendingCount = "<button class=\"msgCheckBox\" data-reconId="+item.reconId+" data-strType="+strType+" name=\"pendingCount\" id=\"pendingCount\" value=\""
				+ item.pendingCount + "\" >" + item.pendingCount + "</button>";
		}else{
			pendingCount = item.pendingCount;
		}
		
		if(item.broughtForwardCount > 0){
			strType = 'broughtForward';
			broughtForwardCount = "<button class=\"msgCheckBox\" data-reconId="+item.reconId+" data-strType="+strType+" name=\"broughtForwardCount\" id=\"broughtForwardCount\" value=\""
				+ item.broughtForwardCount + "\" >" + item.broughtForwardCount + "</button>";
		}else{
			broughtForwardCount = item.broughtForwardCount;
		}		
		
		oTableReconSumary.fnAddData([ item.reconId, notInOUCount,
				notInCUCount, mismatchedCount, pendingCount,
				broughtForwardCount ]);
		
		$('.msgCheckBox').click(function(){
			console.log('testing for the item clicked:',$(this));

			console.log('testing for recon_id:',$(this).attr('data-reconId'));
			console.log('testing for type:',$(this).attr('data-strType'));

			reconDetails($(this).attr('data-strType'), $(this).attr('data-reconId'));
		})
		/*$('#notInCUCount').click(function(){
			reconDetails('notInCU', $('#notInCUCount').attr('data-reconId'));
		})
		$('#mismatchedCount').click(function(){
			reconDetails('mismatched', $('#mismatchedCount').attr('data-reconId'));
		})
		$('#pendingCount').click(function(){
			reconDetails('pending', $('#pendingCount').attr('data-reconId'));
		})
		$('#broughtForwardCount').click(function(){
			reconDetails('broughtForward', $('#broughtForwardCount').attr('data-reconId'));
		})
		*/
		
		
		/*$('.msgCheckBox').click(function(){
			reconDetails(strType, item.reconId);
		})*/

	});
	oTableReconSumary.fnDraw(false);
}
function reconDetails(str, reconId) {
	
	var stDate = $("#datepkerEffctvFrom").val();
	var endDate = $("#datepkerEffctvTo").val();
	var inData = JSON.stringify({
		'startDt': stDate,
		'endDt': endDate
	});
	
	var tbl = $('#reconDetails').serialize();
	$
			.ajax({
				url : "/portal/reconciliation/reconDetailsReport",
				type : "POST",
				dataType : "json",
				contentType : "application/json; charset=utf-8",
				data : inData,
				success : function(data) {					
					var arr = data.result;
					//alert(arr.length);
					//alert(str);
					console.log(arr);
					if (arr != null && arr.length > 0) {
						$('#divReconDetails').show();
						$.each(arr, function(key, item) {
							//alert(item.reconId +"="+reconId);
							console.log(item.notInOuLists.length);
							if(item.reconId == reconId){
								if(str == "notInOU"){
									loadDataTableReconDetails(item.notInOuLists);
								}
								else if(str == "notInCu"){
									loadDataTableReconDetails(item.notInCuLists);
								}
								else if(str == "mismatched"){
									loadDataTableReconDetails(item.mismatchedLists);
								}
								else if(str == "pending"){
									loadDataTableReconDetails(item.pendingLists);
								}
								else if(str == "broughtForward"){
									loadDataTableReconDetails(item.broughtForwardLists);
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
				error: function(err){
					$('.alert').html('Error');
					$('.alert').show();
				}
			});
}

function loadDataTableReconDetails(arr) {
	oTableReconDetails.fnClearTable();// clear the DataTable
	oTableReconDetails.fnDraw();
	$.each(arr, function(key, item) {
		oTableReconDetails.fnAddData([ item.txnRefId, item.agentId, item.billerId,
				item.reconDescription, item.reconStatus ]);

	});
	oTableReconDetails.fnDraw(false);
}