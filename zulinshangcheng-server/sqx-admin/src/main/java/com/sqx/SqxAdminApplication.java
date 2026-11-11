package com.sqx;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@MapperScan(basePackages = {
        "com.sqx.domain.repository.mapper"
})
@EnableScheduling
@SpringBootApplication
@Slf4j
public class SqxAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqxAdminApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  租赁商城启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "       _    \n" +
                "      | |   \n" +
                "  ___ | | __\n" +
                " / _ \\| |/ /\n" +
                "| (_) |   < \n" +
                " \\___/|_|\\_\\");

    }
}
