/**
 * Created by giangbb on 10/05/2016.
 */
$(document).ready(function(){
    $('.datetimepicker').datetimepicker({
        icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-chevron-up",
            down: "fa fa-chevron-down",
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-screenshot',
            clear: 'fa fa-trash',
            close: 'fa fa-remove'
        },

    });

});

function setPromotion(){
    if($('#promotionSwitch').is(":checked")){
        $('.discoutdiv').show();
        $('#promotion').val(true);
    }else{
        $('.discoutdiv').hide();
        $('#promotion').val(false);
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