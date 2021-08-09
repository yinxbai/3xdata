package com.txdata.crawler.controller;

import com.txdata.crawler.service.BaiduPageItemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler/baidu/items")
public class BaiduPageItemController {

    private final BaiduPageItemService baiduPageItemService;

    public BaiduPageItemController(BaiduPageItemService baiduPageItemService) {
        this.baiduPageItemService = baiduPageItemService;
    }

    /**
     * 根据关键词爬取百度搜索结果，然后保存到数据库中
     *
     * @param keyword 关键词
     */
    @PostMapping("/extractAndSave")
    public void extractAndSave(@RequestParam("keyword") String keyword) throws Exception {
        baiduPageItemService.extractAndSave(keyword);
    }
}
