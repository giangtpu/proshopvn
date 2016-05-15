

var current_array_list=[];
$().ready(function(){

    });
function InitValue(){
    for(i=0; i<array_list.length ; i++){
        var element = $('#' + array_list[i]);
        if(current_array_list !=null){
            //console.log(current_array_list.indexOf(array_list[i]));
            if(current_array_list.indexOf(array_list[i]) != -1){
                if( element.is(':hidden')) {
                    element.show();
                }
            }else{
                element.hideAndReset();
            }
            //console.log(current_array_list.indexOf( array_list[i]));
        }else{
            element.hideAndReset();
        }
    }
}

//Add filter event handler
function  ChangeFilterValue(value){
    if(current_array_list.indexOf(value) == -1) {
        current_array_list.push(value);
            var element = $('#' + value);
            if (element.is(':hidden')) {
                element.show();
            }
    }
    $('#ChooseFilter').val(current_array_list).change();
    //console.log( $('#ChooseFilter').val());
}

//Click on tag a to hide field div
$('.filedHide').click(function() {

    var searchFieldId =(($(this).parent().parent().parent()).attr('id'));
    //console.log($(this).parent().parent().parent());
    var indexOfField =current_array_list.indexOf(searchFieldId);
    //console.log(current_array_list);
    if( indexOfField !=-1){
        current_array_list.splice(indexOfField,1);
    }
    //console.log(current_array_list);
    //$('#ChooseFilter').val(current_array_list).change();
    $(this).parent().parent().parent().hideAndReset();
    $('#ChooseFilter').val(current_array_list).change();
    //console.log( $('#ChooseFilter').val());
});

//Display date to div when option is between
$('.DateOption').change(function() {
    if($(this).val() == "between") {
       if( ($(this).closest('.search-field').find('.search-field-date-to')).is(':hidden')) {
            $(this).closest('.search-field').find('.search-field-date-to').show();
       }
    }else{
        if( !($(this).closest('.search-field').find('.search-field-date-to')).is(':hidden')) {
            $(this).closest('.search-field').find('.search-field-date-to').hideAndReset();
        }
    }
});

//Awesome function bind hide event to customer event
(function($) {

    $.fn.resetValue = function() {
        this.each( function() {
            $(this).find('.conditionTextValue').val('');
            $(this).find('.conditionIntValue').val('');
            $(this).find('.conditionBooleanValue').val("any").change();
            $(this).find('.conditionHourValue').val('00:').change();
            $(this).find('.conditionMinuteValue').val('00').change();
            //$(this).find('.conditionDateValue').val(nowString);
            $(this).find('.conditionDateValue').val('');
            $(this).find('.conditionOptionValue').val("=").change();
            $(this).find('.conditionDateOptionValue').val("between").change();
            $(this).find('.conditionObjNameServerLogValue').val("any").change();
            $(this).find('.conditionActionServerLogValue').val("any").change();
        });
    };

    $.fn.hideAndReset = function() {
        this.each( function() {
            $(this).resetValue();
            $(this).hide();
        });
    };
}(jQuery));
//Awesome function bind hide event to customer event- end

