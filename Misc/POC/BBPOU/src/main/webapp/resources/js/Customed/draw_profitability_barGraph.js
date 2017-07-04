 
//-------------------------------display Profitability Graph-------------------------------------------//

function draw_profitability_barGraph(id, name, chartType, URI,url_profitabilityGraph) {
	$.ajax({
		 
	//	 url : "http://localhost:8080/Customer360Interface/displayGraph",
	 	url : url_profitabilityGraph, 
	 	
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
	      var data1=[] , data2=[] ;/*, data3=[] ,data4=[] ;*/
	     /* var data5=[] , data6=[] , data7=[] ,data8=[];*/
	      var newlabels=[];
	      for (i in results)
		   {
	    	  
	    	  month_val={};
	    	 
	    	  if(results[i].month=="1"){
				  
				   newlabels[i]="JAN";
			   }
			   else  if(results[i].month=="2"){
				 
				   newlabels[i]="FEB";
			   }
			   else  if(results[i].month=="3"){
				  
				   newlabels[i]="MAR";
			   }
			   
			   else  if(results[i].month=="4"){
				 
				   newlabels[i]="APR";
			   }
			   
			   else  if(results[i].month=="5"){
				   
				   newlabels[i]="MAY";
			   }
			   else  if(results[i].month=="6"){
				   
				   newlabels[i]="JUN";
			   }else  if(results[i].month=="7"){
				  
				   newlabels[i]="JUL";
			   }
			   else  if(results[i].month=="8"){
				  
				   newlabels[i]="AUG";
			   }
			   else  if(results[i].month=="9"){
				  
				   newlabels[i]="SEP";
			   }
			   else  if(results[i].month=="10"){
				   
				   newlabels[i]="OCT";
			   }
			   else  if(results[i].month=="11"){
				  
				   newlabels[i]="NOV";
			   }
			   else  if(results[i].month=="12"){
				  
				   newlabels[i]="DEC";
			   }
			   
	    	  month_val["status"]=newlabels[i];
	    	  
			   labels.push(month_val);
	    	  
		     item1={};
		    
		    item1["value"]=results[i].profit;
		    
		    data1.push(item1);
		    
		    item2={};
		    
		    item2["value"]=-1*(results[i].loss);
		    
		    data2.push(item2);
 
		   }
	   
	      var settings = {
	                title: "Merchant Profitability",
	                description: "Last quater Profit/Loss",
	                enableAnimations: true,
	                animationDuration: 2000,
	                showLegend: true,
	                padding: { left: 5, top: 5, right: 40, bottom: 5 },
	                titlePadding: { left: 90, top: 0, right: 0, bottom: 10 },
	                source: labels,
	               // source: labels,
	                xAxis:
	                {
	                    dataField: 'status',
	                    unitInterval: 1,
	                    gridLines: { visible: true },
	                    title: { text: 'Months' }
	                },
	                colorScheme: 'scheme02',
	                valueAxis:
	                {   unitInterval: 1,
	                	minValue: -5,
	                    maxValue: 5,

	                	
	                    visible: true,
	                    title: { text: 'Merchant Profit/Loss Count' }
	                },
	                seriesGroups:
	                    [
	                        {
	                            type: 'stackedcolumn',
	                            source: data1,
	                            series: [
	                                  { dataField: 'value', displayText: 'PROFIT' }
	                            ]
	                        },
	                        {
	                            type: 'stackedcolumn',
	                            source: data2,
	                            series: [
	                                    { dataField: 'value', displayText: 'LOSS' }
	                            ]
	                        }
	                        
	                        
	                       
	                    ]
	            };
	            // setup the chart
	            $('#jqx_'+id).jqxChart(settings);
	            
	            function myEventHandler(event) {
	  	      
	  	          // alert(event.args.elementIndex); 
	  	        
	  	      var mnth =  results[event.args.elementIndex].month;
	  	    //  var month_value = monthID_generation(mnth);
	  	   //   alert("mnthno =  "+mnth );
	  	      showMonthlyProfitability(mnth,url_profitabilityGrid);
	  	    
	  	         
	  	      };
	            
	            $('#jqx_'+id).on('click', function (event) {
	            	
	  	        if (event.args)
	  	            myEventHandler(event); 
	  	  	  	          
	  	          });
	         
	  }
		 		 
		});
		
}


