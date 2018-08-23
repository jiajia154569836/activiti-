<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<head>
    <title>登陆页</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1-min.js"></script>
    <meta charset="utf-8">
</head>
<body>
<h2>Hello World!</h2>
<form action="/home" method="get">
    <input type="submit" value="登陆">
</form>
</body>

</html>
