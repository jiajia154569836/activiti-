package com.activiti;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: [Activiti工作流公共方法Controller，提供工作流相关公共方法]
 * @Author: [Double]
 * @CreateDate: [2015-10-22]
 * @Version: [v2.0.0]
 */
@Controller
@RequestMapping("/activiti-process")
public class ActivitiController {

	static final Logger logger = Logger.getLogger(ActivitiController.class);

	private  ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();


	private RepositoryService repositoryService = processEngine.getRepositoryService();


	private TaskService taskService = processEngine.getTaskService();;


	private RuntimeService runtimeService =processEngine.getRuntimeService();


	private HistoryService historyService =processEngine.getHistoryService();

	/** 
	 * 打开流程图显示页面
	 **/
	@RequestMapping(params = "openActivitiProccessImagePage")
	public ModelAndView openActivitiProccessImagePage(String pProcessInstanceId) throws Exception {
		logger.info("[开始]-打开流程图显示页面");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("processInstanceId", pProcessInstanceId);
		modelAndView.setViewName("common/jsp/ActivitiProccessImagePage.jsp");
		logger.info("[完成]-打开流程图显示页面");
		return modelAndView;
	}

	/** 
	 * 获取流程图像，已执行节点和流程线高亮显示
	 */
	//@RequestMapping(params = "getActivitiProccessImage")
	@RequestMapping(value = "getActivitiProccessImage")
	public void getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) throws Exception {

		//String pProcessInstanceId ="25001";
		logger.info("[开始]-获取流程图图像");
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			//  获取历史流程实例
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(pProcessInstanceId).singleResult();

			if (historicProcessInstance == null) {
				throw new Exception("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
			} else {
				// 获取流程定义
				ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());

				// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
				List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(pProcessInstanceId)/*.orderByHistoricActivityInstanceId().asc()*/.list();

				// 已执行的节点ID集合
				List<String> executedActivityIdList = new ArrayList<String>();
				int index = 1;
				logger.info("获取已经执行的节点ID");
				for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
					executedActivityIdList.add(activityInstance.getActivityId());
					logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
					index++;
				}

				// 获取流程图图像字符流
				InputStream imageStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", executedActivityIdList);

				response.setContentType("image/png");
				OutputStream os = response.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = imageStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				imageStream.close();
			}
			logger.info("[完成]-获取流程图图像");
		} catch (Exception e) {
			logger.error("【异常】-获取流程图失败！" + e.getMessage());
			throw new Exception("获取流程图失败！" + e.getMessage());
		}
	}
}
