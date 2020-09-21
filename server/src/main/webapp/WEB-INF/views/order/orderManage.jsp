<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="utf-8">
    <title>订单管理页</title>
    <link rel="stylesheet" href="${ctx}/public/layui/css/layui.css" media="all">
</head>
<%--style="width: 800px;height: 600px"--%>
<body>
<ul class="layui-nav layui-tab-card" lay-filter="" style="width: 100%">
    <li class="layui-nav-item"><a href="${ctx}/kill/merchant/toKillManage">秒杀商品管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/order/merchant/toOrderManage">订单管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/item/merchant/toItemManage">商品管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/logout">注销</a></li>
</ul>
<div style="text-align: center;margin-top: 200px">
    <div class="layui-inline">
        <table id="myOrderTable" lay-filter="orderTable"></table>
    </div>
</div>

<script type="text/html" id="myToolBar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm" lay-event="detail">查看</button>
        <button class="layui-btn layui-btn-sm layui-btn-warm" lay-event="updateStatus">更新状态</button>

    </div>
</script>

<script src="${ctx}/public/layui/layui.js"></script>
<script src="${ctx}/public/jquery-1.8.3.js"></script>
<script>
    layui.use('table', function () {
        var table = layui.table;
        var layer = layui.layer;

        layer.config({
            anim: 1, //默认动画风格
            skin: 'layui-layer-molv' //默认皮肤
        });

        //渲染数据
        table.render({
            elem: '#myOrderTable'
            // , even: true //开启隔行背景
            , title: "订单表"
            , id: "myOrderTable"
            , url: '${ctx}/order/listOrderInfo' //数据接口
            , page: true //开启分页
            , height: "435"
            , cellMinWidth: 80
            , limits: [10, 15, 20, 25]
            , limit: 10
            , totalRow: true
            , cols: [[ //表头
                {field: 'code', title: '订单编号', width: 120, align: "center", sort: true}
                , {field: 'itemId', title: '商品id', width: 80, align: "center"}
                , {field: 'killId', title: '秒杀id', width: 100, align: "center", sort: true}
                , {field: 'userId', title: '用户id', width: 100, align: "center", sort: true}
                , {field: 'statusMsg', title: '订单状态', width: 110, align: "center"}
                , {field: 'createTime', title: '创建时间', align: "center", width: 200}
                , {field: 'diffTime', title: '下单时长', width: 110, align: "center", sort: true}
                , {field: 'statusCode', title: '订单状态码', width: 110, align: "center"}
                , {title: "操作", width: 200, templet: '#myToolBar', fixed: 'right', align: "center"}
            ]]
        });


        //tabel.on使用了数据表格中的事件监听，监听tool的userList
        table.on('tool(orderTable)', function (obj) {
            var layEvent = obj.event;
            var data = obj.data;
            console.log(data);
            if (layEvent === 'detail') {
                alert("查看详情");
                layer.open({
                    title: '订单详情页'
                    , type: 2
                    , area: ['1000px', '800px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/order/record/detail/' + data.code
                    ,end: function(){
                        table.reload("myOrderTable");
                    }
                });

            } else if (layEvent === 'updateStatus') {
                if (data.statusCode !== 0) {
                    layer.msg('只能更改未支付的订单');
                    table.reload("mySecKillTable");
                }
                alert("更改状态");
                layer.open({
                    title: '修改订单状态页'
                    , type: 2
                    , area: ['550px', '390px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/order/toUpdateStatus?orderNo=' + data.code + '&statusCode=' + data.statusCode
                    ,end: function(){
                        table.reload("myOrderTable");
                    }
                });
            } else {
                alert("未知的操作");
                //刷新页面
                table.reload("myOrderTable");
            }
        });


    });
</script>
</body>
</html>
