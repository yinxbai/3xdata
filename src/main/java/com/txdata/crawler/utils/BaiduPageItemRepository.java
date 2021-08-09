package com.txdata.crawler.utils;

import com.txdata.crawler.domain.BaiduPageItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaiduPageItemRepository {

    void insert(BaiduPageItem item);

    void insert(List<BaiduPageItem> items);
}
