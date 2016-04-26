function showNotification(from, align,petype,pecontent,peicon){
    $.notifyClose();
    var notify = $.notify({
        icon: peicon,
        message: pecontent

    },{
        type: petype,
        timer: 100,
        delay:1500,
        allow_dismiss: true,
        newest_on_top: true,
        allow_duplicates: true,
        placement: {
            from: from,
            align: align
        }
    });
}


function showNotification_slow(from, align,petype,pecontent,peicon){
    $.notifyClose();
    var notify = $.notify({
        icon: peicon,
        message: pecontent

    },{
        type: petype,
        timer: 100,
        delay:5000,
        allow_dismiss: true,
        newest_on_top: true,
        allow_duplicates: true,
        placement: {
            from: from,
            align: align
        }
    });
}