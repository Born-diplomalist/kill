<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-秒杀商品添加</title>
</head>
<body>
<form action="${ctx}/kill/merchant/add" class="layui-form" style="width:60%; margin: 150px auto">
    <div class="layui-form-item layui-row layui-col-xs12">
        <label class="layui-form-label">商品ID：</label>
        <div class="layui-input-block">
            <input type="text" id="itemId" name="itemId" class="layui-input" lay-verify="required">
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">秒杀数量：
                <input type="text" id="total" name="total" class="layui-input total" lay-verify="number required">
            </label>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">开始时间：
                <input type="text" id="startTime" name="startTime" class="layui-input"lay-verify="required">
            </label>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <label class="layui-form-label">结束时间：
                <input type="text" id="endTime" name="endTime" class="layui-input"lay-verify="required">
            </label>
        </div>
    </div>
    <div class="layui-row">
        <div class="magb15 layui-col-md8 layui-col-xs12">
            <label class="layui-form-label">秒杀商品状态：</label>
            <div class="layui-input-block isActive">
                <input type="radio" name="isActive" value="0" title="可以秒杀" checked>可秒杀
                <input type="radio" name="isActive" value="1" title="不允许秒杀">禁止秒杀
            </div>
        </div>
    </div>
    <div class="layui-form-item layui-row layui-col-xs12">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-sm layui-btn-normal" lay-submit="" lay-filter="add">立即添加</button>
            <button type="reset" class="layui-btn layui-btn-sm layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script src="${ctx}/public/layui/layui.js"></script>
<script src="${ctx}/public/jquery-1.8.3.js"></script>
<script type="text/javascript">
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        var layer=layui.layer;
        //执行一个laydate实例
        laydate.render({
            elem: '#startTime' //指定元素
            , type: 'datetime'
            , min: '2020-1-1 0:00:00'
            , max: '2037-12-31 0:00:00'
            , trigger: 'click' //采用click弹出
            , theme: 'grid'
        });
        laydate.render({
            elem: '#endTime' //指定元素
            , type: 'datetime'
            , min: '2020-1-1 0:00:00'
            , max: '2037-12-31 0:00:00'
            , trigger: 'click' //采用click弹出
        });
    });
    $("#itemId").onblur(function(){
        alert("blur  获取焦点");
    })

    //输入完itemId后，判断该id是否存在
    $("#itemId").blur(function(){
        alert("1  失去焦点");
        $.ajax({
            type:'get'
            ,url:'${ctx}/item/itemExist'
            ,data:{itemId:$("#itemId").val()}
            ,dataType:'json'
            ,async:'false'
            ,success: function (data) {
                if (data.code===521){
                    layer.msg(data.msg);
                    //若商品id不存在，将输入框清空，由于该输入框为必填项，无效的id将无法被提交
                    $("#itemId").val("");
                }
            }

        })
    });

</script>
</body>
</html>
