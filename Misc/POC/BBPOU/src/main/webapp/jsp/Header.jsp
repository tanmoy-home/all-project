<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<!-- Bootstrap Core CSS -->
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">

<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script language="javascript" type="text/javascript"> 

var name='<%= session.getAttribute("UserName") %>' ;

logout = function() {
	location.href = "${pageContext.request.contextPath}/logout";
	}
//alert("user name "+name);

</script>
</head>
<body>
<!-- Start Header-->
<nav class="navbar navbar-default navbar-fixed-top" >
  <div class="container-fluid" id="custom-header">
   <div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#TIB-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				
			</div>
    
	 <div class="navbar-header col-lg-8" >
      <ul class="nav navbar-nav active">
	  
					<h3><font color="#FFFFFF"> Welcome to Customer360</font></h3>
					
					
		</ul>
		</div>
		<div class="collapse navbar-collapse" id="TIB-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right" id="bs-example-navbar-collapse-1">
				
					<li><img src="resources/images/RSSoftware.png" alt="" height="65" width="120" ></li>	
					
				  </ul>
				 &nbsp;&nbsp;
				  <%! String ctx="${pageContext.request.contextPath}/logout"; %>  
				  <ul class="nav navbar-nav navbar-right">
				  <li><a href="#"><span class="glyphicon glyphicon-user"></span>Signed in <strong><label id="user_name"><%= session.getAttribute("UserName") %></label></strong></a></li>
					<li><a href="Customer360-dashboard-ceo.html"><span class="glyphicon glyphicon-home"></span>Home</a></li>
					<li><a href= "javascript:logout()"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
				  </ul>
			</div>	
    
  </div>
</nav>
</body>
</html>