
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
	

    <title>Customer 360 Home Page</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/modern-business.css" rel="stylesheet">
    <style>
    .error_msg_class1 {
    display: none;
    color : red;
    font-size:75%;
	}
	.error_msg_class2 {
    display: none;
    color:red;
    font-size:75%;
	}
	.error_msg_class3 {
    display: none;
    color:red;
    font-size:75%;
	}
	footer {
      background-color: #555;
      color: white;
      padding: 15px;
      position: fixed;
    height: 60px;
    bottom: 0;
    width: 100%;
    }
    
   /*  #loading {
   background-color: #fff;
   width: 100%;
   height: 100%;
   position: absolute;
   z-index: 999;
   text-align:center;
    top:0;  
    bottom:0; 
   right:0;
    left:0;
  /*   left     : 50%;
    top      : 50%;
    margin-left : -100px; 
    margin-top  : -100px; */
} */
    </style>

    <!-- Custom Fonts -->
    
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css" />
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src= "resources/js/jquery.blockUI.js"></script>
   
   	<style>
	
		.carousel-inner > .item > img,
		.carousel-inner > .item > a > img {
		width: 60%;
		margin: auto;
  }
	</style>
	    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="js/jquery.js"></script>

<!-- script for validate log in credentials -->

<script type="text/javascript">


 

$(document).ready(function(){
	
	 

	
	
	$('#user_username').keyup(function(e) {
	    if (e.keyCode == 13) {
	        $('#loginBtn').click();
	    }
	});
	
	$('#user_password').keyup(function(e) {
	    if (e.keyCode == 13) {
	        $('#loginBtn').click();
	    }
	});
		
	
	$("#loginBtn").click(function(){ 
		var useer_Name = document.getElementById('user_username').value;
		var userName=useer_Name.toLowerCase();
        var pwd = document.getElementById('user_password').value;
        
        if (userName.length == 0) {
			$("#error_msg1").show();
			$("#error_msg2").hide();
			$("#error_msg3").hide();
        } else if (pwd.length == 0) {
            //alert("Please input the password");
            $("#error_msg1").hide();
            $("#error_msg2").show();
			$("#error_msg3").hide();
        	
        }   
        else  {
        	
        	var uri = "${pageContext.request.contextPath}/validateCredentials";
        	
        	//alert(uri);
		$.ajax ({
			//url: "http://172.25.4.173:8080/Customer360/validateLoginCredentials",
			
			//url: "http://172.18.19.150:8083/Customer360/validateLoginCredentials",
			
		//	url :"http://172.25.4.244:8080/UserMgmtService/validateLoginCredentials",
		
		
			url :uri,
			
		
	//	url : "http://172.18.19.150:8083/Customer360/validateCredential",
			
			type: 'POST',
			datatype : "json",
			contentType: "application/json",
			async:true,
			/* header:{ "Access-Control-Allow-Headers" : "http://172.18.19.96:8080" }, */
				
			data: JSON.stringify({user_id:userName,password:pwd}),
			/* data : ({user_id: userName,password:pwd}), */
			cache:false, success: function (data, textStatus, xhr) {
			
			//alert(data);
			//alert(data.user_name);
			
			if (data.errFound)
				{
				///alert("invalid");
				$("#error_msg1").hide();
	            $("#error_msg2").hide();
				$("#error_msg3").show();
				$("#error_msg3")[0].innerText(data.errInfo.errDesc);
			}
			else
				{
				//alert(data.user_name);
				uri = "${pageContext.request.contextPath}/main";
				$("#error_msg1").hide();
	            $("#error_msg2").hide();
				$("#error_msg3").hide();
				//alert("logging into CEO dashboard");
				//login to user//
				$("#loading").show();
				window.location.href= uri;
		
				}

			},

			error: function(jqXHR, textStatus, errorThrown) {
			alert(errorThrown);
			//$("#error_msg3").show();
			} 
			});	
        }
		
		//document.forms["myForm"].reset();
	}); 
	
	/* end of validation */
	
	$('#signUp').on('click',function(){
		$('#formUserRegister').reset();
                
    });
	 
	 $.clearFormFields = function(area) {
		$(area).find('input[type="text"],input[type="email"],textarea,select').val('');
	};
	
	$('#formUserRegister').on('submit',function(){
		$('#register').attr('aria-hidden',true);
        alert("Registration Done!! We will getback to you...");
    });
	
	
	
	$('#btnForgotPassword').on('click',function(){
                
                if ($('#inputEmailForgot').val()==="") {
					alert("Specify the email address.");
                    //prependAlert("#spacer","Specify the email address.");
                }
                else {
                    alert("Sorry!! Process is not yet completed.");
                }
                
    });
	
	$( '#userRegistration' ).on('hide.bs.modal', function(){
		alert("I want this to appear after the modal has opened!");
	});
	
		
});

	  $(document).ajaxStart($.blockUI({ message: '<img src="resources/images/gif-load.gif" />',
		    css: { width: '30%', border:'0px solid #FFFFFF',cursor:'progress',backgroundColor:'#FFFFFF'},
		    overlayCSS:  { backgroundColor: '#FFFFFF',opacity:0.0,cursor:'progress'}                                     
		  })).ajaxStop($.unblockUI);  

	</script>
