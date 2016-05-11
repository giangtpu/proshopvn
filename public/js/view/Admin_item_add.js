/**
 * Created by giangbb on 10/05/2016.
 */
$(document).ready(function(){


    $('#updateitem').validate({

        messages: {
            name:{
                required:Messages("valid.require"),
                minlength:Messages("valid.minlength",5),
            },
            quantity:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            price_receipt:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
            },
            price_sell:{
                required:Messages("valid.require"),
                number:Messages("valid.number"),
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

});

function initDateTime(){
    $('.datetimepicker').datetimepicker({
        sideBySide:true,
        icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-chevron-up",
            down: "fa fa-chevron-down",
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-screenshot',
            clear: 'fa fa-trash',
            close: 'fa fa-remove',
            minDate:true,           //ky quai !!!!
        },
    });
    var today=new Date();
    $( "#datePromotionStart" ).data("DateTimePicker").date( today);
    $( "#datePromotionEnd" ).data("DateTimePicker").date( today);
    $( "#datePromotionStart" ).data("DateTimePicker").minDate( today);
    $( "#datePromotionEnd" ).data("DateTimePicker").minDate( today);

    $("#datePromotionStart").on("dp.change", function(e) {
        $( "#datePromotionEnd" ).data("DateTimePicker").date(e.date);
        $( "#datePromotionEnd" ).data("DateTimePicker").minDate(e.date);
    });
}

function setPromotion(){
    if($('#promotionSwitch').is(":checked")){
        //$('.discoutdiv').show();
        $('#promotion').val(true);
        var divappend="<div id='discoutdiv'>"+
                            "<div class='col-md-6'>"+
                                "<div class='form-group'>"+
                                    "<label for='discountRate'>"+Messages("Admin.Item.discountRate")+"</label>"+
                                    "<input type='text' style='display:none'>"+
                                    "<input name='discountRate' type='text'  required='true' number='true' class='form-control' placeholder='discountRate' autocomplete='off'>"+
                                "</div>"+
                            "</div>"+
                            "<div class='col-md-6'>"+
                                "<div class='form-group'>"+
                                    "<label for='datePromotionStart'>"+Messages("Admin.Item.datePromotionStart")+"</label>"+
                                    "<input id='datePromotionStart' required='true' name='datePromotionStart'  type='text' class='form-control datetimepicker'>"+
                                "</div>"+
                            "</div>"+
                            "<div class='col-md-6'>"+
                                "<div class='form-group'>"+
                                    "<label for='datePromotionEnd'>"+Messages("Admin.Item.datePromotionEnd")+"</label>"+
                                    "<input id='datePromotionEnd' required='true' name='datePromotionEnd' type='text' class='form-control datetimepicker'>"+
                                "</div>"+
                            "</div>"+
                      "</div>";
        $(divappend).appendTo("#promotiongroup");
        initDateTime();
    }else{
        //$('.discoutdiv').hide();
        $('#promotion').val(false);
        $("#discoutdiv").remove();
    }
}

$(function () {
    $("#wizard-picture").change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = imageIsLoaded;
            reader.readAsDataURL(this.files[0]);
        }
    });
});
function imageIsLoaded(e) {
    $('#wizardPicturePreview').attr('src', e.target.result);
};

var techid=0;
function appendTechSpecific(){

    var divappend="<div id='techSpecific"+techid+"'>"+
                      "<div class='col-md-6'>" +
                            "<div class='form-group'>" +
                                    "<label>"+Messages("Admin.Item.techSpecifics.key")+"</label>"+
                                    "<input id='techkey"+techid+"' name='techkey[]' class='form-control' placeholder='key' type='text' autocomplete='off'>"+
                            "</div>"+
                      "</div>"+
                      "<div class='col-md-6'>" +
                             "<div class='form-group'>" +
                                   "<label>"+Messages("Admin.Item.techSpecifics.value")+"</label>"+
                                   "<input id='techvalue"+techid+"' name='techvalue[]' class='form-control' placeholder='value' type='text' autocomplete='off'>"+
                             "</div>"+
                      "</div>"+
                  "</div>";
    $(divappend).appendTo("#techSpecifics");
    techid++;
}

function removelastTechSpecific(){

    if (techid>0){
        techid--;
    }

    var divremove="#techSpecific"+techid;
    $(divremove).remove();

}
