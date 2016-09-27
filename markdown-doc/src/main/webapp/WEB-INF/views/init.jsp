<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-CN">
<head>
	<title>Markdown Doc</title>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<meta charset="UTF-8">
	
	<link href="/resources/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
	<link href="/resources/css/login.css" rel="stylesheet"  type="text/css" />

</head>
<body>
<div class="container">

      <form class="form-signin" >
        <h3 class="form-signin-heading">初始化Markdown Doc</h3>
       超级管理员： <input type="txt" id="adminName" class="form-control" placeholder="用户名"  role="init" autofocus value="admin">
      密码：  <input type="password" id="passwd" class="form-control" placeholder="密码" role="init">
       确认密码： <input type="password" id="passwdRe" class="form-control" placeholder="确认密码" role="init">
        
        <a class="btn btn-lg btn-primary" id="submitBtn">初始化</a>
        <label id="errorMsg" class="alert-danger"></label>
      </form>

    </div> <!-- /container -->
<script type="text/javascript" src="/resources/js/common.js" ></script>
<script type="text/javascript" src="/resources/jquery/1.6/jquery.js" ></script>
<script type="text/javascript" src="/resources/jquery/jquery.cookie.js" ></script>
<script type="text/javascript">
$(document).ready(function() {

	$("#submitBtn").click(function() {
		var valid = true;
		$("input[role=init]").each(function(){
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
		var passwd = $("#passwd").val();
		var passwdRe = $("#passwdRe").val();
		if (passwd != passwdRe) {
			$("#passwd").css({"border-color": "#DA5430"});
			$("#passwdRe").css({"border-color": "#DA5430"});
			return;
		}
		$.ajax({
			url: "/init/init",
			type: "POST",
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			async: "true",
			dataType: "JSON",
			data: {
				adminName: $.trim($("#adminName").val()),
				passwd: $.trim($("#passwd").val())
			},
			success: function(data){
				if (data.code == 200) {
					window.location.href = "/auth/login";
				} else {
					alert(data.message);
					return false;
				}
			},
			error: function(data){alert('error');}
		});
	});

});

</script>
</body>
</html>