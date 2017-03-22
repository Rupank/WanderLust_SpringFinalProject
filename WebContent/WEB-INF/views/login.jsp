<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
 <jsp:include page="BlackHeader.jsp"></jsp:include>
<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link rel="stylesheet" href="static/css/style.css"> 

<SCRIPT type="text/javascript">
    window.history.forward();
    function noBack() { window.history.forward(); }
</SCRIPT>
</head>




<body onload="noBack();"
    onpageshow="if (event.persisted) noBack();" onunload="">

<a href="?lang=en">English</a>|<a href="?lang=de">German</a><br> 
  <div class="login-page">
  <div class="form">
  
    <form:form class="login-form" action="./LoginCheck" commandName="user">
      <spring:message code="label.username"></spring:message><form:input path="userName" placeholder="username"/>   
     <spring:message code="label.password"></spring:message> <form:password path="userPassword" placeholder="password"/>   
      <button type="submit">login</button>

      <p class="message">Not registered? <a href="index.jsp">Create an account</a></p>
   </form:form>
  </div>
</div>
  <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

    <script src="static/js/index.js"></script>

</body>
</html>
 