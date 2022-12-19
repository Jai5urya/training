/**
 * 
 */
$(document).ready(function() {
	$('#searchNotes').submit(function() {
		var tag = $('#tagID').val();
		$.ajax({
			url: 'Search',
			type: 'POST',
			data: JSON.stringify({
				tag: tag,
			}),
			datatype: "json",
			success: function(responseString) {
				var object = JSON.parse(responseString);
				let status = object.status;
				alert(status);
				console.log(object.data[0]);
				if (status) {
					var tbl = $("<table/>").attr("id", "mytable");
					$("#div1").append(tbl);
					$("#mytable").append(
						"<thead><tr><td width=75>id</td><td width=150>tag</td><td width=200>note</td></tr></thead> ");
					for (var i = 0; i < object.data.length; i++) {
						var tr = "<tr>";
						var td1 = "<td>" + object.data[i]["id"] + "</td>";
						var td2 = "<td>" + object.data[i]["tag"] + "</td>";
						var td3 = "<td>" + object.data[i]["note"] + "</td></tr>";
						$("#mytable").append(tr + td1 + td2 + td3);
					}
	
				}
				else { 
					alert("no data available");
				}
			}
		});
		return false;
	});

});