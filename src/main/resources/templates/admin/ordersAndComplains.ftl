<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Complains and orders</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/adminOrdersAndComplaints.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>

<body>

<div class="container">

<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-12" id="alerts-bar"></div>
    </div>
    <div class="row">
        <div class="col-sm-12" id="tab-bar">
            <ul class="nav nav-tabs" id="nav-tabs">
                <li id="orders-tab"><a href="#" onclick="selectOrdersTab()">Orders</a></li>
                <li id="complaints-tab"><a href="#" onclick="selectComplaintsTab()">Complaints</a></li>
            </ul>
        </div>
    </div>
<#--Orders-->
    <div class="row" id="orders-area">
        <div class="col-sm-4" id="orders-list-area">
            <div class="panel panel-default" id="order-list-panel">
                <div class="panel-heading">Orders:</div>
                <div class="panel-body">
                    <ul class="list-group" id="admin-order-list">
                    </ul>
                </div>
                <div class="panel-footer">
                    <button type="button" class="btn" id="order-btn-prev" onclick="getPrevOrderPage()"> &larr; Prev.
                    </button>
                    <button type="button" class="btn" id="order-btn-next" onclick="getNextOrderPage()"> Next &rarr;
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-8" id="orderst-content-area">
            <div class="panel panel-default" id="order-content-panel">
                <div class="panel-heading">Order datails:</div>
                <div class="panel-body">
                    <div class="form-group">
                        <label for="selected-order-user-name">User</label>
                        <input type="text" class="form-control" name="order-user-name" id="selected-order-user-name"
                               readonly>
                    </div>
                    <div class="form-group">
                        <label for="selected-order-responsible-name">Responsible:</label>
                        <select type="text" class="form-control" name="order-user-name"
                                id="selected-order-responsible-name" disabled></select>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-domain">Domain</label>
                        <input type="text" class="form-control" name="order-domain" id="selected-order-domain"
                               disabled></input>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-product">Product</label>
                        <input type="text" class="form-control" name="order-product" id="selected-order-product"
                               disabled></input>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-aim">Aim</label>
                        <input type="text" class="form-control" name="order-aim" id="selected-order-aim" readonly>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-status">Status</label>
                        <input type="text" class="form-control" name="order-status" id="selected-order-status" readonly>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-start-date">Start date</label>
                        <input type="text" class="form-control" name="selected-order-start-date"
                               id="selected-order-start-date" readonly>
                    </div>

                    <div class="form-group">
                        <label for="selected-order-end-date">End date</label>
                        <input type="text" class="form-control" name="selected-order-end-date"
                               id="selected-order-end-date" readonly>
                    </div>

                </div>
                <div class="panel-footer">
                    <button type="button" class="btn btn-default" id="edit-order-responsible-btn" onclick="editOrderResponsible()">Edit Responsible</button>
                    <button type="button" class="btn btn-default" id="assign-order-responsible-btn" onclick="assignOrderResponsible()">Assign</button>
                </div>
            </div>
        </div>
    </div>

<#--Complaints-->
    <div class="row" id="complaint-area">
        <div class="col-sm-4" id="complaint-list-area">
            <div class="panel panel-default" id="complaint-list-panel">
                <div class="panel-heading">Complaints:</div>
                <div class="panel-body">
                    <ul class="list-group" id="complain-list"></ul>
                </div>
                <div class="panel-footer">
                    <button type="button" class="btn" id="complaint-btn-prev" onclick="getPrevComplaintPage()">
                        &larr; Prev.
                    </button>
                    <button type="button" class="btn" id="complaint-btn-next" onclick="getNextComplaintPage()"> Next
                        &rarr;
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-8" id="complaint-content-area">
            <div class="alert alert-warning" id="no-complain-selected-alert">
                <strong>Warning!</strong> Select a complaint to view information.
            </div>
            <div class="panel panel-default hidden" id="complaint-content-panel">
                <div class="panel-heading">Complaint details:</div>
                <div class="panel-body">
                <#--user-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-4">
                                    <label for="complain-user-email">User email:</label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="email" class="form-control" id="complain-user-email"
                                           disabled>
                                </div>
                            </div>
                        </form>

                        <div class="col-sm-4">
                            <button class="btn btn-default" data-toggle="collapse"
                                    data-target="#complain-user-details-collapse">&darr; More
                            </button>
                        </div>
                    </div>

                    <div class="col-sm-12">
                        <div class="collapse" id="complain-user-details-collapse">
                            <div class="row" id="complain-user-details">
                                user details
                            </div>
                            <br>
                        </div>
                    </div>


                <#--responsible-->
                    <div id="responsible-info-row">
                        <div class="row">
                            <form>
                                <div class="form-group">
                                    <div class="col-sm-4">
                                        <label for="complain-responsible-email">Responsible:</label>
                                    </div>
                                    <div class="col-sm-4">
                                        <select class="form-control" id="complain-responsible-email" disabled></select>
                                    </div>
                                </div>
                            </form>
                            <div class="col-sm-4">
                                <button class="btn btn-default" data-toggle="collapse"
                                        data-target="#complain-responsible-details-collapse">&darr; More
                                </button>
                            </div>
                        </div>
                        <div class="col-sm-12">
                            <div id="complain-responsible-details-collapse" class="collapse">
                                <div class="row" id="complain-responsible-details">
                                    responsible details
                                </div>
                                <br>
                            </div>
                        </div>
                    </div>

                <#--instanse-->
                    <div id="instance-info-row">
                        <div class="row">
                            <form>
                                <div class="form-group">
                                    <div class="col-sm-4">
                                        <label for="complain-instance-name">Instance name:</label>
                                    </div>
                                    <div class="col-sm-4">
                                        <input type="text" class="form-control" id="complain-instance-name"
                                               disabled>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                <#--reason and status-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-3">
                                    <label for="selected-complain-stats">Complain status: </label>
                                </div>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control" id="selected-complain-stats"
                                           disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-3">
                                    <label for="selected-complain-reason">Complain reason: </label>
                                </div>
                                <div class="col-sm-3">
                                    <input type="text" class="form-control" id="selected-complain-reason" value="REASON"
                                           disabled>
                                </div>
                            </div>
                        </form>
                    </div>
                <#--start and end-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-2">
                                    <label for="selected-complain-start-date">Start date: </label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="datetime" class="form-control" id="selected-complain-start-date"
                                           disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-2">
                                    <label for="selected-complain-end-date">End date: </label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="selected-complain-end-date"
                                           disabled>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="col-sm-12">
                        <form>
                            <div class="row">
                                <div class="form-group">
                                    <label for="selected-complain-title">Complaint subject:</label>
                                    <input type="text" class="form-control" id="selected-complain-title" disabled>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label for="selected-complain-content ">Complaint content:</label>
                                    <textarea class="form-control " name="complain-content" rows="4"
                                              placeholder="Content"
                                              id="selected-complain-content"
                                              maxlength="240" readonly></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label for="omplain-response">Complaint Responce:</label>
                                    <textarea class="form-control" name="complain-response" rows="4"
                                              placeholder="Responce"
                                              id="selected-complain-responce"
                                              maxlength="240" readonly></textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="button" class="btn btn-default" id="edit-conplaint-responsible-btn" onclick="editComplaintResponsible()">Edit Responsible</button>
                    <button type="button" class="btn btn-default" id="assign-complaint-responsible-btn" onclick="assignComplaintResponsible()">Assign</button>
                </div>
            </div>
        </div>
    </div>

</div>
</body>