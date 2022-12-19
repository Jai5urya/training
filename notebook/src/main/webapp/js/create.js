/**
 * 
 */


$(document).ready(function(){
	 $('#updateNotes').submit(function(){
	//	 $("#displayMessage").hide();
		 var note = $("#noteID").val();
		 var tag = $("#tagID").val();
		 $.ajax({
			 url:'Notes',
			 type:'POST',
			data: JSON.stringify({
				note: note,
				tag: tag,
			}),
			datatype : 'json',
			success: function(responseString) {
				alert(responseString);
				responseString = JSON.parse(responseString); 
				console.log(response);
			 if (responseString.status) {
				
			} else {
				$("#displayMessage").show();
			}
		  },
		 error:function(responseString){
			 alert(responseString);
		  }
		 });
	 });
});

 