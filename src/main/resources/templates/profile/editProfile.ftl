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

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="tabbable">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tab1" data-toggle="tab">General</a></li>
            <li><a href="#tab2" data-toggle="tab">Password</a></li>
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

        </div>
    </div>
</div>

</div>
</body>
