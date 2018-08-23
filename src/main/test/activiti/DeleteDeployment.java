package activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

public class DeleteDeployment {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService = processEngine.getRepositoryService();
    TaskService taskService = processEngine.getTaskService();
    String processInstanceId = "";
    Task task  = taskService.createTaskQuery()
            .processInstanceId(processInstanceId)
            .taskDefinitionKey("option2")
            .singleResult();

    String processDefinitionId=task.getProcessDefinitionId(); // 获取流程定义id

    ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

    String deploymentId =  processDefinition.getDeploymentId();  //部署id

    RepositoryServiceImpl repositoryServices = new RepositoryServiceImpl();

    //   processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
    //普通删除，如果当前部署的规则还存在正在制作的流程，则抛异常

      //repositoryService.deleteDeployment(部署id,boolean值);  //级联删除，如果 boolean值为true时，会删除所有和当前部署的规则相关的信息，包括历史的信息。
}
