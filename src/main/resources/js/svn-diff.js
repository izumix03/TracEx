var SvnDiff = {};

SvnDiff.displayTemplete = Handlebars.compile(
	'<div id="{{value}}" class="compare"></div>');

SvnDiff.displayErrorTemplate = Handlebars.compile(
	'<div class="col-sm-11"><a href="{{link}}">差分取得できません。</a></div>'
	);

SvnDiff.getSvnDiff = function(e){
	var target = $(e.currentTarget);
	
	//差分データがない場合のみajaxで取得
	if (target.next('div').children().length == 1 && !target.children().length){
		//送信データの取得 
		var ulIndex = e.pageX + '_' + e.pageY;
		var postData       = {
			rev       : $('#rev').val(),
			fromPath  : target.text(),
			toPath    : target.text()
		}

		//ajax通信の実行
		$.ajax({
			url  : 'ajax/SVNDIFF',
			data : postData
		}).done(function(res) {			
			//load-msg hide
			target.next('div').children('.load-msg').fadeOut(200);
			for (var j in res){
				if((res[j].preCodeString == null||res[j].preCodeString == '') && (res[j].aftCodeString == null ||res[j].aftCodeString == '')){
					continue;
				}
				//ul要素に追加
				var ulId =  ulIndex + '_' + j;
				target.next('div').append(
					SvnDiff.displayTemplete(
						{
						value : ulId
						}
					)
				);
				//追加したdivに差分データを設定し、mergelyを設定
				$('#'+ulId).mergely({
					editor_height: 'auto',
					cmsettings: {readOnly: true, lineNumbers: true},
					lhs: function(setValue) {
						setValue(res[j].preCodeString);
					},
					rhs: function(setValue) {
						setValue(res[j].aftCodeString);
					},
					resized:function(e){
						var correctWidth = (target.width() - 20 - $('.mergely-canvas').width() -($('.mergely-margin').width()*2))/2;
						$('.mergely-column').width(correctWidth);
						
					}
				});				
			}
			if(target.next('div').children().length <= 1){
				var linkUrl = "http://ccm-server/svn/ccm/"+target.text();
				target.next('div').append(SvnDiff.displayErrorTemplate({link : linkUrl}));
			}
		});
	}
	
	//アコーディオン実行
	target.toggleClass("active");  
	target.next("div").slideToggle();
}

$(function(){
	//OnClickEventを設定
	$('.change-code-head').click(SvnDiff.getSvnDiff);
});