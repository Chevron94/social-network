<%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 17.04.2016
  Time: 18:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Password reset</title>
    <%@include file="templates/scripts.jsp"%>
</head>
<body>
<br>
    <%@include file="templates/header.jsp"%>
    <form method="post" class="form-horizontal" id="resetPassword" name="resetPassword">
        <div class="form-group form-group-sm">
            <div class="row" style="margin-left:20%">
                <label class="control-label col-xs-2" for="password">Password<sup style="color: red">*</sup></label>
                <div class="col-xs-6" align="left">
                    <input type="password" class="form-control" id="password" name="password" pattern="[A-Za-z0-9_-]{6,64}" required onchange="checkPasswords()">
                    <label class="control-label" for="password" style="font-size: smaller">Password must have length 6-64 and can contains A-Z,a-z,0-9,_,-</label>
                </div>
            </div>
        </div>

        <div class="form-group form-group-sm">
            <div class="row" style="margin-left:20%">
                <label class="control-label col-xs-2" for="retype">Retype password<sup style="color: red;">*</sup></label>
                <div class="col-xs-6" align="left">
                    <input type="password" class="form-control" id="retype" name="retype" pattern="[A-Za-z0-9_-]{6,64}" required onchange="checkPasswords()">
                </div>
            </div>
        </div>

        <div class="form-group form-group-sm">
            <div class="row" style="margin-left:20%">
                <div class="col-xs-offset-2 col-xs-6">
                    <input type="submit" class="btn btn-primary" value="Submit">
                </div>
            </div>
        </div>
        <script type="text/javascript">
            function checkPasswords()
            {
                var passl = document.getElementById("password");
                var pass2 = document.getElementById("retype");
                if(passl.value!==pass2.value)
                    passl.setCustomValidity("Passwords are not equals!");
                else
                    passl.setCustomValidity("");
            }
        </script>
    </form>
</body>
</html>
