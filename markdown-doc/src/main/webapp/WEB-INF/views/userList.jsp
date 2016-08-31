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
		用户列表
		<a class="btn btn-default pull-right" name="btnAdd" style="margin-top: -6px;">新增用户</a>
		</div>
		<table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>权限</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody id="table-content">
        
        </tbody>
        </table>
		  <ul class="pagination pull-right" id="pageUl">
		</ul>
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
			      <input type="text" id="edit-name"  disabled class="form-control" placeholder="名称" aria-describedby="basic-addon1">
			    </div>
		        </li>
		        <li class="list-group-item">
		        <div class="col-xs-2" style="margin-top:6px;"><span> 权 限：</span></div>
		        <div class="input-group col-xs-10">
		        	<input type="checkbox" name="edit-menu" value="markdown" aria-label="...">编辑页面
        			<input type="checkbox" name="edit-menu" value="topicList" aria-label="...">页面管理
        			<input type="checkbox" name="edit-menu" value="userList" aria-label="...">用户管理
			    </div>
			    </li>
			    <li class="list-group-item">
		        <div class="col-xs-2" style="margin-top:6px;"><span> 状 态：</span></div>
		        <div class="input-group col-xs-10">
		        	<input type="radio" name="edit-status" value="0" aria-label="..." >启用
        			<input type="radio" name="edit-status" value="4" aria-label="..." >禁用
			    </div>
			    </li>
      		</ul>
      		
          </div>
          <div class="modal-footer"><div class="" id="editMessage" role="alert"></div>
          	<button type="button" class="btn btn-default" onclick="closeModal('editModal')">关闭</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-primary" onclick="editUser()">修改</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-danger">删除</button>&nbsp;&nbsp;
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
		        <div class="col-xs-2" style="margin-top:6px;"><span> 用户名：</span></div>
			    <div class="input-group col-xs-10">
			      <input type="text" id="add-name" class="form-control" role="add" placeholder="名称" aria-describedby="basic-addon1">
			    </div>
		        </li>
		        <li class="list-group-item">
		        <div class="col-xs-2" style="margin-top:6px;"><span> 密 码：</span></div>
			    <div class="input-group col-xs-10">
			      <input type="text" id="add-passwd" class="form-control" role="add" placeholder="密码" aria-describedby="basic-addon1">
			    </div>
		        </li>
		        <li class="list-group-item">
		        <div class="col-xs-2" style="margin-top:6px;"><span> 权 限：</span></div>
		        <div class="input-group col-xs-10">
		        	<input type="checkbox" name="add-menu" value="markdown" aria-label="...">编辑页面
        			<input type="checkbox" name="add-menu" value="topicList" aria-label="...">页面管理
        			<input type="checkbox" name="add-menu" value="userList" aria-label="...">用户管理
			    </div>
			    </li>
			    <li class="list-group-item">
		        <div class="col-xs-2" style="margin-top:6px;"><span> 状 态：</span></div>
		        <div class="input-group col-xs-10">
		        	<input type="radio" name="add-status" value="0" aria-label="..." checked>启用
        			<input type="radio" name="add-status" value="4" aria-label="..." >禁用
			    </div>
			    </li>
      		</ul>
      		
          </div>
          <div class="modal-footer"><div class="" id="addMessage" role="alert"></div>
          	<button type="button" class="btn btn-default" onclick="closeModal('addModal')">关闭</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-primary" onclick="addUser()">保存</button>&nbsp;&nbsp;
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
              <h3 class="panel-title">删除用户</h3>
            </div>
            <div class="panel-body">
		      <strong>是否确认删除用户？</strong>&nbsp;&nbsp;&nbsp;&nbsp;
              <button type="button" class="btn btn-danger" onclick="deleteUser()">删除</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-default" onclick="closeModal('deleteModal');">取消</button>&nbsp;&nbsp;
            </div>
          </div>
         </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    
    
</div>
	
<script type="text/javascript">
var currentEditUserId = 0;
var currentDeleteUserId = 0;
$(document).ready(function() {
	getUserList(1);
	$('a[name="btnAdd"]').click(function() {
		openAddUser();
    });
});

