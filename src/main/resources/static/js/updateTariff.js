if (currentStatus === "ACTIVE") {
    document.getElementById("statusActive").checked =true;
} else {
    document.getElementById("statusDisabled").checked =true;
}

for (var i in list) {
    document.getElementById("service_" + list[i]).click();
}
