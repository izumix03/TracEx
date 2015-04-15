var DetailTicket = {};

DetailTicket.handleVersionChange = function() {
	$('#ticket input[name=subsystem]').typeahead('val', '');
	$('#ticket input[name=function_group]').typeahead('val', '');
};

DetailTicket.handleSubsystemChange = function() {
	$('#ticket input[name=function_group]').typeahead('val', '');
};

DetailTicket.handleFunctionGroupChange = function(obj, datum, name) {
	$('#ticket input[name=owner]').typeahead('val', datum['developper_id']);
	$('#ticket input[name=evaluator]').typeahead('val', datum['evaluator']);
};

DetailTicket.handleStatusChange = function() {
	if ($('#ticket select[name=status]').val() == 'closed') {
		$('#ticket #resolution-area').show();
	} else {
		$('#ticket #resolution-area').hide();
	}
};

DetailTicket.valiationOption = {
	onfocusout : false,
	onkeyup : false,
	onclick : false
};

$(function() {
	// Validation
	$('#ticket').validate(DetailTicket.valiationOption);

	// Submit
	$('#submit-ticket').click(function() {
		if (!$('#ticket').valid()) {
			return;
		}
		$(this).button('loading');
		$('#ticket').submit();
	});
	
	// CopyTicket
	$('#copy-ticket').click(function() {
		if (!$('#ticket').valid()) {
			return;
		}
		$(this).button('loading');
		
		var form = $('<form/>', {method: 'post'})
		.append($('<input/>', {type: 'hidden', name: 'id', value: $("*[name=id]")[0].value}))
   		.append($('<input/>', {type: 'hidden', name: 'copy-ticket'}));
		
		$('body').append(form);
		
		form.submit();
		
		Detail.showPopup();
	});

	// typeahead
	Common.typeaheadMilestone($('#ticket input[name=milestone]'));
	Common.typeaheadMember($('#ticket input[name=reporter]'));
	Common.typeaheadMember($('#ticket input[name=owner]'));
	Common.typeaheadMember($('#ticket input[name=evaluator]'));

	Common.typeaheadSubsystem($('#ticket input[name=subsystem]'), function() {
		return $('#ticket select[name=product_code]').val();
	});
	Common.typeaheadFunctionGroup($('#ticket input[name=function_group]'), function() {
		return {
			pid : $('#ticket select[name=product_code]').val(),
			version : $('#ticket select[name=version]').val(),
			subsystem : $('#ticket input[name=subsystem]').val()
		}
	});

	// datepicker
	$('#ticket input[name=due_close]').datepicker({
		format: 'yyyy/mm/dd',
		autoclose: true
	});


	// Version
	$('#ticket select[name=version]').change(DetailTicket.handleVersionChange);

	// Subsystem
	$('#ticket input[name=subsystem]')
		.on('typeahead:autocompleted', DetailTicket.handleSubsystemChange)
		.on('typeahead:selected', DetailTicket.handleSubsystemChange);

	// FunctionGroup
	$('#ticket input[name=function_group]')
		.on('typeahead:autocompleted', DetailTicket.handleFunctionGroupChange)
		.on('typeahead:selected', DetailTicket.handleFunctionGroupChange);

	// Status -> Resolution
	$('#ticket select[name=status]').change(DetailTicket.handleStatusChange);
	DetailTicket.handleStatusChange();
});