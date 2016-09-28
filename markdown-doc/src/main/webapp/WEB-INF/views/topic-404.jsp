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
		
		
		</div>
	<div class="panel-body" id="devShow">
	 <div class="jumbotron">
	 	<p class="lead">404 - 页面不存在
	 	<c:if test="${mn_markdown}">
        ，可以快速创建页面 ><a class="btn btn-lg btn-link" href="#" id="addBtn" role="button">创建</a></p>
        </c:if>
        </p>
      </div>
	</div>
	<div class="panel-footer" id="divShowFooter">
	<a>&nbsp;</a>
	<span class="pull-right"></span>
	</div>
</div>
</div>

<!-- 新增弹框 -->
<div class="modal fade" id="addModal">
  <div class="modal-dialog">
    <div class="modal-content modal-center">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeModal('addModal')">
        <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">新增页面</h4>
      </div>
      <div class="modal-body">
        <ul class="list-group">
      <li class="list-group-item">
      <div class="col-xs-2" style="margin-top:6px;"><span> 名 称：</span></div>
   <div class="input-group col-xs-10">
     <input type="text" id="add-name" class="form-control" role="add" placeholder="名称" aria-describedby="basic-addon1">
   </div>
      </li>
      <li class="list-group-item">
      <div class="col-xs-2" style="margin-top:6px;"><span> URI：</span></div>
      <div class="input-group col-xs-10">
      <span class="input-group-addon">/markdown/</span>
     <input type="text" id="add-uri" class="form-control" role="add" placeholder="URI" aria-describedby="basic-addon1" value="${currentUri}" disabled>
   </div>
   </li>
  		</ul>
  		
      </div>
      <div class="modal-footer"><div class="" id="addMessage" role="alert"></div>
      	<button type="button" class="btn btn-default" onclick="closeModal('addModal')">关闭</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-primary" onclick="addTopic()">保存</button>&nbsp;&nbsp;
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
    
    
<script type="text/javascript">
$(document).ready(function() {
	$('#addBtn').click(function() {
		openModal('addModal');
    });
	
});

function openModal(modalId) {
	$('#' + modalId).attr("class", "modal-backdrop static");
}

function closeModal(modalId) {
	$('#' + modalId).attr("class", "modal fade");
}

function addTopic() {
	var valid = true;
	$("input[role=add]").each(function(){
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
	var name = $("#add-name").val();
	var uri = "/markdown/${currentUri}";
	$.ajax({
		url: "/topic/addTopic",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			name: name,
			uri: uri
		},
		success: function(data){
			if (data.code == 200) {
				window.location.href = "/markdown/${currentUri}";
			} else {
				$('#addMessage').html(data.message);
				$('#addMessage').attr("class", "alert-danger pull-left");
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

</script>
</body>
</html>