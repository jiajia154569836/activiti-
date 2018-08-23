//package com.activiti;
//
//import com.utils.*;
//import org.activiti.engine.ProcessEngine;
//import org.activiti.engine.ProcessEngines;
//import org.activiti.engine.RepositoryService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//
//
///**
// * 下载xml和png
// *
// */
//
//@Controller(value = "/download")
//public class UpandDownController extends BaseActivitiController {
//
//
//
////20001
//    @GetMapping
//    public void download( HttpServletResponse response)throws Exception{
//        System.out.println("OK");
//        String DEPLOYMENT_ID_ = "20001";		//部署ID
//        createXmlAndPng(DEPLOYMENT_ID_);							//生成XML和PNG
//        /*生成的全部代码压缩成zip文件*/
//        if(FileZip.zip(PathUtil.getClasspath()+"uploadFiles/activitiFile", PathUtil.getClasspath()+"uploadFiles/activitiFile.zip")){
//            /*下载代码*/
//            FileDownload.fileDownload(response, PathUtil.getClasspath()+"uploadFiles/activitiFile.zip", "activitiFile.zip");
//        }
//    }
//
//
//
//}
