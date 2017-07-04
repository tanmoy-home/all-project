
function printpage() {
	var printButton = document.getElementById("printpagebutton");
	var cmd = document.getElementById("cmd");
	printButton.style.visibility = 'hidden';
	cmd.style.visibility = 'hidden';
	window.print();
	printButton.style.visibility = 'visible';
	cmd.style.visibility = 'visible';

}

function printDiv(div) {  
    // Create and insert new print section
	var footer = document.getElementById("modalFooter");
	footer.style.visibility = 'hidden';
    var elem = document.getElementById(div);
    var domClone = elem.cloneNode(true);
    footer.style.visibility = 'visible';
    var $printSection = document.createElement("div");
    $printSection.id = "printSection";
    $printSection.appendChild(domClone);
    document.body.insertBefore($printSection, document.body.firstChild);

    window.print(); 

    // Clean up print section for future use
    var oldElem = document.getElementById("printSection");
    if (oldElem != null) { oldElem.parentNode.removeChild(oldElem); } 
                          //oldElem.remove() not supported by IE

    return true;
}

function showmodal()
{
	$("#Name").html($("#customerName").val());
	$("#MobileNumber").html($("#customerMobileNumber").val());
	$("#AccountNumber").html($("#customerAccountNumber").val());
	$("#TransactionRefId").html($("#CustomerTransactionRefId").val());
	$("#PaymentChannel").html($("#customerPaymentChannel").val());
	$("#PaymentMode").html($("#customerPaymentMode").val());
	$("#BillDate").html($("#customerBillDate").val());
	$("#BillAmount").html($("#CustomerBillAmount").val());
	$("#ConvFee").html($("#customerConvFee").val());
	$("#TotalAmount").html($("#customerTotalAmount").val());
	$("#DateTime").html($("#customerDateTime").val());
	$("#AuthCode").html($("#customerAuthCode").val());
	
	}

function showcompmodal()
{
	$("#compId").html($("#id").val());
	$("#compDt").html($("#cdate").val());
	$("#compTranid").html($("#transactionId").val());
	$("#compLbl").html($("#tranLabel").val());
	$("#compRsn").html($("#reason").val());
//	$("#compAsnto").html($("#assignedTo").val());
//	$("#compsts").html($("#status").val());
}
/*
function download_pdf(){
    var pdf = new jsPDF();
    var printButton = document.getElementById("printpagebutton");
	var cmd = document.getElementById("cmd");
	/*
	 * printButton.style.visibility = 'hidden'; cmd.style.visibility = 'hidden';
	 */
/*	
var options = {backgroud:'#fff'};
    pdf.addHTML($('#target').get(0), options, function() {
        pdf.save('bill-receipt.pdf');
    });
    printButton.style.visibility = 'visible';
	cmd.style.visibility = 'visible';
};
*/
////////////////// for itext /////////////////////////
/*function download_pdf_server(){
	var tableData = $("#tableData").html();
	//var data = document.getElementById("tableData").value
	alert(tableData);
	//alert(data);
	$.ajax({
        type: "POST",
        //data :JSON.stringify("<html><tbody><tr><th class=\"\" style=\"  background: rgba(0, 0, 0, 0.04);\">Customer Name</th><th style=\"  background: rgba(0, 0, 0, 0.04);\">Sumana Paul</th></tr><tr><td>Customer Mobile</td><td>9999999999</td></tr><tr><th style=\"  background: rgba(0, 0, 0, 0.04);\">Account</th><th style=\"  background: rgba(0, 0, 0, 0.04);\">99</th></tr><tr></tr><tr><td>Transaction Reference Id:</td><td>OU0300125907</td></tr><tr></tr><tr><th style=\"  background: rgba(0, 0, 0, 0.04);\">Payment Channel</th><th style=\"  background: rgba(0, 0, 0, 0.04);\">Internet Banking</th></tr><tr></tr><tr><td>Payment Mode</td><td>Internet Banking</td></tr><tr></tr><tr><th style=\"  background: rgba(0, 0, 0, 0.04);\">Bill Date</th><th style=\"  background: rgba(0, 0, 0, 0.04);\">2016-02-06</th></tr><tr></tr><tr><td>Bill Amount</td><td>2.00</td></tr><tr></tr><tr><th style=\"  background: rgba(0, 0, 0, 0.04);\">Customer Convenience Fee</th><th style=\"  background: rgba(0, 0, 0, 0.04);\"> 0.12</th></tr><tr></tr><tr><td>Total Amount</td><td>2.12</td></tr><tr></tr><tr><th style=\"  background: rgba(0, 0, 0, 0.04);\">Trasnaction Date &amp; Time</th><th style=\"  background: rgba(0, 0, 0, 0.04);\">01-12-2016 06:19</th></tr><tr></tr><tr><td>Authorization Code</td><td>1480596570411</td></tr><tr></tr></tbody></html>"),
        data :JSON.stringify(tableData),
        url: "/resource/downloadPDF",
        contentType: "application/pdf"
    });
};*/

function printpage() {
	var printButton = document.getElementById("printpagebutton");
	var cmd = document.getElementById("cmd");
	printButton.style.visibility = 'hidden';
	cmd.style.visibility = 'hidden';
	window.print();
	printButton.style.visibility = 'visible';
	cmd.style.visibility = 'visible';

};

function download_pdf_server(filename,title,header){
//	var tenantId = document.getElementById("tenantId").value;
	var form = document.getElementById("reciptData");
//	form.method = "POST";
	form.action = "/AgentService/downloadReciept/"+filename+"/"+title+"/"+header;
	form.submit();
}
	//alert("Test.....");
	/*
	$.ajax({
        type: "POST",
        data : input,
        url: "/resource/downloadPDF"
    });
	
	
	$.ajax({
        url: "/resource/downloadPDF",
        data : escape(input),
        type: 'POST',
        success: function(data){
            if (data == "FAIL") {
                alert("File not found!");
            } else {
                //alert("Success ...." +data);
            	window.open("data:application/pdf," + escape(data)); // Download the file
            	//window.location = data;
            }
        },
        error: function (request, status, error) {
            alert("The request failed: " + request.responseText);
        }
  });
}
        //contentType: "application/pdf"


$(function () {

    var specialElementHandlers = {
        '#editor': function (element,renderer) {
            return true;
        }
    };
 $('#cmd').click(function () {
        var doc = new jsPDF();
        doc.fromHTML($('#target').get(0).html(), 15, 15, {
            'width': 170,'elementHandlers': specialElementHandlers
        });
        doc.save('sample-file.pdf');
    });  
});*/