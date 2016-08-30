<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>spring-mvc-showcase</title>
	<link href="/resources/form.css" rel="stylesheet"  type="text/css" />		
	<link href="/resources/jqueryui/1.8/themes/base/jquery.ui.core.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/jqueryui/1.8/themes/base/jquery.ui.theme.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/jqueryui/1.8/themes/base/jquery.ui.tabs.css"  rel="stylesheet" type="text/css"/>
	
	<!--
		Used for including CSRF token in JSON requests
		Also see bottom of this file for adding CSRF token to JQuery AJAX requests
	-->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
<h1>Get Api 文档</h1>
<p>基于Markdown语法的文档。</p>
<div id="tabs">
	<ul>
		<li><a href="#simple">登录</a></li>
    </ul>
    <div id="simple">
    	<font id="login-message" color="red"></font><br/>
		用户名：<input id="login-username" type="text" /><br/>
		密 &nbsp;&nbsp;码：<input id="login-password" type="password" /></br/>
		<input id="submitBtn" type="submit" value="登录" />
	</div>
	
</div>
<script type="text/javascript" src="/resources/jquery/1.6/jquery.js" ></script>
<script type="text/javascript" src="/resources/jqueryform/2.8/jquery.form.js" ></script>
<script type="text/javascript" src="/resources/jquery/jquery.cookie.js" ></script>
<script type="text/javascript" src="/resources/jqueryui/1.8/jquery.ui.core.js" ></script>
<script type="text/javascript" src="/resources/jqueryui/1.8/jquery.ui.widget.js" ></script>
<script type="text/javascript" src="/resources/jqueryui/1.8/jquery.ui.tabs.js" ></script>
<script type="text/javascript" src="/resources/json2.js" ></script>
<script>
	MvcUtil = {};
	MvcUtil.showSuccessResponse = function (text, element) {
		MvcUtil.showResponse("success", text, element);
	};
	MvcUtil.showErrorResponse = function showErrorResponse(text, element) {
		MvcUtil.showResponse("error", text, element);
	};
	MvcUtil.showResponse = function(type, text, element) {
		var responseElementId = element.attr("id") + "Response";
		var responseElement = $("#" + responseElementId);
		if (responseElement.length == 0) {
			responseElement = $('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>').insertAfter(element);
		} else {
			responseElement.replaceWith('<span id="' + responseElementId + '" class="' + type + '" style="display:none">' + text + '</span>');
			responseElement = $("#" + responseElementId);
		}
		responseElement.fadeIn("slow");
	};
	MvcUtil.xmlencode = function(xml) {
		//for IE 
		var text;
		if (window.ActiveXObject) {
		    text = xml.xml;
		 }
		// for Mozilla, Firefox, Opera, etc.
		else {
		   text = (new XMLSerializer()).serializeToString(xml);
		}			
		    return text.replace(/\&/g,'&'+'amp;').replace(/</g,'&'+'lt;')
	        .replace(/>/g,'&'+'gt;').replace(/\'/g,'&'+'apos;').replace(/\"/g,'&'+'quot;');
	};
</script>	
<script type="text/javascript">
$(document).ready(function() {
	$("#tabs").tabs();

	// Append '#' to the window location so "Back" returns to the selected tab
	// after a redirect or a full page refresh (e.g. Views tab).

	// However, note this general disclaimer about going back to previous tabs: 
	// http://docs.jquery.com/UI/API/1.8/Tabs#Back_button_and_bookmarking

	$("#tabs").bind("tabsselect", function(event, ui) { window.location.hash = ui.tab.hash; });



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
			url: "/doLogin",
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
					window.location.href = "/markdown";
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