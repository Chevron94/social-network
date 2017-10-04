<%@include file="templates/userForm.jsp"%>
<script type="text/javascript">
    var otherStart = 0;
    document.title = "Other users";
</script>
<div class="col-xs-8">
    <div class="tab-content table-bordered">
        <div id="otherUsersTab" style="max-height: 80vh; overflow-y: auto">
            <div id="otherUsers" style="margin: 1%">

            </div>
            <div align="center">
                <button id="loadMoreOther" class="btn btn-info" style="display: none" onclick="loadMore(otherStart,idUser,'other')">Load more</button>
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
