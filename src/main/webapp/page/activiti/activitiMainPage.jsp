<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<head>
    <title>activiti首页</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="<%=path%>/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="<%=path%>/css/bootstrap.min.css" />
    <meta charset="utf-8">
</head>
<body>
<!-- Provides extra visual weight and identifies the primary action in a set of buttons -->
<button id="deployment" type="button" class="btn btn-primary">部署</button>

<!-- Indicates a successful or positive action -->
<button id="download" type="button" class="btn btn-success">下载</button>

<!-- Contextual button for informational alert messages -->
<button id="searchimage" onclick="view(22501)" type="button" class="btn btn-info">查看流程图</button>

<!-- Indicates a dangerous or potentially negative action -->
<button id="processimage" onclick="viewprocess(25001)" type="button" class="btn btn-danger">查看流程节点</button>

<!-- Indicates caution should be taken with this action -->
<button id="processdesignimage" type="button" class="btn btn-warning">查看流程设计器图片</button>

</body>
<script type="application/javascript">
    //生成key
    $("#project").click(function () {
        window.open( "<%=path%>/project")
    });

    $("#processdesignimage").click(function () {
        window.location.href="../../activiti-editor/modeler.html?modelId=40001";
    });

    //查看流程节点
    function viewprocess(processId){

        $.ajax({
            type: "POST",
            url: '<%=basePath%>/activiti-process/getActivitiProccessImage',
            data: {processId:processId},
          //  dataType:'json',
            cache: false,
            success: function(data){

            }
        });
    }

    //预览
    function view(deploymentId){

        $.ajax({
            type: "POST",
            url: '<%=basePath%>/activiti-watchimage/goViewPng',
            data: {deploymentId:deploymentId},
            //  dataType:'json',
            cache: false,
            success: function(data){

            }
        });
    }
</script>
</html>

