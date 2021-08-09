package com.txdata.crawler.service;

import com.txdata.crawler.utils.BaiduPageProcessor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class BaiduPageItemService {

    private final BaiduPageProcessor baiduPageProcessor;

    public BaiduPageItemService(BaiduPageProcessor baiduPageProcessor) {
        this.baiduPageProcessor = baiduPageProcessor;
    }

    public void extractAndSave(String keyword) throws UnsupportedEncodingException {
        baiduPageProcessor.run(keyword);
    }

}
