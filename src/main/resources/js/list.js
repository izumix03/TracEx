var List = {};

List.sortTemplate = Handlebars.compile(
		'<li class="list-group-item"">' +
		'  <span>{{displayText}}</span>' +
		'  <input type="hidden" name="sort-field" value="{{field}}"></input>' +
		'  <select class="sort-select" name="sort-method">' +
		'    <option value="asc">Asc</option>' +
		'    <option value="desc">Desc</option>' +
		'  </select>' +
		'  <span class="glyphicon glyphicon-remove remove-field" aria-hidden="true"></span>' +
		'</li>');

List.displayTemplate = Handlebars.compile(
		'<li class="list-group-item"">' +
		'  <span>{{displayText}}</span>' +
		'  <input type="hidden" name="display-field" value="{{field}}"></input>' +
		'  <span class="glyphicon glyphicon-remove remove-field" aria-hidden="true"></span>' +
		'</li>');

List.groupByTemplate = '<div class="pull-right"><input class="groupBy" name="groupBy" type="checkbox" checked="true"/><span>グループ化対象&nbsp;&nbsp;</span></div>'

List.scratchMultiList = Handlebars.compile(
	 '<li class="select2-selection__choice">'+
	   '<span class="select2-selection__choice__remove" onclick="$(this).parent().remove();">×</span>'+
	   '<span>{{text}}</span>'+
	 '</list>');
	   

/**
 * Field削除
 */
List.onRemoveField = function(e) {
	$(this).off().parent().remove();
	List.addGroupByCheckBox();
};

List.addGroupByCheckBox = function(){
  if ($('#sort-targets li:eq(0)').children().length == 4){
    $('#sort-targets li:eq(0)').append(List.groupByTemplate);
  }
}

/**
 * Field追加
 */
List.onFieldAdd = function(e) {
	var parentId = $(this).parent().parent().attr('id');
	var inputSelector, template, targetId, target;
	
	if (parentId == 'sort-candidates') {
		inputSelector = 'input[name=sort-field]';
		template = List.sortTemplate;
		targetId = 'sort-targets';
		target = $('#sort-targets');
	} else {
		inputSelector = 'input[name=display-field]';
		template = List.displayTemplate;
		targetId = 'display-targets';
		target = $('#display-targets');
	}

	var displayText = $(this).prev().text();
	var field       = $(this).prev().attr('value');
	
	var exist = false;
	$('#' + targetId + ' ' + inputSelector).each(function() {
		if ($(this).val() == field) {
			exist = true;
			return false;
		}
	});
	if (exist) {
		return;
	}

	target.append(template({displayText : displayText, field : field}));
	target.find('.remove-field').off().on('click', List.onRemoveField);
	
	List.addGroupByCheckBox();
};

List.onRemoveSratchMultiSelect = function(e, tar){
	var tarLi = tar.parent();
	var tarUl = tarLi.parent();
	var tarText = tar.next(['span']).text();
	
	
	$('#'+ tarUl.attr('target')).children(['option']).each( function(){
    	if($(this).val() == tarText) {
        	$(this).remove();
    	}
	});
	tarLi.remove();
};

List.onDefineTextScratchMultiSelect = function(e, tar){
	//表示処理
	var text = tar.val();
	if (text == null || text == ''){
		$('#search').button('loading');
		$("#criteria").attr({"action":"LIST", "method":"GET"});
		$('#criteria').submit();
		return false;	
	}
	
	//※重複は拒否
	var duplication = false;
	tar.parent().parent().children(['li:eq(0)']).children(['span']).each(function(){
		if($(this).text() == text){
			duplication = true;
		}
	});
	if (duplication == true) {
		tar.val('');
		return false;
	};

	tar.parent().parent().prepend(List.scratchMultiList({text : text}));
	$('.scratch-multi-select-remove').click(List.onRemoveSratchMultiSelect);	
	tar.val('');
			
	//選択処理
	$('#'+tar.attr('target')).append('<option selected="selected" value='+text+'></option>');
	return false;		
};

List.criteriaSubmit = function(e) {
	if (e.keyCode == 13 && (e.target.value==null || e.target.value =='')){
		$('#search').button('loading');
		$("#criteria").attr({"action":"LIST", "method":"GET"});
		$('#criteria').submit();
		return false;
	}
	return true;
}

