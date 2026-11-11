//package com.sqx.runner;
//
//import com.sqx.domain.repository.service.sys.ICommonInfoService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author chengming
// * @since 2024/7/23
// */
//@Slf4j
//@Component
//public class SqxAdminApplicationRunner implements ApplicationRunner {
//    @Resource
//    private ICommonInfoService commonInfoService;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        log.info("初始化系统配置...");
//        commonInfoService.loadCommonInfo();
//    }
//}
