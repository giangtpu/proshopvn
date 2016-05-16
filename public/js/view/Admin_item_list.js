/**
 * Created by giangdaika on 09/05/2016.
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

var r_delItem=jsRoutes.controllers.ItemController.deleteItem();
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

