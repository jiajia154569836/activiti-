package com.activiti;


import com.utils.Const;
import com.utils.ImageAnd64Binary;
import com.utils.PathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

//activiti-watchimage/goViewPng/20001
@Controller
@RequestMapping(value = "/activiti-watchimage")

public class WatchImageController extends BaseActivitiController {

    /**去预览PNG页面
     * @param
     * @throws Exception
     */
    @RequestMapping(value="/goViewPng")
    public ModelAndView goViewPng(/*@PathVariable String deploymentId*/)throws Exception{
        String deploymentId="20001";
        ModelAndView mv = new ModelAndView();
        Map<String,Object> pd = new HashMap<String, Object>();
        String DEPLOYMENT_ID_ = deploymentId;		//部署ID
        createXmlAndPng(DEPLOYMENT_ID_);							//生成XML和PNG
        String FILENAME = URLDecoder.decode("请假流程(反射)", "UTF-8");
        pd.put("FILENAME", "请假流程(反射)");
        String imgSrcPath = PathUtil.getClasspath()+Const.FILEACTIVITI+FILENAME;
        pd.put("imgSrc", "data:image/jpeg;base64,"+ImageAnd64Binary.getImageStr(imgSrcPath)); //解决图片src中文乱码，把图片转成base64格式显示(这样就不用修改tomcat的配置了)
        mv.setViewName("activiti/png_view");
        mv.addObject("pd", pd);
        return mv;
    }
}
