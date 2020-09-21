package com.born.secKill.server.service.impl;

import com.born.secKill.model.entity.Item;
import com.born.secKill.model.entity.ItemType;
import com.born.secKill.model.mapper.ItemMapper;
import com.born.secKill.server.common.ConstantClass;
import com.born.secKill.server.service.IItemService;
import com.born.secKill.server.utils.JsonBean;
import com.born.secKill.server.utils.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author:gyk
 * @Date: 2020/3/16 22:45
 **/
@Service
public class ItemServiceImpl implements IItemService{

    private static final Logger log= LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<Item> listItem() {
        return itemMapper.listItem();
    }

    @Override
    public JsonBean<Item> getItem(Integer itemId) {
        Item item = itemMapper.selectByPrimaryKey(itemId);
        return item==null
                ?new JsonBean<>(StatusCode.ITEM_FAIL)
                :new JsonBean<>(StatusCode.ITEM_SUCCESS,item);
    }


    @Override
    public JsonBean<Object> isItemExist(Integer itemId) {
        return itemMapper.selectByPrimaryKey(itemId)==null
                ?new JsonBean<>(StatusCode.ITEM_NOT_EXISTS)
                :new JsonBean<>(StatusCode.ITEM_HAS_EXISTS);
    }

    @Override
    public Integer addKillItem(Item item) {
        return itemMapper.insert(item);
    }

    @Override
    public Integer updateKillItem(Item item) {
        return itemMapper.updateByPrimaryKeySelective(item);
    }

    @Override
    public Integer changeItemActive(Integer itemId) {
        Item item = itemMapper.selectByPrimaryKey(itemId);
        if (null==item){
            log.error("id为{}的商品不存在",itemId);
            throw new RuntimeException("id为"+itemId+"的商品不存在");
        }
        if (ConstantClass.ITEM_ACTIVE.equals(item.getIsActive())){
            item.setIsActive(ConstantClass.ITEM_FORBIDDEN);
        }else if (ConstantClass.ITEM_FORBIDDEN.equals(item.getIsActive())){
            item.setIsActive(ConstantClass.ITEM_ACTIVE);
        }else {
            log.error("id为{}的商品启用状态异常",itemId);
            throw new RuntimeException("id为"+itemId+"的商品启用状态异常");
        }
        return itemMapper.updateByPrimaryKeySelective(item);
    }

        /**
     * 获取所有商品类别
     */
    @Override
    public List<ItemType> listType(){
        return itemMapper.listType();
    }
}



















