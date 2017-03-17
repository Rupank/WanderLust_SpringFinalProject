<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <jsp:include page="UserDashBoard.jsp"></jsp:include>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Details Flight</title>
<style>
td {
    height: 30px;
    vertical-align: bottom;
}
th, td {
    padding: 5px;
    text-align: left;
}
body{
color:maroon;
}</style>
</head>
<body>

<form:form action="./ConfirmBooking"  >
<table border="1">
<tr><td>Source:</td><td>${flightPicked.flightSource}</td></tr>
<tr> <td>Destination:</td><td>${flightPicked.flightDestination}</td></tr>
<tr><td> Flight Company:</td><td>${flightPicked.flightCompanyName}</td></tr>
 <tr><td>Date of Flight:</td><td>${}</td></tr>
 <tr><td>Departure Time:</td><td>${flightPicked.flightDepartureTime}</td></tr>
 <tr><td>Arrival Time:</td><td>${flightPicked.flightArrivalTime}</td></tr>
 <tr><td>Amount to be paid:</td><td>${finalValuetobepaid}</td></tr>
 
<tr><td></td><td><input type="submit" value="Confirm Flight Booking"></td></tr>
</table>
</form:form>


 
</body>
</html>