function submitRating(name, rating) {
	$.post("/submit-rating", {name: name, rating: rating});
}

$(function(){
	 $('.auto-submit-star').rating({
	  callback: function(rating, link){	   
	   submitRating($(this).attr('name'), rating);
	  }
	 });
	});