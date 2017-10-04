<%@ page import="socialnetwork.entities.Language" %>
<%@ page import="java.util.List" %>
<%@ page import="socialnetwork.entities.LanguageLevel" %><%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 23.04.2016
  Time: 14:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Set languages</title>
    <%@include file="templates/scripts.jsp"%>
    <%
        List<Language> languages = (List<Language>) request.getAttribute("languages");
        List<LanguageLevel> languageLevels = (List<LanguageLevel>) request.getAttribute("languageLevels");
        String error = (String) request.getAttribute("error");
    %>
</head>
<body>
<br>
<%@include file="templates/header.jsp"%>
<h3 align="center">Please tell us about languages which you are learning or know</h3>
<%
    if(error!=null){
%>
<div style="background-color: red">
    <h4 align="center"><%=error%></h4>
</div>
<%
    }
%>
<form method="post" class="form-horizontal" id="languagesForm" modelAttribute="languagesForm" name="languagesForm">
    <div id="selects">
        <div id="languageGroup1" class="form-group form-group-sm">
            <div class="row" style="margin-left:20%">
                <label class="control-label col-xs-2" for="languages[0]">Language and level<sup style="color: red">*</sup></label>
                <div class="col-xs-6" align="left">
                    <select name="languages[0]" class="form-control select" id="languages[0]">
                        <option value="0" selected>Select language</option>
                        <% for (int i=0;i<languages.size(); i++)
                        {
                        %>
                        <option  value="<%=languages.get(i).getId()%>">
                            <%=languages.get(i).getName()%>
                        </option>
                        <%
                            }
                        %>
                    </select>
                    <select name="languageLevels[0]" class="form-control select" id="languageLevels[0]">
                        <option value="0" selected>Select level</option>
                        <% for (int i=0;i<languageLevels.size(); i++)
                        {
                        %>
                        <option value="<%=languageLevels.get(i).getId()%>">
                            <%=languageLevels.get(i).getName()%>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="form-group form-group-sm">
        <div class="row" style="margin-left:20%">
            <div class="col-xs-offset-2 col-xs-6">
                <button class="btn btn-success" value="Add one more language" onclick="addOneLanguage(); return false;"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add one more language</button>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        var i = 1;
        function addOneLanguage(){
            var html = document.getElementById("languageGroup").cloneNode(true);
            html.id = "languageGroup"+i;
            html.childNodes[1].childNodes[3].childNodes[1].id = "languages["+i+"]";
            html.childNodes[1].childNodes[3].childNodes[1].name = "languages["+i+"]";
            html.childNodes[1].childNodes[3].childNodes[3].id = "languageLevels["+i+"]";
            html.childNodes[1].childNodes[3].childNodes[3].name = "languageLevels["+i+"]";
            i++;
            $('#selects').append(html);
        }
    </script>

    <div class="form-group form-group-sm">
        <div class="row" style="margin-left:20%">
            <div class="col-xs-offset-2 col-xs-6">
                <input type="submit" class="btn btn-primary" value="Submit">
            </div>
        </div>
    </div>
</form>

<div style="display: none">
    <div id="languageGroup" class="form-group form-group-sm">
        <div class="row" style="margin-left:20%">
            <label class="control-label col-xs-2" for="languages">Language and level<sup>*</sup></label>
            <div class="col-xs-6" align="left">
                <select name="languages" class="form-control" id="languages">
                    <option value="0" selected>Select language</option>
                    <% for (Language language : languages) {
                    %>
                    <option value="<%=language.getId()%>">
                        <%=language.getName()%>
                    </option>
                    <%
                        }
                    %>
                </select>
                <select name="languageLevels" class="form-control" id="languageLevels">
                    <option value="0" selected>Select level</option>
                    <% for (LanguageLevel languageLevel : languageLevels) {
                    %>
                    <option value="<%=languageLevel.getId()%>">
                        <%=languageLevel.getName()%>
                    </option>
                    <%
                        }
                    %>
                </select>
            </div>
        </div>
    </div>
</div>
</body>
</html>
