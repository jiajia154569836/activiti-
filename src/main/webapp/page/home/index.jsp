<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<head>
    <title>首页</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1-min.js"></script>
    <meta charset="utf-8">
</head>
<body>
<h2>Hello ${name}</h2>
<table>
    <tr>
        <td><button id="project">项目</button></td> <td><button id=trail>实验</button></td> <td><button id="data">数据</button></td>
    </tr>
</table>
</body>
<script type="application/javascript">
    //生成key
    $("#project").click(function () {
        window.open( "<%=path%>/project")
    });
</script>
</html>

