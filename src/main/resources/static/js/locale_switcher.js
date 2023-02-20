function setSelectBoxByText(eid, etxt) {
    var eid = document.getElementById(eid);
    for (var i = 0; i < eid.options.length; ++i) {
        if (eid.options[i].value === etxt)
            eid.options[i].selected = true;
    }
}
console.log(currentLocale);
setSelectBoxByText("locales", currentLocale);

$(document).ready(function() {
    $("#locales").change(function () {
        var selectedOption = $('#locales').val();
        if (selectedOption !== ''){
            window.location.replace('?lang=' + selectedOption);
        }
    });
});