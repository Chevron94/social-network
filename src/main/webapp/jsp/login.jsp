<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login page</title>
    <%@include file="templates/scripts.jsp"%>
</head>
<body>
<link rel="stylesheet" href="/resources/css/menu.css">
<br>
<%@include file="templates/header.jsp"%>
<br>
<form class="form-horizontal login-box" action="/login" method='POST'>
    <c:if test="${not empty error}">
        <div id="error" class="error">${error}</div>
    </c:if>
    <c:if test="${not empty msg}">
        <div class="msg" id="message">${msg}</div>
    </c:if>
    <div class="form-group form-group-sm">
        <label for="login" class="col-xs-2 control-label">Login</label>
        <div class="col-xs-10">
            <input type="text" class="form-control" name='login' id="login" placeholder="Login">
        </div>
    </div>
    <div class="form-group form-group-sm">
        <label for="password" class="col-xs-2 control-label">Password</label>
        <div class="col-xs-10">
            <input type="password" class="form-control" name='password' id="password" placeholder="Password">
        </div>
    </div>
    <div class="form-group form-group-sm">
        <div class="col-xs-offset-2 col-xs-10">
            <button type="submit" class="btn btn-primary">Sign in</button>
            <input type="button" class="btn btn-link" data-toggle="modal" data-target="#resetWindow" value="Reset password"/>
        </div>
    </div>
</form>

<div class="modal fade" id="resetWindow" tabindex="-1" role="dialog" aria-labelledby="resetLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="resetLabel"><h2>Reset password</h2></label>
            </div>
            <div class="modal-body">
                <form id="resetForm" action="/resetRequest" method="post">
                    <div class="form-group form-group-sm">
                        <div class="row">
                            <div class="col-xs-3">
                                <label class="control-label" for="emailInput">Your email<sup style="color: red;">*</sup></label>
                            </div>
                            <div class="col-xs-9">
                                <input type="email" id="emailInput" class="form-control" name="emailInput" required>
                            </div>
                        </div>

                        <div class="row" style="margin-top: 1%">
                            <div class="col-xs-offset-3 col-xs-9">
                                <input type="submit" class="btn btn-primary btn-sm" value="Reset">
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
</body>
</html>