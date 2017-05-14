var reportData;

function generateReport() {
    var data = {};

    $(".report-value-div").each(function() {
        data[$(this).find('[name="param-id"]').val()] = $(this).find('[name="param-value"]').val();
    });

    // console.log(data);

    $.ajax({
        url: "/api/csr/reports/generate/" + $("#report-select").val(),
        method: "POST",
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (report) {
            var table = document.createElement("table");
            // table.empty();
            table.setAttribute("id", "report-table");
            table.setAttribute("class", "table table-bordered");
            var tableBody = document.createElement("tbody");
            table.appendChild(tableBody);

            var currentRow = document.createElement("tr");
            report.header.forEach(function(item) {
                var th = document.createElement("th");
                th.appendChild(document.createTextNode(item));
                currentRow.appendChild(th);
            });

            tableBody.appendChild(currentRow);

            report.data.forEach(function(row) {
                currentRow = document.createElement("tr");

                row.forEach(function (value) {
                    var td = document.createElement("td");
                    td.appendChild(document.createTextNode(value));
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

function loadReportParams() {
    var reportId = $("#report-select").val();
    var report;

    reportData.forEach(function (item) {
        if (item.id == reportId) {
            report = item;
        }
    });

    var paramsPlace = $("#report-params-container");
    paramsPlace.empty();
    if (report != undefined) {
        report.params.forEach(function(item) {
            // console.log("add param");
            var inputType = "text";
            if (item.type == "DATE") {
                inputType = "date";
            }
            if (item.type == "DATETIME") {
                inputType = "datetime-local"
            }

            var div = '<div class="report-value-div input-group">' +
                '<span class="input-group-addon">' + item.name + '</span>' +
                '<input type="hidden" name="param-id" value="' + item.id + '"/>' +
                '<input type="' + inputType + '" class="form-control" name="param-value">' +
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
            reportData = data;
            var select = $("#report-select");
            select.empty();
            data.forEach(function(data) {
                var option = document.createElement("option");
                option.text = data.name;
                option.value = data.id;
                select.append(option);
            });
        },
        error: function (data) {
            console.error(data);
        }
    });
}

$(document).ready(function () {
    loadReports();
});