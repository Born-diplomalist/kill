<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-用户登录</title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${ctx}/public/layui/css/layui.css" media="all"/>
    <%--<link rel="stylesheet" href="${ctx}/public/css/public.css" media="all"/>--%>
</head>
<body class="childrenBody">
<div class="container" style="margin-top: 300px;margin-left:200px;width:40%;height: 500px">

    <form action="${ctx}/login" method="post" class="layui-form">
        <div class="layui-form-item layui-row layui-col-xs12">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-block">
                <input type="text" id="userName" name="userName" class="layui-input userName" lay-verify="required"
                       placeholder="请输入用户名">
            </div>
        </div>
        <div class="layui-form-item layui-row layui-col-xs12">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-block">
                <input type="password" name="password" class="layui-input password-field" lay-verify="password"
                       placeholder="请输入密码">
            </div>
        </div>

        <div class="layui-form-item layui-row layui-col-xs12">
            <label class="layui-form-label">验证码</label>
            <input type="text" id="cpacha" class="layui-input" name="cpacha" placeholder="请输入您的验证码" onfocus="this.placeholder='';" onblur="this.placeholder='请输入您的验证码'" style="width:40%;float: left"/>
            <img id="cpacha-img" src="${ctx}/base/get_cpacha?vl=4&w=150&h=35&type=loginCpacha" style="cursor:pointer; width: 110px;float: right"
                 height="30px" onclick="changeCpacha()" title="点击切换验证码">
        </div>

        <div class="layui-form-item layui-row layui-col-xs12">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-sm layui-btn-normal" lay-submit lay-filter="loginNow">立即登录
                </button>
                <button type="button" id="reg" class="layui-btn layui-btn-sm layui-btn-primary">立即注册</button>
            </div>
            <h3 style="color: #01AAED">${userName}&nbsp;&nbsp;&nbsp;${errorMsg}</h3>
        </div>
    </form>


</div>
</body>
<script type="text/javascript" src="${ctx}/public/layui/layui.all.js"></script>
<script type="text/javascript" src="${ctx}/public/jquery-1.8.3.js"></script>
<script type="text/javascript">
    $("#reg").click(function () {
        window.location.href = "${ctx}/toRegedit";
    });

    /**
	* 切换验证码
	* 更换验证码图片的路径，在路径上加上一个时间戳参数，以避免多次更换验证码请求都使用了同一个缓存
	*/
	function changeCpacha(){
		$("#cpacha").val("");
		$("#cpacha-img").attr("src","${ctx}/base/get_cpacha?v1=4&w=150&h=35&type=loginCpacha&t="+new Date().getTime());
	}
</script>

</html>
















