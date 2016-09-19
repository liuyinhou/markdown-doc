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
		文档
		<c:if test="${mn_markdown}">
		<a class="btn btn-default pull-right" id="btnEdit" style="margin-top: -6px;">编辑</a>
		</c:if>
		</div>
	<div class="panel-body" id="devShow">
	 ${result.contentHtml}
	</div>
	<div class="fade" id="divFooter">
		<div class="col-xs-2" style="margin-top:6px;width:100px;">&nbsp;&nbsp;&nbsp;&nbsp;<span>描述：</span></div>
	    <div class="col-xs-7" style="margin-top:-7px;">
	      <input type="text" id="remark" class="form-control" role="edit" placeholder="修改描述" aria-describedby="basic-addon1">
	    </div>
	    <div class="col-xs-2 pull-right">
		<a class="btn btn-default pull-right" id="btnCancle" style="margin-top: -6px;" data-action="edit">取消</a>
		<span class="pull-right">&nbsp;&nbsp;&nbsp;&nbsp;</span>
		 <a class="btn btn-primary pull-right" id="btnSave" style="margin-top: -6px;" data-action="edit">保存</a>
		 &nbsp;&nbsp;
		 </div>
		 <a>&nbsp;</a>
	</div>
	<div class="panel-footer" id="divShowFooter">
	<a>&nbsp;</a>
	<span class="pull-right">最后更新：${result.lastUpdateTime} 【${result.operatorName}】 - ${result.remark}</span>
	</div>
</div>
</div>
<script type="text/javascript">
var currentContentId = ${result.contentId};
$(document).ready(function() {
	$('#btnEdit').click(function() {
		getCurrentTopicContent('edit');
    });
	$('#btnCancle').click(function() {
		getCurrentTopicContent('show');
    });
	
	$('#btnSave').click(function() {
		saveTopicContent(currentContentId);
    });
});

function saveTopicContent(id) {
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
	var contentMarkdown = $("#editText").val();
	var remark = $("#remark").val();
	$.ajax({
		url: "/topic/addTopicContent",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			preId:id,
			contentMarkdown:contentMarkdown,
			remark:remark
		},
		success: function(data){
			if (data.code == 200) {
				currentContentId = data.data.id;
				getCurrentTopicContent('show');
			} else {
				$('#devShow').html(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function getCurrentTopicContent(action) {
	$.ajax({
		url: "/topic/getTopicContentById",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			id:currentContentId
		},
		success: function(data){
			if (data.code == 200) {
				var html = '';
				if (action == 'edit') {
					html = '<textarea id="editText" style="width: 100%;max-width: 100% !important;height:400px;">';
					html += data.data.contentMarkdown + '</textarea>';
					$('#divFooter').attr('class','panel-footer');
					$('#divShowFooter').attr('class','fade');
				} else {
					html = data.data.contentHtml;
					$('#divFooter').attr('class','fade');
					$('#divShowFooter').attr('class','panel-footer');
				}
				$('#devShow').html(html);
			} else {
				$('#devShow').html(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

</script>
</body>
</html>