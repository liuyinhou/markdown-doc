<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="zh-CN">
<head>
	<title>Markdown Doc</title>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<meta charset="UTF-8">
	
	<link href="/resources/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet"  type="text/css" />
	<link href="/resources/css/content.css" rel="stylesheet"  type="text/css" />

</head>
<body>
	<%@ include file="frame/header.jsp"%>
	<div class="content container" id="content">
	<div class="panel panel-default">
		<div class="panel-heading">
		搜索结果
		</div>
		<div class="panel-body" id="searchResult">
		</div>
            <ul id="nextPage" class="pager hide">
              <li><a style="cursor:hand">更多...</a></li>
            </ul>
	</div>
</div>

<script type="text/javascript">
var searchKey = "${searchKey}";
var nextPageNum = 1;
$(document).ready(function() {
	searchTopic(searchKey, 1);
	$('#nextPage').click(function() {
		searchTopic(searchKey, nextPageNum);
    });
});

function searchTopic(sKey, pageNum) {
	$.ajax({
		url: "/topic/searchTopicApi",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			searchKey: sKey,
			pageNum: pageNum,
			pageSize: 10
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				if (res.length==0) {
					$('#searchResult').append("未搜索到结果");
				}
				var html = "";
				for(var i=0; i<res.length; i++){
					html += "<h3><a href='" + res[i].topicUri + "'>";
					html += res[i].topicName + "</a></h3>";
					html += "<a href='" + res[i].topicUri + "'>" + res[i].topicUri + "</a>";
					html += "<p>" + res[i].topicContentMarkdown + "</p><hr>";
                }
				$('#searchResult').append(html);
				//页码
				if (data.page.next) {
					$('#nextPage').removeClass("hide");
				} else {
					$('#nextPage').addClass("hide");
				}
				nextPageNum ++;
			} else {
				$('#content').html("请求数据异常");
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

</script>
</body>
</html>