<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-秒杀商品详情页面</title>
    <%@include file="../../basic/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <input id="killId" value="${detail.id}" type="hidden"/>
        <div class="panel-heading">
            <h1>商品名称：${detail.itemName}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">剩余数量：${detail.total}</h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">开始时间：<fmt:formatDate value="${detail.startTime}" pattern='yyyy-MM-dd HH:mm:ss'/></h2>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">结束时间：<fmt:formatDate value="${detail.endTime}" pattern='yyyy-MM-dd HH:mm:ss'/></h2>
        </div>
    </div>

    <a class="btn btn-info" style="font-size: 20px;width: 55px;float:left;" href="${ctx}/logout">注销</a>
    <td>
        <c:choose>
            <c:when test="${detail.canKill==1}">
                <a class="btn btn-info" style="font-size: 20px;float: right" onclick="executeKill()">抢购</a>
            </c:when>
            <c:otherwise>
                <a class="btn btn-info" style="font-size: 20px">商品已抢购完毕或者不在抢购时间段!</a>
            </c:otherwise>
        </c:choose>

    </td>
</div>

</body>
<script src="${ctx}/static/plugins/jquery.js"></script>
<script src="${ctx}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${ctx}/static/plugins/jquery.cookie.min.js"></script>
<script src="${ctx}/static/plugins/jquery.countdown.min.js"></script>

<link rel="stylesheet" href="${ctx}/static/css/detail.css" type="text/css">
<script type="text/javascript">
    function executeKill() {
        $.ajax({
            type: "POST",
            url: "${ctx}/kill/execute",
            // contentType: "application/json;charset=utf-8",
            data: {killId:$("#killId").val()},
            dataType: "json",
            success: function(res){
                if (res.code===301) {
                    alert(res.msg);
                    window.location.href="${ctx}/kill/execute/toSuccess"
                }else{
                    alert(res.msg);
                    window.location.href="${ctx}/kill/execute/toFail"
                }
            },
            error: function (message) {
                alert(message);
                alert("提交数据失败！");
            }
        });
    }

</script>

</html>
















