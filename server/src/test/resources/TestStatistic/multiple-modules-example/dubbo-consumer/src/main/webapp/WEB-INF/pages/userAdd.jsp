<html>
<body>
	<h1>Add User</h1>
	<hr>

	<form action="<%=request.getContextPath()%>/userAdd" method="post">
		user &nbsp;&nbsp;&nbsp;&nbsp;name:<input type="text" name="userName" /><br /> 
		user EN name&nbsp;:<input type="text" name="userEnName" /><br /> 
		company &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:<input type="text" name="company" /><br />
		<hr>
		<input type="submit" value="Add" />
	</form>
	

</body>
</html>