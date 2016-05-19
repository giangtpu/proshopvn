/**
 * Created by giangdaika on 18/05/2016.
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

    $('#editPromotionForm').validate({
        messages: {
            id:{
                required:Messages("valid.require"),
            },
            item_id:{
                required:Messages("valid.require"),
            },
            item_name:{
                required:Messages("valid.require"),
            },
            category_id:{
                required:Messages("valid.require"),
            },
            discountRate:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            datePromotionStart:{
                required:Messages("valid.require"),
            },
            datePromotionEnd:{
                required:Messages("valid.require"),
            },
        }
    });

    $('#editPromotionForm #datePromotionStart').on("dp.change", function(e) {
        $('#editPromotionForm #datePromotionEnd').data("DateTimePicker").minDate(e.date);
    });


});


function itemModel(){
    id="";
}

var r_delItem=jsRoutes.controllers.PromotionController.delPromotion();
function delItem(){
    var item=new itemModel();
    item.id=$('#deleteconfirm input[name=id]').val();
    $.ajax({
        url: r_delItem.url,
        dataType: 'json',
        data: JSON.stringify(item),
        contentType: "application/json; charset=utf-8",
        type: r_delItem.type,
        success: function(data){
            console.log(data);
            if(data.success){
                showNotification('top','center','success',Messages("Admin.deletesuccess"),'pe-7s-like2');
                $("#fresh-table").bootstrapTable("remove", {field:"id", values:[data.id]});

            }else{
                showNotification('top','center','danger',Messages("Admin.deletefailed"),'pe-7s-close-circle');
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

////////////////////////////////EDIT PROMOTION
function setPromotionid(id,item_id,category_id,item_name){
    $('#editPromotionForm input[name=id]').val(id);
    $('#editPromotionForm input[name=item_id]').val(item_id);
    $('#editPromotionForm input[name=category_id]').val(category_id);
    $('#editPromotionForm input[name=item_name]').val(item_name);


    var row= $("#fresh-table").bootstrapTable('getRowByUniqueId', id);

    $('#editPromotionForm input[name=discountRate]').val(row.discountRate);
    $('#editPromotionForm #datePromotionStart').data("DateTimePicker").date( row.datePromotionStart);
    $('#editPromotionForm #datePromotionEnd').data("DateTimePicker").date( row.datePromotionEnd);



}

var r_editPromotion=jsRoutes.controllers.PromotionController.editPromotion();
$("#editPromotionForm").submit(function(e) {
    $.ajax({
        type: r_editPromotion.type,
        url: r_editPromotion.url,
        data: $("#editPromotionForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#promotionModal").modal('hide');
            if(data.success){


                var positionTable=$("#fresh-table").bootstrapTable("findrow", {field:"id", values:[data.id]});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"discountRate", fieldValue:data.discountRate});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"datePromotionStart", fieldValue:data.datePromotionStart});
                $("#fresh-table").bootstrapTable("updateCell", {rowIndex:positionTable,fieldName:"datePromotionEnd", fieldValue:data.datePromotionEnd});


                showNotification('top','center','success',Messages("Admin.reciept.Editpromotionsuccess"),'pe-7s-like2');

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }


        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});