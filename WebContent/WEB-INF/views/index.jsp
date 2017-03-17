<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>


<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
	function phonenumber(inputtxt) {
		var phoneno = /^\d{10}$/;
		if (inputtxt.value.match(phoneno)) {
			return true;
		} else {
			alert("Enter 10 digit Phone Number");
			return false;
		}
	}
</script>
<style>
#submit {
	border-style: solid;
	border-width: 1px;
	border-color: green;
	background-color: green;
	color: white;
	padding: 5px;
	font: bold;
	text-align: center;
}
</style>
</head>
<body>

	<jsp:include page="BlackHeader.jsp"></jsp:include>


	<p style="text-align: center;">
		<c:out value="${requestScope.exist}" />
	</p>


	<div class="container">
		<h2>Enter Information Here</h2>
		<form:form path="userId" action="./userRegister" modelAttribute="user"
			id="myform">
			<div class="form-group">
				<form:label path="userId" class="control-label col-sm-2">Enter User id</form:label>
				<div class="col-sm-10">

					<form:input path="userId" class="form-control" />
					<form:errors path="userId"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<form:label path="userName" class="control-label col-sm-2">Enter user Name</form:label>
				<div class="col-sm-10">

					<form:input path="userName" class="form-control" />
					<form:errors path="userName"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<form:label path="userPhoneNo" class="control-label col-sm-2">Enter User Phone No</form:label>
				<div class="col-sm-10">

					<form:input path="userPhoneNo" class="form-control" />
					<form:errors path="userPhoneNo"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<form:label path="userEmailId" class="control-label col-sm-2">Enter User Email Id</form:label>
				<div class="col-sm-10">

					<form:input path="userEmailId" class="form-control" />
					<form:errors path="userEmailId"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<form:label path="userAddress" class="control-label col-sm-2">Enter User Address</form:label>
				<div class="col-sm-10">

					<form:input path="userAddress" class="form-control" />
					<form:errors path="userAddress"></form:errors>
				</div>
			</div>
			<div class="form-group">
				<form:label path="userPassword" class="control-label col-sm-2">Enter User Password</form:label>
				<div class="col-sm-10">

					<form:input path="userPassword" class="form-control" />
					<form:errors path="userPassword"></form:errors>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-10">
					<input type="submit" value="Register">
				</div>
			</div>

		</form:form>
	</div>

</body>
</html>