/**
 * Created by giangdaika on 18/05/2016.
 */

$().ready(function(){
    $('#addPromotionForm').validate({
        messages: {
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



});

function setPromotionid(id,name,category_id){
    $('#addPromotionForm input[name=item_id]').val(id);
    $('#addPromotionForm input[name=item_name]').val(name);
    $('#addPromotionForm input[name=category_id]').val(category_id);
    $('#addPromotionForm input[name=discountRate]').val('');
    //$('#addPromotionForm input[name=datePromotionStart]').val('');
    //$('#addPromotionForm input[name=datePromotionEnd]').val('');

    var today=new Date();
    $( "#datePromotionStart" ).data("DateTimePicker").date( today);
    $( "#datePromotionEnd" ).data("DateTimePicker").date( today);

    $("#datePromotionStart").on("dp.change", function(e) {
        $( "#datePromotionEnd" ).data("DateTimePicker").date(e.date);
        $( "#datePromotionEnd" ).data("DateTimePicker").minDate(e.date);
    });
}

var r_addPromotionForm=jsRoutes.controllers.PromotionController.addPromotion();
$("#addPromotionForm").submit(function(e) {
    $.ajax({
        type: r_addPromotionForm.type,
        url: r_addPromotionForm.url,
        data: $("#addPromotionForm").serialize(), // serializes the form's elements.
        success: function(data)
        {
            $("#promotionModal").modal('hide');
            if(data.success){

                showNotification('top','center','success',Messages("Admin.reciept.Addpromotionsuccess"),'pe-7s-like2');

            }else{
                showNotification('top','center','danger',data.errorMessage,'pe-7s-close-circle');
            }


        }
    });
    e.preventDefault(); // avoid to execute the actual submit of the form.
});

