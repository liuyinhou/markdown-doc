<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<ul class="nav navbar-nav navbar-right">
				<li <c:if test="${topMenu=='markdown'}"> class="open" </c:if>><a href="/markdown">Markdown</a></li>
				<c:if test="${mn_topicList}">
				<li <c:if test="${topMenu=='topicList'}"> class="open" </c:if>><a href="/topic/topicList">页面管理</a></li>
				</c:if>
				<c:if test="${mn_userList}">
				<li <c:if test="${topMenu=='userList'}"> class="open" </c:if>><a href="/user/userList">用户管理</a></li>
				</c:if>
			</ul>
		</div>
		<div class="navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class=""><a href="/userCenter/user" id="curName"></a></li>
				<li class=""><a href="#" onclick="logout()">退出</a></li>
			</ul>
		</div>
	</div>
</nav>
<script type="text/javascript" src="/resources/js/common.js" ></script>
<script type="text/javascript" src="/resources/jquery/1.6/jquery.js" ></script>
<script type="text/javascript" src="/resources/jquery/jquery.cookie.js" ></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#curName").html('欢迎 '+$.cookie('userName'));
});

</script>