<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Users</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/users.js"></script>

</head>
<body>
<div class="container">
<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-12">

            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#tab1" data-toggle="tab">Create new user</a></li>
                    <li><a href="#tab2" data-toggle="tab">Edit existing user</a></li>
                    <li><a href="#tab3" data-toggle="tab" onclick="loadRegions(); hideUserInfo()">Users</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab1">
                        <h1>Create new account</h1>
                        <div class="row" id="registerRow">
                            <div class="col-sm-6">
                                <div id="registration-header-alert1"></div>
                                <form id="registration-form1">
                                    <div class="form-group">
                                        <label for="firstName">First name</label>
                                        <input type="text" class="form-control" name="firstName"
                                               placeholder="First name">
                                    </div>
                                    <div class="form-group">
                                        <label for="lastName">Last name</label>
                                        <input type="text" class="form-control" name="lastName" placeholder="Last name">
                                    </div>
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <input type="email" class="form-control" name="email" placeholder="Email">
                                    </div>
                                    <div class="form-group">
                                        <label for="password">Password</label>
                                        <input type="password" class="form-control" name="password"
                                               placeholder="Password">
                                    </div>

                                    <div class="form-group">
                                        <label for="phone">Phone</label>
                                        <input type="text" class="form-control" name="phone" placeholder="Phone">
                                    </div>
                                </form>


                                <p>Roles assigned to user:</p>
                                <form id = user-roles-checkboxes>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="adminchbx">Admin
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="client" checked>Client
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="csrchbx">CSR
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="pmgchbx">PMG
                                    </label>
                                </form>

                                <div class="row">
                                    <div class="col-sm-12" style="margin-top: 10px;">

                                        <div class="btn-group inline" >

                                            <button type="button" class="btn btn-success" onclick="registerByAdmin()" >Register</button>

                                        </div>
                                    </div>
                                </div>
                            </div>


                            <div class="col-sm-3">


                                <div class="list-group" id="list2">

                                </div>



                            </div></div></div>
                    <div class="tab-pane" id="tab2">
                        <h3>Edit existing user</h3>
                        <div class="row">
                            <div class="col-sm-4">
                                <div id="search-alert"></div>
                                <form id="userSearch">

                                    <div class="form-group">
                                        <label for="email">Load user</label>
                                        <input type="email" class="form-control" id="tab2-email" name="email" placeholder="Email">
                                        <div class="form-group">
                                            <a class="btn btn-success pull-right"  style="margin-top: 10px" onclick="getUser()">
                                                Load
                                            </a>
                                        </div>
                                    </div>

                                </form>

                                <label for="user-roles-checkboxesM" style="margin-top: 10px">User roles</label>
                                <form id = user-roles-checkboxesM>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="adminM">Admin
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="clientM">Client
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="csrM">CSR
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="pmgM">PMG
                                    </label>
                                </form>
                                <div></div>
                                <div class="row"></div>

                                <label for="userData" >User info</label>
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
                                        <label for="password">Password</label>
                                        <input type="password" id="mf4" class="form-control" name="password"
                                               placeholder="Password">
                                    </div>
                                <#--<div class="form-group">
                                    <label for="address">Address</label>
                                    <input type="text" id="mf5" class="form-control" name="address"
                                           placeholder="Address">
                                </div>
                                <div class="form-group">
                                    <label for="aptNumber">Apartment number</label>
                                    <input type="text" id="mf6" class="form-control" name="aptNumber"
                                           placeholder="Apartment number">
                                </div>-->

                                    <div class="form-group">
                                        <label for="phone">Phone</label>
                                        <input type="text" id="mf7" class="form-control" name="phone"
                                               placeholder="Phone">
                                    </div>

                                </form>


                                <div class="row">
                                    <div class="col-sm-12" style="margin-top: 10px;">
                                        <div class="col-xs-12 text-center">
                                            <div class="form-group">
                                                <a class="btn btn-success" onclick="saveUser()">
                                                    Save changes
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="col-sm-6">
                                <div class="list-group" id="list1"></div>

                                <div class=" hidden" id="domain-editor">

                                    <div class="row">
                                        <div class="col-sm-12" id="product-type-values">
                                            <div class="form-group hidden">
                                                <label for="domain-id">Id</label>
                                                <input type="text" class="form-control" name="domain-id" placeholder="Id" id="domain-id-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="domain-name">Name</label>
                                                <input type="text" class="form-control" name="domain-name" placeholder="Domain name" id="domain-name-input" disabled >
                                            </div>

                                            <div class="form-group">
                                                <label for="domain-type">Type</label>
                                                <input type="text" class="form-control" name="domain-type" placeholder="Domain type" id="domain-type-input" disabled>
                                            </div>

                                            <div class="form-group">
                                                <label for="domain-region">Region </label>
                                                <input type="text" class="form-control" name="domain-region" placeholder="Domain region" id="domain-region-input" disabled>
                                            </div>

                                            <div class="form-group">
                                                <label for="domain-address">Address</label>
                                                <input type="text" class="form-control" name="domain-address" placeholder="Domain address" id="domain-address-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="domain-address-apt">Apartment number</label>
                                                <input type="text" class="form-control" name="domain-address-apt" placeholder="Address" id="domain-address-apt-input" disabled>
                                            </div>


                                        </div>
                                    </div>



                                </div>
                                <div class="row">
                                    <div class="col-sm-12" style="margin-top: 10px;">

                                        <div id="domains-buttons" class="hidden" >
                                            <button type="button" class="btn btn-danger btn-sm hidden" onclick="deleteSelected()" id="del-dom-btn">Delete selected domains from user</button>
                                           <#-- <button type="button" class="btn btn-default btn-sm" onclick="redirectToDomains()" id="add-domain-btn">Add user to existing domain</button>-->
                                        </div>

                                    </div>
                                </div>



                            </div>
                            <div class="col-sm-4 ">

                            </div>

                        </div>


                    </div>
                    <div class="tab-pane" id="tab3">
                        <h3>Users</h3>
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label for="new-region">Region</label>
                                    <select class="form-control" name="new-region" id="new-region"
                                            onchange="clearPhoneField(); sortById(); loadUsers();">
                                    </select>
                                </div>
                                <div class="input-group">
                                    <input type="text" class="form-control" placeholder="phone" id="find-by-phone">
                                    <span class="input-group-btn">
                                        <button type="button" class="btn btn-default" onclick="searchUsers()">Find by phone<span class="glyphicon "></span></button></span>
                                </div>
                                <div class="btn-group btn-group-justified" role="group">
                                    <div class="btn-group" role="group">
                                    <button type="button" class="btn btn-primary" onclick="sortByEmail(); loadUsers()">Sort by email</button>
                                    </div>
                                    <div class="btn-group" role="group">
                                    <button type="button" class="btn btn-primary" onclick="sortByLastName(), loadUsers()">Sort by last name</button>
                                    </div>
                                </div>
                                <div class="list-group" id="csr-users-list"></div>
                                <ul class="pager">
                                    <li class="previous hidden" id="users-page-previous"><a href="#" onclick="previousPage()">Previous</a></li>
                                    <li class="next hidden" id="users-page-next"><a href="#" onclick="nextPage()">Next</a></li>
                                </ul>
                            </div>
                            <div class="col-sm-8" id="user-info">
                                <div id="registration-header-alert1"></div>
                                <form id="user-info-form">
                                    <div class="form-group">
                                        <label for="userFirstName">First name</label>
                                        <input type="text" class="form-control" id="userFirstName" name="userFirstName" placeholder="First name" disabled="disabled">
                                    </div>
                                    <div class="form-group">
                                        <label for="userLastName">Last name</label>
                                        <input type="text" class="form-control" id="userLastName" name="userLastName" placeholder="Last name" disabled="disabled">
                                    </div>
                                    <div class="form-group">
                                        <label for="userEmail">Email</label>
                                        <input type="email" class="form-control" id="userEmail" name="userEmail" placeholder="Email" disabled="disabled">
                                    </div>
                                    <div class="form-group">
                                        <label for="userPhone">Phone</label>
                                        <input type="text" class="form-control" id="userPhone" name="userPhone" placeholder="Phone" disabled="disabled">
                                    </div>
                                </form>
                                <div class="row">
                                    <div class="col-sm-12" style="margin-top: 10px;">
                                        <div class="col-xs-12 text-center">
                                            <div class="form-group">
                                                <a class="btn btn-success" onclick="editUser()">
                                                    Edit user
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


</div><!-- /.container -->

</body>