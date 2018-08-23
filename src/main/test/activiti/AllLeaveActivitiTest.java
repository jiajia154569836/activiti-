package activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class AllLeaveActivitiTest {
    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void test() {
        //以下两种方式选择一种创建引擎方式：1.配置写在程序里 2.读对应的配置文件
        //1
        testCreateProcessEngine();
        //2
        testCreateProcessEngineByCfgXml();

        deployProcess();
        startProcess();
      //  queryTask();
        //handleTask();
    }

    /**
     * 测试activiti环境
     */
    @Test
    public void testCreateProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver("com.mysql.jdbc.Driver");
        cfg.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        cfg.setJdbcUsername("test");
        cfg.setJdbcPassword("1234");
        //配置建表策略
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    /**
     * 根据配置文件activiti.cfg.xml创建ProcessEngine
     */
    @Test
    public void testCreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    /**
     * 发布流程
     * RepositoryService
     */
    @Test
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("bpmn/请假流程(反射).bpmn20.xml");
        builder.deploy();
    }

    /**
     * 启动流程
     * RuntimeService
     */
    @Test
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //可根据id、key、message启动流程
        runtimeService.startProcessInstanceByKey("KEY_leave");

    }

    /**
     * user1新建任务并处理
     * TaskService
     */
    @Test
    public void user1Task() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String processInstanceId = "2501";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("user2","wangshanjun");
        Task task  = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey("option1")
                .singleResult();

//        List<Task> list = taskService.createTaskQuery().processInstanceId(processId).list();
//        Optional<Task> taskOptional = list.stream().filter(task -> StringUtils.equals(task.getTaskDefinitionKey(), "option1")).findFirst();
//        if (!taskOptional.isPresent()) {
//            System.out.println("工作流当前节点不在{}节点"+"option1");
//            return;
//        }

            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
            taskService.complete(task.getId(),map);


    }

    /**
     * user2处理任务
     * TaskService
     */
    @Test
    public void user2Task() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String processInstanceId = "2501";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("RESULT","批准");
        map.put("user3","jiangyue");
        map.put("user2opinion","ok");
        Task task  = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey("option2")
                .singleResult();

        System.out.println("taskId:" + task.getId() +
                ",taskName:" + task.getName() +
                ",assignee:" + task.getAssignee() +
                ",createTime:" + task.getCreateTime());

        taskService.complete(task.getId(),map);


    }

    /**
     * user3处理任务
     * TaskService
     */
    @Test
    public void user3Task() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String processInstanceId = "2501";
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("RESULT","批准");
        map.put("user3opinion","ok");
        Task task  = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskDefinitionKey("option3")
                .singleResult();


        System.out.println("taskId:" + task.getId() +
                ",taskName:" + task.getName() +
                ",assignee:" + task.getAssignee() +
                ",createTime:" + task.getCreateTime());

//
//        String user1 = (String) taskService.getVariable(task.getId(), "user1");
//        System.out.println(user1);

        taskService.complete(task.getId(),map);

    }

    /**
     * 判断流程结束
     */
    @Test
    public void ifEnd() {
        String processInstanceId = "2501";
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .singleResult();
        if (pi == null) {
            System.out.println("流程结束");
        } else {
            System.out.println("未结束");
        }
    }
}
