var Detail = {};
Detail.showPopup = function(){
	var h = window.innerHeight ? window.innerHeight: $(window).height();
	var w = window.innerWidth ? window.innerWidth: $(window).width();
	
  	var left_positon = (w/2) - ($("#popup").width()/2);
  	var top_positon = (h/2)
  	
	// ポップアップのスタイルを定義
	$( "#popup" )
 	.css("position", "fixed")
 	.css("top", top_positon)
 	.css("left", left_positon)
 	.fadeIn(1500);
 	setTimeout(function(){
 		$( "#popup" ).fadeOut(2000);
 	}, 2000);
 	history.forward();
};

$(function() {
	//送信データの取得 
	var postData = {
		id  :  $('input[name=id]').val(),
	}
	
	$.ajax({
		method: 'POST',
		url  : 'ajax/UPDATED',
		data : postData
	}).done(function(res){
		if (res == false){
			return;
		}
		//成功時
		Detail.showPopup();	
	});
	
	
});