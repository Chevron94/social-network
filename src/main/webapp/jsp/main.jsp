<%@ page import="java.util.List" %>
<%@ page import="socialnetwork.entities.Country" %><%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 24.03.2016
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello From</title>
    <%@include file="templates/scripts.jsp"%>
    <% List<Country> countries = (List<Country>)request.getAttribute("countries"); %>
</head>
<body>
    <br>
        <%@include file="templates/header.jsp"%>
    <div id="content" style="margin: 1%;background-image: url('/resources/images/system/background.png'); background-size: contain; background-repeat: no-repeat; background-position: center; word-wrap: break-word;
    over-flow:break-word; min-height: 525px">
        <h3 align="center">Welcome to site &ldquo;Hello From&rdquo;!</h3>
            <h3> Here you can:
                <li>find friends around the world;</li>
                <li>learn new languages;</li>
                <li>have very interesting conversations with users from your, or another countries!</li>
            </h3>
        <br>
        <h3>Just now you can meet users from &hellip;</h3>
        <%for(int i = 0; 6*i<countries.size(); i++){
        %>
        <div class="row">
            <%
            for(int j = 0; j<6 && 6*i+j<countries.size(); j++){
            %>
            <div class="col-xs-2">
                <img src="<%=countries.get(6*i+j).getFlagURL()%>"> <b><%=countries.get(6*i+j).getName()%></b>
            </div>
            <%
            }
            %>
        </div>
        <%
        }%>
        <h3>It's time to start making new friends! <a href="/registration">Join now!</a> Already with us? <a href="/login">Login!</a> </h3>
    </div>
</body>
</html>
