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
	<div class="login">
        <div class="login-screen">
          <div class="login-icon">
            <h4><small>Welcome to Markdown Doc</small></h4>
          </div>

          <div class="login-form">
            <div class="control-group">
              <input type="text" class="login-field" value="" placeholder="Enter your name" id="login-username">
              <label class="login-field-icon fui-man-16" for="login-name"></label>
            </div>

            <div class="control-group">
              <input type="password" class="login-field" value="" placeholder="Password" id="login-password">
              <label class="login-field-icon fui-lock-16" for="login-pass"></label>
            </div>

            <a class="btn btn-primary btn-large btn-block" id="submitBtn" href="#" >Login</a>
          </div>
        </div>
      </div>
</div>

<script type="text/javascript" src="/resources/jquery/1.6/jquery.js" ></script>
<script type="text/javascript" src="/resources/jquery/jquery.cookie.js" ></script>

<script type="text/javascript">
$(document).ready(function() {

	$("#submitBtn").click(function() {
		var _username = $('#login-username');
		var _password = $('#login-password');
		if ($.trim(_username.val()) == "") {
			$('#login-message').html("请输入用户名");
			return;
		}
		if ($.trim(_password.val()) == "") {
			$('#login-message').html("请输入密码 ");
			return;
		}
		$.ajax({
			url: "/auth/doLogin",
			type: "POST",
			async: "true",
			dataType: "JSON",
			data: {
				userName: $.trim(_username.val()),
				pwd: $.trim(_password.val())
			},
			success: function(data){
				if (data.code == 200) {
					var res = data.data;
					$.cookie('token', res.token);
					window.location.href = "/main";
				} else {
					$('#login-message').html(data.message);
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