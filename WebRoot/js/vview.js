function changeimg(){
	var myimg = document.getElementById("certpic"); 
	//myimg.src = "util/makecertpic.jsp";
	now = new Date();
	myimg.src="util/makecertpic.jsp?code="+now.getTime();
}

/* ajax */
var req = null;
var element = null;
var url = null;
if(window.XMLHttpRequest){
	req = new XMLHttpRequest();
}else if(window.ActiveXObject){
	req = new ActiveXObject("Microsoft.XMLHttp");
}
	/*  start of register  */

function register_validate(e){
	element = e;
	if(element.value == null || element.value.trim() == ""){
		register_span("不能为空");
		element.value = element.value.trim();
		return;
	}
	//密码
	if(element.id == "password1" || element.id == "password2"){
		if(element.value.trim().length < 6){
			register_span("密码长度不能小于6哦...");
			return;
		}
		if(element.id == "password2" && element.value != document.getElementById("password1").value){
			register_span("两次密码输入错误！");
			return;
		}
		register_span("ok...");
		return;
	}
	//邮箱
	if(element.id == "email"){
		if(!element.value.isEmail()){
			register_span("邮箱无效！");
			return;
		}
	}
	
	var url = "register_validate.jsp?id="+escape(escape(element.id.trim()))+"&value=" + escape(escape(element.value.trim()));
	
	req.open("GET", url, true);
	req.onreadystatechange = register_callback;
	req.send(null);
}

function register_callback(){
	if(req.readyState < 4){
		register_span("<img src=\"images/ajaxloader.gif\" alt=\"加载中\"/>");
	}
	if(req.readyState == 4 && req.status == 200){
		register_span(req.responseText);
		//使用 responseXML
		//var msg = req.responseXML.getElementsByTagName("msg")[0];//得到XML中的msg标签对象
        //register_span(msg.childNodes[0].nodeValue);//获取msg对象中的第0个子节点对象的值
	}
}

function register_span(str){
	var span = document.getElementById(escape(escape(element.id))+"_span");
	span.innerHTML = str;
}
	/*  end of register  */

	/* start of login */
function login_validate(e){
	element = e;
	if(element.value == null || element.value.trim() == ""){
		login_span("不能为空");
		element.value = element.value.trim();
		return;
	}
	//密码
	if(element.id == "password" && element.value.trim().length < 6){
		login_span("密码长度不能小于6哦...");
		return;
	}
	if(element.id == "password"){
		login_username = document.getElementById("username");
		url = "login_validate.jsp?username="+escape(escape(login_username.value.trim()))+"&id="+escape(escape(element.id.trim()))+"&value=" + escape(escape(element.value.trim()));
	}else{
		url = "login_validate.jsp?id="+escape(escape(element.id.trim()))+"&value=" + escape(escape(element.value.trim()));
	}
	
	req.open("GET", url, true);
	req.onreadystatechange = login_callback;
	req.send(null);
}

function login_callback(){
	if(req.readyState < 4){
		login_span("<img src=\"images/ajaxloader.gif\" alt=\"加载中\"/>");
	}
	if(req.readyState == 4 && req.status == 200){
		login_span(req.responseText);
	}
}

function login_span(str){
	var span = document.getElementById(escape(escape(element.id))+"_span");
	span.innerHTML = str;
}
	/* end of login */
	/* start of labels_show */




	/* end of labels_show */
/* end of ajax */

function deleteElement(id){
	var my = document.getElementById(id);
	if (my != null)
		my.parentNode.removeChild(my);
}

function move_form(id){
	var parent_id = document.getElementById('parent_id');
	parent_id.value = id;
	
	var form_div = document.getElementById('form_div');
	
	deleteElement('form_div');
	
	var o_div = document.getElementById(id);
	o_div.appendChild(form_div);
}
/* util */
String.prototype.trim = function() {//去除前后空格
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.isEmail = function(){//是否为邮箱
	return /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(this);
}
/* end of util */
