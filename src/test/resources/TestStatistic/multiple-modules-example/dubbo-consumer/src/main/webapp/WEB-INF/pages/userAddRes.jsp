<html>
<body>
	<h1>save success:</h1>
	<hr>

	<form action="<%=request.getContextPath()%>/userAdd" method="post">
		user &nbsp;&nbsp;&nbsp;&nbsp;name:<input type="text" name="userName" readonly="readonly" value=${saveUser.userName} /><br /> 
		user EN name&nbsp;:<input type="text" name="userEnName" readonly="readonly"   value=${saveUser.userEnName}  /><br /> 
		company &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:<input type="text" name="company"  value=${saveUser.company}  readonly="readonly"  /><br />
		DB_ID: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:<input type="text" name="userId"  value=${saveUser.userId}  readonly="readonly"  /><br />
		<hr>
	</form>
	

</body>
</html>