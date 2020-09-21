<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-商品修改</title>
</head>
<body>
<form action="${ctx}/item/merchant/updateItem" class="layui-form" style="width:60%; margin: 150px auto">
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">商品名</label>
        <div class="layui-input-block">
            <input type="hidden" id="itemId" name="itemId" class="layui-input itemId" value="${item.id}">
            <input type="text" id="name" name="name" class="layui-input name" lay-verify="required" value="${item.name}">
        </div>
    </div>

    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">商品编号</label>
        <div class="layui-input-block">
            <input type="text" name="code" value="${item.code}" class="layui-input code" readonly>
        </div>
    </div>

    <div class="magb15 layui-row layui-col-xs12">
		<label class="layui-form-label">商品类别：</label>
		<div class="layui-input-block">
			<select name="type" class="layui-form-select" lay-ignore>
                <option>--请选择--</option>
				<c:forEach items="${itemTypeList}" var="itemType">
					<option value="${itemType.typeNum}"
                    <c:if test="${itemType.typeNum==item.type}">selected</c:if>>
							${itemType.typeName }
					</option>
				</c:forEach>
			</select>
		</div>
	</div>

    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">库存量：
                <input type="text" id="stock" name="stock" class="layui-input stock" lay-verify="number" value="${item.stock}">
            </label>
        </div>
    </div>

    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">采购时间：
                <input type="text" id="purchaseTime" name="purchaseTime" class="layui-input" value="${item.purchaseTime}">
            </label>
        </div>
    </div>

    <div class="layui-row">
        <div class="magb15 layui-col-md8 layui-col-xs12">
            <label class="layui-form-label">商品状态：</label>
            <div class="layui-input-block isActive">
                <input type="radio" name="isActive" value="0" title="启用"
                <c:if test="${item.isActive==1}">checked</c:if>>启用
                <input type="radio" name="isActive" value="1" title="禁用"
                       <c:if test="${item.isActive==0}">checked</c:if>>禁用
            </div>
        </div>
    </div>


    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-sm layui-btn-normal" lay-submit="" lay-filter="update">立即更新</button>
            <button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script src="${ctx}/public/layui/layui.js"></script>
<script src="${ctx}/public/jquery-1.8.3.js"></script>
<script type="text/javascript">
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        //执行一个laydate实例
        laydate.render({
            elem: '#startTime' //指定元素
            , type: 'datetime'
            , min: '2020-1-1 0:00:00'
            , max: '2037-12-31 0:00:00'
            , trigger: 'click' //采用click弹出
            , theme: 'grid'
        });
    });

</script>
</body>
</html>
