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
            description_id:{
                required:Messages("valid.require"),
            },

        }
    });

    $('#summernote').summernote({
        height: 400,                 // set editor height
        minHeight: null,             // set minimum height of editor
        maxHeight: null,             // set maximum height of editor
        lang: 'vi-VN',
        dialogsInBody: true,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold','italic' ,'underline', 'strikethrough','clear']],
            ['fontname', ['fontname']],
            ['group2', ['fontsize']],
            ['height', ['height']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ],
        callbacks: {
            onImageUpload: function(files) {
                summernote_loadnewfile=true;
                sendFile(files[0]);
            }
        }
    });

    //console.log(summernotecode);
    $('#summernote').summernote('code', $.parseHTML( summernotecode ));

    $(window).bind('beforeunload', function(){
        //alert("bye");
        if (summernote_loadnewfile){
            if (!window.submit_clicked){
                return 'Are you sure?';
            }
        }


    });
    $(window).on('unload', function(){
        if (summernote_loadnewfile){
            if (!window.submit_clicked){
                deleteDescripFilePrefix();
            }
        }


    });
});

////////////////////////////////////UPLOAD IMAGE////////////////////////////////////////
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
////////////////////////////////////UPLOAD IMAGE////////////////////////////////////////

/////////////////////////////PROMOTION/////////////////////////////////
function initDateTime(){
    $('.datetimepicker').datetimepicker({
        format:"DD/MM/YYYY HH:mm",
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

    if(!promo_init){
        var today=new Date();
        $( "#datePromotionStart" ).data("DateTimePicker").date( today);
        $( "#datePromotionEnd" ).data("DateTimePicker").date( today);
        $( "#datePromotionStart" ).data("DateTimePicker").minDate( today);
        $( "#datePromotionEnd" ).data("DateTimePicker").minDate( today);


    }
    else{
        $( "#datePromotionStart" ).data("DateTimePicker").date( startpromot_int);
        $( "#datePromotionEnd" ).data("DateTimePicker").date( endpromot_int);
        $( "#datePromotionStart" ).data("DateTimePicker").minDate( startpromot_int);
        $( "#datePromotionEnd" ).data("DateTimePicker").minDate( endpromot_int);
        //promo_init=false;
    }

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
            "<input name='discountRate' id='discountRate' type='text'  required='true' number='true' class='form-control' placeholder='discountRate' autocomplete='off'>"+
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
        $( "#discountRate").val(discountRate_init);
        initDateTime();
    }else{
        //$('.discoutdiv').hide();
        $('#promotion').val(false);
        $("#discoutdiv").remove();
    }
}
/////////////////////////////PROMOTION/////////////////////////////////



////////////////////////////TECHSPECIFIC//////////////////////////////////////////////////
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
////////////////////////////TECHSPECIFIC//////////////////////////////////////////////////

////////////////////////////DESCRIPTION//////////////////////////////////////////////////

var descrip_img=[];
var r_sendFile=jsRoutes.controllers.ItemController.saveitemImageDescription();
function sendFile(file) {
    var description_id=$("#description_id").val();
    var validator = $( "#updateitem" ).validate();
    if (!validator.element("#description_id")){
        alert("description_id null");
        return;
    }

    data = new FormData();
    data.append("image", file);
    data.append("description_id", description_id);
    $.ajax({
        url: r_sendFile.url,
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        type: r_sendFile.type,
        success: function(data){
            console.log(data);
            if (data.success){
                descrip_img.push(data.filename);
                $('#summernote').summernote('insertImage', data.url, data.filename);
            }

        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus+" "+errorThrown);
        }
    });
}

var r_deleteDescripFilePrefix=jsRoutes.controllers.ItemController.deleteDescripFilePrefix();
function deleteDescripFilePrefix(){
    var description_id=$("#description_id").val();
    data = new FormData();
    data.append("description_id", description_id);
    $.ajax({
        url: r_deleteDescripFilePrefix.url,
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        async : false,
        type: r_deleteDescripFilePrefix.type,
        success: function(data){
            //console.log(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus+" "+errorThrown);
        }
    });

}


var r_deleteDescripFile=jsRoutes.controllers.ItemController.deleteDescripFile();
function deleteDescripFile(fileNameToDel){
    data = new FormData();
    data.append("fileNameToDel", fileNameToDel);
    $.ajax({
        url: r_deleteDescripFile.url,
        data: data,
        cache: false,
        contentType: false,
        async : false,
        processData: false,
        type: r_deleteDescripFile.type,
        success: function(data){
            //console.log(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(textStatus+" "+errorThrown);
        }
    });

}


$("#submit").click(function(){
    var validator = $( "#updateitem" ).validate();
    if (validator.form()){
        var summernote_code = $('#summernote').summernote('code');
        for (i=0;i<descrip_img.length;i++){
            if (summernote_code.search(descrip_img[i])==-1){
                //xoa no di
                deleteDescripFile(descrip_img[i])
            }
        }
        $("#description").val(summernote_code);

        window.submit_clicked = true;
    }
});

////////////////////////////DESCRIPTION//////////////////////////////////////////////////
