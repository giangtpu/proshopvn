/**
 * Created by giangdaika on 05/05/2016.
 */
$(document).ready(function(){
    $('#updatecategory').validate({

        messages: {
            name:{
                required:Messages("valid.require"),
                minlength:Messages("valid.minlength",5),
            }

        }
    });
});

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

function setIsItemCategory(){
    if($('#isItemCategorySwitch').is(":checked")){
        $('#isItemCategory').val(true);
    }else{
        $('#isItemCategory').val(false);
    }
}