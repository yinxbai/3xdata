package com.txdata.crawler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "crawler.baidu")
public class BaiduCrawlerConfig {

    private Integer thread;
    private Site site;

    public BaiduCrawlerConfig() {
        this.thread = Runtime.getRuntime().availableProcessors();
        this.site = new Site();
    }

    public Integer getThread() {
        return thread;
    }

    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static class Site {

        private String charset;
        private Integer retryTimes;
        private Integer sleepTime;
        private Integer timeout;

        public Site() {
            this.charset = "UTF-8";
            this.retryTimes = 3;
            this.sleepTime = 1000;
            this.timeout = 10000;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public Integer getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(Integer retryTimes) {
            this.retryTimes = retryTimes;
        }

        public Integer getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(Integer sleepTime) {
            this.sleepTime = sleepTime;
        }

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
    }
}
