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
            <li><a href="#tab2" data-toggle="tab">Password</a></li>
            <li><a href="#tab3" data-toggle="tab" onclick="loadAllInstances()">My services</a></li>
            <li><a href="#tab4" data-toggle="tab" onclick="loadOrders();">My orders</a></li>
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
            <div class="tab-pane active" id="tab3">
                <div class="row">
                <div class=" col-sm-5 col-sm-offset-1">
                    </br>
                    <div class="list-group" id="instances-list"></div>
                </div>
            </div>
            </div>
            <div class="tab-pane active" id="tab4">
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
