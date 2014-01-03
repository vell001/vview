function open_labels(o){
	$("#labels_div").fadeIn();
	o.setAttribute("onclick","close_labels(this)");
	o.innerHTML="【收起已有标签】";
}

function close_labels(o){
	$("#labels_div").fadeOut();
	o.setAttribute("onclick","open_labels(this)");
	o.innerHTML="【已有标签】";
}

KindEditor.ready(function(K) {
	var editor = K.create('textarea[name="editor_content"]', {
		uploadJson : 'upload.jsp',
		fileManagerJson : 'file_manager.jsp',
		allowFileManager : true,
		autoHeightMode : true,
		afterCreate : function() {
			var self = this;
			K.ctrl(document, 83, function() {
				self.sync();
				document.forms['editor'].submit();
			});
			K.ctrl(self.edit.doc, 83, function() {
				self.sync();
				document.forms['editor'].submit();
			});
			this.loadPlugin('autoheight');
		},
		afterChange : function() {
			K('.word_count').html(this.count('text'));
		}
	});
	K('input[name=save]').click(function(e) {
		editor.sync();
		document.getElementById("action").value="save";
		document.forms['editor'].action="index.jsp";
		document.forms['editor'].submit();
	});
	K('input[name=publish]').click(function(e) {
		editor.sync();
		document.getElementById("action").value="publish";
		document.forms['editor'].action="index.jsp";
		document.forms['editor'].submit();
	});
});