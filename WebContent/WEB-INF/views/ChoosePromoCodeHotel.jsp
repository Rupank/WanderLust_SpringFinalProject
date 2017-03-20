<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="UserDashBoard.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Choose Promo</title>
<script>
	function loadDoc() {
		document.getElementById("demo").innerHTML = "Congrats! You got a discount of "
				+ '${pdiscountHotel}'
				+ "%<br>Your Promotion ID is "
				+ ' ${pidHotel}';

	}
</script>
<style>
#applyPromo, #continue {
	border-style: solid;
	border-width: 1px;
	border-color: red;
	background-color: red;
	color: white;
	padding: 5px;
	font: bold;
	text-align: center;
}
</style>
</head>
<body>
	<form:form action="./PaymentHotel" commandName="pickedPromoCode">


		<p>
			<b>Select a Promo Code</b>
			<form:select path="promotionId">
				<c:forEach var="element" items="${arrayListPromoHotel}">
					<form:option value="${element.promotionId}">${element.promotionName} </form:option>
				</c:forEach>
			</form:select>
			&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;
			&nbsp;&nbsp; &nbsp;&nbsp; <input type="button" id="applyPromo"
				value="Apply" onclick="loadDoc();" />
		</p>
		<p id="demo"></p>

		<p>
			<input type="submit" id="continue" value="Pay Now" />
		</p>

	</form:form>
</body>
</html>