/**
 * Created by DeniG on 16.05.2017.
 */

var selectedComplain = -1;
var complaintListSize = 10;
var complaintListCurrentPage = 0;
var complaintsData;
var modalAlert;
var selectUserId;

function loadComplaints() {
    $.ajax({
        url: "/api/pmg/complaint/get/all/size/" + (complaintListSize + 1) + "/offset/" + complaintListSize * complaintListCurrentPage,
        success: function (data) {
            $("#no-complain-selected-alert").removeAttr("hidden");
            list = $("#complain-list");
            list.empty();
            complaintsData = data;
            data.forEach(function (item, i) {
                if (i < complaintListSize) {
                    ref = document.createElement("a");
                    complaintName = item.complainReason + "_" + item.productInstanceName;
                    ref.appendChild(document.createTextNode(complaintName));
                    ref.className = "list-group-item";
                    ref.href = "#";
                    ref.onclick = function () {
                        selectComplaint(i);
                    };
                    list.append(ref);
                }
            });
            prevPage = $("#complaint-btn-prev");
            nextPage = $("#complaint-btn-next");
            nextPage.addClass("disabled");
            prevPage.addClass("disabled");
            if (complaintListCurrentPage > 0) {
                prevPage.removeClass("disabled");
            }
            if (data.length > complaintListSize) {
                nextPage.removeClass("disabled");
            }
            selectComplaint(-1);
        },
        error: function () {
            console.error("Cannot load list complaints");
        }
    });
}

function selectComplaint(index) {
    list = $("#complain-list");
    selectedComplain = index;
    if (index == -1) {
        $("#no-complain-selected-alert").removeClass("hidden");
        $("#complain-info-panel").addClass("hidden");
        list.find("a").removeClass("active");
    } else {
        $("#no-complain-selected-alert").addClass("hidden");
        $("#complain-info-panel").removeClass("hidden");
        list.find("a").removeClass("active");
        list.find("a:nth-child(" + (selectedComplain + 1) + ")").addClass("active");
    }
    if (selectedComplain != -1) {
        $("#complain-user-email").val(complaintsData[selectedComplain].userEmail);
        //TODO add other user data
        $("#complain-responsible-email").val(complaintsData[selectedComplain].responsibleEmail);
        //TODO add other responsible data
        $("#complain-instance-name").val(complaintsData[selectedComplain].productInstanceName);
        $("#selected-complain-stats").val(complaintsData[selectedComplain].status);
        $("#selected-complain-reason").val(complaintsData[selectedComplain].complainReason);

        $("#selected-complain-start-date").val(moment.unix(complaintsData[selectedComplain].openDate).format("LLL"));
        $("#selected-complain-end-date").val(moment.unix(complaintsData[selectedComplain].closeDate).format("LLL"));
        $("#selected-complain-title").val(complaintsData[selectedComplain].complainTitle);
        $("#selected-complain-content").val(complaintsData[selectedComplain].content);
        $("#selected-complain-responce").val(complaintsData[selectedComplain].response);
    }
}

function loadDomainsInModal(keyCode) {
    if (keyCode != 13) {
        return;
    }
    modalAlert = $("#new-complaint-modal-error-msg");
    $("#new-complaint-domain").empty();
    if ($("#new-complaint-user-email").val().length < 1) {
        modalAlert.html("<strong>Warning! </strong> E-mail field is empty!");
        modalAlert.removeClass("hidden");
        clearNewComplaintModalFormByIncorrectEmail();
    } else {
        $.ajax({
            url: "/api/pmg/domains/find/bymail/"+$("#new-complaint-user-email").val()+"/",
            success:function (data) {
                if(data.status == "not found"){
                    modalAlert.html("<strong> Warning! </strong> Email not found");
                    modalAlert.removeClass("hidden");
                    clearNewComplaintModalFormByIncorrectEmail();
                }else {
                    selectUserId = data.userId;
                    if (data.domains.length > 0){
                        options = $("#new-complaint-domain");
                        data.domains.forEach(function (item, i) {
                            modalAlert.empty();
                            modalAlert.addClass("hidden");
                            option = document.createElement("option");
                            if(i==0){
                                option.setAttribute("selected", "selected");
                            }
                            option.setAttribute("value", item.domainId);
                            option.appendChild(document.createTextNode(item.domainName));
                            options.append(option);
                        });
                       options.removeAttr("disabled");
                        loadProductInstancesInModal();
                    } else {
                        modalAlert.html("<strong> Warning! </strong> This user does not have any domains!");
                        modalAlert.removeClass("hidden");
                        clearNewComplaintModalFormByIncorrectEmail();
                    }
                }
            },
            error:function () {
                modalAlert.html("<strong>Error! </strong> Internal server error!");
                modalAlert.removeClass("hidden");
                clearNewComplaintModalFormByIncorrectEmail();
            }
        });
    }
}

