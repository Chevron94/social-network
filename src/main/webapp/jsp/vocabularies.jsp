<%@ page import="socialnetwork.entities.User" %>
<%@ page import="socialnetwork.entities.Vocabulary" %>
<%@ page import="java.util.List" %>
<%@ page import="socialnetwork.dto.VocabularyDto" %>
<html>
<head>
    <title>Vocabularies</title>
    <%@include file="templates/scripts.jsp" %>
    <%
        User requestUser = (User) request.getAttribute("requestUser");
        List<VocabularyDto> vocabularies = (List<VocabularyDto>) request.getAttribute("vocabularies");
    %>
</head>
<body>
<br>
<%@include file="templates/header.jsp" %>
</body>
</html>