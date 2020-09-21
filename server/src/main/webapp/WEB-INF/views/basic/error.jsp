<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>错误页面</title>
</head>
<body>
    <c:if test="${errorMsg}!=null">
        <center>
            <h2 align="center">${errorMsg}</h2>
        </center>
    </c:if>
    <c:if test="${errorMsg}==null">
        <center></center>
            <h2>出现异常-或者请求页面不存在...</h2>
        <center>
    </c:if>
</body>
</html>