$(function() {
	//select2
	$('.simple-select').select2({ width: '100%' });
	
	//select2_ajax
	Common.select2Load($('#criteria select[name=evaluator]'), 'ajax/MEMBERS',    Common.menbersFormatRepo);
	Common.select2Load($('#criteria select[name=owner]'),     'ajax/MEMBERS',    Common.menbersFormatRepo);
	Common.select2Load($('#criteria select[name=milestone]'), 'ajax/MILESTONES', Common.milestonesFormatRepo);
	Common.select2Load($('#criteria select[name=client]'),    'ajax/CLIENTS',    Common.simpleFormatRepo);
	
	// 検索実行
	$('#search').click(function() {
		$(this).button('loading');
		$("#criteria").attr({"action":"LIST", "method":"GET"});
		$('#criteria').submit();
	});
	
	//Enterキーで検索
	$('#criteria .form-control').keypress(function(e) {
		if (e.keyCode == 13){
			$('#search').button('loading');
			$("#criteria").attr({"action":"LIST", "method":"GET"});
			$('#criteria').submit();
			return false;
		}
	});
	//イベント設定し直し
	$('.select2-search__field').each(function(key, obj){
		obj.addEventListener('keydown',  List.criteriaSubmit, true);
	});

	
	//scratch-multi-select入力
	$('.scratch-multi-select-input').keypress(function(e) {
		if (e.keyCode == 13){
			return List.onDefineTextScratchMultiSelect(e, $(this));
		}
	});
	$('.scratch-multi-select-input').blur(function(e){
		if ($(this).val() == null||$(this).val()==''){return false;}
		return List.onDefineTextScratchMultiSelect(e, $(this));
	})
	
	//idリスト
	$('#ticket-id-input').keypress(function(e) {
		if (e.keyCode == 13){
			//数値分ループ
			var idText = $(this).val();
			var idCommaText = idText.replace(/[^0-9]/g, ',');
			idCommaText = idCommaText.replace(/,+/g, ',');
			var idList = idCommaText.split(',');
			
			for (i=0; i<idList.length;i++){
				if ((idList[i] == null || idList[i] == '') && (idText != null && idText != '')){
					continue;
				}
				$(this).val(idList[i]);
				List.onDefineTextScratchMultiSelect(e, $(this));
			}
			//数値なく、nullでもない場合は初期化
			if (!idText.match(/^[0-9]+$/) && idText != null ){
				$(this).val('');
				return false;
			}
			return false;
		}
	});
	$('#ticket-id-input').blur(function(e){
		var idText = $(this).val();
		if (idText == null||idText == ''){return false;}
		var idCommaText = idText.replace(/[^0-9]/g, ',');
		idCommaText = idCommaText.replace(/,+/g, ',');
		var idList = idCommaText.split(',');
			
		for (i=0; i<idList.length;i++){
			if ((idList[i] == null || idList[i] == '') && (idText != null && idText != '')){
				continue;
			}
			$(this).val(idList[i]);
			List.onDefineTextScratchMultiSelect(e, $(this));
		}
		if (!idText.match(/^[0-9]+$/) && idText != null ){
			$(this).val('');
			return false;
		}
		return false;
	})
	
	//scratch-multi-select-remove削除
	$('.scratch-multi-select-remove').click(function(e) {
		List.onRemoveSratchMultiSelect(e, $(this));
	});
	
	$('#additional-toggle').click(function(){
		$('#additional').toggle(450);
		return false;
	});
	
	$('#standard-toggle').click(function(){
		$('#standard').toggle(450);
		return false;
	});

	// ソート設定、表示設定
	$('#sort-candidates .add-field').click(List.onFieldAdd);
	$('#sort-targets .remove-field').click(List.onRemoveField);
	$('#display-candidates .add-field').click(List.onFieldAdd);
	$('#display-targets .remove-field').click(List.onRemoveField);
	
	//一覧がない場合はゆっくりcollapse
	$('#standard-panel').show(500, function(){
		if ($('#total').text() == '0') {
			$('#standard').toggle(500);
		}
	})
	
	
});