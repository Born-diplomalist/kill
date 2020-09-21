package com.born.secKill.server.common;

public class ConstantClass {

    //用户身份标识
    public static final Byte USER_IDENTITY=0;
    //商家身份标识
    public static final Byte MERCHANT_IDENTITY=1;


    //秒杀启用
    public static final Byte SECKILL_ACTIVE=1;
    //秒杀禁用
    public static final Byte SECKILL_FORBIDDEN=0;

    //商品启用
    public static final Byte ITEM_ACTIVE=1;
    //商品禁用
    public static final Byte ITEM_FORBIDDEN=0;


    //用户已秒杀当前商品
    public static final String USER_HAS_SECKILLED ="1";
    //用户未秒杀当前商品
    public static final String USER_NOT_SECKILLED ="0";

    public static final Byte ORDER_HAS_INVALID=-1;


    //订单状态--已支付
    public static final Byte ORDER_PAY =1;
    //订单状态--未支付
    public static final Byte ORDER_NOT_PAY =0;

}