</head>
<body>
<!-- Nav Bar -->
	<nav class="navbar navbar-inverse navbar-fixed-top">
	  <div class="container-fluid">
		<div class="navbar-header">
		  <a class="navbar-brand" href="#">Customer 360</a>
		</div>
		<div>
		  <ul class="nav navbar-nav">
			<li class="active"><a href="login.html">Home</a></li>
			<li><a href="#">About</a></li>
			<li><a href="#">Contact</a></li>
		  </ul>
		  <ul class="nav navbar-nav navbar-right">
			<li><a id="signUp" name="signUp" data-toggle="modal" href="#userRegistration"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
			<li class="dropdown">
				<a class="dropdown-toggle" href="#" data-toggle="dropdown">Log In <strong class="caret"></strong></a>
			
				
				<div class="dropdown-menu" style="padding: 15px; padding-bottom: 10px;">
					  <!--<form action="[YOUR ACTION]" method="post" accept-charset="UTF-8">-->
					  <form class="form-horizontal" role="form" autocomplete="off">
							<input id="user_username" style="margin-bottom: 15px;" type="text" placeholder="Enter your Username" size="30" value="" />
							
							<div id = "error_msg1" class = "error_msg_class1">Please input the User Name!!!</div>
							<input id="user_password" style="margin-bottom: 15px;" type="password" placeholder="Enter the Password" size="30" value="" />
							
							<div id = "error_msg2" class = "error_msg_class2">Please input the Password !!</div>
							<!--<input id="user_remember_me" style="float: left; margin-right: 10px;" type="checkbox" name="user[remember_me]" value="1" />
							<label class="string optional" for="user_remember_me"> Remember me</label>-->
							<input class="btn btn-primary" style="clear: left; width: 100%; height: 32px; font-size: 13px;"  id="loginBtn" value="Log In" />
					 <br/>
					 
					 <div id = "error_msg3" class = "error_msg_class3">Please enter the valid credential!!</div>
					 </form>
					<br/>
					
					<a data-toggle="modal" role="button" href="#forgotPasswordModal">Forgot Username or Password?</a>
				</div>
			</li>
		  </ul>
		</div>
	  </div>
	</nav>
 <!--End of NaV Bar--> 
 
 
 <!-----------------------------------------------------Image Carousel part-------------------------------------------------->
 
	<div class="container">
	<br>
	<div id="myCarousel" class="carousel slide container-fluid" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
      <li data-target="#myCarousel" data-slide-to="1"></li>
      <li data-target="#myCarousel" data-slide-to="2"></li>
      <li data-target="#myCarousel" data-slide-to="3"></li>
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner" role="listbox">

      <div class="item active">
        <img src="images/images1.jpg" alt="c1" width="460" height="120">
        <div class="carousel-caption">
          <h3>Corrective Action</h3>
          <p>Allows viewer to identify trends before they become an issue for corrective action.</p>
        </div>
      </div>

      <div class="item">
        <img src="images/images2.jpg" alt="c2" width="460" height="120">
        <div class="carousel-caption">
          <h3>Increase Revenue</h3>
          <p>Increase in the bottom line revenue.</p>
        </div>
      </div>
    
      <div class="item">
        <img src="images/images3.jpg" alt="c2" width="460" height="120">
        <div class="carousel-caption">
          <h3>Operational Efficiency</h3>
          <p>Track operational efficiency.</p>
        </div>
      </div>

      <div class="item">
        <img src="images/images4.jpg" alt="c4" width="460" height="120">
        <div class="carousel-caption">
          <h3>Merchant Management</h3>
          <p> Monitoring speed of merchant activation.</p>
        </div>
      </div>
  
    </div>

    <!-- Left and right controls -->
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
  </div>
