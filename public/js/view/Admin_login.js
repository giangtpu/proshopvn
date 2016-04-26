/**
 * Created by giangdaika on 26/04/2016.
 */
$(document).ready(function () {
    $('#loginform').validate({
        rules: {
            email: {
                required: true,
                email: true,
            },
            password: {
                required: true,
            }
        },
        messages: {
            email: {
                required: Messages("valid.require"),
                email: Messages("valid.email"),
            },
            password: {
                required: Messages("valid.require"),
            }
        },
        highlight: function (element) {
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
        }
    });
});