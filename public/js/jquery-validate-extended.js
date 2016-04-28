/**
 * Created by giangbb on 24/12/2015.
 */
jQuery.validator.addMethod('emailmobileVN', function(value, element) {


    isEmail=this.optional( element ) || /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test( value );

    phone_number = value.replace(/\(|\)|\s+|-/g,'');
    isPhone =this.optional(element) || phone_number.length > 9 && phone_number.match(/^0\d{9,10}$/);


    return isPhone || isEmail;
}, Messages("valid.emailmobileVN"));


jQuery.validator.addMethod('mobileVN', function(phone_number, element) {
    phone_number = phone_number.replace(/\(|\)|\s+|-/g,'');
    return this.optional(element) || phone_number.length > 6 &&
        phone_number.match(/^0\d{9,10}$/);
}, Messages("valid.mobileVN"));