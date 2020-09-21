package com.born.secKill.server.utils;

/**
 * 通用状态码
 */
public enum StatusCode {

    SUCCESS(0,"成功"),
    FAIL(-1,"失败"),
    InvalidParams(201,"非法的参数!"),
    UserNotLogin(202,"用户没登录"),
    UserHasExists(203,"用户名已存在"),
    UserNotExists(204,"用户名不存在"),


    SECKILL_SUCCESS(301,"秒杀成功"),
    SECKILL_FAIL(302,"秒杀失败"),
    ADD_SECKILL_SUCCESS(303,"添加秒杀商品成功"),
    ADD_SECKILL_FAIL(304,"添加秒杀商品失败"),
    UPDATE_SECKILL_SUCCESS(305,"更新秒杀商品成功"),
    UPDATE_SECKILL_FAIL(306,"更新秒杀商品失败"),
    UPDATE_SECKILL_ACTIVE_SUCCESS(307,"更改秒杀商品状态成功"),
    UPDATE_SECKILL_ACTIVE_FAIL(308,"更改秒杀商品状态失败"),
    USER_HAS_SECKILL(309,"用户已抢购过该商品"),
    SECKILL_STOCK_NOT_ENOUGH(310,"当前秒杀商品库存不足"),
    SECKILL_OUT_OF_TIME(311,"当前秒杀商品不在秒杀时间段内"),


    ORDER_NOT_EXISTS(403,"订单不存在"),
    ORDER_HAS_EXISTS(404,"订单已存在"),

    UPDATE_ORDER_STATUS_SUCCESS(405,"更新订单状态成功"),
    UPDATE_ORDER_STATUS_FAIL(406,"更新订单状态失败"),
    //订单状态
    ORDER_LOST_EFFICACY(411,"订单已失效"),
    ORDER_NOT_PAY(412,"订单未支付"),
    ORDER_HAS_PAY(413,"订单已支付"),
    //支付结果
    ORDER_PAY_SUCCESS(421,"订单支付成功"),
    ORDER_PAY_FAIL(422,"订单支付失败"),


    ITEM_SUCCESS(501,"操作商品成功"),
    ITEM_FAIL(502,"操作商品失败"),
    ADD_ITEM_SUCCESS(503,"添加商品成功"),
    ADD_ITEM_FAIL(504,"添加商品失败"),
    UPDATE_ITEM_SUCCESS(505,"更新商品成功"),
    UPDATE_ITEM_FAIL(506,"更新商品失败"),
    UPDATE_ITEM_ACTIVE_SUCCESS(507,"更改商品状态成功"),
    UPDATE_ITEM_ACTIVE_FAIL(508,"更改商品状态失败"),
    ITEM_NOT_EXISTS(521,"该商品不存在"),
    ITEM_HAS_EXISTS(522,"该商品已存在"),
    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
