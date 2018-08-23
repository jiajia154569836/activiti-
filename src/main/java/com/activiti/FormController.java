package com.activiti;


import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller(value = "activiti-form")
public class FormController {


    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


    private RepositoryService repositoryService = processEngine.getRepositoryService();


    private TaskService taskService = processEngine.getTaskService();;


    private RuntimeService runtimeService =processEngine.getRuntimeService();


    private HistoryService historyService =processEngine.getHistoryService();

    private FormService formService = processEngine.getFormService();

    /*
     * 启动流程 启动流程，只考虑首次登录。 首次登录：启动工作流，并且更新/{processDefinitionId} @RequestMapping(value = "get-form/start/{processDefinitionId}")
     */
    @RequestMapping(value = "/start")
    public String start( HttpServletRequest request) throws Exception {

        //可根据id、key、message启动流程
        String trialId = "001";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("trialId", trialId);            //当前实验id
        map.put("user1", "陈越");         //当前用户的姓名
        //   map.put("user2","王善军");         //当前用户的姓名
        //当前用户的姓名
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey("KEY_leave", map);
        System.out.println("流程实例ID:" + pi.getId()); //流程实例ID
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); //流程定义ID
        System.out.println("存入实验id" + trialId + "流程实例ID" + pi.getId()+"流程定义ID:" + pi.getProcessDefinitionId());
       /*
            // 定义map用于往工作流数据库中传值。
            Map<String, String> formProperties = new HashMap<String, String>();
            //启动流程-何静媛-2015年5月24日--processDefinitionId,
            ProcessInstance processInstance = formService
                    .submitStartFormData(processDefinitionId,
                            formProperties);
            // 返回到显示用户信息的controller
           // logger.debug("start a processinstance: {}", processInstance);
        */
       System.out.println("processinstance"+pi);
            return "redirect:/activiti-form/get-form/task/"+ pi.getId();




    }

    /**
     * 读取Task的表单
     * @RequestMapping(value = "get-form/task/{processDefinitionkey}")
     * @PathVariable("processDefinitionkey") String processDefinitionkey
     */
    @RequestMapping(value = "/get-form/task/{processInstanceId}")
    @ResponseBody
    public ModelAndView findTaskForm(
            @PathVariable("processInstanceId") String processInstanceId,
            HttpServletRequest request) throws Exception {
        ModelAndView mav = new ModelAndView("activiti/apply");
        // 获取当前登陆人信息。
        /* User user = UserUtil.getUserFromSession(request.getSession()); */

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId).taskDefinitionKey("option1").singleResult();



        Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
        System.out.println(renderedTaskForm.toString());
        mav.addObject("renderedTaskForm", renderedTaskForm.toString());//整个页面，参数已经赋值（整个页面是什么时候赋上值的？）
        mav.addObject("taskId", task.getId());
        mav.addObject("processInstanceId", processInstanceId);
        return mav;
    }

    /**
     * 办理任务，提交task的并保存form
     */
    @RequestMapping(value = "task/complete/{taskId}/{processInstanceId}")
    @SuppressWarnings("unchecked")
    public String completeTask(@PathVariable("taskId") String taskId, @PathVariable("processInstanceId") String processInstanceId, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Map<String, String> formProperties = new HashMap<String, String>();

        // 从request中读取参数然后转换
        Map<String, String[]> parameterMap = request.getParameterMap();
        Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String[]> entry : entrySet) {
            String key = entry.getKey();

            /*
             * 参数结构：fq_reason，用_分割 fp的意思是form paremeter 最后一个是属性名称
             */
            if (StringUtils.defaultString(key).startsWith("fp_")) {
                String[] paramSplit = key.split("_");
                formProperties.put(paramSplit[1], entry.getValue()[0]);
            }
        }

       // logger.debug("start form parameters: {}", formProperties);

        try {
            formService.submitTaskFormData(taskId, formProperties);
        } finally {
           // identityService.setAuthenticatedUserId(null);
        }

        redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
        return "redirect:/workflow/auto/get-form/task/"+processInstanceId;

    }

}
