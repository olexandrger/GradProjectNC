<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Domains</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/js/domains.js"></script>
</head>
<body>

<div class="container">
<#include "../resources/navbar.ftl"/>
    <div class="row">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div id="new-domain-alert-place">
                </div>
            </div>
        </div>
        <div class="row">
            <div class=" col-sm-3 col-sm-offset-1">
                <div class="list-group" id="domain-list"></div>
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="New domain" id="new-domain-name">
                    <span class="input-group-btn">
                <button type="button" onclick="addDomain()" class="btn btn-default"><span
                        class="glyphicon glyphicon-plus"></span>Add domain</button>
            </span>
                </div>
            </div>
            <div class="col-sm-7" id="domain-editor">

                <div class="row">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#domain">Main information</a></li>
                        <li><a data-toggle="tab" href="#user-editor">Users</a></li>
                    </ul>
                    <div class="tab-content">
                        <div class="col-sm-7 tab-pane fade in active" id="domain">
                            <div class="form-group">
                                <label for="domain-name">Domain name</label>
                                <input type="text" class="form-control" name="domain-name" placeholder="Name"
                                       id="domain-name-input">
                            </div>

                            <div class="form-group">
                                <label for="domain-type">Domain type</label>
                                <select class="form-control" name="domain-type-value" id="domain-type-selector"
                                        onchange="changeDomainType()">
                                    <option value="PRIVATE">PRIVATE</option>
                                    <option value="CORPORATIVE">CORPORATIVE</option>
                                </select>
                            </div>
                            <div class="form-group city-input">
                                <label for="domain-address">City</label>
                                <input type="text" class="form-control" name="city" placeholder="City"
                                       id="domain-city-input">
                            </div>
                            <div class="form-group street-input">
                                <label for="domain-address">Street</label>
                                <input type="text" class="form-control" name="street" placeholder="Street"
                                       id="domain-street-input">
                            </div>
                            <div class="form-group building-input">
                                <label for="domain-address">Building</label>
                                <input type="text" class="form-control" name="building" placeholder="Building"
                                       id="domain-building-input">
                            </div>
                            <div class="form-group apartment-input">
                                <label for="domain-address">Apartment</label>
                                <input type="text" class="form-control" name="apartment" placeholder="Apartment"
                                       id="domain-apartment-input">
                            </div>
                        </div>
                        <div class="form-group col-sm-9 tab-pane fade" id="user-editor">
                            <label>Users</label>
                            <input type="text" class="form-control" name="user-email" placeholder="E-mail"
                                   id="user-email-input">
                            <a class="btn btn-default" onclick="addUser()">
                                <span class="glyphicon glyphicon-plus"></span>Add
                            </a>
                            <div class="table-responsive" id="user-table">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>E-mail</th>
                                        <th>First name</th>
                                        <th>Last name</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12" style="margin-top: 10px;">
                    <div class="col-xs-12 text-center">
                        <div class="form-group">
                            <a class="btn btn-success" onclick="saveDomain()">
                                <span class="glyphicon glyphicon-floppy-disk"></span>Save
                            </a>
                            <a class="btn btn-danger" onclick="deleteDomain()">
                                <span class="glyphicon glyphicon-remove "></span>Delete
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</div>

</body>
</html>