package com.txdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author InRoota
 */
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableTransactionManagement
@ServletComponentScan
@MapperScan({"com.txdata.*.*.dao","com.txdata.*.dao"})
@SpringBootApplication
@EnableCaching
public class TxdataApplication {
    public static void main(String[] args) {
        SpringApplication.run(TxdataApplication.class, args);
        System.out.println("ヾ(◍°∇°◍)ﾉﾞ    txdata启动成功 ");
    }
}
