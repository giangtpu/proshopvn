/**
 * Created by giangdaika on 16/05/2016.
 */

$().ready(function(){
    $('#addIssueForm').validate({
        messages: {
            item_id:{
                required:Messages("valid.require"),
            },
            item_name:{
                required:Messages("valid.require"),
            },
            quantity:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            price:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            datePurchase:{
                required:Messages("valid.require"),
            },
        }
    });
    $('#addRecieptForm').validate({
        messages: {
            item_id:{
                required:Messages("valid.require"),
            },
            item_name:{
                required:Messages("valid.require"),
            },
            quantity:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            price:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            datePurchase:{
                required:Messages("valid.require"),
            },
        }
    });
});


function initReceipt(id,name){
    $('#addRecieptForm input[name=item_id]').val(id);
    $('#addRecieptForm input[name=item_name]').val(name);
    $('#addRecieptForm input[name=quantity]').val('');
    $('#addRecieptForm input[name=price]').val('');
    $('#addRecieptForm input[name=datePurchase]').val('');
    $('#addRecieptForm textarea[name=description]').val('');
}

function initIssue(id,name){
    $('#addIssueForm input[name=item_id]').val(id);
    $('#addIssueForm input[name=item_name]').val(name);
    $('#addIssueForm input[name=quantity]').val('');
    $('#addIssueForm input[name=price]').val('');
    $('#addIssueForm input[name=datePurchase]').val('');
    $('#addIssueForm textarea[name=description]').val('');
}

var r_addRecieptForm=jsRoutes.controllers.RecieptIssueController.addReciept();
$("#addRecieptForm").submit(function(e) {
    $.ajax({
        type: r_addRecieptForm.type,
        url: r_addRecieptForm.url,
        data: $("#addRecieptForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#addReciept").modal('hide');
            if(data.success){
                var positionTable=$("#fresh-table").bootstrapTable("findrow", {field:"id", values:[data.item_id]});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"quantity", fieldValue:data.quantity});
                showNotification('top','center','success',Messages("Admin.reciept.addsuccess"),'pe-7s-like2');

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }


        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});

var r_addIssueForm=jsRoutes.controllers.RecieptIssueController.addIssue();
$("#addIssueForm").submit(function(e) {
    $.ajax({
        type: r_addIssueForm.type,
        url: r_addIssueForm.url,
        data: $("#addIssueForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#addIssue").modal('hide');
            if(data.success){
                var positionTable=$("#fresh-table").bootstrapTable("findrow", {field:"id", values:[data.item_id]});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"quantity", fieldValue:data.quantity});
                showNotification('top','center','success',Messages("Admin.issue.addsuccess"),'pe-7s-like2');

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }


        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});
