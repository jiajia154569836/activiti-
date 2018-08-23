package activiti;

import org.activiti.engine.*;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitwayTest {

    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    RuntimeService runtimeService = processEngine.getRuntimeService();

    RepositoryService repositoryService = processEngine.getRepositoryService();

    TaskService taskService = processEngine.getTaskService();


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
        builder.addClasspathResource("bpmn3/Cy.bpmn20.xml");
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
        String trialId = "001";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trialId", trialId);            //当前实验id
        //   map.put("user2","王善军");         //当前用户的姓名
        //当前用户的姓名
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("Cy1", map);
        System.out.println("流程实例ID:" + pi.getId()); //流程实例ID
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); //流程定义ID
        System.out.println("存入实验id" + trialId + "流程实例ID" + pi.getId());
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            Task task = tasks.get(i);

        }

        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }

        // TaskDefinition  a= getNextTaskInfo();
    }

//    流程实例ID:75001
//    流程定义ID:Cy1:1:72504
//    存入实验id001流程实例ID75001
//    taskId:75006,taskName:K,assignee:null,createTime:Tue Aug 21 10:06:43 CST 2018

    /**
     * 办理任务根据id
     */

    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "75006";
        String taskName = "K";
        String processId = "75001";
        Task task = taskService.createTaskQuery().processInstanceId(processId).taskDefinitionKey(taskName).singleResult();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("git1", "1");
        taskService.complete(taskId, map);
    }

    /**
     * 办理任务根据key
     */

    @Test
    public void handle1Task() {
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "62506";
        String taskName = "A";
        String taskKey = "user1";
        String processId = "75001";
        Task task = taskService.createTaskQuery().taskDefinitionKey(taskKey).singleResult();
        System.out.println("taskid"+task.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("git1", "1");
        taskService.complete(task.getId(), map);
    }

    /**
     * 查看任务
     * TaskService
     */
    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据processid
        String processid = "75001";
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processid).list();
        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            Task task = tasks.get(i);

        }

        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }
    }

    /**
     * 判断流程结束
     */
    @Test
    public void ifEnd() {
        String processInstanceId = "55001";
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

    /*
     *输出下一节点的key
     * */

    @Test
    public void searchTask() throws Exception {

        String taskId = "77505";
        String processId = "75001" ;
        String processDefId = "Cy1:1:72504";
        List<TaskDefinition> nextTaskGroup = getNextTaskInfo(taskId);
        if (nextTaskGroup.size() > 0) {
            for (TaskDefinition aa : nextTaskGroup) {
                System.out.println(aa.getKey());
                //查出taskname
                System.out.println(aa.getNameExpression());

            }
        }
    }

    //获取下一节点办理人
    //TaskDefinition a =    getNextTaskInfo();

    /**
     * 获取下一个用户任务信息
     *
     * @param String taskId     任务Id信息
     * @return 下一个用户任务用户组信息
     * @throws Exception
     */
    public List<TaskDefinition> getNextTaskInfo(String taskId) throws Exception {

        ProcessDefinitionEntity processDefinitionEntity = null;

        String id = null;

        List<TaskDefinition> tasks = new ArrayList<TaskDefinition>();

        //获取流程实例Id信息
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();

        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();

        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definitionId);

        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

        //当前流程节点Id信息
        String activitiId = execution.getActivityId();

        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

        //遍历所有节点信息
        for (ActivityImpl activityImpl : activitiList) {
            id = activityImpl.getId();
            System.out.println("id=" + id);
            if (activitiId.equals(id)) {
                //获取下一个节点信息
                tasks = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
                break;
            }
        }
        return tasks;
    }

    /**
     * 下一个任务节点信息,
     * <p>
     * 如果下一个节点为用户任务则直接返回,
     * <p>
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务
     *
     * @param ActivityImpl activityImpl     流程节点信息
     * @param String       activityId             当前流程节点Id信息
     * @param String       elString               排他网关顺序流线段判断条件
     * @param String       processInstanceId      流程实例Id信息
     * @return
     */
    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId) {

        PvmActivity ac = null;
        List<TaskDefinition> tasks = new ArrayList<TaskDefinition>();
        List<TaskDefinition> taskss = new ArrayList<TaskDefinition>();
        Object s = null;

        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                    .getTaskDefinition();
            tasks.add(taskDefinition);
            return tasks;
        } else {
            // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination(); // 获取线路的终点节点
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();

                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = getGatewayCondition(ac.getId(), processInstanceId);
                        System.out.println("el=" + elString);
                    }

                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        System.out.println("1条");
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
                                elString, processInstanceId);

                    } else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
                        System.out.println("多条");
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
                            System.out.println("s=" + s.toString());
                            // 判断el表达式是否成立
//                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                            System.out.println("成立");

                            taskss.add(nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                    processInstanceId).get(0));
                            // }

                        }
                        return taskss;
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {

                    taskss.add(((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition());
                    return taskss;
                } else {
                }
            }
            return null;
        }
    }

    /**
     * 查询流程启动时设置排他网关判断条件信息
     *
     * @param String gatewayId          排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
     * @param String processInstanceId  流程实例Id信息
     * @return
     */
    public String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object = runtimeService.getVariable(execution.getId(), gatewayId);
        return object == null ? "" : object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过信息
     *
     * @param String key    el表达式key信息
     * @param String el     el表达式信息
     * @param String value  el表达式传入值信息
     * @return
     */
    public boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }
}
