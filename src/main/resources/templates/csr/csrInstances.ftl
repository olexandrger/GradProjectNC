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
    <script src="/js/csrInstances.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="row">
    <div class="col-sm-12" id="alerts-bar"></div>
    </div>


    <div class=" col-sm-3 col-sm-offset-1">

        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label for="order-search-aim123">Status</label>
                    <select class="form-control" name="order-search-aim123" id="instance-status123" onchange="loadAllInstances()">
                        <option value="ALL" selected>All</option>
                        <option value="9">Created</option>
                        <option value="10">Activated</option>
                        <option value="11">Suspended</option>
                        <option value="12">Deactivated</option>
                    </select>
                </div>
            </div>


        </div>

        <div class="list-group" id="instances-list"></div>
        <ul class="pager">
            <li class="previous hidden" id="instances-page-previous"><a href="#" onclick="previousInstancesPage()">Previous</a></li>
            <li class="next hidden" id="instances-page-next"><a href="#" onclick="nextInstancesPage()">Next</a></li>
        </ul>

    </div>

    <div class="col-sm-7 hidden " id="instanceInfo">
    <div id="complaint-modal" class="modal fade" role="dialog">

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
                        <p>Discount price: <span id="instance-product-discount-price"></span></p>
                    </div>
                </div>
                <div class="col-sm-3 pull-down" style="margin-bottom: 5px">
                    
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
                    <button id="more-orders" class="btn btn-default center-block hidden" onclick="loadMoreOrders()">More</button>

                </div>
            </div>
        </div>
    </div>



    </div>






</div>
</body>