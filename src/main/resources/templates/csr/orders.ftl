<!DOCTYPE html>
<html lang="en">
<head>
    <title>Orders</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/orders.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-12">
            <div id="csr-order-alert-place"
            ">
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-5">

        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="order-search-aim">Aim</label>
                    <select class="form-control" name="order-search-aim" id="order-search-aim" onchange="searchOrders()">
                        <option value="ALL" selected>All</option>
                        <option value="CREATE">Create</option>
                        <option value="SUSPEND">Suspend</option>
                        <option value="RESUME">Resume</option>
                        <option value="DEACTIVATE">Deactivate</option>
                    </select>
                </div>
            </div>

            <div class="col-sm-6">
                <div class="form-group">
                    <label for="order-search-status">Status</label>
                    <select class="form-control" name="order-search-status" id="order-search-status" onchange="searchOrders()">
                        <option value="ALL" selected>All</option>
                        <option value="CREATED">Created</option>
                        <option value="IN_PROGRESS">In progress</option>
                        <option value="CANCELLED">Cancelled</option>
                        <option value="COMPLETED">Completed</option>
                    </select>
                </div>
            </div>
        </div>
        <#---->
        <#--<div class="form-group">-->
            <#--<button class="btn btn-default btn-block" onclick="searchOrders()">Search</button>-->
        <#--</div>-->

        <div class="list-group" id="csr-orders-list">
        <#--<a href="#" class="list-group-item">First order</a>-->
                <#--<a href="#" class="list-group-item">Second order</a>-->
                <#--<a href="#" class="list-group-item">Third order</a>-->
        </div>

        <ul class="pager">
            <li class="previous hidden" id="orders-page-previous"><a href="#" onclick="previousPage()">Previous</a></li>
            <li class="next hidden" id="orders-page-next"><a href="#" onclick="nextPage()">Next</a></li>
        </ul>

        <!-- Trigger the modal with a button -->
        <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#new-order-modal"
                onclick="loadNewOrderModal()">New Order
        </button>

        <!-- Modal -->
        <div id="new-order-modal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">New Order:</h4>
                    </div>
                    <div class="modal-body">

                        <div class="alert alert-danger" id="new-order-modal-error-msg">
                            <strong>Danger!</strong> Indicates a dangerous or potentially negative action.
                        </div>

                        <div class="form-group">
                            <label for="new-order-user-email">Email</label>
                            <input type="email" class="form-control" name="new-order-email" placeholder="Email"
                                   id="new-order-user-email" onblur="loadDomainsInModal(13)" ,
                                   onkeypress="loadDomainsInModal(event.keyCode)">
                        </div>
                        <div class="form-group">
                            <label for="new-order-domain">Domain</label>
                            <select class="form-control" name="new-order-domain" id="new-order-domain"
                                    onchange="loadProductInstancesInModal()">
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="new-order-instanse">Product instance</label>
                            <select class="form-control" name="new-order-instanse" id="new-order-instanse"
                                    onchange="loadOrderAaimsInModal()">
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="new-order-aim">Order aim</label>
                            <select class="form-control" name="new-order-aim" id="new-order-aim" disabled>
                            </select>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" data-dismiss="modal"
                                onclick="createNewOrderFromModal()" id="create-new-order-from-modal-button">Create
                        </button>
                    </div>
                </div>

            </div>
        </div>

    </div>
    <div class="col-sm-7 hidden" id="order-main">

        <div class="form-group">
            <label for="order-user-name">User</label>
            <input type="text" class="form-control" name="order-user-name" id="order-user-name" readonly>
        </div>

        <div class="form-group">
            <label for="order-domain">Domain</label>
            <select class="form-control" name="order-domain" id="order-domain">
            <#--<option>Domain 1</option>-->
                    <#--<option selected>Domain 2</option>-->
                    <#--<option>Domain 3</option>-->
            </select>
        </div>

        <div class="form-group">
            <label for="order-product">Product</label>
            <select class="form-control" name="order-product" id="order-product">
            <#--<option>Product 1</option>-->
                    <#--<option selected>Product 2</option>-->
                    <#--<option>Product 3</option>-->
            </select>
        </div>

        <div class="form-group">
            <label for="order-aim">Aim</label>
            <input type="text" class="form-control" name="order-aim" id="order-aim" readonly>
        </div>

        <div class="form-group">
            <label for="order-status">Status</label>
            <input type="text" class="form-control" name="order-status" id="order-status" readonly>
        </div>

        <div class="form-group">
            <label for="order-start-date">Start date</label>
            <input type="text" class="form-control" name="order-start-date" id="order-start-date" readonly>
        </div>

        <div class="form-group">
            <label for="order-end-date">End date</label>
            <input type="text" class="form-control" name="order-end-date" id="order-end-date" readonly>
        </div>

        <div class="form-group text-center">
            <button class="btn btn-default order-button hidden" id="order-button-update"
                    onclick="updateSelectedOrder()">Update
            </button>
            <button class="btn btn-default order-button hidden" id="order-button-cancel"
                    onclick="cancelSelectedOrder()">Cancel
            </button>
            <button class="btn btn-default order-button hidden" id="order-button-start" onclick="startSelectedOrder()">
                Start
            </button>
            <button class="btn btn-default order-button hidden" id="order-button-complete"
                    onclick="completeSelectedOrder()">Complete
            </button>
        </div>

    </div>
</div>

</div>
</body>
