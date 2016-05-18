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
});


function setdelid(id){
    $('#deleteconfirm input[name=id]').val(id);
}
