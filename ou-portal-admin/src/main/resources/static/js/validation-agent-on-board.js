$(document).ready(function() {
	
 
// VALIDATION START
$('form').validate({
  rules: {
    agentName: {
      required: true,
      minlength:3,
      maxlength:45,
      agentNamePat: true
    },
    agentAliasName: {
      required: true,
      maxlength:45,
      agentAliasNamePat: true
    },
    agentMobileNo: {
      required: true,
      agentMobilePat: true
    },
    agentShopName: {
      required: true,
      maxlength:45,
      agentShopNamePat: true
    },
    agentRegisteredAdrline: {
      required: true,
      maxlength:200,
      agentRegisteredAdrlinePat: true
    },
    agentRegisteredState: {
      required: true
    },
    agentRegisteredCity: {
      required: true,
    },
    agentRegisteredPinCode: {
      required: true,
    },
    agentPaymentModes: {
      required: true,
    },
    agentPaymentChannels: {
        required: true,
      },
    agentType: {
          required: true,
        },
    agentGeoCode: {
      required: true,
     minlength:7,
     maxlength:8
    },
   /* agentEffctvFrom: {
        required: true,
      },
      agentEffctvTo: {
          required: true,
        },*/
    agentBankAccount: {
      required: true,
      minlength: 12,
      /* maxlength: 12,*/
    
    },
    scheme: {
      required: true,
    }
  },
  messages: {
    agentName: {
      required: "Please enter Agent Name",
      minlength:"Agenet Name at least 3 charectors long.",
      maxlength:"Agenet Name not more than 45 charectors long."
    },
    agentAliasName: {
      required: "Please enter Agent Alias Name",
      maxlength:"Agent Alias Name not more than 45 charectors long."
    },
    agentMobileNo: {
      required: "Please enter Agent Mobile"
    },
    agentShopName: {
      required: "Please enter Agent Shop Name",
      maxlength:"Agent Shop Name not more than 45 charectors long."
    },
    agentRegisteredAdrline: {
      required: "Please enter Agent Address",
      maxlength:"Address not more than 200 charectors long."
    },
    agentRegisteredState: {
      required: "Please Select Agent State "
    },
    agentRegisteredCity: {
      required: "Please Select Agent City "
    },
    agentRegisteredPinCode: {
      required: "Please Select Pin Code "
    },
    agentPaymentModes: {
      required: "Please Select Payment Mode "
    },
    agentPaymentChannels: {
        required: "Please Select Payment Channel"
      },
     agentType: {
          required: "Please Select Agent Type"
        },
    agentGeoCode: {
      required: "Please Enter Geo Code "
    },
  /*  agentEffctvFrom: {
        required: "Please Enter Valid date "
      },
      agentEffctvTo: {
          required: "Please Enter Valid date "
        },*/
    agentBankAccount: {
      required: "Please Enter Bank Account Number ",
      minlength:'bank account should be 12 digit number.'
    },
    scheme: {
      required: "Please Select Scheme"
    }
  }
});
//VALIDATION END
 
 
// --- SET PATTERN FOR REQUIRED ABOVE FIELDS - START ---
 
// ------ AGENT NAME PATTERN [ Firstname Middlename Lastname] Start
jQuery.validator.addMethod("agentNamePat", function(value, element) {
// return this.optional(element) || /^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$/.test(value);
	return this.optional(element) || /^[ a-zA-Z0-9_.-]{3,45}$/.test(value);
}, "Please enter valid Agent Name");
// ------ AGENT NAME PATTERN [ Firstname Middlename Lastname] End
 
// ------ AGENT ALIAS NAME PATTERN Start
jQuery.validator.addMethod("agentAliasNamePat", function(value, element) {
  return this.optional(element) || /^[a-zA-Z][ a-zA-Z0-9_.-]+$/.test(value);
}, "Please enter valid Aliase Name");
// ------ AGENT ALIAS NAME PATTERN End
 
//------ AGENT MOBILE VALIDATION  Start
jQuery.validator.addMethod("agentMobilePat", function(value, element) {
  return this.optional(element) || /^[789]\d{9}$/.test(value);
}, "Please enter valid Mobile Number");
 
 
var mobileNo = $('#agentMobileNo');
mobileNo.on('input', function(event) {
  var number = $(this).val().replace(/[^\d]/g, '');
  /*if (number.length == 7) {
    number = number.replace(/(\d{3})(\d{4})/, "$1.$2");
  }*/
  $(this).val(number).attr('maxlength',10);
});
 
 
/*
var mobileNo = $('#agentMobileNo');
mobileNo.attr('value', '+91 ');
mobileNo.attr('maxlength', 14);
var readOnlyLength = mobileNo.val().length;
mobileNo.on('keypress, keydown', function(event) {
  //  return (event.which != 8 && event.which != 0 && (event.which < 48 || event.which > 57)) ? false : true;
if ((event.which != 37 && (event.which != 39)) && ((this.selectionStart <readOnlyLength) || (event.which > 57) || ((this.selectionStart ==readOnlyLength) &&(event.which == 8)))) {
  return false;
}
 
});
*/
 
// ------ AGENT MOBILE VALIDATION End
 
// ------ AGENT SHOP NAME PATTERN Start
jQuery.validator.addMethod("agentShopNamePat", function(value, element) {
  return this.optional(element) || /[a-zA-Z][a-zA-Z0-9'._\-@' ]*$/.test(value);
}, "Please enter valid Agent Shop Name");
// ------ AGENT SHOP NAME PATTERN End
 
// ------ AGENT ADDRESS PATTERN Start
jQuery.validator.addMethod("agentRegisteredAdrlinePat", function(value, element) {
	//return this.optional(element) || /^[ a-zA-Z0-9_./-()@,]{3,200}$/.test(value);
	return this.optional(element) || /^[ a-zA-Z0-9_.\-@/,/(/)]{3,200}$/.test(value);
}, "Please enter valid Address");
// ------ AGENT ADDRESS PATTERN End
 
// ---- AGENT GEO CODE PATTERN START
var geoCode = $('#agentGeoCode');
geoCode.on('input', function(event) {
  var number = $(this).val().replace(/[^\d]/g, '');
  if (number.length == 6) {
    number = number.replace(/(\d{2})(\d{4})/, "$1.$2");
  }
  $(this).val(number).attr('maxlength',6);
});
// ---- AGENT GEO CODE PATTERN END
 
//---- BANK ACCOUNT PATTERN START
var bankAcc = $('#bankAccountNo');
bankAcc.on('input', function(event) {
  var number = $(this).val().replace(/[^\d]/g, '');
 
  $(this).val(number).attr('maxlength',12).attr('minlength',12);
});
//---- BANK ACCOUNT PATTERN END
 
// --- SET PATTERN FOR REQUIRED ABOVE FIELDS - END ---
 
});