<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reports</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/reports.js"></script>

</head>
<body>
<div class="container">

<#include "../resources/navbar.ftl"/>
    <div class="row">
        <div class="col-sm-12" id="report-alert-place">

        </div>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <div class="form-group">
                <label for="report-select">Select report</label>
                <select class="form-control" name="report-select" id="report-select" onchange="loadReportParams()">
                </select>
            </div>
            <div id="report-params-container" class="form-group">

            </div>
            <div class="form-group">
                <button class="btn btn-primary" onclick="generateReport()">Generate</button>
                <button class="btn btn-primary" onclick="generateXls()">Download</button>
            </div>
        </div>
        <div class="col-sm-8">
            <table id="report-table" class="table table-bordered">

            </table>
        </div>
    </div>
</div>
</body>