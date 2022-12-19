<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script type="text/javascript" src="js/fetch.js"></script>
</head>
<body>
<div align = "center">
<form id = "searchNotes">
		Search : <input id = "tagID" type=text name=tag> <br>       
		<input id ="submitID" type="submit" value="bytag" name="button">
	</form>
</div>
	<p id = "showNotes" />
	<div id="div1" align="center">
	<table id = "mytable" border=1>
	</table>
	
	</div>
	
</body>
</html>