</div>
<!--End of Image Carousel part-->
<!-- <div id="loading">
        <img src="resources/images/gif-load.gif" alt="Loading..." />
   </div>
 -->

<!---------------------------------------------User Registration Form-------------------------------------------------------->
	<div id="userRegistration" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
					<a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
					<h3>Registration Form</h3>
				</div>
				<form id="formUserRegister" role="form" name="formUserRegister" method="post">
					<div class="modal-body">					
							<div class="well well-sm"><strong><span class="glyphicon glyphicon-asterisk">
								</span>Required Field</strong>
							</div>
							<div class="form-group">
								<label for="InputName">Enter Name</label>
								<div class="input-group">
									<input type="text" class="form-control" name="InputName" id="InputName" placeholder="Enter Name" required>
									<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
								</div>
							</div>
							<div class="form-group">
								<label for="InputEmail">Enter Email</label>
								<div class="input-group">
									<input type="email" class="form-control" id="InputEmail" name="InputEmail" placeholder="Enter Email" required>
									<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
								</div>
							</div>
							<div class="form-group">
								<label for="InputEmail1">Confirm Email</label>
								<div class="input-group">
									<input type="email" class="form-control" id="InputEmail1" name="InputEmail1" placeholder="Confirm Email" required>
									<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
								</div>
							</div>
							<div class="form-group">
								<label for="InputMessage">Address</label>
								<div class="input-group">
									<textarea name="InputMessage" id="InputMessage" class="form-control" rows="5" required></textarea>
									<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>
								</div>
							</div>
							
						
					</div>
					<div class="modal-footer" >
						<a href="#" data-dismiss="modal" aria-hidden="true" class="btn">Cancel</a> 
						<input type="submit" name="register" id="register" value="Register" class="btn  btn-success pull-right">
					</div>
				</form>
			</div>
        </div>
</div>
<!-- End of User Registration Form-->
<!-- Forgot Password Form-->

	<div id="forgotPasswordModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
					<a href="#" data-dismiss="modal" aria-hidden="true" class="close">×</a>
					<h3>Password Lookup</h3>
				</div>
				<div class="modal-body">
					<form class="form form-horizontal" id="formForgotPassword">    
						<div class="control-group">
							<label class="control-label" for="inputEmailForgot">Email</label>
							<div class="controls">
								<input name="_csrf" id="token" type="hidden" value="ouap3fLi-VYO4yAWqIB9Ivh0PVxVSANlAJE4">
								<input type="email" name="email" id="inputEmailForgot" placeholder="you@youremail.com" required="">
								<span class="help-block"><small>Enter the email address you used to sign-up.</small></span>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer" >
					<a href="#" data-dismiss="modal" aria-hidden="true" class="btn">Cancel</a> 	
					<a href="#" data-dismiss="modal" id="btnForgotPassword" class="btn btn-success">Reset Password</a>
					
				</div>
			</div>
        </div>
</div>
<!-- End Forgot Password Form-->
</body>
<br><br><br><br><br></br>
<div>
<footer class="container-fluid text-center">
     <div><%@ include file="footer.jsp" %></div> 
</footer>
</div>
<script type="text/javascript" src="Scripts/ExercisePage3JS.js"> </script>

</html>
