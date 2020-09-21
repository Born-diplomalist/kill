package com.born.secKill.server.controller;

import com.born.secKill.model.entity.SecKill;
import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.utils.DateUtil;
import com.born.secKill.server.utils.StatusCode;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.dto.KillDto;
import com.born.secKill.server.service.ISecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀controller   围绕的是秒杀，重心在于秒杀相关的操作
 * @Author:gyk
 * @Date: 2020/3/17 22:14
 **/
@Controller
public class KillController {

    private static final Logger log = LoggerFactory.getLogger(KillController.class);

    private static final String prefix = "kill";

    @Autowired
    private ISecKillService killService;

    /**
     * 获取所有秒杀商品列表传给前端页面
     *
     */
    @GetMapping(prefix+"/listAllKill")
    @ResponseBody
    public JsonBean<List<SecKill>> listAllKill(){
        try {
            //获取待秒杀商品列表
            List<SecKill> killList=killService.listAllKill();
            if (killList!=null && killList.size()>0){
                log.info("获取待秒杀商品列表-数据：{}",killList);
                return new JsonBean<>(StatusCode.SUCCESS,killList);
            }
        }catch (Exception e){
            log.error("获取待秒杀商品列表-发生异常：",e.fillInStackTrace());
            return new JsonBean<>(StatusCode.FAIL);
        }
        return new JsonBean<>(StatusCode.FAIL);
    }

    /**
     * 获取所有启用的秒杀商品列表前往用户秒杀详情页
     */
    @GetMapping(value = {"/","/index",prefix+"/user/list",prefix+"/index.html"})
    public String index(ModelMap modelMap){
        try {
            //获取待秒杀商品列表
            List<SecKill> killList=killService.listActiveKill();

            modelMap.addAttribute("killList",killList);
            log.info("获取待秒杀商品列表-数据：{}",killList);
        }catch (Exception e){
            log.error("获取待秒杀商品列表-发生异常：",e.fillInStackTrace());
            return "redirect:/base/error";
        }
        //当前用户身份是用户,将数据传递到秒杀商品详情页
        return "kill/user/killList";
    }

    /**
     * 获取指定待秒杀商品的详情
     */
    @RequestMapping(value = prefix+"/detail/{id}",method = RequestMethod.GET)
    public String getDetail(@PathVariable Integer id,ModelMap modelMap){
        if (id==null || id<=0){
            return "redirect:/base/error";
        }
        try {
            SecKill detail=killService.getKillDetail(id);
            modelMap.put("detail",detail);
        }catch (Exception e){
            log.error("获取待秒杀商品的详情-发生异常：id={}",id,e.fillInStackTrace());
            return "redirect:/base/error";
        }
        return "kill/user/killDetail";
    }

    /***
     * 商品秒杀核心业务逻辑
     */
    //@RequestMapping(value = prefix+"/execute",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ResponseBody
    //public BaseResponse<Object> execute(@RequestParam Integer killId, BindingResult result, HttpSession session){
    @RequestMapping(value = prefix+"/execute",method = RequestMethod.POST)
    @ResponseBody
    public JsonBean<Object> execute(@RequestParam Integer killId, HttpServletRequest request){
        HttpSession session = request.getSession();

        //if (result.hasErrors() || killId<=0){
        if (killId<=0){
            return new JsonBean<Object>(StatusCode.InvalidParams);
        }
        Object userIdInSession=session.getAttribute("userId");
        if (userIdInSession==null){
            return new JsonBean<Object>(StatusCode.UserNotLogin);
        }
        Integer userId= (Integer)userIdInSession ;

        JsonBean<Object> response;
        try {
            return killService.killItem(killId, userId);
        }catch (Exception e){
            response=new JsonBean<Object>(StatusCode.FAIL.getCode(),e.getMessage());
        }
        return response;
    }

    //@RequestMapping(value = prefix+"/execute/lock",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@ResponseBody
    //public BaseResponse<Object> executeTest(@RequestBody @Validated KillDto dto, BindingResult result){
    //if (result.hasErrors() || dto.getKillId()<=0){
    /***
     * 商品秒杀-压力测试
     //* @param dto 秒杀数据封装
     */
    @PostMapping(value = prefix+"/execute/lock")
    @ResponseBody
    public JsonBean<Object> executeTest(Integer killId,Integer userId){
        if (killId<=0){
            return new JsonBean<Object>(StatusCode.InvalidParams);
        }
        JsonBean<Object> response;
        try {
            //基于Redisson的分布式锁进行控制
            return killService.killItem(killId,userId);
        }catch (Exception e){
            response=new JsonBean<Object>(StatusCode.FAIL.getCode(),e.getMessage());
        }
        return response;
    }


