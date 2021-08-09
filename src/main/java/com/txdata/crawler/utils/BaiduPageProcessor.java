package com.txdata.crawler.utils;

import com.txdata.crawler.config.BaiduCrawlerConfig;
import com.txdata.crawler.domain.BaiduPageItem;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BaiduPageProcessor implements PageProcessor {

    private final BaiduCrawlerConfig config;
    private final Site site;
    private final BaiduPageItemRepository repository;

    public BaiduPageProcessor(BaiduCrawlerConfig config, BaiduPageItemRepository repository) {
        this.config = config;
        this.site = Site.me()
            .setCharset(config.getSite().getCharset())
            .setRetryTimes(config.getSite().getRetryTimes())
            .setSleepTime(config.getSite().getSleepTime())
            .setTimeOut(config.getSite().getTimeout());
        this.repository = repository;
    }

    @Override
    public void process(Page page) {
        Document doc = page.getHtml().getDocument();
        List<BaiduPageItem> items = new ArrayList<>();
        for (Element element : doc.select(".result.c-container")) {
            BaiduPageItem item = new BaiduPageItem();
            item.setId(UUID.randomUUID().toString());
            Elements title = element.select("h3 a");
            item.setTitle(title.text());
            item.setUrl(title.attr("href"));
            Elements summary = element.select(".c-abstract");
            item.setSummary(summary.text());
            items.add(item);
        }
        repository.insert(items);
        Elements nextPage = doc.select("#page a:last-child");
        if (nextPage.text().contains("下一页")) {
            page.addTargetRequest(URI.create(page.getUrl().toString()).resolve(URI.create(nextPage.attr("href"))).toString());
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run(String keyword) throws UnsupportedEncodingException {
        Spider
            .create(this)
            .setDownloader(new HttpClientDownloader())
            .addUrl("http://www.baidu.com/s?gpc=stf=1569168000,1569168000|stftype=2&wd=" + URLEncoder.encode(keyword, "UTF-8"))
            .thread(config.getThread())
            .run();
    }
}
