/**
 * Created by giangdaika on 26/04/2016.
 */
$(document).ready(function () {
    $('#loginform').validate({
        messages: {
            email: {
                required: Messages("valid.require"),
                email: Messages("valid.email"),
            },
            password: {
                required: Messages("valid.require"),
            }
        }
    });
});