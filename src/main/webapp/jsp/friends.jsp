<%@include file="templates/userForm.jsp"%>
<%
    Long idRequestUser = (Long)request.getAttribute("idRequestUser");
%>
<script type="text/javascript">
    var friendsStart = 0;
    var receivedRequestsStart = 0;
    var sentRequestsStart = 0;
    var idRequestUser = <%=idRequestUser%>;
    document.title = "Friends";

</script>
<div class="col-xs-8">
    <ul class="nav nav-tabs nav-justified" id="tabMenu">
        <li class="active"><a data-toggle="tab" href="#friendsTab">Friends</a></li>
        <%if(idUser.equals(idRequestUser)){%>
        <li><a data-toggle="tab" href="#friendsRequestsTab">Received requests</a></li>
        <li><a data-toggle="tab" href="#sentRequestsTab">Sent requests</a></li>
        <%}%>
    </ul>
    <div class="tab-content" style="border-bottom: 1px solid #ddd; border-left: 1px solid #ddd; border-right: 1px solid #ddd;">
        <div id="friendsTab" class="tab-pane fade in active" style="max-height: 75vh; overflow-y: auto">
            <div id="friends" style="margin: 1%">

            </div>
            <div align="center">
                <button id="loadMoreFriends" class="btn btn-info" style="display: none" onclick="loadMore(friendsStart,<%=idRequestUser%>,'friends')">Load more</button>
            </div>
        </div>
        <%if(idUser.equals(idRequestUser)){%>
        <div id="friendsRequestsTab" class="tab-pane fade" style="max-height: 75vh; overflow-y: auto">
            <div id="friendRequests" style="margin: 1%">

            </div>
            <div align="center">
                <button id="loadMoreReceived" class="btn btn-info" style="display: none" onclick="loadMore(receivedRequestsStart,<%=idRequestUser%>,'received')">Load more</button>
            </div>
        </div>
        <div id="sentRequestsTab" class="tab-pane fade" style="max-height: 75vh; overflow-y: auto">
            <div id="sentRequests" style="margin: 1%">

            </div>
            <div align="center">
                <button id="loadMoreSent" class="btn btn-info" style="display: none" onclick="loadMore(sentRequestsStart,<%=idRequestUser%>,'sent')">Load more</button>
            </div>
        </div>
        <%}%>
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
</div>
</div>
</body>
</html>
