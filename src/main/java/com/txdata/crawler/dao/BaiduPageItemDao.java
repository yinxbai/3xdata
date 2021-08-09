package com.txdata.crawler.dao;

import com.txdata.crawler.domain.BaiduPageItem;
import com.txdata.crawler.utils.BaiduPageItemRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BaiduPageItemDao extends BaiduPageItemRepository {

    @Override
    void insert(@Param("item") BaiduPageItem item);

    @Override
    void insert(@Param("items") List<BaiduPageItem> items);
}
