<!DOCTYPE html>
<html lang="en">
<head>
    <title>Complains</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/complain.js"></script>
    <script src="https://momentjs.com/downloads/moment.js"></script>

</head>

<body>

<div class="container">
<#include "../resources/navbar.ftl"/>

    <div class="row">
        <div class="col-sm-4">
            <div class="panel panel-default">
                <div class="panel-heading" id="complain-list-panel-heading">Complains</div>
                <div class="panel-body" id="complain-list-panel-body">
                    <ul class="list-group" id="complain-list">
                        <a class="list-group-item" href="#">Complain 1</a>
                        <a class="list-group-item" href="#">Complain 2 </a>
                        <a class="list-group-item" href="#">Complain 3</a>
                    </ul>
                </div>
                <div class="panel-footer" id="complain-list-panel-footer">
                    <button type="button" class="btn"> &larr; Prev.</button>
                    <button type="button" class="btn"> Next &rarr;</button>
                    <button type="button" class="btn btn-success">New</button>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="alert alert-warning" id="no-complain-selected-alert">
                <strong>Warning!</strong> Select a complaint to view information.
            </div>
            <div class="panel panel-default" id="complain-info-panel">
                <div class="panel-heading" id="complain-content-panel-heading">Complain content:</div>
                <div class="panel-body" id="complain-content-panel-body">
                <#--user-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-4">
                                    <label for="complain-user-email">User email:</label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="email" class="form-control" id="complain-user-email"
                                           value="denigam@gmail.com" disabled>
                                </div>
                            </div>
                        </form>

                        <div class="col-sm-4">
                            <button class="btn btn-default" data-toggle="collapse"
                                    data-target="#complain-user-details">&darr; More
                            </button>
                        </div>
                    </div>

                    <div class="col-sm-12">
                        <div id="complain-user-details" class="collapse">
                            <div class="row">
                                user details
                            </div>
                            <br>
                        </div>
                    </div>


                <#--responsible-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-4">
                                    <label for="complain-responsible-email">User email:</label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="email" class="form-control" id="complain-responsible-email"
                                           value="1@1.com" disabled>
                                </div>
                            </div>
                        </form>
                        <div class="col-sm-4">
                            <button class="btn btn-default" data-toggle="collapse"
                                    data-target="#complain-responsible-details">&darr; More
                            </button>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div id="complain-responsible-details" class="collapse">
                            <div class="row">
                                responsible details
                            </div>
                            <br>
                        </div>
                    </div>

                <#--instanse-->
                    <div class="row">
                        <form>
                            <div class="form-group">
                                <div class="col-sm-4">
                                    <label for="complain-instance-name">Instance name:</label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="complain-instance-name"
                                           value="Internet 100500" disabled>
                                </div>
                            </div>
                        </form>
                        <div class="col-sm-4">
                            <button class="btn btn-default" data-toggle="collapse"
                                    data-target="#complain-instance-details">&darr; More
                            </button>
                        </div>
                    </div>
                    <div class="col-sm-12">
                        <div id="complain-instance-details" class="collapse">
                            <div class="row">
                                responsible details
                            </div>
                            <br>
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
                                    <input type="datetime" class="form-control" id="selected-complain-start-date"
                                           value="2017.01.25 11:10" disabled>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-2">
                                    <label for="selected-complain-end-date">End date: </label>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control" id="selected-complain-end-date" value=""
                                           disabled>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="col-sm-12">
                        <form>
                            <div class="row">
                                <div class="form-group">
                                    <label for="selected-complain-title">Complain subject:</label>
                                    <input type="text" class="form-control" id="selected-complain-title" disabled>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label for="selected-complain-content">Complain content:</label>
                                    <textarea class="form-control" name="complain-content" rows="5"
                                              placeholder="Content"
                                              id="selected-complain-content">
                                </textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <label for="omplain-response">Complain Responce:</label>
                                    <textarea class="form-control" name="complain-response" rows="5"
                                              placeholder="Responce"
                                              id="selected-complain-responce">
                                </textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="panel-footer" id="complain-content-panel-footer">
                    <button type="button" class="btn">Update</button>
                    <button type="button" class="btn">Start</button>
                    <button type="button" class="btn">Cansel</button>
                    <button type="button" class="btn">Complete</button>
                </div>
            </div>
        </div>
    </div>

</div>

</body>