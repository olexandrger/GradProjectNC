<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link rel="stylesheet" type="text/css" media="screen" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
    <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css">

    <link href="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/build/css/bootstrap-datetimepicker.css" rel="stylesheet">


    <script type="text/javascript" src="//code.jquery.com/jquery-2.1.1.min.js"></script>

    <script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment-with-locales.js"></script>
    <script src="//cdn.rawgit.com/Eonasdan/bootstrap-datetimepicker/e8bddc60e73c1ec2475f827be36e1957af72e2ea/src/js/bootstrap-datetimepicker.js"></script>
    <script src="/js/discounts.js"></script>

</head>
<body>

<div class="container">

<#include "../resources/navbar.ftl"/>

    <div class="row">

        <div class="row">
            <div class="col-sm-10 col-sm-offset-1">
                <div id="new-discount-alert-place">
                </div>
            </div>
        </div>
        <div class="row">
            <div class=" col-sm-3 col-sm-offset-1">
                <div class="list-group" id="discounts-list"></div>
                <ul class="pager">
                    <li class="previous hidden" id="discounts-page-previous"><a href="#" onclick="previousPage()">Previous</a></li>
                    <li class="next hidden" id="discounts-page-next"><a href="#" onclick="nextPage()">Next</a></li>
                </ul>
                <span class="input-group-btn">
                <#--<button type="button" onclick="this.style.visibility='hidden'; loadAllDiscounts();" class="btn btn-info btn-block"> show all</button>-->
                    </span>
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="New discount" id="new-discount-name">
                    <span class="input-group-btn">
                <button type="button" onclick="addDiscount()" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>Add discount</button>
            </span>
                </div>
            </div>
            <div class="col-sm-7 " id="discount-editor">

                <div class="row">
                    <div class="col-sm-12" id="discount-values">
                        <div class="form-group">
                            <label for="discount-name-input">Name</label>
                            <input type="text" class="form-control" name="discount-name" placeholder="Name" id="discount-name-input">
                        </div>

                        <div class="form-group">
                            <label for="discount-region-selector">Discount region</label>
                            <select class="form-control" name="discount-region-value" id="discount-region-selector"
                                    onchange="changeRegion()">

                            </select>
                        </div>



                        <div class="form-group">
                            <label for="datetime1">Start date</label>
                            <div class='input-group date' id='datetimepicker1'>
                                <input type='text' class="form-control" id="datetime1"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="datetime2">End date</label>
                            <div class='input-group date' id='datetimepicker2'>
                                <input type='text' class="form-control" id="datetime2"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </div>



                        <label for="ammInput">Ammount of discount</label>
                        <div class="input-group">
                            <span class="input-group-addon" id="basic-addon1">%</span>
                            <input type="text" class="form-control" id="ammInput" placeholder="Username" aria-describedby="basic-addon1">
                        </div>


                        <label for="discount-product-selector">Discount products</label>
                        <form class="form-inline">
                        <div class="form-group">

                            <select class="form-control" name="discount-product-value" id="discount-product-selector"
                                    onchange="changeProduct()">
                            </select>

                        </div>

                            <button type="button" onclick="addToProducts()" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span>Add</button>

                                <button type="button" class="btn btn-danger btn-sm " onclick="deleteSelectedProducts()" id="del-dom-btn">Remove selected products </button>


                        </form>

                        <div class="list-group" style="margin-top: 10px;" id="products"></div>
                        <div class="row">
                            <div class="col-sm-12" style="margin-top: 10px;">



                            </div>
                        </div>


                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12" style="margin-top: 10px;">
                        <div class="col-xs-12 text-center">
                            <div class="form-group">
                                <a class="btn btn-success" onclick="saveSelected()">
                                    <span class="glyphicon glyphicon-floppy-disk"></span>Save
                                </a>

                            <#--<a class="btn btn-danger" onclick="deleteSelected()">-->
                            <#--<span class="glyphicon glyphicon-remove "></span>Delete-->
                            <#--</a>-->

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>