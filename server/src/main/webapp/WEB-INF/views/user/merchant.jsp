<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>简易秒杀-后台管理</title>
    <link rel="stylesheet" href="${ctx}/public/layui/css/layui.css">
</head>
<body class="layui-layout-body">

<ul class="layui-nav layui-bg-molv" lay-filter="">
    <li class="layui-nav-item"><a href="${ctx}/kill/merchant/toKillManage">秒杀商品管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/order/merchant/toOrderManage">订单管理</a></li>
</ul>
<script src="${ctx}/public/layui/layui.js"></script>
<script src="${ctx}/public/jquery-1.8.3.js"></script>

<script>
    //默认进入秒杀管理
    window.location.href="${ctx}/kill/merchant/toKillManage";
    function logout() {
        window.location.href = "${ctx}/logout";
    }
    //注意：导航 依赖 element 模块，否则无法进行功能性操作
    layui.use('element', function(){
      var element = layui.element;
    });
</script>
</body>
</html>
