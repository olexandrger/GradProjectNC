/**
 * Created by DeniG on 16.05.2017.
 */

var selectedComplain = -1;
var complaintListSize = 10;
var complaintListCurrentPage = 0;

function loadComplaints() {
$("#complain-list").empty();
$("#no-complain-selected-alert").removeAttr("hidden");
    $.ajax({
        url: "/api/pmg/complaint/get/all/size/"+(complaintListSize+1)+"/offset/"+complaintListSize*complaintListCurrentPage,
        success:function () {

        },
        error:{

        }
    });

}


$(document).ready(function () {
    loadComplaints();
});
