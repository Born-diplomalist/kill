package com.born.secKill.server.controller;

import com.born.secKill.model.dto.KillSuccessUserInfo;
import com.born.secKill.model.entity.OrderStatus;
import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.service.IOrderInfoService;
import com.born.secKill.server.service.IOrderStatusService;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.utils.PageBean;
import com.born.secKill.server.utils.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 订单控制器
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-04-01 18:36:37
 */
@Controller
public class OrderController {

    private static final Logger log= LoggerFactory.getLogger(ItemController.class);

    private static final String prefix="order";

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private IOrderStatusService orderStatusService;

    /**
     * 前往订单管理页面
     */
    @RequestMapping(prefix+"/merchant/toOrderManage")
    public String toOrderManage(){
        return "order/orderManage";
    }

    /**
     * 查看订单详情
     */
    @RequestMapping(value = prefix+"/record/detail/{orderNo}",method = RequestMethod.GET)
    public String orderDetail(@PathVariable String orderNo, ModelMap modelMap){
        if (StringUtils.isBlank(orderNo)){
            modelMap.addAttribute("errorMsg", StatusCode.InvalidParams.getMsg());
            return "redirect:basic/error";
        }
        KillSuccessUserInfo info= orderInfoService.getOrderDetailResult(orderNo);
        if (info==null){
            modelMap.addAttribute("errorMsg",StatusCode.ORDER_NOT_EXISTS.getMsg());
            return "redirect:basic/error";
        }
        modelMap.put("info",info);
        return "order/orderDetail";
    }

    /**
     * 前往修改订单状态页面
     * @param statusCode 修改前的订单状态
     */
    @RequestMapping(value = prefix+"/toUpdateStatus",method = RequestMethod.GET)
    public String toUpdateStatus(@RequestParam String orderNo, @RequestParam Byte statusCode, ModelMap modelMap){
        List<OrderStatus> orderStatusList = orderStatusService.listStatus();
        modelMap.addAttribute("orderNo",orderNo);
        modelMap.addAttribute("statusCode",statusCode);
        modelMap.addAttribute("orderStatusList",orderStatusList);
        return "order/changeOrderStatus";
    }




     /**
     * 分页获取所有订单信息
     */
    @RequestMapping(value = prefix+"/listOrderInfo",method = RequestMethod.GET)
    @ResponseBody
    public JsonBean<List<KillSuccessUserInfo>> detail(@RequestParam Integer page, @RequestParam Integer limit){
        //layui发的请求的参数默认第一页页码为1，但数据库第一页页码为0
        PageBean<KillSuccessUserInfo> pageBean = new PageBean<>(null, page - 1, limit, orderInfoService.getOrderNum());
        List<KillSuccessUserInfo> killSuccessUserInfoList;
        try {
            killSuccessUserInfoList = orderInfoService.listOrderDetailResult(pageBean.getPageStartIndex(),pageBean.getPageSize());
        }catch (Exception e){
            log.error("获取所有订单信息-发生异常",e.fillInStackTrace());
            return new JsonBean<>(StatusCode.FAIL);
        }
        return new JsonBean<>(StatusCode.SUCCESS, killSuccessUserInfoList);
    }


    /**
     * 修改订单状态
     */
    @RequestMapping(value = prefix+"/updateStatus",method = RequestMethod.POST)
    public String updateStatus(@RequestParam String orderNo,ModelMap modelMap){
        if (orderInfoService.modifyOrderStatus(orderNo)){
            modelMap.addAttribute("successMsg", StatusCode.UPDATE_ORDER_STATUS_SUCCESS.getMsg());
            return "redirect:/base/success";
        }
        modelMap.addAttribute("successMsg", StatusCode.UPDATE_ORDER_STATUS_FAIL.getMsg());
        return "redirect:/base/error";

    }


    /**
     * 支付
     *
     * 1. 确认状态为未支付
     * 2. 执行支付，更改未支付的订单的订单状态为已支付
     */
    @GetMapping(value = prefix+"/pay")
    @ResponseBody
    public JsonBean<Object> pay(@RequestParam String orderNo,ModelMap modelMap){
        KillSuccessUserInfo orderDetailResult = orderInfoService.getOrderDetailResult(orderNo);
        if (orderDetailResult.getStatusCode().equals(ConstantClass.ORDER_NOT_PAY) && orderInfoService.modifyOrderStatus(orderNo)){
                return new JsonBean<>(StatusCode.ORDER_PAY_SUCCESS.getCode(),orderDetailResult.getStatusMsg());
        }
        return new JsonBean<>(StatusCode.ORDER_PAY_FAIL);

    }

}
