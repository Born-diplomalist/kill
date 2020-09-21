<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-秒杀商品修改</title>
</head>
<body>
<form action="${ctx}/kill/merchant/update" class="layui-form" style="width:60%; margin: 150px auto">
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">商品ID</label>
        <div class="layui-input-block">
            <input type="hidden" id="killId" name="killId" class="layui-input killId" value="${kill.id}">
            <input type="text" id="itemId" name="itemId" class="layui-input itemId" lay-verify="required" value="${kill.itemId}">

        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">秒杀数量</label>
        <div class="layui-input-block">
            <input type="text" name="total" value="${kill.total}" class="layui-input total" lay-verify="number required">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">开始时间</label>
        <div class="layui-input-block">
            <input type="text" id="startTime" name="startTime" value="${kill.startTime}" class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">结束时间</label>
        <div class="layui-input-block">
            <input type="text" id="endTime" name="endTime" value="${kill.endTime}" class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-row">
        <div class="magb15 layui-col-md8 layui-col-xs12">
            <label class="layui-form-label">秒杀商品状态</label>
            <div class="layui-input-block isActive">
                <input type="radio" name="isActive" value="0" title="可秒杀"
                <c:if test="${kill.isActive==1}">checked</c:if>>可秒杀
                <input type="radio" name="isActive" value="1" title="禁止秒杀"
                       <c:if test="${kill.isActive==0}">checked</c:if>>禁止秒杀
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
            , change: function (value, date, endDate) {
                //得到日期生成的值，如：2017-08-18
                // console.log(value);
                //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
                // console.log(date);
                //得到日期时间对象所在年的最后一天，开启范围选择（range: true）才会返回。对象成员同上。
                // console.log(endDate);
            }
        });
        laydate.render({
            elem: '#endTime' //指定元素
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
