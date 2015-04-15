var Common = {};

/**
 * typeahead:設定
 */
Common.typeaheadOptions = {
	highlight : true,
	hint : false,
	minLength : 0
};

Common.typeaheadEmpty = '<div class="text-muted typeahead-empty">There are no matches.</div>';

/**
 * Autocomplete後の値検証
 * @param input 入力要素
 * @param kind マスタの種類
 * @param additional 追加パラメータ
 */
Common.validateAutocomplete = function(input, kind, additional) {
	var value = input.val().trim();
	if (!value) {
		return;
	}

	$.ajax({
		url : 'ajax/' + kind,
		data : $.extend({value : value}, additional)
	}).done(function(res) {
		if (!res) {
			input.val('');
		} else {
			input.val(value);
		}
	}).fail(function(res) {
		alert(res.statusText);
	});
};

/**
 * typeahead:milestone
 */
Common.typeaheadMilestone = function(input) {
	var b =new Bloodhound({
		datumTokenizer : Bloodhound.tokenizers.obj.whitespace('name'),
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		limit : 10,
		remote : 'ajax/milestones?query=%QUERY'
	});
	b.initialize();

	input.typeahead(Common.typeaheadOptions, {
		displayKey: 'name',
		source : b.ttAdapter(),
		template : {
			empty : Common.typeaheadEmpty
		}
	}).blur(function(e) {
		Common.validateAutocomplete($(this), 'milestone');
	});
};

/**
 * typeahead:member
 */
Common.typeaheadMember = function(input) {
	var b =new Bloodhound({
		datumTokenizer : Bloodhound.tokenizers.obj.whitespace('id'),
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		limit : 10,
		remote : 'ajax/members?query=%QUERY'
	});
	b.initialize();

	input.typeahead(Common.typeaheadOptions, {
		displayKey: 'id',
		source : b.ttAdapter(),
		templates : {
			empty : Common.typeaheadEmpty,
			suggestion: Handlebars.compile('<p>{{id}} : {{name}}</p>')
		}
	}).blur(function(e) {
		Common.validateAutocomplete($(this), 'member');
	});
};

Common.menbersFormatRepo = function(repo) {
    if (repo.loading) return repo.text;

    var markup = '<div class="clearfix">' +
    '<div clas="col-sm-5">' +
    '<div class="clearfix">' +
    '<div class="col-sm-2">' + repo.id + '</div>' +
    '<div class="col-sm-2"><i class="fa fa-code-fork"></i> ' + repo.name + '</div>' +
    '</div>';

    if (repo.description) {
      markup += '<div>' + repo.description + '</div>';
    }

    markup += '</div></div>';

    return markup;
 };
 
Common.simpleFormatRepo = function(repo) {
    if (repo.loading) return repo.text;

    var markup = '<div class="clearfix">' +
    '<div clas="col-sm-5">' +
    '<div class="clearfix">' +
    '<div class="col-sm-12">' + repo.id + '</div>' +
    '</div>';

    markup += '</div></div>';

    return markup;
 };
 

Common.FormatRepoSelection =function(repo) {
  return repo.id;
};

Common.milestonesFormatRepo = function(repo) {
    if (repo.loading) return repo.text;
	if(repo.completed != null && repo.completed != '') {	
		completed = "完了";
	}else{
		completed = "";
	}
    var markup = '<div class="clearfix">' +
    '<div clas="col-sm-5">' +
    '<div class="clearfix">' +
    '<div class="col-sm-2">' + repo.id + '</div>' +
    '<div class="col-sm-2"><i class="fa fa-code-fork"></i> ' + completed + '</div>' +
    '</div>';

    markup += '</div></div>';

    return markup;
};

 
Common.select2Load = function(select, url, formatRepo) {
	select.select2({
		ajax: {
			url: url,
			dataType: 'json',
			delay: 250,
			data: function (params) {
				var queryParameters = {
		  　　　		query: params.term
   　　				}
    	  		return queryParameters;
    		},
    		processResults: function (data) {
       		return {
        		results: data
	      	};
		},
    	cache: true
  	},
	//escapeMarkup: function (markup) { return markup; },
	minimumInputLength: 1,
	templateResult: formatRepo,
	templateSelection: Common.FormatRepoSelection,
	width: '100%',
	allowClear: true
  	});
};


/**
 * typeahead:subsystem
 */
