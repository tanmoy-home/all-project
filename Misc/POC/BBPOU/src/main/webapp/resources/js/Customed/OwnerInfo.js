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
         width: 1000,
         height: 150,
         source: dataAdapter,
         pageable: true,
         pagesize: 5,
         selectionmode: 'singlecell',
         
        // autoheight: true,
         columns: [
                   { text: 'ownerId', datafield: 'ownerId'},
                   { text: 'appPin', datafield: 'appPin' },
                   { text: 'firstName', datafield: 'firstName'},
                   { text: 'lastName', datafield: 'lastName'},
                   { text: 'title', datafield: 'title'},
                   { text: 'percentOwner', datafield: 'percentOwner' },
                   { text: 'address', datafield: 'address' },
                   { text: 'phone', datafield: 'phone'},
                   { text: 'fax', datafield: 'fax' },
                   { text: 'bankName', datafield: 'bankName'},
                   { text: 'bankAddress', datafield: 'bankAddress'},
                   { text: 'bankPhone', datafield: 'bankPhone'},
                   { text: 'bankACType', datafield: 'bankACType' },
                   { text: 'bankACHOlder', datafield: 'bankACHOlder' },
                   { text: 'bankACNo', datafield: 'bankACNo'},
                   { text: 'transitNo', datafield: 'transitNo'},
                   { text: 'ssn', datafield: 'ssn'}

         ]
     });

     ko.applyBindings(model);
	 
     }    	   