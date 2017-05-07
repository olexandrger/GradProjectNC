
function buttonsToDown() {
    $('.pull-down').each(function() {
        var $this = $(this);
        $this.css('margin-top', $this.parent().height() - $this.height())
    });
}

function loadInstance() {

    var instanceId = window.name.data.selectedInstance;

    $.ajax({
        url: "/api/client/instance/get/byId/" + instanceId,
        success: function (data) {
            console.log(data);
        },
        error: function (data) {
            console.log(data);
        }
    });
}

$(document).onload(function () {
    loadInstance();
});