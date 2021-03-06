package activitiListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.utils.*;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;


public class LeaveActivitiTest {
    /**
     * 会默认按照Resources目录下的activiti.cfg.xml创建流程引擎
     */
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();



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
        builder.addClasspathResource("bpmn2/测试监听器请假流程.bpmn20.xml");
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
        //user1赋值不进入网关所以赋值没有意义
        map.put("user1", "abc");         //当前用户的姓名
        //   map.put("user2","王善军");         //当前用户的姓名
        //当前用户的姓名
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("KEY_leave", map);
        System.out.println("流程实例ID:" + pi.getId()); //流程实例ID
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); //流程定义ID
        System.out.println("存入实验id" + trialId + "流程实例ID" + pi.getId());
    }

    /**
     * 查看任务
     * TaskService
     */
    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
        String assignee = "user1";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
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
     * 办理任务
     */
    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "25004";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("RESULT", "批准");
        taskService.complete(taskId, map);
    }

    /**
     * 判断流程结束
     */
    @Test
    public void ifEnd() {
        String processInstanceId = "17501";
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

    /**
     * 删除流程
     */
    @Test
    public void deleteDeployment() {

        String deploymentId = "37501";
        //获取仓库服务对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //普通删除，如果当前规则下有正在执行的流程，则抛异常
        repositoryService.deleteDeployment(deploymentId);
        //级联删除，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
        //repositoryService.deleteDeployment(deploymentId, true);
        System.out.println("删除成功" + deploymentId);
    }

    /**
     * 删除key相同的所有不同版本的流程定义
     */
    @Test
    public void deleteProcessDefinitionByKey() {

        //获取数据库连接
        ProcessEngineConfiguration config = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration();
        config.setJdbcDriver("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/activiti-demo?useUnicode=true&characterEncoding=utf8");
        config.setJdbcUsername("root");
        config.setJdbcPassword("root");
         /*
              DB_SCHEMA_UPDATE_FALSE 不能创建表，需要表存在
              DB_SCHEMA_UPDATE_TRUE 先删除表再创建表
              DB_SCHEMA_UPDATE_TRUE 如表不存在自动创建表
         */
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = config.buildProcessEngine();


        //流程定义的key
        String processDefinitionKey = "HelloWorld";
        //先使用流程定义的key查询流程定义，查询出所有的版本
        RepositoryService repositoryServic = processEngine.getRepositoryService();
        ProcessDefinitionQuery query = repositoryServic.createProcessDefinitionQuery();
        List<ProcessDefinition> list = query.processDefinitionKey(processDefinitionKey).list();

        //遍历，获取每个流程定义的部署ID
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                //获取部署ID
                String deploymentId = pd.getDeploymentId();
                processEngine.getRepositoryService()
                        .deleteDeployment(deploymentId, true);//级联删除
            }
        }
    }

    /**
     * 查看最新版本的流程定义
     */
    @Test
    public void queryAllLatestVersions() {
        //获取数据库连接
        ProcessEngineConfiguration config = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration();
        config.setJdbcDriver("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/activiti-demo?useUnicode=true&characterEncoding=utf8");
        config.setJdbcUsername("root");
        config.setJdbcPassword("root");
         /*
              DB_SCHEMA_UPDATE_FALSE 不能创建表，需要表存在
              DB_SCHEMA_UPDATE_TRUE 先删除表再创建表
              DB_SCHEMA_UPDATE_TRUE 如表不存在自动创建表
         */
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = config.buildProcessEngine();

        RepositoryService repositoryServic = processEngine.getRepositoryService();
        ProcessDefinitionQuery query = repositoryServic
                .createProcessDefinitionQuery();
        //先做一个升序排列
        List<ProcessDefinition> list = query.
                orderByProcessDefinitionVersion()
                .asc() //使用版本升序排序
                .list();//获取流程定义对象List集合

        /**
         * Map<String,ProcessDefinition>
         * map集合的key：流程定义的key
         * map集合的value：流程定义的对象
         * map集合的特点：当map集合key值相同的情况下，后一次的值将替换前一次的值
         * */
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                map.put(pd.getKey(), pd);
            }
        }

        List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
        //遍历集合，查看内容
        for (ProcessDefinition pd : pdList) {
            System.out.println("id:" + pd.getId());
            System.out.println("name:" + pd.getName());
            System.out.println("key:" + pd.getKey());
            System.out.println("version:" + pd.getVersion());
            System.out.println("resourceName:" + pd.getDiagramResourceName());
            System.out.println("###########################################");
        }
    }

    /**
     * 集成流程设计器
     */
    @Test
    public void create(/*@RequestParam("name") String name, @RequestParam("key") String key,@RequestParam("description") String description, HttpServletRequest request, HttpServletResponse response*/
    ) {
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String name = "test";
        String key = "test";
        String description = "test";
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            ObjectNode editorNode = objectMapper.createObjectNode();

            editorNode.put("id", "canvas");

            editorNode.put("resourceId", "canvas");

            ObjectNode stencilSetNode = objectMapper.createObjectNode();

            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

            editorNode.put("stencilset", stencilSetNode);

            Model modelData = repositoryService.newModel();


            ObjectNode modelObjectNode = objectMapper.createObjectNode();

            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);

            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);

            description = StringUtils.defaultString(description);

            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);

            modelData.setMetaInfo(modelObjectNode.toString());

            modelData.setName(name);

            modelData.setKey(StringUtils.defaultString(key));


            repositoryService.saveModel(modelData);

            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));


            response.sendRedirect(request.getContextPath() + "activiti-editor/modeler.html?modelId=" + modelData.getId());

        } catch (Exception e) {

            //   logger.error("创建模型失败：", e);

            System.out.println("创建模型失败");
        }

    }


    /*
     * 下载png和xml
     * */

    @Test
    public void download() throws Exception {
        HttpServletResponse response=null;
        String DEPLOYMENT_ID_ = "22501";        //部署ID
        createXmlAndPng(DEPLOYMENT_ID_);                            //生成XML和PNG
        /*生成的全部代码压缩成zip文件*/
        if (FileZip.zip(PathUtil.getClasspath() + "uploadFiles/activitiFile", PathUtil.getClasspath() + "uploadFiles/activitiFile.zip")) {
            /*下载代码*/
            FileDownload.fileDownload(response, PathUtil.getClasspath() + "uploadFiles/activitiFile.zip", "activitiFile.zip");
        }
    }

    protected void createXmlAndPng(String DEPLOYMENT_ID_) throws IOException {

        RepositoryService repositoryService = processEngine.getRepositoryService();
        DelAllFile.delFolder(PathUtil.getClasspath() + "uploadFiles/activitiFile");            //生成先清空之前生成的文件
        List<String> names = repositoryService.getDeploymentResourceNames(DEPLOYMENT_ID_);
        for (String name : names) {
            if (name.indexOf("zip") != -1) continue;
            InputStream in = repositoryService.getResourceAsStream(DEPLOYMENT_ID_, name);
            FileUpload.copyFile(in, PathUtil.getClasspath() + Const.FILEACTIVITI, name);        //把文件上传到文件目录里面
            in.close();
        }
    }
}
