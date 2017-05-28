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

<div class="container" id="min-container">
<#include "../resources/navbar.ftl"/>
    <div class="col-sm-12" id="alerts-bar"></div>
<#--new complaint modal-->
    <div id="new-complaint-modal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">New complaint:</h4>
                </div>
                <div class="modal-body">
                    <div class="alert alert-danger" id="new-complaint-modal-error-msg">
                    </div>

                    <div class="form-group">
                        <label for="new-complaint-user-email">Email</label>
                        <input type="email" class="form-control" name="new-complaint-user-email" placeholder="Email"
                               id="new-complaint-user-email" onblur="loadDomainsInModal(13)" ,
                               onkeypress="loadDomainsInModal(event.keyCode)">
                    </div>

                    <div class="form-group">
                        <label for="new-complaint-domain">Domain</label>
                        <select class="form-control" name="new-complaint-domain" id="new-complaint-domain"
                                onchange="loadProductInstancesInModal()">
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="new-complaint-instanse">Product instance</label>
                        <select class="form-control" name="new-complaint-instanse" id="new-complaint-instanse">
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="new-complaint-reason">Complaint reason:</label>
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
                    <button type="button" class="btn btn-success" data-dismiss="modal"
                            id="create-complaint-ftom-modal-btn"
                            onclick="createNewComplaintFromModal()">Create
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-4">
            <div class="panel panel-default">
                <div class="panel-heading" id="complain-list-panel-heading">
                    <b>Complaints:</b>
                    <button type="button" class="btn" id="complaint-btn-prev-up" onclick="getPrevPage()"> &larr; Prev.
                    </button>
                    <button type="button" class="btn" id="complaint-btn-next-up" onclick="getNextPage()"> Next &rarr;
                    </button>
                    <button type="button" class="btn btn-success" data-toggle="modal"
                            data-target="#new-complaint-modal" onclick="openNewComplaintModal()">
                        New
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
                    <button type="button" class="btn btn-success" data-toggle="modal"
                            data-target="#new-complaint-modal" onclick="openNewComplaintModal()">
                        New
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-8">
            <div class="alert alert-warning" id="no-complain-selected-alert">
                <strong>Warning!</strong> Select a complaint to view information.
            </div>
            <div class="panel panel-default hidden" id="complain-info-panel">
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
                                        <label for="complain-responsible-email">Responsible email:</label>
                                    </div>
                                    <div class="col-sm-4">
                                        <input type="email" class="form-control" id="complain-responsible-email"
                                               value="1@1.com" disabled>
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
                                               value="Internet 100500" disabled>
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
                                              maxlength="240"
                                              onkeyup="unlockCompletConsiderationButton()"></textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="panel-footer" id="complain-content-panel-footer">
                    <button type="button" class="btn" id="take-complaint-btn" onclick="takeComplaintForConsideration()">
                        Take for consideration
                    </button>
                    <button type="button" class="btn" id="complet-consideration-complaint-btn"
                            onclick="completComplaintConsideration()" disabled>
                        Reply and close
                    </button>
                </div>
            </div>
        </div>
    </div>

</div>

</body>