$(function(){
	$("#com").click(click1);
	$("#jav").click(click2);
	$("#mat").click(click3);
	$("#web").click(click4);
	$("#ana").click(click5);
	$("#submit").click(click6);
});
function click1(){
	myWindow = window.open('component', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
}
function click2(){
	myWindow = window.open('java', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
}
function click3(){
	myWindow = window.open('math', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
}
function click4(){
	myWindow = window.open('web', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
}
function click5(){
	myWindow = window.open('analize', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
}
function click6(){
	if($("#num").val() == 0){
		alert("数量不能为0！");
	}else{
		var total = $("#type").val() *$("#num").val();
		if(total != $("#sum").val()){
			alert("商品总价值与所填金额不符！，总价值为：" +total);
		}else{
			myWindow = window.open('success', '', 'height=300, width=400, top=300,left=500, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no,status=no');
		}
	}
}