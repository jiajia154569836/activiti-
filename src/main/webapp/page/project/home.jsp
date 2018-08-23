<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<head>
    <title>项目页</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="<%=path%>/js/jquery-1.11.1-min.js"></script>
    <meta charset="utf-8">

    <link rel="StyleSheet" href="<%=path%>/page/dtree/dtree.css" type="text/css" />
    <script type="text/javascript" src="<%=path%>/page/dtree/dtree.js"></script>


</head>
<body>
<h2>项目！！！</h2>
<div class="dtree">

    <p><a href="javascript: d.openAll();">文件树展开</a> | <a href="javascript: d.closeAll();">文件树关闭</a></p>

    <script type="text/javascript">
        <!--

        d = new dTree('d');

        d.add(0,-1,'My example tree');
        d.add(1,0,'Node 1','<%=path%>/project/home/content.jsp');
        d.add(2,0,'Node 2','');
        d.add(3,1,'Node 1.1','1');
        d.add(4,0,'Node 3','1');
        d.add(5,3,'Node 1.1.1','1');
        d.add(6,5,'Node 1.1.1.1','1');
        d.add(7,0,'Node 4','1');
        d.add(8,1,'Node 1.2','1');
        d.add(9,0,'My Pictures','1','Pictures I\'ve taken over the years','','<%=path%>/page/dtree/img/imgfolder.gif');
        d.add(10,9,'The trip to Iceland','1','Pictures of Gullfoss and Geysir');
        d.add(11,9,'Mom\'s birthday','1');
        d.add(12,0,'Recycle Bin','home/index','','','<%=path%>/page/dtree/img/trash.gif');

        document.write(d);

        //-->
    </script>
</div>

<div class="dcontent">
    <table border="1">
        <tr>
            <td>项目名称</td>
            <td>项目来源</td>
            <td>时间</td>
            <td>项目经理</td>
            <td>密级</td>
        </tr>

        <c:forEach var="user" items="${list}">
        <tr>
            <td><a href="/home/${user.id}">项目${user.id}</a></td>
            <td>${user.username}</td>
            <td>${user.password}</td>
            <td>${user.id}</td>
            <td>${user.id}</td>
        </tr>
        </c:forEach>

    </table>
</div>
</body>

</html>

