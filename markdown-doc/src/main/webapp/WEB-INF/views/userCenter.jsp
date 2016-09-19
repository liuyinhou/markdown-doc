<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-CN">
<head>
	<title>Markdown Doc</title>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<meta charset="UTF-8">
	
	<link href="/resources/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
	<link href="/resources/css/content.css" rel="stylesheet"  type="text/css" />
	<link href="/resources/css/markdown.css" rel="stylesheet"  type="text/css" />

</head>
<body>
<%@ include file="frame/header.jsp"%>
<div class="content container">
<div class="panel panel-default">
	<div class="panel-heading">
		修改密码
	</div>
	<div class="panel-body" id="devShow">
	 <ul class="list-group">
		        <li class="list-group-item">
		        	<div class="col-xs-2" style="margin-top:6px;"><span> 原密码：</span></div>
			    	<div class="input-group col-xs-10">
			      		<input type="text" id="oldPasswd" class="form-control" role="edit" placeholder="原密码" aria-describedby="basic-addon1">
			    	</div>
		        </li>
		        <li class="list-group-item">
		        	<div class="col-xs-2" style="margin-top:6px;"><span> 新密码：</span></div>
			    	<div class="input-group col-xs-10">
			      		<input type="text" id="newPasswd" class="form-control" role="edit" placeholder="新密码" aria-describedby="basic-addon1">
			    	</div>
		        </li>
		        <li class="list-group-item">
		        	<div class="col-xs-2" style="margin-top:6px;"><span> 确认新密码：</span></div>
			    	<div class="input-group col-xs-10">
			      		<input type="text" id="repeatPasswd" class="form-control" role="edit" placeholder="确认新密码" aria-describedby="basic-addon1">
			    	</div>
		        </li>
      		</ul>
	</div>
	<div class="panel-footer"><div class="" id="editMessage" role="alert"></div>
          	<button type="button" class="btn btn-default" id="btnCancle">取消</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-primary" id="btnEdit">修改</button>&nbsp;&nbsp;
          </div>
</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	$('#btnEdit').click(function() {
		changePasswd();
    });
	$('#btnCancle').click(function() {
		window.location.href = "/markdown";
    });
});

function changePasswd() {
	var valid = true;
	$("input[role=edit]").each(function(){
		if (checkEmptyString($(this).val())) {
			$(this).css({"border-color": "#DA5430"});
			valid = false;
		} else {
			$(this).css({"border-color": ""});
		}
	});
	if (!valid) {
		return;
	}
	var oldPasswd = $("#oldPasswd").val();
	var newPasswd = $("#newPasswd").val();
	var repeatPasswd = $("#repeatPasswd").val();
	$.ajax({
		url: "/userCenter/changePasswd",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			oldPasswd:oldPasswd,
			newPasswd:newPasswd,
			repeatPasswd:repeatPasswd
		},
		success: function(data){
			if (data.code == 200) {
				$('#editMessage').html('修改成功');
			} else {
				$('#editMessage').html(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

</script>
</body>
</html>