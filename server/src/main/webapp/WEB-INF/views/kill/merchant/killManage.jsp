<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-秒杀商品管理</title>
    <link rel="stylesheet" href="${ctx}/public/layui/css/layui.css" media="all">
</head>
<body class="layui-layout-body">

<ul class="layui-nav layui-tab-card" lay-filter="" style="width: 100%">
    <li class="layui-nav-item"><a href="${ctx}/kill/merchant/toKillManage">秒杀商品管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/order/merchant/toOrderManage">订单管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/item/merchant/toItemManage">商品管理</a></li>
    <li class="layui-nav-item"><a href="${ctx}/logout">注销</a></li>
</ul>
<div style="text-align: center;margin-top: 200px">
    <div class="layui-inline">
        <table id="mySecKillTable" lay-filter="secKillTable"></table>
    </div>
</div>

<script type="text/html" id="myToolBar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">添加</button>
        <button class="layui-btn layui-btn-sm layui-btn-warm" lay-event="update">更新</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="active">启用/禁用</button>
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
            elem: '#mySecKillTable'
            , even: true //开启隔行背景
            , title: "秒杀表"
            , id: "mySecKillTable"
            , url: '${ctx}/kill/listAllKill' //数据接口
            , page: false //不开启分页
            // , height: "435"
            , cellMinWidth: 80
            , limits: [10, 15, 20, 25]
            , limit: 10
            // , totalRow: true
            , cols: [[ //表头
                {field: 'id', title: '秒杀ID', width: 100, align: "center", sort: true}
                , {field: 'itemId', title: '商品ID', width: 80, align: "center"}
                , {field: 'total', title: '总数量', width: 90, align: "center", sort: true}
                , {field: 'itemName', title: '商品名', width: 90, align: "center", sort: true}
                , {field: 'startTime', title: '开始时间', width: 150, align: "center"}
                , {field: 'endTime', title: '结束时间', width: 150, align: "center"}
                , {
                    field: 'isActive', title: '是否启用', width: 90, align: "center", templet: function (data) {
                        return data.isActive === 1 ? "启用" : "禁用";
                    }
                }
                , {field: 'createTime', title: '创建时间', width: 150, align: "center", sort: true}
                , {
                    field: 'canKill', title: '当前时段', width: 90, align: "center", sort: true, templet: function (data) {
                        return data.canKill === 1 ? "秒杀时段内" : "秒杀时段外";
                    }
                }
                , {title: "操作", width: 240, templet: '#myToolBar', fixed: 'right'}
            ]]
        });


        //tabel.on监听seckillTable工具条
        table.on('tool(secKillTable)', function (obj) {
            var layEvent = obj.event;
            var data = obj.data;
            if (layEvent === 'add') {//添加秒杀商品
                layer.open({
                    title: '添加秒杀商品页'
                    , type: 2
                    , area: ['400px', '600px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/kill/merchant/toAdd'
                    ,end: function(){
                        table.reload("mySecKillTable");
                    }
                });

            } else if (layEvent === 'update') {//修改秒杀商品
                layer.open({
                    title: '修改秒杀商品页'
                    , type: 2
                    , area: ['400px', '600px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/kill/merchant/toUpdate?killId=' + data.id
                    ,end: function(){
                        table.reload("mySecKillTable");
                    }
                });
            } else if (layEvent === 'active') {//修改秒杀商品状态
                if (data.isActive !== 1 && data.isActive !== 0) {
                    layer.open({
                        title: '修改秒杀状态页'
                        , type: 0
                        , content: '未知的秒杀状态'
                        ,end: function(){
                            table.reload("mySecKillTable");
                        }
                    });
                }
                $.post('${ctx}/kill/merchant/changeActiveStatus', {killId: data.id}, function (data) {
                    layer.msg(data.msg);
                    table.reload("mySecKillTable");
                });
            } else {
                layer.msg("未知的操作");
                table.reload("mySecKillTable");
            }
            //刷新页面
            table.reload("mySecKillTable");
        });


    });
</script>
</body>
</html>