    /**
     * 抢购成功跳转页面
     */
    @RequestMapping(value = prefix+"/execute/toSuccess",method = RequestMethod.GET)
    public String toExecuteSuccess(){
        return "kill/user/killSuccess";
    }

    /**
     * 抢购失败跳转页面
     */
    @RequestMapping(value = prefix+"/execute/toFail",method = RequestMethod.GET)
    public String toExecuteFail(){
        return "kill/user/killFail";
    }


    /**
     *  跳转秒杀管理页面
     */
    @RequestMapping(value = prefix+"/merchant/toKillManage",method = RequestMethod.GET)
    public String toKillManage(ModelMap modelMap){
        try {
            //获取待秒杀商品列表
            List<SecKill> list=killService.listAllKill();
            log.info("获取待秒杀商品列表-数据：{}",list);
            return "kill/merchant/killManage";
        }catch (Exception e){
            log.error("获取待秒杀商品列表-发生异常：",e.fillInStackTrace());
            modelMap.put("errorMsg","获取全部秒杀商品时发生异常");
            return "redirect:/base/error";
        }
    }
    //跳转到添加
    @GetMapping(prefix+"/merchant/toAdd")
    public String toKillAdd(){
        return "kill/merchant/killAdd";
    }

    //跳转到更新
    @GetMapping(prefix+"/merchant/toUpdate")
    public String toKillUpdate(@RequestParam Integer killId, ModelMap modelMap){
        SecKill kill;
        try {
            kill = killService.getKillDetail(killId);
            modelMap.addAttribute("kill",kill);
        } catch (Exception e) {
            log.error("获取ID为{}的秒杀商品失败",killId);
            e.printStackTrace();
        }
        return "kill/merchant/killUpdate";
    }

    /**
     * 添加秒杀商品
     */
    @GetMapping(prefix+"/merchant/add")
    public String addKill(Integer itemId, Integer total, String startTime,String endTime,Byte isActive, ModelMap modelMap){
        SecKill kill = new SecKill();
        kill.setItemId(itemId);
        kill.setTotal(total);
        kill.setStartTime(DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        kill.setEndTime(DateUtil.parseStrToDate(endTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        kill.setIsActive(isActive);
        if (killService.addKill(kill)>0){
            modelMap.addAttribute("successMsg", StatusCode.ADD_SECKILL_SUCCESS.getMsg());
            return "redirect:/base/success";
        }
        modelMap.addAttribute("errorMsg", StatusCode.ADD_SECKILL_FAIL.getMsg());
        return "redirect:/base/error";    }

    /**
     * 更新秒杀商品
     */
    @GetMapping(prefix+"/merchant/update")
    public String updateKill(Integer killId,Integer itemId, Integer total, String startTime,String endTime,Byte isActive, ModelMap modelMap){
        if (killId==null){
            modelMap.addAttribute("errorMsg", "该秒杀商品ID为空");
            return "redirect:/base/error";
        }
        SecKill kill = new SecKill();
        kill.setId(killId);
        kill.setItemId(itemId);
        kill.setTotal(total);
        kill.setStartTime(DateUtil.parseStrToDate(startTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        kill.setEndTime(DateUtil.parseStrToDate(endTime, DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        kill.setIsActive(isActive);
        if (killService.modifyKill(kill)>0){
            modelMap.addAttribute("successMsg", StatusCode.UPDATE_SECKILL_SUCCESS.getMsg());
            return "redirect:/base/success";
        }
        modelMap.addAttribute("errorMsg", StatusCode.UPDATE_SECKILL_FAIL.getMsg());
        return "redirect:/base/error";
    }

    /**
     * 更新秒杀商品启用状态
     */
    @PostMapping(prefix+"/merchant/changeActiveStatus")
    @ResponseBody
    public JsonBean<Object> changeStatus(@RequestParam Integer killId, ModelMap modelMap){
        return killService.changeActiveStatus(killId)>0
            ?new JsonBean<>(StatusCode.UPDATE_ITEM_ACTIVE_SUCCESS)
            :new JsonBean<>(StatusCode.UPDATE_ITEM_ACTIVE_FAIL);
    }


}













