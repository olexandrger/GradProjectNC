<!DOCTYPE html>
<html lang="en">
<head>
    <title>NcGrad</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <link rel="stylesheet" href="/css/main.css">
    <script src="/js/navbar.js"></script>
    <script src="/js/mainPage.js"></script>
</head>
<body>

<div class="container">

    <#include "resources/navbar.ftl"/>

    <div class="row">
        <div class="col-md-4 col-md-offset-4">

            <div id="mainCarousel" class="carousel slide" data-ride="carousel">
                <div class="carousel-inner">
                    <div class="item active">
                        <h2>This is first box</h2>
                        <p>Product description here</p>
                    </div>
                    <div class="item">
                        <h2>This is second box</h2>
                        <p>Product description here</p>
                    </div>
                    <div class="item">
                        <h2>This is third box</h2>
                        <p>Product description here</p>
                    </div>
                </div>

                <ol class="carousel-indicators">
                    <li data-target="#mainCarousel" data-slide-to="0" class="active"></li>
                    <li data-target="#mainCarousel" data-slide-to="1"></li>
                    <li data-target="#mainCarousel" data-slide-to="2"></li>
                </ol>
            </div>
        </div>
        <div class="col-md-4"></div>
    </div>
</div>

</body>
</html>