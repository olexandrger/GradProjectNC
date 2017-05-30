<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit profile</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/editProfile.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="tabbable">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab1" data-toggle="tab">General</a></li>
            <li><a href="#tab2" data-toggle="tab">Password</a>
            <li><a href="#tab3" data-toggle="tab" onclick="firstLoadComplaints()">My complaints</a></li>
            <li><a href="#tab4" data-toggle="tab" onclick="loadAllInstances()">My services</a></li>
            <li><a href="#tab5" data-toggle="tab" onclick="loadOrders();">My orders</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab1">
                <div class="row">
                    <div class="col-sm-4">
                        </br>
                        <div id="update-profile-alert"></div>
                        <form id="userData">

                            <div class="form-group">
                                <label for="firstName">First name</label>
                                <input type="text" id="mf1" class="form-control" name="firstName"
                                       placeholder="First name">
                            </div>
                            <div class="form-group">
                                <label for="lastName">Last name</label>
                                <input type="text" id="mf2" class="form-control" name="lastName"
                                       placeholder="Last name">
                            </div>
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" id="mf3" class="form-control" name="email"
                                       placeholder="Email">
                            </div>
                            <div class="form-group">
                                <label for="phone">Phone</label>
                                <input type="text" id="mf4" class="form-control" name="phone"
                                       placeholder="Phone">
                            </div>

                        </form>


                        <div class="row">
                            <div class="col-sm-12" style="margin-top: 10px;">
                                <div class="col-xs-12 text-center">
                                    <div class="form-group">
                                        <a class="btn btn-success" onclick="saveChange()">
                                            Save changes
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-3" id="changeDomain" style="display: none;">
                        <div  id="domain-editor2">
                            <div class="row">
                                <div class="col-sm-12" style="margin-top: 45px;">
                                    <div class="col-xs-12 text-center">
                                        <div class="form-group">
                                            <a class="btn btn-success" href="/client/domains">
                                                Change domains
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div class="tab-pane" id="tab2">
                <div class="row">
                    <div class="col-sm-4">
                        </br>
                        <div id="change-password-alert"></div>
                        <form id="userData">
                            <div class="form-group">
                                <label for="currentPassword">Current password</label>
                                <input type="password" id="currentPassword" class="form-control" name="currentPassword"
                                       placeholder="Current password">
                            </div>
                            <div class="form-group">
                                <label for="newPassword">New password</label>
                                <input type="password" id="newPassword" class="form-control" name="newPassword"
                                       placeholder="New password">
                            </div>
                        </form>
                        <div class="row">
                            <div class="col-sm-12" style="margin-top: 10px;">
                                <div class="col-xs-12 text-center">
                                    <div class="form-group">
                                        <a class="btn btn-success" onclick="changePassword()">
                                            Change password
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane" id="tab3">

                <div class="row">
                    <div class="col-sm-4">
                        <div class="panel panel-default">
                            <div class="panel-heading" id="complain-list-panel-heading">
                                <b>Complaints:</b>
                                <button type="button" class="btn" id="complaint-btn-prev-up" onclick="getPrevPage()"> &larr; Prev.
                                </button>
                                <button type="button" class="btn" id="complaint-btn-next-up" onclick="getNextPage()"> Next &rarr;
                                </button>
                            </div>
                            <div class="panel-body" id="complain-list-panel-body">
                                <ul class="list-group" id="complain-list"></ul>
                            </div>
                            <div class="panel-footer" id="complain-list-panel-footer">
                                <button type="button" class="btn" id="complaint-btn-prev" onclick="getPrevPage()"> &larr; Prev.
                                </button>
                                <button type="button" class="btn" id="complaint-btn-next" onclick="getNextPage()"> Next &rarr;
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-8">
                        <div class="panel panel-default hidden" id="complain-info-panel">
                            <div class="panel-heading" id="complain-content-panel-heading">Complain content:</div>
                            <div class="panel-body" id="complain-content-panel-body">
                                <div class="row">
                                    <form>
                                        <div class="form-group">
                                            <div class="col-sm-4">
                                                <label for="complain-user-email">User email:</label>
                                            </div>
                                            <div class="col-sm-4">
                                                <input type="email" class="form-control" id="complain-user-email" disabled>
                                            </div>
                                        </div>
                                    </form>
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
                                                    <label for="complain-responsible-email">Responsible email:</label>
                                                </div>
                                                <div class="col-sm-4">
                                                    <input type="email" class="form-control" id="complain-responsible-email" disabled>
                                                </div>
                                            </div>
                                        </form>
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
                                                    <input type="text" class="form-control" id="complain-instance-name" disabled>
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
                                                <input type="text" class="form-control" id="selected-complain-stats" value="STATUS"
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
                                                <input type="datetime" class="form-control" id="selected-complain-start-date" disabled>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-2">
                                                <label for="selected-complain-end-date">End date: </label>
                                            </div>
                                            <div class="col-sm-4">
                                                <input type="text" class="form-control" id="selected-complain-end-date" value="" disabled>
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
                        </div>
                    </div>
                </div>
            </div>
            <div class="tab-pane active" id="tab4">
                <div class="row">
                    <div class=" col-sm-5 col-sm-offset-1">
                        </br>
                        <div class="list-group" id="instances-list"></div>
                        <ul class="pager">
                            <li class="previous hidden" id="instances-page-previous"><a href="#" onclick="previousInstancesPage()">Previous</a></li>
                            <li class="next hidden" id="instances-page-next"><a href="#" onclick="nextInstancesPage()">Next</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="tab-pane active" id="tab5">
                </br>
                <div class="row">
                    <div class="row">
                        <div class="col-sm-12">
                            <div id="csr-order-alert-place"">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-5">
                        <div class="list-group" id="csr-orders-list">
                        </div>
                        <ul class="pager">
                            <li class="previous hidden" id="orders-page-previous"><a href="#" onclick="previousPage()">Previous</a></li>
                            <li class="next hidden" id="orders-page-next"><a href="#" onclick="nextPage()">Next</a></li>
                        </ul>
                    </div>
                    <div class="col-sm-7 hidden" id="order-main">
                        <div class="form-group">
                            <label for="order-user-name">User</label>
                            <input type="text" class="form-control" name="order-user-name" id="order-user-name" readonly>
                        </div>

                    <#--
                                            <div class="form-group">
                                                <label for="order-domain">Domain</label>
                                                <select class="form-control" name="order-domain" id="order-domain">
                                                &lt;#&ndash;<option>Domain 1</option>&ndash;&gt;
                                        &lt;#&ndash;<option selected>Domain 2</option>&ndash;&gt;
                                        &lt;#&ndash;<option>Domain 3</option>&ndash;&gt;
                                                </select>
                                            </div>

                                            <div class="form-group">
                                                <label for="order-product">Product</label>
                                                <select class="form-control" name="order-product" id="order-product">
                                                &lt;#&ndash;<option>Product 1</option>&ndash;&gt;
                                        &lt;#&ndash;<option selected>Product 2</option>&ndash;&gt;
                                        &lt;#&ndash;<option>Product 3</option>&ndash;&gt;
                                                </select>
                                            </div>
                    -->

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
        </div>
    </div>
</div>
</div>
</div>

</div>
</body>
