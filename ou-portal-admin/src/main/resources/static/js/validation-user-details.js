function GetAgentDetails() {
       var agentId = $('#agentID').val();
       $.ajax({
              url : "/portal/user/add-user/" + agentId,
              type : "POST",
              success : function(data) {
            	 
                     $('#UserDetail').show();
                     $('#firstName').val(data.result.firstName);
                     $('#lastName').val(data.result.lastName);
                     $('#address').val(data.result.address);
                     $('#state').val(data.result.state);
                     $('#country').val(data.result.country);
                     $('#pincode').val(data.result.pincode);
                     $('#mobile').val(data.result.mobile);
              },
              error : function(err) {
                     $('.alert').removeClass('alert-success');
                     $('.alert').addClass('alert-danger');
                     $('.alert').html('Error');
                     $('.alert').show();
              }
       });
}
 
 
 
 
function checkDuplicateUser() {
      
       var username = $('#username').val();
       $.ajax({
              url : "/portal/user/add-user/checkDuplicateUser/" + username,
              type : "POST",
              success : function(data) {
            	  //alert(data.result);
            	  if(data.result == '0'){                     
                     $('#usernameIcon').addClass("fa fa-check").css("color", "green");
            	  }
            	  else{
            		  $('#usernameIcon').addClass("fa fa-times").css("color", "red");
            	  }
              },
              error : function(err) {
            	//  alert('error');
                     //$('#UserDetail').hide();
                    // $('#usernameIcon').addClass("fa fa-times").css("color", "red");
                     $('.alert').removeClass('alert-success');
                     $('.alert').addClass('alert-danger');
                     $('.alert').html('Error');
                     $('.alert').show();
              }
       });
}
 
 
$(document).ready(function() {
	
	/*if($('#userRefId').val().length>0){
		$('#userRefId').attr('readonly','readonly');
	}
	else{
		$('#userRefId').removeAttr('readonly');
	}*/
	
	if($('#username').val().length>0){
		$('#username').attr('readonly','readonly');
		$('.rolePrivilege').attr('disabled','disabled');
	}
	else{
		$('#username').removeAttr('readonly');
	}
	/*$('#UserDetail').show();
    
	if($('#agentID').val().length>0){
		$('#UserDetail').show();
		$('#btn-getDetails').hide();
	}else{
		
		$('#UserDetail').hide();
		$('#btn-getDetails').show();
	}*/
	
    	  //$('#UserDetail').true();
      
      // $('#UserDetail').hide();
      /*$('#username').blur(function(){
    	  checkDuplicateUser();
      });*/
	
	$('.AIList').hide();
	$('.rolePrivilege')
			.change(
					function() {
						if ($('.rolePrivilege option:selected')
								.text() == 'AI_ADMIN') {
							$('.AIList').show();
						} else {
							$('.AIList').hide();
						}
					});
	
	$('#state')
	.change(
			function() {
				var state = $("#state option:selected")
						.val();
				$
						.ajax({
							type : 'POST',
							dataType : 'json',
							url : "/portal/postalList/"
									+ state,
							success : function(data) {
								$('#pincode').empty();
								$('#pincode')
										.append(
												$(
														"<option></option>")
														.val(
																0)
														.html(
																"--Select PinCode--"));
								$
										.each(
												data.result.postals,
												function(
														key,
														value) {
													$(
															'#pincode')
															.append(
																	$(
																			"<option></option>")
																			.val(
																					value.postalCode)
																			.html(
																					value.postalCode));
												});
							},
							error: function(err){
								$('.alert').removeClass('alert-success');
								$('.alert').addClass('alert-danger');
								$('.alert').html('Error');
								$('.alert').show();
							}
						});
			});
 
      
  // VALIDATION START
  $('form').validate({
    rules: {
    	userRefId: {
        required: true
      },
      username: {
        required: true,
        maxlength:45,
        usernamePat:true,
        usernameValidation:($('#editMode').length === 0)
      },
    
      email: {
        required: true,
        emailPat:true
      },
      contactNumber: {
          required: true,
          phonePat:true,
          minlength:14
      },
      mobile: {
        required: true,
        MobilePat:true
       
      },
      roles:{
         required:true
      },
      address: {
          required: true,
          maxlength:200,
          addressPat: true
        },
        state: {
        	required:true
        },
        pincode: {
        	required: true
        },     
        firstName: {
            required: true,
            minlength:3,
            maxlength:45,
            fullNamePat: true
          }
   
    },
    
    messages: {
    	userRefId: {
        required: 'Please enter Ref. ID',
      },
      username: {
        required: 'Please enter Username',
        maxlength:'User name not more than 45 vharecters long.'
      },     
      firstName: {
    	  required: "Please enter Full Name",
          minlength:"Name at least 3 charectors long.",
          maxlength:"Name not more than 45 charectors long."
      },
      address: {
    	  required: "Please enter Agent Address",
          maxlength:"Address not more than 200 charectors long."
      },
      state: {
        required: 'Please enter State'
      },
      pincode: {
        required: 'Please enter Pincode'
      },
      email: {
        required: 'Please enter email'
      },
      contactNumber: {
          required: 'Please enter Contact Number',
          minlength:'Please Enter valid Contact Number with STD code'
        },
      mobile: {
        required: 'Please enter Mobile Number'
      },
      roles:{
        required:'Please Select Role'
        },
     
    }
  });
  // VALIDATION END
 
 // --- SET PATTERN FOR REQUIRED ABOVE FIELDS - START ---
 
//------ FULL NAME PATTERN [ Firstname Middlename Lastname] Start
  jQuery.validator.addMethod("fullNamePat", function(value, element) {
  // return this.optional(element) || /^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$/.test(value);
  	return this.optional(element) || /^[ a-zA-Z0-9_.-]{3,45}$/.test(value);
  }, "Please enter valid Name");
  // ------FULL NAME PATTERN [ Firstname Middlename Lastname] End
  
//------ ADDRESS PATTERN Start
  jQuery.validator.addMethod("addressPat", function(value, element) {
  	//return this.optional(element) || /^[ a-zA-Z0-9_.-/()@,]{3,200}$/.test(value);
  	return this.optional(element) || /^[ a-zA-Z0-9_.\-@/,/(/)]{3,200}$/.test(value);
  }, "Please enter valid Address");
  // ------ ADDRESS PATTERN End
  
 
//------  MOBILE VALIDATION  Start
  jQuery.validator.addMethod("MobilePat", function(value, element) {
    return this.optional(element) || /^[789]\d{9}$/.test(value);
  }, "Please enter valid Mobile Number");
   
   
  var mobileNo = $('#mobile');
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
   
  // ------  MOBILE VALIDATION End
  
  
 
// ------ AGENT EMAIL PATTERN Start
jQuery.validator.addMethod("emailPat", function(value, element) {
  return this.optional(element) || /^[a-z0-9._-]*[@][a-z0-9]{2,}[.][a-z0-9.]{2,5}$/.test(value);
}, "Please enter valid email");
// ------ AGENT EMAIL PATTERN End
 
// ------ USERNAME PATTERN Start
jQuery.validator.addMethod("usernamePat", function(value, element) {
  return this.optional(element) || /^[ a-zA-Z0-9._/-]*$/.test(value);
}, "Please enter valid username");
// ------ USERNAME PATTERN End


//------ USER NAME VALIDATION Start
jQuery.validator.addMethod("usernameValidation", function(value, element) {
		
	  var username = $('#username').val();
	  var duplicate = 0;
    $.ajax({
           url : "/portal/user/add-user/checkDuplicateUser/" + username,
           type : "POST",
           async : false,
           success : function(data) {
         	 // alert(data.result);
         	  if(data.result === '0'){  
         		  console.log($('#username-error').css());
         		 $('#usernameIcon').removeClass();
                  $('#usernameIcon').addClass("fa fa-check").css("color", "green");
                  duplicate = 1;
         	  }
         	  else{
         		 $('#usernameIcon').removeClass();
         		  $('#usernameIcon').addClass("fa fa-times").css("color", "red");
         		 duplicate = 0;
         	  }
           },
           error : function(err) {
         	 // alert('error');
                  //$('#UserDetail').hide();
                 // $('#usernameIcon').addClass("fa fa-times").css("color", "red");
                  $('.alert').removeClass('alert-success');
                  $('.alert').addClass('alert-danger');
                  $('.alert').html('Error');
                  $('.alert').show();
           }
    });
	    if (duplicate === 1 ) {
	        return true
	    }
	    else { 
	        return false
	    }
    
}, "Duplicate Username found");
//------ USER NAME VALIDATION END


 
 
//----- AGENT CONTACT NUMBER VALIDATION START



//------ PHONE No. PATTERN Start
jQuery.validator.addMethod("phonePat", function(value, element) {
  return this.optional(element) || /^[(](?!0{3})[0-9]{3}[)]\s{1}(?!0{4})[0-9]{4}[-][0-9]{4}$/.test(value);
}, "Please enter valid Phone No.");
// ------ PHONE No. PATTERN End


 
//Used to format phone number
function phoneFormatter() {
$('#contactNumber').on('input', function() {
var number = $(this).val().replace(/[^\d]/g, '')
if (number.length == 7) {
   number = number.replace(/(\d{3})(\d{4})/, "$1-$2");
} else if (number.length == 11) {
   number = number.replace(/(\d{3})(\d{4})(\d{4})/, "($1) $2-$3");
}
$(this).val(number)
}).attr('maxlength',15);
};
 
$(phoneFormatter);
 
//----- AGENT CONTACT NUMBER VALIDATION END
 
 // --- SET PATTERN FOR REQUIRED ABOVE FIELDS - END ---
 
 
});