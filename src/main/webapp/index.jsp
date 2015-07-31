<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.11.0.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cookie.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd/ReloadExtension.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd-reload.js"></script>
    <script type="text/javascript" src="application.js"></script>
    <script type="text/javascript">
        var config = {
            contextPath: '${pageContext.request.contextPath}'
        };
    </script>
</head>
<body>
	
    <div id="body"></div>

</body>
</html>
