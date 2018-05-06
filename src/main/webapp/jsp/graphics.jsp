<%@ page import="socialnetwork.entities.Country" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <%@include file="templates/scripts.jsp" %>
    <script src="/resources/js/highcharts/highcharts.js"></script>
    <script src="/resources/js/highcharts/highcharts-3d.js"></script>
</head>
<body>
<%@include file="templates/header.jsp" %>
<%
    List<Country> countryList = (List<Country>) request
            .getAttribute("countries");
%>
<div class="col-xs-12">
    <ul class="nav nav-tabs nav-justified" id="tabMenu">
        <li class="active"><a data-toggle="tab" href="#personalTab">Personal</a></li>
        <li><a data-toggle="tab" href="#globalTab">Global</a></li>
    </ul>
    <div class="tab-content" style="border-bottom: 1px solid #ddd; border-left: 1px solid #ddd; border-right: 1px solid #ddd; max-height: 75vh; overflow-y: scroll; overflow-x: hidden; margin-inside: 1%">
        <div id="personalTab" class="tab-pane fade in active">
            <div class="row">
                <div class="col-xs-12">
                    <div id="personalTotalMessagesByCountryLine" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div id="personalTotalSentMessagesByCountryPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div id="personalTotalReceivedMessagesByCountryPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="personalTotalMessagesByPersonLine" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div id="personalTotalSentMessagesBPersonPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div id="personalTotalReceivedMessagesByPersonPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-offset-3 col-xs-6">
                    <div id="friendsByCountries" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="friendsByLanguages" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
        </div>
        <div id="globalTab" class="tab-pane fade">
            <div class="row">
                <div class="col-xs-12">
                    <div id="totalMessagesByCountryLine" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div id="totalSentMessagesByCountryPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
                <div class="col-xs-6">
                    <div id="totalReceivedMessagesByCountryPie" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="usersByCountries" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="usersByLanguages" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-3"><p align="right"><label>Select country:</label></p></div>
                <div class="col-xs-6">
                    <select name="country" class="icon-menu form-control" id="country" onchange="redrawCountryGraphics()">
                        <option value="0" selected>Select country</option>
                        <% for (Country aCountryList : countryList) {
                        %>
                        <option style="background-image:url(<%=aCountryList.getFlagURL()%>); background-size: 18px 18px; background-position: left center;"
                                value="<%=aCountryList.getId()%>">
                                <%=aCountryList.getName()%>
                        </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="usersFromSpecificCountryByLanguages" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div id="friendsForSpecificCountry" style="margin: 1%; height: 400px">
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        $('#tabMenu a').click(function(e) {
            e.preventDefault();
            $(this).tab('show');
        });

        // store the currently selected tab in the hash value
        $("ul.nav-tabs > li > a").on("shown.bs.tab", function(e) {
            var id = $(e.target).attr("href").substr(1);
            window.location.hash = id;
        });

        // on load of the page: switch to the currently selected tab
        var hash = window.location.hash;
        $('#tabMenu a[href="' + hash + '"]').tab('show');
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalMessagesByCountryLine', {
            title: {
                text: 'Your messages by countries'
            },
            credits: {
                enabled: false
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Messages'
                }
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                area: {
                    fillColor: {
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 5
                    },
                    lineWidth: 2,
                    states: {
                        hover: {
                            lineWidth: 2
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'Finland (sent)',
                data: [
                    [1167609600000, 150],
                    [1167696000000, 550],
                    [1167782400000, 985],
                    [1167868800000, 1420],
                    [1167955200000, 2188],
                    [1168214400000, 2501],
                    [1168300800000, 3007]
                ]
            },
                {
                    type: 'area',
                    name: 'Poland (sent)',
                    data: [
                        [1167609600000, 150],
                        [1167696000000, 1000],
                        [1167782400000, 1285],
                        [1167868800000, 1705],
                        [1167955200000, 1893],
                        [1168214400000, 2394],
                        [1168300800000, 2701]
                    ]
                },
                {
                    type: 'area',
                    name: 'Germany (sent)',
                    data: [
                        [1167609600000, 450],
                        [1167696000000, 1116],
                        [1167782400000, 1245],
                        [1167868800000, 1557],
                        [1167955200000, 1720],
                        [1168214400000, 2400],
                        [1168300800000, 2987]
                    ]
                },
                {
                    type: 'area',
                    name: 'Finland (received)',
                    data: [
                        [1167609600000, 10],
                        [1167696000000, 280],
                        [1167782400000, 452],
                        [1167868800000, 1000],
                        [1167955200000, 1520],
                        [1168214400000, 2000],
                        [1168300800000, 3005]
                    ]
                },
                {
                    type: 'area',
                    name: 'Poland (received)',
                    data: [
                        [1167609600000, 180],
                        [1167696000000, 1120],
                        [1167782400000, 1400],
                        [1167868800000, 1685],
                        [1167955200000, 1901],
                        [1168214400000, 2355],
                        [1168300800000, 2684]
                    ]
                },
                {
                    type: 'area',
                    name: 'Germany (received)',
                    data: [
                        [1167609600000, 800],
                        [1167696000000, 2000],
                        [1167782400000, 2100],
                        [1167868800000, 2406],
                        [1167955200000, 2589],
                        [1168214400000, 2601],
                        [1168300800000, 2900]
                    ]
                }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalSentMessagesByCountryPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            title: {
                text: 'You sent messages by country'
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8695<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                        name:'Poland',
                        y:2701
                    },
                    {
                        name: 'Germany',
                        y: 2987
                    },
                    {
                        name: 'Finland',
                        y: 3007
                    }]
            }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalReceivedMessagesByCountryPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            title: {
                text: 'You received messages by country'
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8589<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                        name:'Poland',
                        y:2684
                    },
                    {
                        name: 'Germany',
                        y: 2900
                    },
                    {
                        name: 'Finland',
                        y: 3005
                    }]
            }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalMessagesByPersonLine', {
            title: {
                text: 'Your messages by countries'
            },
            credits: {
                enabled: false
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Messages'
                }
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                area: {
                    fillColor: {
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 5
                    },
                    lineWidth: 2,
                    states: {
                        hover: {
                            lineWidth: 2
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'Kann (sent)',
                data: [
                    [1167609600000, 150],
                    [1167696000000, 550],
                    [1167782400000, 985],
                    [1167868800000, 1420],
                    [1167955200000, 2188],
                    [1168214400000, 2501],
                    [1168300800000, 3007]
                ]
            },
                {
                    type: 'area',
                    name: 'Marek (sent)',
                    data: [
                        [1167609600000, 150],
                        [1167696000000, 1000],
                        [1167782400000, 1285],
                        [1167868800000, 1705],
                        [1167955200000, 1893],
                        [1168214400000, 2394],
                        [1168300800000, 2701]
                    ]
                },
                {
                    type: 'area',
                    name: 'Frank (sent)',
                    data: [
                        [1167609600000, 450],
                        [1167696000000, 1116],
                        [1167782400000, 1245],
                        [1167868800000, 1557],
                        [1167955200000, 1720],
                        [1168214400000, 2400],
                        [1168300800000, 2987]
                    ]
                },
                {
                    type: 'area',
                    name: 'Kann (received)',
                    data: [
                        [1167609600000, 10],
                        [1167696000000, 280],
                        [1167782400000, 452],
                        [1167868800000, 1000],
                        [1167955200000, 1520],
                        [1168214400000, 2000],
                        [1168300800000, 3005]
                    ]
                },
                {
                    type: 'area',
                    name: 'Marek (received)',
                    data: [
                        [1167609600000, 180],
                        [1167696000000, 1120],
                        [1167782400000, 1400],
                        [1167868800000, 1685],
                        [1167955200000, 1901],
                        [1168214400000, 2355],
                        [1168300800000, 2684]
                    ]
                },
                {
                    type: 'area',
                    name: 'Frank (received)',
                    data: [
                        [1167609600000, 800],
                        [1167696000000, 2000],
                        [1167782400000, 2100],
                        [1167868800000, 2406],
                        [1167955200000, 2589],
                        [1168214400000, 2601],
                        [1168300800000, 2900]
                    ]
                }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalSentMessagesBPersonPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0,
                }
            },
            credits: {
                enabled: false
            },
            title: {
                text: 'You sent messages by friends'
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    },
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8695<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                    name:'Marek',
                    y:2701
                },
                    {
                        name: 'Frank',
                        y: 2987
                    },
                    {
                        name: 'Kann',
                        y: 3007
                    }]
            }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('personalTotalReceivedMessagesByPersonPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0,
                }
            },
            credits: {
                enabled: false
            },
            title: {
                text: 'You received messages by friends'
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    },
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8589<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                    name:'Marek',
                    y:2684
                },
                    {
                        name: 'Frank',
                        y: 2900
                    },
                    {
                        name: 'Kann',
                        y: 3005
                    }]
            }]
        });
    </script>

    <script type="application/javascript">

        Highcharts.chart('friendsByLanguages', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Friends by Languages'
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: [
                    'Native',
                    'Learning'
                ],
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Friends'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: <b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                name: 'Polish',
                data: [5, 20]

            }, {
                name: 'Russian',
                data: [3, 16]

            }, {
                name: 'English',
                data: [1, 23]

            }, {
                name: 'German',
                data: [10, 3]

            }, {
                name: 'French',
                data: [3, 3]

            }, {
                name: 'Japanese',
                data: [0, 1]

            }, {
                name: 'Finnish',
                data: [10, 0]

            }]
        });
    </script>

    <script type="application/javascript">
        Highcharts.chart('totalMessagesByCountryLine', {
            title: {
                text: 'Total messages by countries'
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Messages'
                }
            },
            credits: {
                enabled: false
            },
            legend: {
                enabled: true
            },
            plotOptions: {
                area: {
                    fillColor: {
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 5
                    },
                    lineWidth: 2,
                    states: {
                        hover: {
                            lineWidth: 2
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'Finland (sent)',
                data: [
                    [1167609600000, 150],
                    [1167696000000, 550],
                    [1167782400000, 985],
                    [1167868800000, 1420],
                    [1167955200000, 2188],
                    [1168214400000, 2501],
                    [1168300800000, 3007]
                ]
            },
                {
                    type: 'area',
                    name: 'Poland (sent)',
                    data: [
                        [1167609600000, 150],
                        [1167696000000, 1000],
                        [1167782400000, 1285],
                        [1167868800000, 1705],
                        [1167955200000, 1893],
                        [1168214400000, 2394],
                        [1168300800000, 2701]
                    ]
                },
                {
                    type: 'area',
                    name: 'Germany (sent)',
                    data: [
                        [1167609600000, 450],
                        [1167696000000, 1116],
                        [1167782400000, 1245],
                        [1167868800000, 1557],
                        [1167955200000, 1720],
                        [1168214400000, 2400],
                        [1168300800000, 2987]
                    ]
                },
                {
                    type: 'area',
                    name: 'Finland (received)',
                    data: [
                        [1167609600000, 10],
                        [1167696000000, 280],
                        [1167782400000, 452],
                        [1167868800000, 1000],
                        [1167955200000, 1520],
                        [1168214400000, 2000],
                        [1168300800000, 3005]
                    ]
                },
                {
                    type: 'area',
                    name: 'Poland (received)',
                    data: [
                        [1167609600000, 180],
                        [1167696000000, 1120],
                        [1167782400000, 1400],
                        [1167868800000, 1685],
                        [1167955200000, 1901],
                        [1168214400000, 2355],
                        [1168300800000, 2684]
                    ]
                },
                {
                    type: 'area',
                    name: 'Germany (received)',
                    data: [
                        [1167609600000, 800],
                        [1167696000000, 2000],
                        [1167782400000, 2100],
                        [1167868800000, 2406],
                        [1167955200000, 2589],
                        [1168214400000, 2601],
                        [1168300800000, 2900]
                    ]
                }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('totalSentMessagesByCountryPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0,
                }
            },
            title: {
                text: 'Total sent messages by country'
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    },
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8695<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                    name:'Poland',
                    y:2701
                },
                    {
                        name: 'Germany',
                        y: 2987
                    },
                    {
                        name: 'Finland',
                        y: 3007
                    }]
            }]
        });
    </script>
    <script type="application/javascript">
        Highcharts.chart('totalReceivedMessagesByCountryPie',{
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0,
                }
            },
            title: {
                text: 'Total received messages by country'
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                pie: {
                    depth: 25,
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y} messages'
                    },
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of 8589<br/>'
            },
            series: [{
                name: 'Messages',
                data: [{
                    name:'Poland',
                    y:2684
                },
                    {
                        name: 'Germany',
                        y: 2900
                    },
                    {
                        name: 'Finland',
                        y: 3005
                    }]
            }]
        });
    </script>
    <script type="application/javascript">

        Highcharts.chart('usersByLanguages', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Users by Languages'
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: [
                    'Native',
                    'Learning'
                ],
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Users'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: <b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                name: 'Polish',
                data: [5, 20]

            }, {
                name: 'Russian',
                data: [3, 16]

            }, {
                name: 'English',
                data: [1, 23]

            }, {
                name: 'German',
                data: [10, 3]

            }, {
                name: 'French',
                data: [3, 3]

            }, {
                name: 'Japanese',
                data: [0, 1]

            }, {
                name: 'Finnish',
                data: [10, 0]

            }]
        });
    </script>
    <script type="application/javascript">

        function drawPieDiagram(urlPath, htmlElement, title, unit) {
            var requestUrl = window.location.protocol+'//'+window.location.hostname+':'+window.location.port+'/graphics' + urlPath;
            $.getJSON(requestUrl, { ajax : 'true'} ,function(data){
                Highcharts.chart(htmlElement,{
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: title
                    },
                    credits: {
                        enabled: false
                    },
                    plotOptions: {
                        pie: {
                            depth: 25,
                            dataLabels: {
                                enabled: true,
                                format: '{point.name}: {point.y} '+unit
                            }
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
                    },
                    series: data
                });
            });
        }
        drawPieDiagram('/user-friends-by-country', 'friendsByCountries', 'Your friends by countries', 'friends');
        drawPieDiagram('/users-by-country', 'usersByCountries', 'Users by countries', 'users');
        function redrawCountryGraphics(){
            var value = $('#country').val();
            drawPieDiagram('/friends-by-specific-country/' + value, 'friendsForSpecificCountry', 'Country\'s friends', 'friends')
        }
    </script>

    <script type="application/javascript">

        Highcharts.chart('usersFromSpecificCountryByLanguages', {
            chart: {
                type: 'column'
            },
            credits: {
                enabled: false
            },
            title: {
                text: 'Users from specific country by Languages'
            },
            xAxis: {
                categories: [
                    'Native',
                    'Learning'
                ],
                crosshair: true
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Users'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: <b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
            series: [{
                name: 'Polish',
                data: [5, 20]

            }, {
                name: 'Russian',
                data: [3, 16]

            }, {
                name: 'English',
                data: [1, 23]

            }, {
                name: 'German',
                data: [10, 3]

            }, {
                name: 'French',
                data: [3, 3]

            }, {
                name: 'Japanese',
                data: [0, 1]

            }, {
                name: 'Finnish',
                data: [10, 0]

            }]
        });
    </script>

</div>
</body>
</html>