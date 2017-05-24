var reportMetadata;

function wrapLinks(text) {
    return text.replace(/(https?\:\/\/[^\s]+)/mg,  '<a href="$1" target="_blank">$1</a>');
}

function wrapRelativeLinks(link, text) {
    return link.replace(/(\/[^\s]+)/mg,  '<a href="$1" target="_blank">' + text + '</a>');
}

function showErrorMessage(message) {
    console.log(message);

    var alert = $('<div class="alert alert-danger" role="alert">' +
        '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
        message + "</div>");

    alert.delay(2000)
        .fadeOut(function () {
            alert.remove();
        });

    $("#report-alert-place").append(alert);
}

function getEncodedParams() {
    var data = {};

    var isEmptyField = false;

    $(".report-value-div").each(function() {
        var value = $(this).find('[name="param-value"]').val();
        if (value == "") {
            isEmptyField = true;
        }
        data[$(this).find('[name="param-id"]').val()] = value;
    });

    if (isEmptyField) {
        return null;
    } else {
        return btoa(JSON.stringify(data));
    }
}

function generateReport() {

    var params = getEncodedParams();

    if (params == null) {
        showErrorMessage("You can not send empty fields");
    } else {
        $.ajax({
            url: "/api/csr/reports/generate/" + $("#report-select").val() + "/json/" + params,
            method: "GET",
            success: function (report) {
                var table = document.createElement("table");
                table.setAttribute("id", "report-table");
                table.setAttribute("class", "table table-bordered");
                var tableBody = document.createElement("tbody");
                table.appendChild(tableBody);

                var currentRow = document.createElement("tr");
                report.header.forEach(function (item) {
                    var th = document.createElement("th");
                    th.appendChild(document.createTextNode(item));
                    currentRow.appendChild(th);
                });

                tableBody.appendChild(currentRow);

                report.data.forEach(function (row) {
                    currentRow = document.createElement("tr");

                    row.forEach(function (value, index) {
                        var td = document.createElement("td");
                        // td.appendChild(document.createTextNode(value));
                        td.innerHTML = wrapLinks(value);
                        // td.innerHTML = wrapRelativeLinks(value, report.header[index]);
                        currentRow.appendChild(td);
                    });

                    tableBody.appendChild(currentRow);
                });

                $("#report-table").replaceWith(table);
            },
            error: function (data) {
                console.error(data);
            }
        });
    }
}

function generateXls() {
    var params = getEncodedParams();
    if (params == null) {
        showErrorMessage("You can not send empty fields");
    } else {
        window.open("/api/csr/reports/generate/" + $("#report-select").val() + "/xls/" + params);
    }
}

function loadReportParams() {
    var reportId = $("#report-select").val();
    var report;

    reportMetadata.forEach(function (item) {
        if (item.id == reportId) {
            report = item;
        }
    });

    var paramsPlace = $("#report-params-container");
    paramsPlace.empty();
    if (report != undefined) {
        report.params.forEach(function(item) {
            var inputType = "text";
            if (item.type == "DATE") {
                inputType = "date";
            }
            if (item.type == "DATETIME") {
                inputType = "datetime-local"
            }
            if (item.type == "NUMBER") {
                inputType = "number";
            }

            var div = '<div class="report-value-div form-group input-group">' +
                '<span class="input-group-addon">' + item.name + '</span>' +
                '<input type="hidden" name="param-id" value="' + item.id + '"/>' +
                '<input type="' + inputType + '" class="form-control" name="param-value" required>' +
                '</div>';

            paramsPlace.append($(div));
        });
    } else {
        console.error("Unknown report selected")
    }
}

function loadReports() {
    $.ajax({
        url: "/api/csr/reports/get/all",
        method: "GET",
        contentType: 'application/json',
        success: function (data) {
            reportMetadata = data;
            var select = $("#report-select");
            select.empty();
            data.forEach(function(data) {
                var option = document.createElement("option");
                option.text = data.name;
                option.value = data.id;
                select.append(option);
            });

            loadReportParams();
        },
        error: function (data) {
            console.error(data);
        }
    });
}

$(document).ready(function () {
    loadReports();
});