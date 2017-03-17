<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="com.mmt.model.bl.FlightBookingBlMMT"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.ArrayList"%>


<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <jsp:include page="UserDashBoard.jsp"></jsp:include>
    <body BACKGROUND="images/bg.jpg">
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Flights</title>
<script>
$(function() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();
	 if(dd<10){
	        dd='0'+dd      
	    } 
	    if(mm<10){
	        mm='0'+mm
	    } 
	today = yyyy+'-'+mm+'-'+dd;
	document.getElementById("departureDate").setAttribute("min", today);
	
  });
  function check(){
	  var source=document.getElementById("source").value;
	  var destination=document.getElementById("destination").value;
	  if(source==destination){
		  alert("Source and Destination cannot be same");
		  document.getElementById("source").value=" ";
		  document.getElementById("destination").value=" ";
	  }
  }
  </script>
 <style>
  #submit{
	border-style: solid;
    border-width: 1px;
    border-color: black;
    background-color: black;
    color: white;
    padding:5px;
    font: bold;
    text-align:center; }
    
#form1{
	text-align:left;
	font-family:serif;
	font-weight: 400;

}
td {
    height: 50px;
    vertical-align: bottom;
}
body{
color:maroon;}
  </style>
</head>
<body>
<form:form action="./SearchFlightBySnD" modelAttribute="flightInfo">
		<table style="padding-bottom: 40px; padding-top: 50px;">
			<tr>
				<td><b>Source</b></td>

				<td>
					 <form:select path="flightSource">
						<c:forEach var="element" items="${flightSourceList}">
							<form:option value="${element.flightSource}">${element.flightSource} </form:option>
						</c:forEach>
					</form:select>
				</td>

			</tr>
			<tr>
				<td><b>Destination:</b></td>
				<td>
				<form:select path="flightDestination">
					<c:forEach var="element" items="${flightDestList}">
						<form:option value="${element.flightDestination}">${element.flightDestination} </form:option>
					</c:forEach>
				</form:select>
				</td>
			</tr>
			<tr>
				<td><b>Depature-Date:</b></td>
				<td><input id="departureDate" name="departureDate" type="date"
					placeholder="dd/mm/yyyy" min="" required /></td>
			</tr>


			<tr>
				<td><b>Number of Seats :</b></td>
				<td><input type="number" name="seats" value="1" min="1" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Search" id="submit"
					onclick="check()" /></td>
			</tr>

		</table>


	</</form:form>
</body>
</html>