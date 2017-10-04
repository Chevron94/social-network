<%--
  Created by IntelliJ IDEA.
  User: Роман
  Date: 09.03.2016
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<div class="modal fade" id="messageWindow" tabindex="-1" role="dialog" aria-labelledby="messageLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="messageLabel"><h2>New message</h2></label>
            </div>
            <div class="modal-body">
                <div class="form-group form-group-sm">
                    <div class="row">
                        <div class="col-xs-3" align="left">
                            <label for="receiverName" class="control-label">Receiver</label>
                        </div>
                        <div class="col-xs-9" align="right">
                            <input type="text" id="receiverName" class="form-control" readonly="readonly" value="">
                        </div>
                    </div>
                    <div class="row" style="margin-top: 1%">
                        <div class="col-xs-3" align="left">
                            <label for="messageText" class="control-label">Message</label>
                        </div>
                        <div class="col-xs-9" align="right">
                            <textarea id="messageText" style="resize: none" required class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="row" style="margin-top: 1%">
                        <div class="col-xs-offset-3 col-xs-9">
                            <input type="button" class="btn btn-primary" value="Send" onclick="send()">
                        </div>
                    </div>
                    <script type="text/javascript">
                        $('#messageText').keydown(function (e) {
                            if (e.ctrlKey && e.keyCode == 13) {
                                if($('#messageText').val().trim() != "")
                                    send();
                            }
                        });
                    </script>
                </div>
            </div>
            <div class="modal-footer">
            </div>
        </div>
    </div>
</div>
