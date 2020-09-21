package com.born.secKill.model.mapper;

import com.born.secKill.model.entity.Item;
import com.born.secKill.model.entity.ItemType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Item record);

    int insertSelective(Item record);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);

    Item selectByPrimaryKey(Integer id);

    List<Item> listItem();

    /**
     * 获取所有商品类别
     */
    List<ItemType> listType();
}