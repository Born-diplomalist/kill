package com.born.secKill.server.service;


import com.born.secKill.model.entity.Item;
import com.born.secKill.model.entity.ItemType;
import com.born.secKill.server.utils.JsonBean;

import java.util.List;

public interface IItemService {

    List<Item> listItem();

    JsonBean<Item> getItem(Integer itemId);

    Integer addKillItem(Item item);

    Integer updateKillItem(Item item);

    Integer changeItemActive(Integer itemId);

    JsonBean<Object> isItemExist(Integer itemId);

        /**
     * 获取所有商品类别
     */
    List<ItemType> listType();
}
