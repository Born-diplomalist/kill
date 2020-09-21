<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>简易秒杀-商品管理</title>
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
        <table id="myItemTable" lay-filter="itemTable"></table>
    </div>
</div>

<script type="text/html" id="myItemToolBar">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">添加</button>
        <button class="layui-btn layui-btn-sm layui-btn-warm" lay-event="update">更新</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="active">启用/禁用</button>
    </div>
</script>

<script src="${ctx}/public/layui/layui.all.js"></script>
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
            elem: '#myItemTable'
            , even: true //开启隔行背景
            , title: "商品表"
            , id: "myItemTable"
            , url: '${ctx}/item/getItemList' //数据接口
            , page: false //不开启分页
            // , height: "435"
            , cellMinWidth: 80
            , limits: [10, 15, 20, 25]
            , limit: 10
            // , totalRow: true
            , cols: [[ //表头
                {field: 'id', title: '商品ID', width: 100, align: "center", sort: true}
                , {field: 'name', title: '商品名', width: 110, align: "center"}
                , {field: 'code', title: '商品编号', width: 110, align: "center", sort: true}
                , {field: 'type', title: '商品类别', width: 110, align: "center", sort: true}
                , {field: 'stock', title: '商品库存', width: 110, align: "center", sort: true}
                , {field: 'purchaseTime', title: '采购时间', width: 150, align: "center"}
                , {field: 'isActive', title: '商品状态', width: 110, align: "center", templet: function (data) {
                        return data.isActive === 1 ? "启用" : "禁用";
                   }}
                , {field: 'createTime', title: '创建时间', width: 150, align: "center", sort: true}
                , {field: 'updateTime', title: '更新时间', width: 150, align: "center", sort: true}
                , {title: "操作", width: 240, templet: '#myItemToolBar', fixed: 'right'}
            ]]
        });


        //tabel.on监听itemTable工具条
        table.on('tool(itemTable)', function (obj) {
            var layEvent = obj.event;
            var data = obj.data;
            if (layEvent === 'add') {//添加商品
                layer.open({
                    title: '添加商品页'
                    , type: 2
                    , area: ['400px', '600px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/item/merchant/toAddItem'
                    ,end: function(){
                        table.reload("myItemTable");
                    }
                });

            } else if (layEvent === 'update') {//修改商品
                layer.open({
                    title: '修改商品页'
                    , type: 2
                    , area: ['400px', '600px'] //设定宽高
                    , btn: '关闭'
                    , shadeClose: true
                    , content: '${ctx}/item/merchant/toUpdateItem?itemId=' + data.id
                    ,end: function(){
                        table.reload("myItemTable");
                    }
                });
            } else if (layEvent === 'active') {//修改商品状态
                if (data.isActive !== 1 && data.isActive !== 0) {
                    layer.open({
                        title: '修改状态页'
                        , type: 0
                        , content: '未知的启用状态'
                        ,end: function(){
                            table.reload("myItemTable");
                        }
                    });
                }
                $.post('${ctx}/item/merchant/changeItemActive', {itemId: data.id}, function (data) {
                    layer.msg(data.msg);
                    table.reload("myItemTable");
                });
            } else {
                layer.msg("未知的操作");
                table.reload("myItemTable");
            }
            //刷新页面
            table.reload("myItemTable");
        });


    });
</script>
</body>
</html>
