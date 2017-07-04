$(document).ready(function(){
 
 
      
       // PASSWORD VALIDATION START
      
       $('form').validate({
              rules:{
                     newPassword:{
                           required:true,
                           passwordPat:true
                     },
                     confirmpassword:{
                           required:true,
                           equalTo: "#newPassword"
                          
                     }
              },
              messages:{
                     newPassword:{
                           required:"Please enter New Password",
                     },
                     confirmpassword:{
                           required:"Please Re-Enter Password",
                           equalTo:'Password did not match'
                     }
              }
       });
      
       // ------ PASSWORD PATTERN Start
       jQuery.validator.addMethod("passwordPat", function(value, element) {
              return this.optional(element) || /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])(?!\\s).{8,}$/.test(value);
       }, "Please enter valid Password");
      
      
      
       //-----PASSWORD PATTERN Start
      
       // PASSWORD VALIDATION START
      
      
      
       autoHide("#errorBox", 3000);
       autoHide("#invalid_username", 5000);
       $("#btnSubmit").click(function () {
              var password = $("#newPassword").val();
              var confirmPassword = $("#confirmpassword").val();
              if (password != confirmPassword) {
                  alert("Passwords do not match.");
                  return false;
              }
              return true;
       });
      
       $("#btn_login").click(function () {
                 var funame = $("#username").val();
                 var fpassword = $("#password").val();
                 if(funame == ''){
                        $("#errorBox").html("Please enter your username.");
                        $("#username").focus();
                 }
                 if(fpassword == ''){
                        $("#errorBox").html("Please enter your password.");
                        $("#password").focus();
                 }
                 if(funame == '' && fpassword == ''){
                        $("#errorBox").html("Please enter your username and password.");
                        $("#username").focus();
                 }
                 if(funame=='' || fpassword=='' ){
                        $("#errorBox").show();
                        return false;
                 }
               
        });
});