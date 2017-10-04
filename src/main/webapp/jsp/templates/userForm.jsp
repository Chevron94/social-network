<%@ page import="java.util.List" %>
<%@ page import="socialnetwork.entities.Continent" %>
<%@ page import="socialnetwork.entities.Language" %>
<%--
  Created by IntelliJ IDEA.
  User: roman
  Date: 10/6/15
  Time: 9:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@include file="scripts.jsp" %>

    <%
        List<Continent> continents = (List<Continent>) request.getAttribute("continents");
        List<Language> languages = (List<Language>) request.getAttribute("languages");
    %>
</head>
<body onload="initPage()">
<br>
<%@include file="header.jsp" %>
<%@include file="message.jsp" %>
<%@include file="photoView.jsp"%>
<div class="col-xs-4">
    <div class="row">
        <div class="col-xs-1"></div>
        <div class="col-xs-9">
            <h4>Search</h4>
            <form method="post" id="search" class="form-horizontal" name="search">
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <label class="control-label col-xs-3" for="login">Login</label>
                            <div class="col-xs-9">
                                <input type="text" name="login" class="form-control" id="login"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <label class="control-label col-xs-3" for="continent-select">Continent</label>
                            <div class="col-xs-9">
                                <select name="continent-select" class="form-control" id="continent-select"
                                        onchange="updateContinentSelectOptions()">
                                    <option value="0" selected>Select continent</option>
                                    <%
                                        for (Continent continent : continents) {
                                    %>
                                    <option value="<%=continent.getId()%>"><%=continent.getName()%>
                                    </option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">

                            <label class="control-label col-xs-3" for="country-select">Country</label>

                            <div class="col-xs-9">
                                <select name="country-select" class="form-control" id="country-select"
                                        onchange="updateCountrySelectOptions()">
                                    <option value="0" selected>Select country</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <label class="control-label col-xs-3" for="city-select">City</label>
                            <div class="col-xs-9">
                                <select name="city-select" class="js-data-example-ajax form-control" id="city-select"
                                        onchange="updateValues('city-select','city')">
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <label class="control-label col-xs-3">Gender</label>
                            <div class="col-xs-9">
                                <label class="check-box checkbox-inline">
                                    <input type="checkbox" id="genderMale"  value="" onclick="checkMale(this)">Male
                                </label>
                                <label class="check-box checkbox-inline">
                                    <input type="checkbox" id="genderFemale" value="" onchange="checkFemale(this)">Female
                                </label>
                            </div>
                        </div>
                    </div>
                    <script type="text/javascript">
                        function checkMale(cb) {
                            if (cb.checked) {
                                $('#gender').val($('#gender').val() + 'm');
                            } else {
                                if ($('#gender').val() == 'fm' || $('#gender').val() == 'mf')
                                    $('#gender').val('f');
                                else $('#gender').val('');
                            }
                        }
                        function checkFemale(cb) {

                            if (cb.checked) {
                                $('#gender').val($('#gender').val() + 'f');
                            } else {
                                if ($('#gender').val() == 'fm' || $('#gender').val() == 'mf')
                                    $('#gender').val('m');
                                else $('#gender').val('');
                            }
                        }
                    </script>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">

                            <label class="control-label col-xs-3" for="range">Age</label>

                            <div class="col-xs-9">
                                <input type="text" name="range" id="range" readonly style="border:0; font-weight:bold;">
                                <div id="slider-range"></div>
                                <script>
                                    $(function () {
                                        $("#slider-range").slider({
                                            range: true,
                                            min: 1,
                                            max: 100,
                                           values: [1, 100],

                                            slide: function (event, ui) {
                                                $("#range").val(ui.values[0] + " - " + ui.values[1]);
                                                $("#ageFrom").val(ui.values[0]);
                                                $("#ageTo").val(ui.values[1]);
                                            }
                                        });
                                        $("#range").val($("#slider-range").slider("values", 0) +
                                                " - " + $("#slider-range").slider("values", 1));
                                    });
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <label class=" control-label col-xs-3 " for="languages-select">Languages</label>
                            <div class="col-xs-9">
                                <select name="languages-select" multiple="multiple" id="languages-select" class="form-control"
                                        onchange="updateValues('languages-select','language')">
                                    <%
                                        for(Language language: languages){
                                    %>
                                    <option  value="<%=language.getId()%>"><%=language.getName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                    </div>

                <script type="text/javascript">
                    $(document).ready(function() {
                        $('#languages-select').multiselect();
                    });
                </script>

                    <div class="form-group form-group-sm">
                        <div class="row" style="margin-bottom: 3%">
                            <div class="col-xs-offset-3 col-xs-9">
                                <input type="button" id="searchButton" class="btn btn-primary" value="Search" onclick="initPage()">
                            </div>
                        </div>
                    </div>
                <input type="hidden" name="continent" id="continent" value="0">
                <input type="hidden" name="country" id="country" value="0">
                <input type="hidden" name="city" id="city" value="0">
                <input type="hidden" name="gender" id="gender" value="">
                <input type="hidden" name="ageFrom" id="ageFrom" value="0">
                <input type="hidden" name="ageTo" id="ageTo" value="100">
                <input type="hidden" name="Language" id="language" value="0">


                <script>

                    function formatCity(data) {
                        return data.name;
                    }

                    function formatCitySelection(data) {
                        return data.name;
                    }

                    $('#city-select').select2({
                        placeholder: "Select a city",
                        allowClear: true,
                        ajax: {
                            url: window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/registration/citiesByCountry',
                            dataType: 'json',
                            type: "GET",
                            quietMillis: 250,
                            data: function (params) {
                                return {
                                    searchId: $('#country-select').select().val(),
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
                        minimumInputLength: 1,
                        templateResult: formatCity,
                        templateSelection: formatCitySelection,
                        escapeMarkup: function (m) {
                            return m;
                        }
                    });
                </script>
            </form>
        </div>
    </div>
</div>