Common.typeaheadSubsystem = function(input, getPid) {
	var b =new Bloodhound({
		datumTokenizer : Bloodhound.tokenizers.obj.whitespace('name'),
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		limit : 10,
		remote : {
			url : 'ajax/subsystems?',
			replace : function(url, query) {
				return url + 'query=' + query + '&pid=' + getPid()
			}
		}
	});
	b.initialize();

	input.typeahead(Common.typeaheadOptions, {
		displayKey: 'name',
		source : b.ttAdapter(),
		templates : {
			empty : Common.typeaheadEmpty,
			suggestion: Handlebars.compile('<p>{{name}} ({{id}})</p>')
		}
	}).blur(function(e) {
		Common.validateAutocomplete($(this), 'subsystem', {pid : getPid()});
	});
};

/**
 * typeahead:function_group
 */
Common.typeaheadFunctionGroup = function(input, getParam) {
	var b =new Bloodhound({
		datumTokenizer : Bloodhound.tokenizers.obj.whitespace('function_group'),
		queryTokenizer : Bloodhound.tokenizers.whitespace,
		limit : 10,
		remote : {
			url : 'ajax/function_groups?',
			replace : function(url, query) {
				var param = getParam();
				return url + 'query=' + query
					+ '&pid=' + param['pid'] + '&version=' + param['version'] + '&subsystem=' + param['subsystem']
			}
		}
	});
	b.initialize();

	input.typeahead(Common.typeaheadOptions, {
		displayKey: 'name',
		source : b.ttAdapter(),
		templates : {
			empty : Common.typeaheadEmpty,
			header : function() {
				var param = getParam();
				return '<h5 class="typeahead-header">' + param['version'] + ' / ' + param['subsystem'] + '</h5>';
			},
			suggestion: Handlebars.compile('<p>{{name}}</p>')
		}
	}).blur(function(e) {
		Common.validateAutocomplete($(this), 'function_group', getParam());
	});
};

Common.login = function(){
	var user_id= $('#modal-user_id').val();	
	var password = $('#modal-password').val();
	
	//送信データの取得 
	var postData = {
		user_id   : user_id,
		password  : password,
	}
	
	$.ajax({
		url  : 'ajax/LOGIN',
		data : postData
	}).done(function(res){
		if (res == null){
			//失敗時
			$('#loginModalLable').text("sign in ...you failed to sign in....");
			$('#modal-user_id').val("");
			$('#modal-password').val("");
			return;
		}
		//成功時
		$('#modal-open').text(res.user_id+'  '+res.name);
		$("#loginModal").modal("hide");
		$("#modal-open").removeAttr("href");
		
		//index画面ならログアウトボタンを表示
		if ($('#index') != null){
			$('.login-form').remove();
			$('#login-div').append('<div class="row" style="height: 212px;"></div>');
		    $('#login-div').append('<h2 class="form-signin-heading" >Now signed in..</h2>');
		    $('#login-div').append('<input type="hidden" name="sign-out" value="out"></input>');
		    $('#login-div').append('<button class="btn btn-lg btn-primary btn-block" type="submit">Sign Out</button>');
		}
		
		//更新画面なら#data-update divを表示させる
		if ($('.data-update')){
			$('.data-update').show();
		}
		$("#modal-open").attr("href", "user?id="+user_id);
	});

}

Common.searchTicket = function(){
	var text = $('#ticket_search').val();
	window.location.href = '/detail?id='+ text.slice(1);
	return false;
}

$(function(){
	//ログインAjax
	$('#login-button').click(Common.login);
	$('#loginModal').on('shown.bs.modal', function () {
    	$('#modal-user_id').focus();
	});
	
	//チケット検索
	$('#ticket_search').keypress(function(e) {
		var c = e.keyCode;
		if (c == 13){
			//#チケットID、ではない
			if (!e.target.value.match(/#[0-9]+/)){
				$("#elastic").attr({"action":"ELASTICSEARCH", "method":"GET"});
				$('#elastic').submit();
				return false;
			}
			return Common.searchTicket();
		}
		//var m = String.fromCharCode(c);
		//if("0123456789\b\r".indexOf(m, 0) < 0) return false;
		return true;
	});
	//チケット検索ボタン
	$('#ticket-search-button').click(Common.searchTicket);
	
	
	//パスワードでEnterもOK
	$('#modal-password').keypress(function(e){
		if (e.keyCode == 13){
			Common.login();
		}
	});
	
	//データ操作機能がある場合、ログイン状態を確認
	if (($('#userId').val() !='admin') && ($('.data-update'))) {
		$('.data-update').show();
	}
	

});