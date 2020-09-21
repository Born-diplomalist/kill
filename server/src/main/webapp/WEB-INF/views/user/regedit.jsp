<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <title>简易秒杀-注册页</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${ctx}/public/layui/css/layui.css" media="all"/>
    <link rel="stylesheet" href="${ctx}/public/css/public.css" media="all"/>
</head>
<body class="childrenBody">
<form action="${ctx}/regedit" class="layui-form" style="width:60%; margin: 150px auto">
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block">
            <input type="text" id="userName" name="userName" class="layui-input userName" lay-verify="required" placeholder="请输入用户名">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">邮 箱</label>
        <div class="layui-input-block">
            <input type="text" name="email" class="layui-input email" lay-verify="email" placeholder="请输入邮箱">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">手机号</label>
        <div class="layui-input-block">
            <input type="text" name="phone" class="layui-input phone" lay-verify="phone" placeholder="请输入手机号">
        </div>
    </div>
    <div class="layui-row">
        <div class="magb15 layui-col-md7 layui-col-xs12">
            <label class="layui-form-label">身 份</label>
            <div class="layui-input-block identity">
                <input type="radio" name="identity" value="0" title="用户" checked>
                <input type="radio" name="identity" value="1" title="商家">
            </div>
        </div>
        <div class="magb15 layui-col-md7 layui-col-xs12">
            <label class="layui-form-label" >是否有效</label>
            <div class="layui-input-block isActive">
                <input type="radio" name="isActive" value="0" title="否" checked>
                <input type="radio" name="isActive" value="1" title="是">
            </div>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="regedit">立即注册</button>
            <button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">取消</button>
        </div>
    </div>
</form>

<script type="text/javascript" src="${ctx}/public/layui/layui.all.js"></script>
<script type="text/javascript">

    $("#userName").onfocus(function () {

    	$.ajax({
    		type: "post",
    		url: "${ctx}/judgeUsername",
    		data: {"userName":$("#userName").val()},
    		dataType: "json",
    		success(data){
    			if (data.code==203){
    				$("#userNameJudge").val(data.msg);
    				$("#userNameJudge").attr("display","block");
    			}
    		}
    	});
    });

</script>

</body>
</html>

<%--
        // //格式化时间
        // function filterTime(val) {
        //     if (val < 10) {
        //         return "0" + val;
        //     } else {
        //         return val;
        //     }
        // }
        //
        // //定时发布
        // var time = new Date();
        // var submitTime = time.getFullYear() + '-' + filterTime(time.getMonth() + 1) + '-' + filterTime(time.getDate()) + ' ' + filterTime(time.getHours()) + ':' + filterTime(time.getMinutes()) + ':' + filterTime(time.getSeconds());


--%>