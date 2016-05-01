/**
 * Created by giangdaika on 29/04/2016.
 */
$(document).ready(function(){
    $('#updateuser').validate({
        rules: {
            phone: {
                mobileVN: true,
            }
        },
        messages: {
            username:{
                required:Messages("valid.require"),
                minlength:Messages("valid.minlength",5),
            },
            email:{
                required:Messages("valid.require"),
                email:Messages("valid.email"),
            },
            password:{
                minlength:Messages("valid.minlength",5),
            },
            repeatPassword:{
                equalTo:Messages("valid.repeatpass"),
            },
            phone: {
                mobileVN: Messages("valid.mobileVN"),

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


