<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#myNavbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand">NcGrad</a>
        </div>

        <div class="navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav">
                <li id="navbar-main-page"><a href="/">Our services</a></li>
                <li id="navbar-about"><a href="/about">About</a></li>
                <li id="navbar-contacts"><a href="/contacts">Contacts</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown" id="region-select">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="region-selected">
                        <!--<span class="caret"></span>-->
                    </a>
                    <ul class="dropdown-menu"><!--
                        <li><a href="#">Region 1</a></li>
                        <li><a href="#">Region 2</a></li>
                        <li><a href="#">Region 3</a></li>-->
                    </ul>
                </li>
                <li>
                    <a href="#" class="hidden" data-toggle="modal" id="navbar-login-button" data-target="#login-modal">Login</a>
                </li>
                <li>
                    <a href="#" class="hidden" data-toggle="modal" id="navbar-registration-button" data-target="#registration-modal">Register</a>
                </li>
                <li id="navbar-account">
                    <a href="#" class="dropdown-toggle hidden" data-toggle="dropdown"  id="navbar-account-button">
                        Welcome, user!<span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu">
                        <li><a href="#">Region 1</a></li>
                        <li><a href="#">Region 2</a></li>
                        <li><a href="#">Region 3</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="modal fade" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="Login modal" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" align="center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
                <h1>Login to Your Account</h1>
                <div id="login-header-alert"></div>
            </div>
            <div class="modal-body">
                <form id="login-form">
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" name="email" placeholder="Email">
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" name="password" placeholder="Password">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="submit" onclick="login()" class="btn btn-primary btn-lg btn-block">Login</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="registration-modal" tabindex="-1" role="dialog" aria-labelledby="Registration modal" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" align="center">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </button>
                <h1>Create new account</h1>
                <div id="registration-header-alert"></div>
            </div>
            <div class="modal-body">
                <form id="registration-form">
                    <div class="form-group">
                        <label for="firstName">First name</label>
                        <input type="text" class="form-control" name="firstName" placeholder="First name">
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
                        <input type="password" class="form-control" name="password" placeholder="Password">
                    </div>
                    <div class="form-group">
                        <label for="repeatPassword">Repeat password</label>
                        <input type="password" class="form-control" name="repeatPassword" placeholder="Repeat password">
                    </div>
                    <div class="form-group">
                        <label for="phone">Phone</label>
                        <input type="text" class="form-control" name="phone" placeholder="Phone">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="submit" class="btn btn-primary btn-lg btn-block" onclick="register()">Register</button>
            </div>
        </div>
    </div>
</div>