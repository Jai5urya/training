<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<div align="center">
		<h2>Notebook</h2>
	</div>
	<form>
		<div>
			<style>
.btn {
	border: none;
	background-color: white;
	padding: 14px 28px;
	font-size: 20px;
	cursor: pointer;
}

.btn:hover {
	background: #eee;
}

.fetch {
	color: green;
}

.create {
	color: dodgerblue;
}
</style>
			<div>
				<div align="center">
					<button class="btn create" formaction="create.jsp">Create</button>
					<button class="btn fetch" formaction="fetch.jsp">Fetch</button>
				</div>
	</form>

</body>
</html>