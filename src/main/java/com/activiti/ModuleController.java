package com.activiti;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;

import org.activiti.engine.ManagementService;

import org.activiti.engine.RepositoryService;

import org.activiti.engine.RuntimeService;

import org.activiti.engine.TaskService;

import org.activiti.engine.repository.Model;

import org.activiti.engine.repository.ProcessDefinition;

import org.activiti.engine.runtime.ProcessInstance;

import org.activiti.engine.task.Task;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;



@Controller
@RequestMapping(value = "/models")
public class ModuleController {

    private Logger logger = LoggerFactory.getLogger(ModuleController.class);

    @Autowired

    private RepositoryService repositoryService;

    @Autowired

    private RuntimeService runtimeService;

    @Autowired

    private TaskService taskService;

    @Autowired

    private ManagementService managementService;


//localhost:8080/models/create?name=六级签审&key=six_approve&description=六级签审
    @GetMapping(value = "/create")
    public String create(@RequestParam("name") String name, @RequestParam("key") String key,@RequestParam("description") String description/*, HttpServletRequest request, HttpServletResponse response*/) throws IOException {
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
        try {
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(""+modelData.getId());
     //   return "activiti-editor/modeler.html?modelId=" + modelData.getId();

         //   response.sendRedirect(request.getContextPath() +"activiti-editor/modeler.html?modelId=" + modelData.getId());

        return "home/editor";


    }



}
