$(document).ready(
		function() {

			$('.alert').hide();
			$('.btn-Edit').hide();
			/****************************Agent List For COU********************/
			
			$('#agentListForCou').hide();
			$('.btn-View').hide();			 
			
			oTableListForCou = $('#agentListForCou').dataTable({
				dom : 'Bfrtip',
				buttons : [ 'csv', 'excel' ],
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
			
			
			$('#agentInstGrp').change(function() {
				var agentInstID = $("#agentInstGrp option:selected").val();
				var tbl = $('#agentListForCou').serialize();
				$.ajax({
					url : "/portal/COUAdmin/agentList/" + agentInstID,
					type : "POST",
					success : function(data) {
						var arr = data.result;
						if (arr != null && arr.length > 0) {
							$('#agentListForCou').show();
							loadDataTableAgtList(arr);
						}
						if (arr.length == 0) {
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
			});
			
			/****************************Agent OnBoard*********************************/

			$('#div-agent-type').hide();
			$('#div-Payment-channel').show();
			$('#dummyAgent').on(
					"change",
					function() {
						if ($(this).is(':checked')) {
							$('#div-agent-type').hide();
							$('#div-Payment-channel').show();

						} else {
							$('#div-agent-type').show();
							$('#div-Payment-channel').hide();
							$.ajax({
								type : 'POST',
								dataType : 'json',
								url : "/portal/COUAdmin/agentTypeList",
								success : function(data) {
									$('#agentType').empty();
									$('#agentType').append(
											$("<option></option>").val('').html(
													"--Select AgentType--"));
									$.each(data.result, function(key,
											value) {
										if(value.configParam.toLowerCase() != 'agent'){
										$('#agentType').append(
												$("<option></option>").val(
														value.configParam).html(
														value.configParam));
										}
									}); 
								},
								error : function(err) {
									$('.alert').removeClass('alert-success');
									$('.alert').addClass('alert-danger');
									$('.alert').html('Error');
									$('.alert').show();
								}
							});
							
						}
					});
			
			$('#pmChannel').change(
					function() {
						var pchannel = $("#pmChannel option:selected").val();
						var pm = '';
						//alert(pchannel);
						$.ajax({
							type : 'POST',
							dataType : 'json',
							url : "/portal/COUAdmin/paymentMode/" + pchannel,
							success : function(data) {
								//alert(data.result);
								$('#pmModes').empty();
								
								$
										.each(
												data.result,
												function(
														key,
														value) {
													pm = value + "," + pm;
												});
								$('#pmModes').val(pm.slice(0,-1));
							},
							error: function(err){
								$('.alert').removeClass('alert-success');
								$('.alert').addClass('alert-danger');
								$('.alert').html('Error');
								$('.alert').show();
							}
						});
					});
			
			$('#agentType').change(
					function() {
						var pchannel = $("#agentType option:selected").val();
						//alert(pchannel);
						$.ajax({
							type : 'POST',
							dataType : 'json',
							url : "/portal/COUAdmin/paymentMode/" + pchannel,
							success : function(data) {
								$('#pmModes').empty();
								$('#pmModes').val(data.result);
							},
							error: function(err){
								$('.alert').removeClass('alert-success');
								$('.alert').addClass('alert-danger');
								$('.alert').html('Error');
								$('.alert').show();
							}
						});
					});
			$("#agentInstGrp").trigger("change");
		})

/******************************Agent Approve/Reject*****************************/

function loadDataTableAgtList(arr) {
	oTableListForCou.fnClearTable();// clear the DataTable
	oTableListForCou.fnDraw();
	$
			.each(
					arr,
					function(key, item) {
						var idstr = item.agentId;
						var agentSelected = '';
							agentSelected = "<input class=\"msgCheckBox\" type=\"radio\" name=\"agentSelected\" id=\"agentSelected\" value=\""
									+ idstr + "\" />";
							$(".msgCheckBox").click(function() {
								if(item.entityStatus == 'DRAFT'){
									$('.btn-Edit').show();
								}
								else{
									$('.btn-Edit').hide();									
								}
							});
							$('.btn-View').show();
							oTableListForCou
								.fnAddData([ agentSelected, item.agentId,
										item.agentName,
										item.agentMobileNo,
										item.agentRegisteredAdrline,
										item.entityStatus ]);

					});
	/*$(".msgCheckBox").click(function() {
		$('#agent-view-buttons').show();
	});*/

	oTableListForCou.fnDraw(false);
}

function agentDtlViewMode(str) {
	var agentID = '';
	// alert($('input[name="agentSelected"]:checked').val());
	if ($('input[name="agentSelected"]:checked').val()) {
		$('input[name="agentSelected"]:checked').each(
				function() {
					var $row = $(this).parents('tr');
					agentID = ($row.find('td:eq(1)').html());
					if (agentID != '') {
						window.location.replace("../COUAdmin/agentBoarding/" + str + "/"
								+ agentID);
					}
				});
	}
}
