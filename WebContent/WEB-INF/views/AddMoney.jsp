<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <jsp:include page="UserDashBoard.jsp"></jsp:include>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Money</title>
<script>

function check(){
	var demo = document.getElementById("demo"), msgFlight,moneyRequired;
	msgFlight = demo.getAttribute("data_messageFlight");
	moneyRequired = demo.getAttribute("moneyRequired");
	if(msgFlight !=null){
	if(document.getElementById("walletBalance").value < moneyRequired ){
		alert("Please Enter the Required Amount");
	}}
	
}
</script>
</head>
<body>
<form:form action="./MoneyAddded" commandName="wallet">
<c:choose>
<c:when test="${not empty messageFlight}">
${messageFlight}   
</c:when>
<c:when test="${not empty messageHotel}">
${messageHotel}   
</c:when>

</c:choose> 

<fieldset>

Amount&nbsp;&nbsp;  :&nbsp; &nbsp;  

<form:input path="walletBalance"  placeholder="Enter Amount" id="walletBalance" onblur="check();" /><br>
<button type="submit">Add</button>


<div id ="demo" > data_messageFlight= ${messageFlight} moneyRequired=${moneyToBeAdded}</div>
</fieldset>
<br>
<br>
</form:form>
</body>
</html>