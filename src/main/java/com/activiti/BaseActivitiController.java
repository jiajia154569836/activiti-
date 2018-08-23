package com.activiti;

import com.utils.Const;
import com.utils.DelAllFile;
import com.utils.FileUpload;
import com.utils.PathUtil;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class BaseActivitiController
{

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    private RepositoryService repositoryService = processEngine.getRepositoryService();
    /*
     * 生成XML和PNG
     * 根据部署ID
     * */
    protected void createXmlAndPng(String DEPLOYMENT_ID_) throws IOException {


        DelAllFile.delFolder(PathUtil.getClasspath()+"uploadFiles/activitiFile"); 			//生成先清空之前生成的文件
        List<String> names = repositoryService.getDeploymentResourceNames(DEPLOYMENT_ID_);
        for (String name : names) {
            if(name.indexOf("zip")!=-1)continue;
            InputStream in = repositoryService.getResourceAsStream(DEPLOYMENT_ID_, name);
            FileUpload.copyFile(in,PathUtil.getClasspath()+Const.FILEACTIVITI,name); 		//把文件上传到文件目录里面
            in.close();
        }
    }
}
