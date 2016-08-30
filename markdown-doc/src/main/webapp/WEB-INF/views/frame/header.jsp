<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<ul class="nav navbar-nav navbar-right">
				<li <c:if test="${topMenu=='markdown'}"> class="open" </c:if> ><a href="/markdown">Markdown</a></li>
				<li <c:if test="${topMenu=='topicList'}"> class="open" </c:if>><a href="/topic/topicList">页面管理</a></li>
				<li class=""><a href="#Cases">成员管理</a></li>
			</ul>
		</div>
		<div class="navbar-collapse">
			<ul class="nav navbar-nav navbar-right">
				<li class=""><a href="#">欢迎 liuyinhou</a></li>
				<li class=""><a href="#Cases">退出</a></li>
			</ul>
		</div>
	</div>
</nav>