function loadProductInstancesInModal() {
    modalAlert = $("#new-complaint-modal-error-msg");
    $.ajax({
        url:"/api/pmg/instances/find/bydomain/"+$("#new-complaint-domain").val()+"/",
        success: function (data) {
            clearNewComplaintModalFormByIncorrectDomain();
            if (data.length > 0){
                modalAlert.empty();
                modalAlert.addClass("hidden");
                options = $("#new-complaint-instanse");
                data.forEach(function (item, i) {
                    var option = document.createElement("option");
                    option.setAttribute("value", item.instanceId);
                    if(i==0){
                        option.setAttribute("selected", "selected");
                    }
                    option.appendChild(document.createTextNode(item.product.productName));
                    options.append(option);
                });
                options.removeAttr("disabled");
                $("#new-complaint-subject").removeAttr("disabled");
                $("#new-complaint-reason").removeAttr("disabled");
                $("#new-complaint-content").removeAttr("readonly");
                $("#create-complaint-ftom-modal-btn").removeAttr("disabled");
            } else {
                clearNewComplaintModalFormByIncorrectDomain();
                modalAlert.html("<strong>Warning! </strong> This domain does not have any instances!");
                modalAlert.removeClass("hidden");
            }
        },
        error: function (){
            clearNewComplaintModalFormByIncorrectDomain();
            modalAlert.html("<strong>Error! </strong> Internal server error!");
            modalAlert.removeClass("hidden");
        }

    });
}

function clearNewComplaintModalForm() {
    $("#new-complaint-user-email").val("");
    clearNewComplaintModalFormByIncorrectEmail();
}
function clearNewComplaintModalFormByIncorrectEmail() {
    $("#new-complaint-domain").attr("disabled","disabled");
    $("#new-complaint-domain").empty();
    clearNewComplaintModalFormByIncorrectDomain();
}
function clearNewComplaintModalFormByIncorrectDomain() {
    $("#new-complaint-instanse").attr("disabled","disabled");
    $("#new-complaint-instanse").empty();
    $("#new-complaint-subject").attr("disabled","disabled");
    $("#new-complaint-subject").val("");
    $("#new-complaint-content").attr("readonly","readonly");
    $("#new-complaint-content").val("");
    $("#new-complaint-reason").attr("disabled","disabled");
    $("#create-complaint-ftom-modal-btn").attr("disabled","disabled");
}

function openNewComplaintModal() {
    modalAlert = $("#new-complaint-modal-error-msg")
    modalAlert.empty();
    modalAlert.addClass("hidden");
    clearNewComplaintModalForm();

    reasons = $("#new-complaint-reason");
    reasons.attr("disabled","disabled");
    option = document.createElement("option");
    $.ajax({
        url: "/api/pmg/category/get/bytype/COMPLAIN_REASON/",
        success: function (data){
            if(data.status = "found") {
                data.categories.forEach(function (item, i) {
                    option = document.createElement("option");
                    if(i==0){
                        option.setAttribute("selected", "selected");
                    }
                    option.setAttribute("value", item.categoryId);
                    option.appendChild(document.createTextNode(item.categoryName));
                    reasons.append(option);
                });
            }
             else{
                modalAlert.html("<strong>Error! </strong> Complaint`s reasons not found");
                modalAlert.removeClass("hidden");
                clearNewComplaintModalForm();
            }
        },
        error: function (){
            modalAlert.html("<strong>Error! </strong> Internal server error!");
            modalAlert.removeClass("hidden");
            clearNewComplaintModalForm();
        }
    });
}
function createNewComplaintFromModal() {
    console.error($("#new-complaint-content").val());
    $.ajax({
        url: "/api/pmg/complaint/new/",
        method: 'POST',
        headers: {
            'X-CSRF-TOKEN': $('meta[name=_csrf]').attr("content")
        },
        contentType: 'application/json',
        data: JSON.stringify({
            userId:selectUserId,
            instanceId:$("#new-complaint-instanse").val(),
            reasonId:$("#new-complaint-reason").val(),
            title:$("#new-complaint-subject").val(),
            content:$("#new-complaint-content").val()


        }),
        success: function (data) {
            loadComplaints();
        },
        error: function () {
        }
    })
}

$(document).ready(function () {
    loadComplaints();
});
