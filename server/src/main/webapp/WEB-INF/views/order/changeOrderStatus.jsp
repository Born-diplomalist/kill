<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<meta charset="utf-8">
	<title>简易秒杀-修改订单状态</title>
	<meta name="renderer" content="webkit">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" href="${ctx}/public/layui/css/layui.css" media="all" />
</head>
<body class="childrenBody">

<form class="layui-form" action="${ctx}/order/updateStatus" method="post" style="width:80%;">

	<div class="layui-form-item layui-row layui-col-xs12">
		<label class="layui-form-label">订单编号：</label>
		<div class="layui-input-block">
			<input type="text" name="orderNo" value="${orderNo}" class="layui-input layui-input-inline" readonly>
		</div>
	</div>

	<div class="magb15 layui-row layui-col-xs12">
		<label class="layui-form-label">订单状态：</label>
		<div class="layui-input-block">
			<select name="statusCode" class="layui-form-select" lay-ignore>
				<c:forEach items="${orderStatusList}" var="status">
					<option value="${status.statusCode }"
						<c:if test="${status.statusCode==statusCode}">selected</c:if>>
							${status.statusMsg }
					</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="layui-form-item layui-row layui-col-xs12">
		<div class="layui-input-block">
		<!-- button不给type属性默认是submit -->
			<button class="layui-btn layui-btn-sm" lay-submit="" lay-filter="update">立即修改</button>
			<button type="reset" class="layui-btn layui-btn-sm layui-btn-normal">取消</button>
		</div>
	</div>
</form>
</body>
<script type="text/javascript">

</script>

</html>