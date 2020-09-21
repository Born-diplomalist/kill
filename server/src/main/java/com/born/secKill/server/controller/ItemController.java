package com.born.secKill.server.controller;

import com.born.secKill.model.entity.Item;
import com.born.secKill.model.entity.ItemType;
import com.born.secKill.server.service.IItemService;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.utils.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待秒杀商品controller   围绕着的是商品，主要是秒杀商品的CRUD
 * @Author:gyk
 * @Date: 2020/3/16 22:41
 **/
@Controller
public class ItemController {

    private static final Logger log= LoggerFactory.getLogger(ItemController.class);

    private static final String prefix="item";

    @Autowired
    private IItemService itemService;

    @GetMapping(prefix+"/merchant/toAddItem")
    public String toAddItem(ModelMap modelMap){
        modelMap.addAttribute("itemTypeList", itemService.listType());
        return "item/itemAdd";
    }

    @GetMapping(prefix+"/merchant/toUpdateItem")
    public String toItemUpdate(Integer itemId,ModelMap modelMap){
        modelMap.addAttribute("item", itemService.getItem(itemId).getData());
        modelMap.addAttribute("itemTypeList", itemService.listType());
        return "item/itemUpdate";
    }


    @GetMapping(prefix+"/merchant/toItemManage")
    public String toItemManage(ModelMap modelMap){
        modelMap.addAttribute("itemList",addItemTypeToItem(itemService.listItem()));
        return "item/itemManage";

    }


    /**
     * 获取所有商品信息
     */
    @GetMapping(prefix+"/getItemList")
    @ResponseBody
    public JsonBean<List<Item>> listItem(){
        try{
            List<Item> itemList = addItemTypeToItem(itemService.listItem());
            if (itemList!=null && itemList.size()>0){
                log.info("获取商品列表-数据：{}",itemList);
                return new JsonBean<List<Item>>(StatusCode.SUCCESS,itemList);
            }
        }catch (Exception e){
           log.error("获取商品列表-发生异常：",e.fillInStackTrace());
            return new JsonBean<List<Item>>(StatusCode.FAIL);
        }
        return new JsonBean<List<Item>>(StatusCode.FAIL);
    }


    /**
     * 判断用户是否存在
     */
    @GetMapping(prefix+"/itemExist")
    public JsonBean<Object> isItemExist(Integer itemId){
        return itemService.isItemExist(itemId);
    }

    /**
     * 添加商品
     */
    @GetMapping(prefix+"/merchant/addItem")
    public String addItem(Item item,ModelMap modelMap){
        if (itemService.addKillItem(item)>0){
            modelMap.addAttribute("successMsg", StatusCode.ADD_ITEM_SUCCESS.getMsg());
            return "redirect:base/success";
        }
        modelMap.addAttribute("errorMsg", StatusCode.ADD_ITEM_FAIL.getMsg());
        return "redirect:base/error";
    }

    /**
     * 修改商品
     *
     */
    @GetMapping(prefix+"/merchant/updateItem")
    public String updateItem(Item item,ModelMap modelMap){
        if (itemService.updateKillItem(item)>0){
            modelMap.addAttribute("successMsg", StatusCode.UPDATE_ITEM_SUCCESS.getMsg());
            return "redirect:base/success";
        }
        modelMap.addAttribute("errorMsg", StatusCode.UPDATE_ITEM_FAIL.getMsg());
        return "redirect:base/error";
    }

    /**
     * 禁用商品
    */
    @PostMapping(prefix+"/merchant/changeItemActive")
    @ResponseBody
    public JsonBean<Item> changeItemActive(Integer itemId, ModelMap modelMap){
        return itemService.changeItemActive(itemId)>0
                ?new JsonBean<Item>(StatusCode.UPDATE_ORDER_STATUS_SUCCESS)
                :new JsonBean<Item>(StatusCode.UPDATE_ORDER_STATUS_FAIL);
    }

    public List<Item> addItemTypeToItem(List<Item> itemList){
        List<ItemType> itemTypeList = itemService.listType();
         for (Item item:itemList) {
            item.setItemTypeList(itemTypeList);
        }
        return itemList;
    }


}





























