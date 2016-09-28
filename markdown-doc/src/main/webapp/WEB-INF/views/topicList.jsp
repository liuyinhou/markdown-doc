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
		页面列表
		<a class="btn btn-default pull-right" name="btnAdd" style="margin-top: -6px;">新增页面</a>
		</div>
		<table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>URI</th>
            <th>更新日志</th>
            <th>更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody id="table-content">
        </tbody>
        </table>
		  <ul class="pagination pull-right" id="pageUl">
		</ul>
	</div>
</div>

<!-- 修改页面 -->
<div class="modal fade" id="editModal">
  <div class="modal-dialog">
    <div class="modal-content modal-center">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeModal('editModal')">
        <span aria-hidden="true">&times;</span>
        </button>
        <h4 class="modal-title">修改页面</h4>
      </div>
      <div class="modal-body">
        <ul class="list-group">
      <li class="list-group-item">
      <div class="col-xs-2" style="margin-top:6px;"><span> 名 称：</span></div>
   <div class="input-group col-xs-10">
     <input type="text" id="edit-name" class="form-control" role="edit" placeholder="名称" aria-describedby="basic-addon1">
   </div>
      </li>
      <li class="list-group-item">
      <div class="col-xs-2" style="margin-top:6px;"><span> URI：</span></div>
      <div class="input-group col-xs-10">
      <span class="input-group-addon">/markdown/</span>
     <input type="text" id="edit-uri" class="form-control" role="edit" placeholder="URI" aria-describedby="basic-addon1">
   </div>
   </li>
  		</ul>
  		
      </div>
      <div class="modal-footer"><div class="" id="editMessage" role="alert"></div>
      	<button type="button" class="btn btn-default" onclick="closeModal('editModal')">关闭</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-primary" onclick="editTopic()">修改</button>&nbsp;&nbsp;
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

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
     <input type="text" id="add-uri" class="form-control" role="add" placeholder="URI" aria-describedby="basic-addon1">
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

<!-- 删除弹框 -->
<div class="modal fade" id="deleteModal">
<div class="modal-dialog">
    <div class="modal-content modal-center">
  <div class="panel panel-danger">
        <div class="panel-heading">
          <h3 class="panel-title">删除页面</h3>
        </div>
        <div class="panel-body">
    <strong>是否确认删除页面？</strong>&nbsp;&nbsp;&nbsp;&nbsp;
          <button type="button" class="btn btn-danger" onclick="deleteTopic()">删除</button>&nbsp;&nbsp;
        <button type="button" class="btn btn-default" onclick="closeModal('deleteModal');">取消</button>&nbsp;&nbsp;
        </div>
      </div>
     </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
    
<script type="text/javascript">
var currentEditTopicId = 0;
var currentDeleteTopicId = 0;
$(document).ready(function() {
	getTopicList(1);
	$('a[name="btnAdd"]').click(function() {
		openAddTopic();
    });
});

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
	var uri = "/markdown/" + $("#add-uri").val();
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
				//$('#addMessage').html("保存成功！");
				//$('#addMessage').attr("class", "alert-success pull-left");
				closeModal('addModal');
				getTopicList(1);
			} else {
				$('#addMessage').html(data.message);
				$('#addMessage').attr("class", "alert-danger pull-left");
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function openModal(modalId) {
	$('#' + modalId).attr("class", "modal-backdrop static");
}

function closeModal(modalId) {
	$('#' + modalId).attr("class", "modal fade");
}

function openAddTopic() {
	$("input[role=add]").each(function(){
		$(this).val('');
	});
	openModal('addModal');
}

function openDeleteTopic(id) {
	openModal('deleteModal');
	currentDeleteTopicId = id;
}

function deleteTopic() {
	$.ajax({
		url: "/topic/deleteTopic",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			id: currentDeleteTopicId
		},
		success: function(data){
			if (data.code == 200) {
				closeModal('deleteModal');
				getTopicList(1);
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function openEditTopic(topicId) {
	$.ajax({
		url: "/topic/getTopic",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			id: topicId
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				$('#edit-name').val(res.name);
				var uri = res.uri;
				uri = uri.substring(10);
				$('#edit-uri').val(uri);
				currentEditTopicId = res.id;
				openModal('editModal');
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function editTopic() {
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
	var name = $("#edit-name").val();
	var uri = "/markdown/" + $("#edit-uri").val();
	$.ajax({
		url: "/topic/editTopic",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			id: currentEditTopicId,
			name:name,
			uri:uri
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				$('#edit-name').val('');
				$('#edit-uri').val('');
				closeModal('editModal');
				getTopicList(1);
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function getTopicList(pageNum) {
	$.ajax({
		url: "/topic/getTopicList",
		type: "POST",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		async: "true",
		dataType: "JSON",
		data: {
			pageNum: pageNum,
			pageSize: 10
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				var html = "";
				for(var i=0; i<res.length; i++){
					html += "<tr><td>" + res[i].id + "</td>";
					html += "<td>" + res[i].name + "</td>";
					html += "<td>" + res[i].uri + "</td>";
					html += "<td>" + res[i].remark + "</td>";
					html += "<td>" + res[i].updateTime + "</td><td>";
					if (res[i].status!=8) {
						//status=8是受保护的，不能修改
						html += '<a class="btn btn-default" onclick="openEditTopic(\'' + res[i].id + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;';
						html += '<a class="btn btn-danger" onclick="openDeleteTopic(\'' + res[i].id + '\')">删除</a>';
					}
					html += '&nbsp;&nbsp;<a class="btn btn-link" href="'+res[i].uri+'">查看</a></td>';
					html += "</tr>";
                }
				$('#table-content').html(html);
				//页码
				html = '<li><a href="#" aria-label="Previous" onclick="getTopicList(1)">';
			    html += '<span aria-hidden="true">&laquo;</span></a></li>';
			    var pages = data.page.pageShow;
			    for (var i=0;i<pages.length;i++) {
			    	html += '<li><a href="#"';
			    	if (pages[i]==data.page.pageNum) {
			    		html += ' style="background-color: #eee;"';
			    	}
			    	html += ' onclick="getTopicList('+pages[i]+')">'+pages[i]+'</a></li>';
			    }
			    
			   html += '<li><a href="#" aria-label="Next" onclick="getTopicList('+data.page.pageCount+')"><span aria-hidden="true">&raquo;</span></a></li>';
			   $('#pageUl').html(html);
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