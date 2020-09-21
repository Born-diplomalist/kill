<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-抢购成功通知页</title>
    <%@include file="../../basic/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1 align="center">抢购成功-请您<span style="color: red">登录邮箱</span>验证相关链接信息,并请在<span style="color: red">1小时内</span>
                完成订单的支付!</h1>
        </div>
    </div>
</div>
</body>
<script src="${ctx}/static/plugins/jquery.js"></script>
<script src="${ctx}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${ctx}/static/plugins/jquery.cookie.min.js"></script>
<script src="${ctx}/static/plugins/jquery.countdown.min.js"></script>
</html>
















