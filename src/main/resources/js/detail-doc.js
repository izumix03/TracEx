var detailDoc = {};

detailDoc.fuguai="【発生バージョン】\n　CCMXXXXXX以降\n\n【発生条件】\n　常に発生します。\n\n【不具合の詳細】\n　\n【エラーメッセージ】\n　なし\n\n【起動経路】\n　COMPANYメインメニュー⇒";
detailDoc.kinoukaizen="【改善の詳細】\n　";
detailDoc.shinkikinou="【機能の概要】\n　\n【利用方法】\n　当機能の利用に際して、新たな設定は必要ありません。\n\n【起動経路】\n　COMPANYメインメニュー⇒";
detailDoc.houkaisei="【改正内容の概略】\n　\n【参考】\n　";

detailDoc.fuguaiS="【修正後】\n　";
detailDoc.kinoukaizenS="【利用方法】\n　当機能の利用に際して、新たな設定は必要ありません。\n\n【起動経路】\n　COMPANYメインメニュー⇒";
detailDoc.shinkikinouS="【想定ユーザー】\n　\n【業務例】\n　";
detailDoc.houkaiseiS="【対応の詳細】\n　\n【利用方法】\n　当機能の利用に際して、新たな設定は必要ありません。\n\n【起動経路】\n　COMPANYメインメニュー⇒";
detailDoc.fuguaiKaijiS="【回避方法】\n ";

detailDoc.refsTemplate = Handlebars.compile(
		'<a href="http://ccm-server/svn/ccm/{{refs}}" class="list-group-item"style="padding-bottom: 0px;padding-top: 0px;">{{refs}}</a>');

detailDoc.getRefsFileAndRelatedTicket = function(){
	var refs = $('#refs');
	var relatedTicket = $('#doc_related_ticket');
	var ticket_id = $('input[name=id]').val();
	
	//送信データの取得 
	var postData       = {
		ticket_id    : ticket_id
	}
	
	if (ticket_id==0) {
		 return;
	}
	//load画像埋め込み
	refs.css({"background-image":'url("/img/loading.gif")',"background-repeat":"no-repeat","background-position":"top center","background-size":"contain", "background-color":"inherit"});
	relatedTicket.css({"background-image":'url("/img/loading.gif")',"background-repeat":"no-repeat","background-position":"top center","background-size":"contain", "background-color":"inherit"});
	
	//ajax通信の実行
	$.ajax({
		url  : 'ajax/DETAILDOC',
		data : postData
	}).done(function(res) {
			refs.css({"background-image":'',"background-repeat":"","background-position":"","background-size":"", "background-color":""});
			if (res.refs.length == 0){
				refs.append('<label class="col-sm-1 control-label" style="text-align: left;">no refs...</label>');
			}else{
				for (var i=0; i< res.refs.length; i++){
					refs.append(detailDoc.refsTemplate({refs : res.refs[i]}));
				}
			}
			
			relatedTicket.css({"background-image":'',"background-repeat":"","background-position":"","background-size":"", "background-color":""});
			var tickets = res.ticketList;
			for (var i=0; i< tickets.length; i++){
				if (i == 0){
					relatedTicket.append('<a href="/detail?id='+tickets[i]+'">'+tickets[i]+'</a>');
				}else{
					relatedTicket.append('/<a href="/detail?id='+tickets[i]+'">'+tickets[i]+'</a>');
				}
			}
			
	});
}

detailDoc.setProblemAndSolution=function(selectedText, pro, sol){
	switch(selectedText){
		case '不具合修正':
			pro.val(detailDoc.fuguai);
			sol.val(detailDoc.fuguaiS);
			break;
		case '機能改善':
			pro.val(detailDoc.kinoukaizen);
			sol.val(detailDoc.kinoukaizenS);
			break;
		case '新規機能':
			pro.val(detailDoc.shinkikinou);
			sol.val(detailDoc.shinkikinouS);
			break;
		case '法改正対応':
			pro.val(detailDoc.houkaisei);
			sol.val(detailDoc.houkaiseiS);
			break;
		case '不具合開示':
			pro.val(detailDoc.fuguai);
			sol.val(detailDoc.fuguaiKaijiS);
			break;
		default:
    		break;
	}
}


$(function(){
	//refsファイル後読み //子チケット検索
	detailDoc.getRefsFileAndRelatedTicket();
	
	//分類onchange
	$('#doc_type').change(function(){
		var selectedText = $('#doc_type').val();
		var pro = $('#doc_problem');
		var sol = $('#doc_solution');
		detailDoc.setProblemAndSolution(selectedText, pro, sol);
	});
	
	// Submit
	$('#submit-doc').click(function() {
		if (!$('#doc').valid()) {
			return;
		}
		$(this).button('loading');
		$('#doc').submit();
	});
	
});