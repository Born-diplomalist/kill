<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>简易秒杀-商品列表</title>
    <%@include file="../../basic/head.jsp" %>
</head>
<body>

<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>简易秒杀-秒杀商品列表</h2>
        </div>

        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <td style="font-size: 15px"><strong style="color: red">商品名称</strong></td>
                    <td style="font-size: 15px"><strong style="color: red">剩余数量</strong></td>
                    <td style="font-size: 15px"><strong style="color: red">开始时间</strong></td>
                    <td style="font-size: 15px"><strong style="color: red">结束时间</strong></td>
                    <td style="font-size: 15px"><strong style="color: red">查看详情</strong></td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${killList}" var="kill">
                    <tr>
                        <td>${kill.itemName}</td>
                        <td>${kill.total}</td>
                        <td><fmt:formatDate value="${kill.startTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
                        <td><fmt:formatDate value="${kill.endTime}" pattern='yyyy-MM-dd HH:mm:ss'/></td>
                        <td>
                            <c:choose>
                                <c:when test="${kill.total<=0}  ${kill.canKill==1} && ${kill.total>0}">
                                    <a class="btn btn-info">商品已售空</a>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${kill.canKill==0}">
                                        <a class="btn btn-info">商品不在秒杀时间段内</a>
                                    </c:if>
                                    <c:if test="${kill.canKill==1}">
                                        <a class="btn btn-info" href="${ctx}/kill/detail/${kill.id}" target="_blank">详情</a>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <a class="btn btn-info" href="${ctx}/logout">注销</a>
    </div>
</div>
</body>
<script src="${ctx}/static/plugins/jquery.js"></script>
<script src="${ctx}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
</html>