function addUser() {
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
	var passwd = $("#add-passwd").val();
	var menu = '';
	$("input[name=add-menu]:checked").each(function(){
		menu += $(this).val() + ",";
	});
	var status = 0;
	$("input[name=add-status]").each(function(){
          if ($(this).attr("checked")) {
               status = $(this).val();
          }
     });
	$.ajax({
		url: "/user/addUser",
		type: "POST",
		async: "true",
		dataType: "JSON",
		data: {
			name: name,
			passwd: passwd,
			menu: menu,
			status: status
		},
		success: function(data){
			if (data.code == 200) {
				//$('#addMessage').html("保存成功！");
				//$('#addMessage').attr("class", "alert-success pull-left");
				closeModal('addModal');
				getUserList(1);
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

function openAddUser() {
	$("input[role=add]").each(function(){
		$(this).val('');
	});
	openModal('addModal');
}

function openDeleteUser(id) {
	openModal('deleteModal');
	currentDeleteUserId = id;
}

function deleteUser() {
	$.ajax({
		url: "/user/deleteUser",
		type: "POST",
		async: "true",
		dataType: "JSON",
		data: {
			id: currentDeleteUserId
		},
		success: function(data){
			if (data.code == 200) {
				closeModal('deleteModal');
				getUserList(1);
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function openEditUser(userId) {
	$.ajax({
		url: "/user/getUser",
		type: "POST",
		async: "true",
		dataType: "JSON",
		data: {
			id: userId
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				$('#edit-name').val(res.name);
				$("input[name=edit-status]").each(function(){
					$(this).attr("checked", false);
			         if ($(this).val()==res.status) {
			             $(this).attr("checked", true);
			         }
			     });
				$("input[name=edit-menu]").each(function(){
					$(this).attr("checked", false);
					if(res.authMenu.indexOf($(this).val())>=0) {
						$(this).attr("checked", true);
					}
			     });
				currentEditUserId = res.id;
				openModal('editModal');
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function editUser() {
	var menu = '';
	$("input[name=edit-menu]:checked").each(function(){
		menu += $(this).val() + ",";
	});
	var status = 0;
	$("input[name=edit-status]").each(function(){
          if ($(this).attr("checked")) {
               status = $(this).val();
          }
     });
	$.ajax({
		url: "/user/editUser",
		type: "POST",
		async: "true",
		dataType: "JSON",
		data: {
			id: currentEditUserId,
			menu:menu,
			status:status
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				closeModal('editModal');
				getUserList(1);
			} else {
				alert(data.message);
				return false;
			}
		},
		error: function(data){alert('error');}
	});
}

function getUserList(pageNum) {
	$.ajax({
		url: "/user/getUserList",
		type: "POST",
		async: "true",
		dataType: "JSON",
		data: {
			pageNum: pageNum,
			pageSize: 5
		},
		success: function(data){
			if (data.code == 200) {
				var res = data.data;
				var html = "";
				for(var i=0; i<res.length; i++){
					html += "<tr><td>" + res[i].id + "</td>";
					html += "<td>" + res[i].name + "</td>";
					html += "<td>" + res[i].authMenu + "</td>";
					html += "<td>" + res[i].statusMessage + "</td>";
					html += "<td>" + res[i].updateTime + "</td><td>";
					if (res[i].status!=8) {
						//status=8是受保护的，不能修改
						html += '<a class="btn btn-default" onclick="openEditUser(\'' + res[i].id + '\')">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;';
						html += '<a class="btn btn-danger" onclick="openDeleteUser(\'' + res[i].id + '\')">删除</a>';
					}
					html += "</td></tr>";
                }
				$('#table-content').html(html);
				//页码
				html = '<li><a href="#" aria-label="Previous" onclick="getUserList(1)">';
			    html += '<span aria-hidden="true">&laquo;</span></a></li>';
			    var pages = data.page.pageShow;
			    for (var i=0;i<pages.length;i++) {
			    	html += '<li><a href="#"';
			    	if (pages[i]==data.page.pageNum) {
			    		html += ' style="background-color: #eee;"';
			    	}
			    	html += ' onclick="getUserList('+pages[i]+')">'+pages[i]+'</a></li>';
			    }
			    
			   html += '<li><a href="#" aria-label="Next" onclick="getUserList('+data.page.pageCount+')"><span aria-hidden="true">&raquo;</span></a></li>';
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