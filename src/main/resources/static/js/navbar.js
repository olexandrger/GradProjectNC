function login() {
    var email = $("#login-form").find('input[name="email"]').val();
    var password = $("#login-form").find('input[name="password"]').val();

    var _csrf = $('meta[name=_csrf]').attr("content");

    $.ajax({
        type: 'POST',
        url: '/login',
        headers: {
            'X-CSRF-TOKEN': _csrf
        },
        data: {
            email: email,
            password: password
        },
        success: function (data) {
            console.log("Cool! " + JSON.stringify(data));
        },
        error: function (data) {
            console.error("Login failure " + JSON.stringify(data));
        }
    });
}