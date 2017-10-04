<%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 24.04.2016
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%
    if(user.getId().equals(idUser)){

    List<Country> countryList = (List<Country>) request
            .getAttribute("countries");
    List<Gender> genders = (List<Gender>) request
            .getAttribute("genders");
    List<Language> languages = (List<Language>) request.getAttribute("languages");
    List<LanguageLevel> languageLevels = (List<LanguageLevel>) request.getAttribute("languageLevels");
%>
<div class="modal fade" id="editViewer" tabindex="-1" role="dialog" aria-labelledby="editLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <label id="editLabel">Edit personal information</label>
            </div>
            <div class="modal-body">
                <form action="/profile" class="form-horizontal" modelAttribute="editForm" name="editForm" method="post"
                      id="editForm" onsubmit="return validate();">
                    <ul class="nav nav-tabs nav-justified" id="tabMenu">
                        <li class="active"><a data-toggle="tab" href="#generalTab">General info</a></li>
                        <li><a data-toggle="tab" href="#languagesTab">Languages</a></li>
                    </ul>
                    <div class="tab-content"
                         style="border-bottom: 1px solid #ddd; border-left: 1px solid #ddd; border-right: 1px solid #ddd;">
                        <div id="generalTab" class="tab-pane fade in active">
                            <div id="general">
                                <br>
                                <!--Login-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="login">Login<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7">
                                            <input type="text" id="login" class="form-control" name="login" required
                                                   pattern="^\w+$"
                                                   value="<%=user.getLogin()%>">
                                            <span class="help-block" style="font-size: smaller">Can
                                                contains only A-Z,a-z,0-9,_</span>
                                        </div>
                                    </div>
                                </div>
                                <!--Old password-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="password">Old password</label>
                                        <div class="col-xs-7" align="left">
                                            <input type="password" class="form-control" id="oldPassword"
                                                   name="oldPassword" pattern="[A-Za-z0-9_-]{6,64}">
                                            <span class="help-block" style="font-size: smaller">Password
                                                must have length 6-64 and can contains A-Z,a-z,0-9,_,-</span>
                                        </div>
                                    </div>
                                </div>
                                <!--New password-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="password">New password</label>
                                        <div class="col-xs-7" align="left">
                                            <input type="password" class="form-control" id="password" name="password"
                                                   pattern="[A-Za-z0-9_-]{6,64}" onchange="checkPasswords()">
                                            <span class="help-block" style="font-size: smaller">Password
                                                must have length 6-64 and can contains A-Z,a-z,0-9,_,-</span>
                                        </div>
                                    </div>
                                </div>
                                <!--New password-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="retype">Retype new password</label>
                                        <div class="col-xs-7" align="left">
                                            <input type="password" class="form-control" id="retype" name="retype"
                                                   pattern="[A-Za-z0-9_-]{6,64}" onchange="checkPasswords()">
                                        </div>
                                    </div>
                                </div>
                                <!--Email-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="email">E-mail<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <input type="email" id="email" class="form-control" name="email" required
                                                   value="<%=user.getEmail()%>">
                                        </div>
                                    </div>
                                </div>
                                <!--Name-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="name">Name<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <input type="text" id="name" class="form-control" pattern="^\w+([\s-]\w+)*$"
                                                   name="name" required
                                                   value="<%=user.getName()%>">
                                            <span class="help-block" style="font-size: smaller">Can
                                                contains only A-Z,a-z,0-9,_,- and space</span>
                                        </div>
                                    </div>
                                </div>
                                <!--Gender-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3"
                                               for="gender_select_1">Gender<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <label class="radio-inline"><input type="radio" name="gender"
                                                                               id="gender_select_1"
                                                                               value="<%=genders.get(0).getId()%>" <%=user.getGender().getId().equals(genders.get(0).getId())?"checked":""%>><%=genders.get(0).getName()%>
                                            </label>
                                            <label class="radio-inline"><input type="radio" name="gender"
                                                                               id="gender_select_2"
                                                                               value="<%=genders.get(1).getId()%>" <%=user.getGender().getId().equals(genders.get(1).getId())?"checked":""%>><%=genders.get(1).getName()%>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <!--Birthday-->
                                <script language="JavaScript">
                                    $(function () {
                                        $("#birthday").datepicker({
                                            dateFormat: 'yy-mm-dd',
                                            changeMonth: true,
                                            changeYear: true,
                                            yearRange: '-99:-3'
                                        });
                                    });
                                </script>
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3"
                                               for="birthday">Birthday<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <input type="date" id="birthday"
                                                   pattern="(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)"
                                                   placeholder="YYYY-MM-DD" class="form-control" name="birthday"
                                                   required value="<%=user.getBirthday().toString().split(" ")[0]%>">
                                            <span class="help-block" style="font-size: smaller">Format:
                                                YYYY-MM-DD. Example: 1990-12-31</span>
                                        </div>
                                    </div>
                                </div>
                                <!--Country-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="country">Country<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <select name="country" class="icon-menu form-control" id="country"
                                                    onchange="updateSelectOptions()">
                                                <option value="0" selected>Select country</option>
                                                <% for (Country aCountryList : countryList) {
                                                %>
                                                <option style="background-image:url(<%=aCountryList.getFlagURL()%>); background-size: 18px 18px; background-position: left center;"
                                                        value="<%=aCountryList.getId()%>"
                                                        <%
                                                            if (user.getCountry().getId().equals(aCountryList.getId())) {
                                                        %>
                                                        selected
                                                        <%
                                                            }
                                                        %>
                                                >
                                                    <%=aCountryList.getName()%>
                                                </option>
                                                <%
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <!--City-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="city">City<sup style="color: red;">*</sup></label>
                                        <div class="col-xs-7" align="left">
                                            <select name="city" id="city" class="form-control">
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <!--About me-->
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <label class="control-label col-xs-3" for="description">About me</label>
                                        <div class="col-xs-7" align="left">
                                            <textarea class="form-control" id="description" name="description"
                                                      rows="3"><%=user.getDescription()%></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="languagesTab" class="tab-pane fade">
                            <div id="languagesInfo">
                                <br>
                                <div id="selects">
                                    <%
                                        for (int i = 0; i < user.getLanguageUsers().size(); i++) {
                                            LanguageUser languageUser = user.getLanguageUsers().get(i);
                                    %>
                                    <div id="languageGroup<%=i%>" class="form-group form-group-sm">
                                        <div class="row" style="margin-left:10%">
                                            <label class="control-label col-xs-3" for="languages[<%=i%>]">Language and
                                                level</label>
                                            <div class="col-xs-7" align="left">
                                                <select name="languages[<%=i%>]" class="form-control select"
                                                        id="languages[<%=i%>]">
                                                    <option value="0">Select language</option>
                                                    <% for (Language language : languages) {
                                                    %>
                                                    <option <%=language.getId().equals(languageUser.getLanguage().getId()) ? "selected" : ""%>
                                                            value="<%=language.getId()%>">
                                                        <%=language.getName()%>
                                                    </option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                                <select name="languageLevels[<%=i%>]" class="form-control select"
                                                        id="languageLevels[<%=i%>]">
                                                    <option value="0" selected>Select level</option>
                                                    <% for (LanguageLevel languageLevel : languageLevels) {
                                                    %>
                                                    <option <%=languageLevel.getId().equals(languageUser.getLanguageLevel().getId()) ? "selected" : ""%>
                                                            value="<%=languageLevel.getId()%>">
                                                        <%=languageLevel.getName()%>
                                                    </option>
                                                    <%
                                                        }
                                                    %>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <%
                                        }%>
                                </div>
                                <div class="form-group form-group-sm">
                                    <div class="row" style="margin-left:10%">
                                        <div class="col-xs-offset-3 col-xs-7">
                                            <button class="btn btn-success" value="Add one more language"
                                                    onclick="addOneLanguage(); return false;"><span
                                                    class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add one
                                                more language
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    var i = <%=user.getLanguageUsers().size()%>;
                                    function addOneLanguage() {
                                        var html = document.getElementById("languageGroup").cloneNode(true);
                                        html.id = "languageGroup" + i;
                                        html.childNodes[1].childNodes[3].childNodes[1].id = "languages[" + i + "]";
                                        html.childNodes[1].childNodes[3].childNodes[1].name = "languages[" + i + "]";
                                        html.childNodes[1].childNodes[3].childNodes[3].id = "languageLevels[" + i + "]";
                                        html.childNodes[1].childNodes[3].childNodes[3].name = "languageLevels[" + i + "]";
                                        i++;
                                        $('#selects').append(html);
                                    }
                                    function validate(){
                                        if(document.getElementById("error-lang")){
                                            $('#error-lang').remove();
                                        }
                                        var correct = false;
                                        for(var j = 0; j<i; j++){
                                            var lang = document.getElementById("languages["+j+"]");
                                            var langLvl = document.getElementById("languageLevels["+j+"]");
                                            if (langLvl.options[langLvl.selectedIndex].value>0 && lang.options[lang.selectedIndex].value>0){
                                                correct = true;
                                            }
                                        }
                                        if(!correct){
                                            var html = "<div id='error-lang' style='background-color: red'><h2>You don't have any correct pair language-language level</h2></div>";
                                            $('#languagesInfo').prepend(html);
                                        }
                                        return correct;
                                    }
                                </script>
                            </div>
                        </div>
                        <div class="form-group form-group-sm">
                            <div class="row" style="margin-left:10%">
                                <div class="col-xs-offset-3 col-xs-7">
                                    <input type="submit" class="btn btn-primary" value="Submit">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
    <div style="display: none">
        <div id="languageGroup" class="form-group form-group-sm">
            <div class="row" style="margin-left:10%">
                <label class="control-label col-xs-3" for="languages">Language and level</label>
                <div class="col-xs-7" align="left">
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
    <script type="text/javascript">
        $.fn.modal.Constructor.prototype.enforceFocus = $.noop;
        function updateSelectOptions() {
            $("#city").empty().append('<option value="0"></option>').val('0').trigger('change');
        }

        function formatCity(data) {
            return data.name;
        }

        function formatCitySelection(data) {
            return data.name;
        }
        $('#editViewer').on('shown.bs.modal', function () {
            $('#city').select2({
                ajax: {
                    url: window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/registration/citiesByCountry',
                    dataType: 'json',
                    type: "GET",
                    quietMillis: 250,
                    data: function (params) {
                        return {
                            searchId: $('#country').select().val(),
                            name: params.term
                        };
                    },
                    processResults: function (data) {
                        return {
                            results: $.map(data, function (item) {
                                return {
                                    name: item.name,
                                    id: item.id
                                }
                            })
                        };
                    }
                },
                placeholder: 'Select a city',
                minimumInputLength: 1,
                allowClear: true,
                templateResult: formatCity,
                templateSelection: formatCitySelection,
                escapeMarkup: function (m) {
                    return m;
                }
            });
            $("#city").empty();
            $("#city").append('<option value="<%=user.getCity().getId()%>" selected="selected"><%=user.getCity().getName()%></option>');
            $("#city").val("<%=user.getCity().getId()%>").trigger("change");
            setTimeout(function () {
                $('#select2-city-container').append('<%=user.getCity().getName()%>');
            }, 100);

        });
    </script>
</div>
<%}%>

