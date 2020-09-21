<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>简易秒杀-成功提示页</title>
</head>
<body>
    <c:if test="${successMsg==null}">
        <center>
            <h1 align="center" style="">操作成功！</h1>
        <center>
    </c:if>
    <c:if test="${successMsg!=null}">
        <center>
            <h1 align="center">${successMsg}</h1>
        </center>
    </c:if>
</body>
</html>
