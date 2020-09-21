<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../basic/tag.jsp" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>简易秒杀-抢购成功商品详情页面</title>
    <%@include file="../basic/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-body">
            <h2>当前用户名：</h2><h2 class="text-danger">${info.userName}</h2>
        </div>
        <div class="panel-body">
            <h2>抢购的商品名称：</h2><h2 class="text-danger">${info.itemName}</h2>
        </div>
        <div class="panel-body">
            <h2>订单编号：</h2><h2 class="text-danger">${info.code}</h2>
        </div>
        <div class="panel-body">
            <h2>成功抢购的时间：</h2><h2 class="text-danger"><fmt:formatDate value="${info.createTime}" pattern='yyyy-MM-dd HH:mm:ss'/></h2>
        </div>
        <div class="panel-body">
            <h2>当前支付的状态：</h2>
            <h2 id="orderStatusMsg" class="text-danger">
                ${info.statusMsg}
<%--                <c:if test="${info.statusCode==1}">--%>
<%--                    已成功付款--%>
<%--                </c:if>--%>
<%--                <c:if test="${info.statusCode!=1}">--%>
<%--                    未支付或已取消--%>
<%--                </c:if>--%>
            </h2>
        </div>
    </div>
    <table>
        <tr>
            <td>
                <c:if test="${info.statusCode==0}">
                    <strong>
                        <input id="pay_btn" type="button" value="点击生成二维码进行支付" style="font-size: 30px;width: 800px;height: auto;background-color: #28a4c9;" onclick="pay();"/>
                        </strong>
                </c:if>
                <input type="hidden" id="orderNo" value="${info.code}"/>
            </td>
        </tr>
    </table>
</div>

</body>
<script src="${ctx}/static/plugins/jquery.js"></script>
<script src="${ctx}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${ctx}/static/plugins/jquery.cookie.min.js"></script>
<script src="${ctx}/static/plugins/jquery.countdown.min.js"></script>

<script type="text/javascript">
    <%--alert('暂未实现，微信支付参考https://pay.weixin.qq.com/wiki/doc/api/index.html，微信公众号开发参考https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Overview.html')--%>
    function pay() {
        //如果状态不是未支付，则弹出信息，提示只能支付未支付的订单
        //如果状态是支付，则ajax更改状态，并将订单状态信息设定到页面上对应位置
        var status=$("#orderStatusMsg");
        $.ajax({
            type:'get'
            ,url:'${ctx}/order/pay'
            ,data:{orderNo:$("#orderNo").val()}
            ,dataType:'json'
            ,success:function (data) {
                //支付成功，修改状态
                if (data.code===421){
                    $("#orderStatusMsg").text(data.msg);
                    $("#pay_btn").style="display:none";
                    alert("支付成功！")
                }
                //支付失败
                if (data.code===422){
                    alert("只能支付未支付状态的订单");
                    //支付失败
                }
                //其他异常
                alert("判断完毕~")
            }
        })
    }
</script>
</html>
















