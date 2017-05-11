$(function (){
	$('#rotate').on('click', function(e){
		$(this).blur()
		$.post('rotary?action=rotate')
	})
	$('#capped').on('click', function(e){
		$(this).blur()
		$.post('baxter?action=capped')
	})
})
