/**
 * Created by giangdaika on 16/05/2016.
 */

var $table = $('#fresh-table');
$().ready(function(){
    $table.bootstrapTable({
        toolbar: ".toolbar",
        showRefresh: false,
        search: true,
        showToggle: true,
        showColumns: true,
        pagination: false,
        searchAlign: 'left',
        pageSize: 100,
        clickToSelect: false,
        pageList: [8,10,25,50,100],

        formatShowingRows: function(pageFrom, pageTo, totalRows){
            //do nothing here, we don't want to show the text "showing x of y from..."
        },
        formatRecordsPerPage: function(pageNumber){
            return pageNumber + " rows visible";
        },
        icons: {
            refresh: 'fa fa-refresh',
            toggle: 'fa fa-th-list',
            columns: 'fa fa-columns',
            detailOpen: 'fa fa-plus-circle',
            detailClose: 'fa fa-minus-circle'
        }
    });

    //activate the tooltips after the data table is initialized
    $('[rel="tooltip"]').tooltip();

    $(window).resize(function () {
        $table.bootstrapTable('resetView');
    });
});

function itemModel(){
    id="";
}

var r_delRecieptIssue=jsRoutes.controllers.RecieptIssueController.deleteRecieptIssue();
function delRecieptIssue(){
    var item=new itemModel();
    item.id=$('#deleteconfirm input[name=id]').val();
    $.ajax({
        url: r_delRecieptIssue.url,
        dataType: 'json',
        data: JSON.stringify(item),
        contentType: "application/json; charset=utf-8",
        type: r_delRecieptIssue.type,
        success: function(data){
            console.log(data);
            if(data.success){
                showNotification('top','center','success',Messages("Admin.deletesuccess"),'pe-7s-like2');
                $("#fresh-table").bootstrapTable("remove", {field:"id", values:[data.id]});

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus+" "+errorThrown);
        }
    });
}


function setdelid(id){
    $('#deleteconfirm input[name=id]').val(id);
}

function editRecieptIssue(id,item_id,item_name,type){
    if(type===1){       //nhap kho
        $('#editRecieptIssueModal #myModalLabel').text(Messages("Admin.Item.reciept"));
    }
    if(type===2){       //xuat kho
        $('#editRecieptIssueModal #myModalLabel').text(Messages("Admin.Item.issue"));
    }

    $('#editRecieptIssueForm input[name=id]').val(id);
    $('#editRecieptIssueForm input[name=type]').val(type);
    $('#editRecieptIssueForm input[name=item_id]').val(item_id);
    $('#editRecieptIssueForm input[name=item_name]').val(item_name);



    var row= $("#fresh-table").bootstrapTable('getRowByUniqueId', id);
    $('#editRecieptIssueForm input[name=quantity]').val(row.quantity);
    $('#editRecieptIssueForm input[name=price]').val(row.price);
    $('#editRecieptIssueForm input[name=datePurchase]').data("DateTimePicker").date( row.datePurchase);
    $('#editRecieptIssueForm textarea[name=description]').val(row.description);




}

var r_editRecieptIssueForm=jsRoutes.controllers.RecieptIssueController.editRecieptIssue();
$("#editRecieptIssueForm").submit(function(e) {
    $.ajax({
        type: r_editRecieptIssueForm.type,
        url: r_editRecieptIssueForm.url,
        data: $("#editRecieptIssueForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#editRecieptIssueModal").modal('hide');
            if(data.success){
                var positionTable=$("#fresh-table").bootstrapTable("findrow", {field:"id", values:[data.id]});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"quantity", fieldValue:data.quantity});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"price", fieldValue:data.price});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"datePurchase", fieldValue:data.datePurchase});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"description", fieldValue:data.description});
                showNotification('top','center','success',Messages("Admin.Updatesuccess"),'pe-7s-like2');

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }


        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});