//--------------------------------------display profitability grid---------------------------------------------------//



function Profit(m_Id1, merchant_Name1, charge_Amt1,total_Cost1,trn_Amt1,net1) {
	
    this.m_Id1 = m_Id1;
    this.merchant_Name1 = merchant_Name1;
    this.charge_Amt1 = charge_Amt1;
    this.total_Cost1 = total_Cost1;
    this.trn_Amt1 = trn_Amt1;
    this.net1 = net1;
}

function Nonprofit(m_Id2, merchant_Name2, charge_Amt2,total_Cost2,trn_Amt2,net2) {
	
    this.m_Id2 = m_Id2;
    this.merchant_Name2 = merchant_Name2;
    this.charge_Amt2 = charge_Amt2;
    this.total_Cost2 = total_Cost2;
    this.trn_Amt2 = trn_Amt2;
    this.net2 = net2;
}




 function showMonthlyProfitability(mnth,url_profitabilityGrid){
	 
	var  month_val = month_generation_grid(mnth);
	 
	/* $('#modalprftbility').modal('show');
     $('#modal-body-prf').html("");
 //  $('#prfmodalheader').append('<label><h4 class="modal-title">Profitability Statistics for '+month_val+' , '+year+'' </h4></label>');
     $('#modal-body-prf').append('<div id="prfdiv"><div class ="caption" align="center"><label>Profitable Merchants</label></div><div id="grd_prf"></div><br/><div class ="caption" align="center"><label>Non Profitable Merchants</label></div><div id="grd_nprf"></div></div>');*/
    
	 var pr=[]; var ls=[];
	 
	 $.ajax({
		 
				
			  url : url_profitabilityGrid, 
			  type: 'POST',
			  dataType: 'JSON',
			  async :false,
			  cache:false,
			  contentType: "application/json;charset=utf-8",
			  data : JSON.stringify({month:mnth}),
			  
			  header:{   "Access-Control-Allow-Origin" : "*" ,
				          'Accept': 'application/json',
				 		 "Access-Control-Allow-Methods": "GET, PUT, POST, DELETE, OPTIONS"},

			  success: function (result) {
				  
				 // alert("success");
				 // alert(results);
				  
				  $('#modalprftbility').modal('show');
				  $('#modal-body-prf').html("");
				  $('#prfmodalheader').html("");
				  $('#prfmodalheader').append('<button type="button" class="close" data-dismiss="modal">&times;</button><label><h4 class="modal-title">Profitability Statistics for '+month_val+' , '+result[0].year+' </h4></label>');
				  $('#modal-body-prf').append('<div id="prfdiv"><div class ="caption" align="center">Profitable Merchants &nbsp;&nbsp;<input type="image" src="resources/images/excel.png" id = "excelExport1"/>&nbsp<input type="image" src="resources/images/pdf.png" id = "pdfExport1"/></div><div id="grd_prf"></div><br/><div class ="caption" align="center">Non Profitable Merchants &nbsp;&nbsp;<input type="image" src="resources/images/excel.png" id = "excelExport2"/>&nbsp<input type="image" src="resources/images/pdf.png" id = "pdfExport2"/></div><div id="grd_nprf"></div></div>');
				  
				  
				  var net=[] 
				  var profit, nonprofit;
				  
				  
				  for( i in result){
					  
						  net = result[i].net;
					  
					  if (net > 0)
						  {
						 
						   profit = new Profit(result[i].m_Id, result[i].merchant_Name, result[i].charge_Amt,result[i].total_Cost,result[i].trn_Amt,result[i].net);
						   pr.push(profit);
						  
					 }
					  
					  else{
						  
						 nonprofit = new Nonprofit(result[i].m_Id, result[i].merchant_Name, result[i].charge_Amt,result[i].total_Cost,result[i].trn_Amt,result[i].net);
						 ls.push(nonprofit);
						 
						 				 
					  	}
				  }
				  
				 var prf = pr;	
				 Profit_Grid(prf);
				 
				 
				 var nprf =  ls;
				 Nonprofit_Grid(nprf);
				 
			  },
				 		
			  error: function(jqXHR, textStatus, errorThrown) {
							alert(errorThrown);
						
							}
							 		 
				 		 
	 });
	 
 }

 
 //---------------------------------------------***-------------------------------------------------------------//
 
