var doc = new jsPDF();
var specialElementHandlers = {
    '#editor': function (element, renderer) {
        return true;
    }
};
$( document ).ready(function() {
       $('#cmd').click(function () {
           doc.fromHTML($('#content').html(), 15, 15, {
              
                   'elementHandlers': specialElementHandlers
           });
           doc.save('Bill-Receipt.pdf');
       });
});