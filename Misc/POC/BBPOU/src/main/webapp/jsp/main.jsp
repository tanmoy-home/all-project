<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title></title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
 
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="resources/js/legend.js"></script>
  
  <style>
    /* Remove the navbar's default margin-bottom and rounded borders */ 
    .navbar {
      margin-bottom: 0;
      border-radius: 0;
    }
    
    /* Set height of the grid so .sidenav can be 100% (adjust as needed) */
    .row.content {height: 450px}
    
    /* Set gray background color and 100% height */
    .sidenav {
      padding-top: 20px;
     /*  background-color: #f1f1f1; */
      height: 120%;
    }
    
    
   .col-sm-2 sidenav{
     height: 100%;
   
   }
    
    /* Set black background color, white text and some padding */
    footer {
      background-color: #555;
      color: white;
      padding: 15px;
      position: fixed;
      height: 90px;
   /*  bottom: 0; */
      width: 100%;
     /* margin: 50px 0; */
    }
    
    /* On small screens, set height to 'auto' for sidenav and grid */
    @media screen and (max-width: 600px) {
      .sidenav {
        height: auto;
        padding: 15px;
      }
      .row.content {height:auto;} 
    }
    
    
  </style>
</head>
<body >

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    
        <div><%@ include file="Header.jsp" %></div>
      <!-- </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
      </ul> -->
    </div>
  </div>
</nav>
  
     <!--  <h1>dashboard page</h1> -->
      <div style="overflow-x: hidden"><div><%@ include file="DashboardMain.jsp" %></div></div>
      
   
    
  </div>
</div>


<footer class="container-fluid text-center">
<div class="row content">
  <!-- <p>Footer Text</p> -->
  <div><%@ include file="footer.jsp" %></div></div>
</footer>

</body>
</html>
