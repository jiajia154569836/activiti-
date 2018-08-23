package com.activiti;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

public class MyExecutionListener implements ExecutionListener,TaskListener {
    /**
     *
     */
    private static final long serialVersionUID = 7960387497099642910L;
    //ExecutionListener类的实现
    public void notify(DelegateExecution execution) throws Exception {
        String eventName = execution.getEventName();
//start
        if ("start".equals(eventName)) {
            System.out.println("start=========");
        }else if ("end".equals(eventName)) {
            System.out.println("end=========");
        }
        else if ("take".equals(eventName)) {
            System.out.println("take====连线事件触发=====");
        }
    }
    //实现TaskListener中的方法
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if ("create".endsWith(eventName)) {
            System.out.println("create====节点时间触发=====");
        }else if ("assignment".endsWith(eventName)) {
            System.out.println("assignment========");
        }else if ("complete".endsWith(eventName)) {
            System.out.println("complete===========");
        }else if ("delete".endsWith(eventName)) {
            System.out.println("delete=============");
        }
    }
}
