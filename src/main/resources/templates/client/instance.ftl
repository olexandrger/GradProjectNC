<!DOCTYPE html>
<html lang="en">
<head>
    <title>Instance</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/instance.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="col-sm-12" id="alerts-bar"></div>
    <div id="complaint-modal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Create complaint:</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="new-complaint-reason">Compleint reason:</label>
                        <select class="form-control" name="new-complaint-reason" id="new-complaint-reason">
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="new-complaint-subject">Subject:</label>
                        <input type="text" class="form-control" name="new-complaint-subject" placeholder="Subject"
                               id="new-complaint-subject" onkeyup="unlockCreateButton()">
                    </div>
                    <div class="form-group">
                        <label for="new-complaint-content">Content:</label>
                        <textarea class="form-control " name="new-complaint-content" rows="5"
                                  placeholder="Content"
                                  maxlength="240"
                                  id="new-complaint-content" resize="none"></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="checkbox-inline">
                        <label><input type="checkbox" id="instanse-depend-check">It refers to the instance</label>
                    </div>
                    <button type="button" class="btn btn-success" data-dismiss="modal" onclick="createComplaint()"
                            id ="create-new-complaint-from-modal-btn" disabled>Complain
                    </button>
                </div>
            </div>

        </div>
    </div>
    <div class="row">
        <div class="col-sm-10 col-sm-offset-1">
            <div class="row">
                <div class="col-sm-9">
                    <h3 id="instance-product-name"></h3>
                    <div class="tab">
                        <p>Status: <span id="instance-status"></span></p>
                        <p>Product type: <span id="instance-product-type"></span></p>
                        <address>Address: <p class="tab" id="instance-address"></p></address>
                        <p>Price: <span id="instance-product-price"></span></p>
                    </div>
                </div>
                <div class="col-sm-3 pull-down" style="margin-bottom: 5px">
                    <button type="button" class="btn btn-default btn-block hidden" id="instance-complain-button"
                            data-toggle="modal" data-target="#complaint-modal" onclick="loadNewComplaintModal()">
                        Create complain
                    </button>
                    <button class="btn btn-default btn-block hidden" id="instance-suspend-button"
                            onclick="suspendInstance()">Suspend
                    </button>
                    <button class="btn btn-default btn-block hidden" id="instance-continue-button"
                            onclick="continueInstance()">Resume
                    </button>
                    <button class="btn btn-default btn-block hidden" id="instance-deactivate-button"
                            onclick="deactivateInstance()">Deactivate
                    </button>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <ul class="nav nav-tabs">
                        <li class="active" id="orders-tab"><a href="#" onclick="selectOrdersTab()">Orders:</a></li>
                        <li id="conplaints-tab"><a href="#" onclick="selectComplaintsTab()">Complaints:</a></li>
                    </ul>
                </div>
                <div class="col-sm-12 hidden" id="instance-complaints">
                    <table class="table table-striped" id="instance-complaints-table">
                        <tr>
                            <th>Reason</th>
                            <th>Status</th>
                            <th>Open date</th>
                            <th>Close date</th>
                        </tr>
                    </table>
                    <div class="row">
                        <button type="button" class="btn" id="complaint-btn-prev" onclick="getPrevComplaintPage()"> &larr;
                            Prev.
                        </button>
                        <button type="button" class="btn" id="complaint-btn-next" onclick="getNextComplaintPage()"> Next
                            &rarr;
                        </button>
                    </div>
                </div>
                <div class="col-sm-12" id="instanse-orders">
                <#--<h3 class="text-center">Orders</h3>-->
                    <table class="table table-striped" id="instance-orders-table">
                        <tr>
                            <th>Action</th>
                            <th>Status</th>
                            <th>Open date</th>
                            <th>Close date</th>
                            <th></th>
                        </tr>
                    <#--<tr><td>Dosos</td><td>Closed</td><td>1.1.1111</td><td>2.1.1111</td><td>-->
                    <#--<button class="btn pull-right">Cancel</button></td></tr>-->
                    <#--<tr><td>Create</td><td>Closed</td><td>1.1.1111</td><td>2.1.1111</td><td></td></tr>-->
                    </table>
                    <button id="more-orders" class="btn btn-default center-block hidden" onclick="loadOrders()">More</button>

                </div>
            </div>
        </div>
    </div>
</div>
</body>
