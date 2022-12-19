<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script type="text/javascript" src="js/create.js"></script>
</head>
<body>
	<form id= "updateNotes">
		<div align="center">
			what's your note : 
			     <input type="text" id = "noteID" name=note required><br> 
			tag: <input type="text" id = "tagID" name=tag required><br>
		</div>
		<div align="center">
		<input type="submit" id="updateButton" value="update">
			<a href=index.jsp>Back</a>
		</div>
	</form>
	<p id ='displayMessage' />
</body>
</html>