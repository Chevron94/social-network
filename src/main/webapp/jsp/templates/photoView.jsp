<!--div id="photoViewer" class="modal">
    <script type="text/javascript">
        var current = 0;
        var max = 1000;
    </script>
    <div class="modal-content">
        <div class="modal-header">
            <span id="photoViewerClose" class="close">x</span>
            <label id="photoNum"></label>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-xs-12 table-bordered" align="center">
                    <img id="photo" src="" class="img-responsive">
                </div>
            </div>
            <div class="row" style="margin: 1%">
                <div class="col-xs-2" align="left">
                    <button class="btn btn-primary" id="previous" value="Previous" onclick="getPrevious();">
                        <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> <b>Previous</b>
                    </button>
                </div>
                <div class="col-xs-8">
                    <div class="row">
                        <div class="col-xs-6">
                            <label>Added: </label> <label id="added" class="control-label"></label>
                        </div>
                        <div class="col-xs-6" align="right">
                            <button id="setAsMainPhoto" class="btn btn-success" style="display: none"
                                    value="Set as profile photo">Set as profile photo
                            </button>
                            <button id="deletePhoto" class="btn btn-danger" style="display: none" value="Delete">Delete
                                photo
                            </button>
                        </div>
                    </div>
                </div>
                <div class="col-xs-2" align="right">
                    <button class="btn btn-primary" id="next" value="Next" onclick="getNext()">
                        <b>Next</b> <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div-->
<div class="modal fade" id="photoViewer" tabindex="-1" role="dialog" aria-labelledby="photoNum">
    <script type="text/javascript">
        var current = 0;
        var max = 1000;
    </script>
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="photoNum"></label>
            </div>
            <div class="modal-body">
                <div class="row table-bordered" align="center" >
                    <input type="image" id="photo" src="" style="height: 500px" class="img-responsive" onclick="getNext()">
                </div>
                <div class="row" style="margin: 1%">
                    <div class="col-xs-2" align="left">
                        <button class="btn btn-primary" id="previous" value="Previous" onclick="getPrevious();">
                            <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> <b>Previous</b>
                        </button>
                    </div>
                    <div class="col-xs-8">
                        <div class="row">
                            <div class="col-xs-4">
                                <b>Added:</b> <label id="added"></label>
                            </div>
                            <div class="col-xs-8" align="right">
                                <button id="setAsMainPhoto" class="btn btn-success" style="display: none"
                                        value="Set as profile photo">Set as profile photo
                                </button>
                                <a id="deletePhoto" class="btn btn-danger" style="display: none" data-toggle="modal" href="#deletePhotoWindow">Delete
                                    photo
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-2" align="right">
                        <button class="btn btn-primary" id="next" value="Next" onclick="getNext()">
                            <b>Next</b> <span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="deletePhotoWindow" tabindex="-1" role="dialog" aria-labelledby="deletePhotoLabel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="deletePhotoLabel"><h2>Delete photo</h2></label>
            </div>
            <div class="modal-body">
                <p>Do you want to delete this photo?</p>
                <p><button class="btn btn-danger" data-dismiss="modal" id="deletePhotoButton" value="Delete">Delete</button> </p>
            </div>
            <div class="modal-footer">

            </div>
        </div>
    </div>
</div>
<!-- Пoдлoжкa -->