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
<h2></h2>
<table>
    <tr>
        <td class='center'><img alt="${pd.FILENAME }" src="${pd.imgSrc }"></td>
    </tr>
</table>
</body>
<script type="application/javascript">
    //生成key
    $("#project").click(function () {
        window.open( "<%=path%>/project")
    });

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

