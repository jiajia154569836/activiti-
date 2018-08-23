package activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class MyShenPi {
    /**获得流程引擎*/
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**部署流程定义*/
    @Test
    public void deployProcessDefinition(){

        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署相关的Service
                .createDeployment()//创建部署对象
                .addClasspathResource("bpmn/请假流程(反射).bpmn20.xml")
                .addClasspathResource("bpmn/请假流程(反射).KEY_leave.png")
                .deploy();//完成部署

        System.out.println("部署ID：" + deployment.getId());//部署ID:1
        System.out.println("部署时间：" + deployment.getDeploymentTime());//部署时间
    }

    /**启动流程实例*/
    @Test
    public void startProcessInstance() {

        String processDefinitionKey = "KEY_leave";//流程定义的key,也就是bpmn中存在的ID

        ProcessInstance pi = processEngine.getRuntimeService()//管理流程实例和执行对象，也就是表示正在执行的操作
                .startProcessInstanceByKey(processDefinitionKey);////按照流程定义的key启动流程实例

        System.out.println("流程实例ID：" + pi.getId());//流程实例ID：101
        System.out.println("流程实例ID：" + pi.getProcessInstanceId());//流程实例ID：101
        System.out.println("流程实例ID:" + pi.getProcessDefinitionId());//myMyHelloWorld:1:4
    }

    }