function Profit_Grid(prf){
	var data = prf;
	var source =
    {
        localdata: data,
        datatype: "json",
        datafields:
        [
            { name: 'm_Id1',  type: 'string' },
            { name: 'merchant_Name1', type: 'string' },
            { name: 'charge_Amt1',  type: 'string' },
            { name: 'total_Cost1',  type: 'string' },
            { name: 'trn_Amt1',  type: 'string' },
            { name: 'net1', type: 'string' }
            
        ]
        
    };
    var dataAdapter = new $.jqx.dataAdapter(source);
  

 
    $("#grd_prf").jqxGrid(
    {
    	theme : "energyblue",
    	source: dataAdapter,
    	sortable: true,
       // showstatusbar: true,
        editable: false,
    	width: 550,
       	autoheight: true,
        pageable: true,
        pagesize: 5,
        selectionmode: 'none',
        
          	
        columns:
        [
            { text: 'MerchantID',  datafield: 'm_Id1'},
            { text: 'Name',  datafield: 'merchant_Name1'},
            { text: 'Charge Amt',  datafield: 'charge_Amt1'},
            { text: 'Total Cost',  datafield: 'total_Cost1'},
            { text: 'Transaction Amt',  datafield: 'trn_Amt1'},
            { text: 'Net Value',  datafield: 'net1'}
           
        ]        
    });
    
   // $("#excelExport1").jqxButton();
  //  $("#pdfExport1").jqxButton();


    $("#excelExport1").click(function () {
        $("#grd_prf").jqxGrid('exportdata', 'xls', 'Profitable_Merchants');           
    });

    $("#pdfExport1").click(function () {
        $("#grd_prf").jqxGrid('exportdata', 'pdf', 'Profitable_Merchants');
    });


}


function Nonprofit_Grid(nprf){
	
	var data = nprf;
	var source =
    {
        localdata: data,
        datatype: "json",
        datafields:
        [
            { name: 'm_Id2',  type: 'string' },
            { name: 'merchant_Name2', type: 'string' },
            { name: 'charge_Amt2',  type: 'string' },
            { name: 'total_Cost2',  type: 'string' },
            { name: 'trn_Amt2',  type: 'string' },
            { name: 'net2', type: 'string' }
            
        ]
        
    };
    var dataAdapter = new $.jqx.dataAdapter(source);
  

 
    $("#grd_nprf").jqxGrid(
    {
    	theme : "energyblue",
    	source: dataAdapter,
    	sortable: true,
        editable: false,
    	width: 550,
       	autoheight: true,
        pageable: true,
        pagesize: 5,
        selectionmode: 'none',
        
          	
        columns:
        [
            { text: 'MerchantID',  datafield: 'm_Id2'},
            { text: 'Name',  datafield: 'merchant_Name2'},
            { text: 'Charge Amt',  datafield: 'charge_Amt2'},
            { text: 'Total Cost',  datafield: 'total_Cost2'},
            { text: 'Transaction Amt',  datafield: 'trn_Amt2'},
            { text: 'Net Value',  datafield: 'net2'}
           
        ]        
    });
    
   // $("#excelExport2").jqxButton();
  //  $("#pdfExport2").jqxButton();


    $("#excelExport2").click(function () {
        $("#grd_prf").jqxGrid('exportdata', 'xls', 'Non_Profitable_Merchants');           
    });

    $("#pdfExport2").click(function () {
        $("#grd_prf").jqxGrid('exportdata', 'pdf', 'Non_Profitable_Merchants');
    });

}

 