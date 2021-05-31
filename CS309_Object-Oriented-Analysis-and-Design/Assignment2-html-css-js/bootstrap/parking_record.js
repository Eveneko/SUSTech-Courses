function onClickAddRecord() {
    let license_plate_number = document.querySelector('form input[name="license_plate_number"]').value;
    let entrance_number = document.querySelector('form input[name="entrance_number"]').value;
    let staff_name = document.querySelector('form input[name="staff_name"]').value;
    let staff_number = document.querySelector('form input[name="staff_number"]').value;
    if (validateInput(license_plate_number, entrance_number, staff_name, staff_number)) {
        addrecord();
    }
}

function validateInput(license_plate_number, entrance_number, staff_name, staff_number) {
    let license_plate_number_re5 = new RegExp(/^[A-Z0-9]{5}$/);
    let license_plate_number_re6 = new RegExp(/^[DF][A-Z0-9]{5}$/);
    if (!license_plate_number_re5.test(license_plate_number) && !license_plate_number_re6.test(license_plate_number)){
        alert("Invalid License Plate Number!");
        return false;
    }
    if (entrance_number < 1 || entrance_number > 7){
        alert("Invalid Entrance Number!");
        return false;
    }
    if (staff_name.length == 0){
        alert("Invalid Staff Name!");
        return false;
    }
    let staff_number_re = new RegExp(/^[35][0-9]{7}/);
    if (!staff_number_re.test(staff_number)){
        alert("Invalid Staff Number!");
        return false;
    }
    return true;
}

function initial() {
    setYear();
    setdHour();
    setdMinute();
    setaHour();
    setaMinute();
}

function setYear() {
    let year = document.getElementById("year");
    year.innerHTML = "";
    year.options.add(new Option("--", null));
    for (let i = 2000; i <= 2020; i++) {
        year.options.add(new Option(i, i));
    }
}

function setMonth() {
    let month = document.getElementById("month");
    month.innerHTML = "";
    month.options.add(new Option("--", null));
    for (let i = 1; i <= 12; i++) {
        month.options.add(new Option(i, i));
    }
}

function setDay() {
    let year = document.getElementById("year").value;
    let month = document.getElementById("month").value;
    let day = document.getElementById("day");
    let data = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
// clear the items
    day.innerHTML = "";
// add new items
    day.options.add(new Option("--", null));
    for (let i = 1; i <= data[month - 1]; i++) {
        day.options.add(new Option(i, i));
    }
    if (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) && month == 2) {
        day.options.add(new Option(29, 29));
    }
}

function setdHour() {
    let ahour = document.getElementById("dhour");
    ahour.innerHTML = "";
    ahour.options.add(new Option("--", null));
    for (let i = 0; i <= 23; i++) {
        let t = i;
        t = t > 9 ? t : '0' + t;
        ahour.options.add(new Option(t, t));
    }
}

function setdMinute() {
    let aminute = document.getElementById("dminute");
    aminute.innerHTML = "";
    aminute.options.add(new Option("--", null));
    for (let i = 0; i <= 59; i++) {
        let t = i;
        t = t > 9 ? t : '0' + t;
        aminute.options.add(new Option(t, t));
    }
}

function setaHour() {
    let ahour = document.getElementById("ahour");
    ahour.innerHTML = "";
    ahour.options.add(new Option("--", null));
    for (let i = 0; i <= 23; i++) {
        let t = i;
        t = t > 9 ? t : '0' + t;
        ahour.options.add(new Option(t, t));
    }
}

function setaMinute() {
    let aminute = document.getElementById("aminute");
    aminute.innerHTML = "";
    aminute.options.add(new Option("--", null));
    for (let i = 0; i <= 59; i++) {
        let t = i;
        t = t > 9 ? t : '0' + t;
        aminute.options.add(new Option(t, t));
    }
}

function addrecord() {
    let record = document.getElementById("record");
    if (record == null) {
        alert("Record of Table not Exist!");
        return;
    }
    let year = document.getElementById("year").value;
    let month = document.getElementById("month").value;
    let day = document.getElementById("day").value;
    let dhour = document.getElementById("dhour").value;
    let dminute = document.getElementById("dminute").value;
    let ahour = document.getElementById("ahour").value;
    let aminute = document.getElementById("aminute").value;
    let radios = document.getElementsByName("status");
    let status_checked0 = radios[0].checked;
    let status_checked1 = radios[1].checked;
    if (year == "null" || month == "null" || day == "null")
    {
        alert("Incomplete Data!");
        return;
    }
    if (!status_checked0 && !status_checked1)
    {
        alert("Incomplete Status!");
        return;
    }
    if (ahour == "null" || aminute == "null")
    {
        alert("Incomplete Arrival Time!");
        return;
    }
    if (status_checked1 && (dminute == "null" || dhour == "null"))
    {
        alert("Incomplete Departure Time!");
        return;
    }
    if(status_checked1 && (dhour < ahour || (dhour == ahour && dminute < aminute))){
        alert("Departure Time must be later than Arrival Time!");
        return;
    }
    let rowCount = record.rows.length;
    let cellCount = record.rows[0].cells.length;
    let st = "粤B" + document.forms[0]["license_plate_number"].value + year + "/" + month + "/" + day + ahour + ":" + aminute;
    for (let i = 0; i< rowCount; i++)
    {
        let tmp ="";
        tmp += record.rows[i].cells[0].innerHTML;
        tmp += record.rows[i].cells[2].innerHTML;
        tmp += record.rows[i].cells[3].innerHTML;
        if (st == tmp)
        {
            alert("Line " + i +" has a duplicate record!");
            return;
        }
    }
    record.style.display = ""; // display the record
    let newRow = record.insertRow(rowCount++);
    newRow.insertCell(0).innerHTML = "粤B" + document.forms[0]["license_plate_number"].value;
    newRow.insertCell(1).innerHTML = document.forms[0]["entrance_number"].value;
    newRow.insertCell(2).innerHTML = year + "/" + month + "/" + day;
    newRow.insertCell(3).innerHTML = ahour + ":" + aminute;
    newRow.insertCell(4).innerHTML = dhour + ":" + dminute;
    newRow.insertCell(5).innerHTML = document.forms[0]["staff_name"].value;
    newRow.insertCell(6).innerHTML = document.forms[0]["staff_number"].value;
    if (status_checked0) {
        newRow.insertCell(7).innerHTML = "In";
        record.rows[rowCount - 1].cells[4].innerHTML = "--";
    } else newRow.insertCell(7).innerHTML = "Out";
    newRow.insertCell(8).innerHTML = record.rows[0].cells[cellCount - 1].innerHTML; // copy the "delete" button
    record.rows[0].style.display = "none"; // hide first row
    alert("Success add a record!");
}

function showaddrecord() {
    let divObj = document.getElementById("div1");
    divObj.style.display = "block";
    // let buttonObj = document.getElementById("showbutton");
    // buttonObj.style.display = "";
}

function closeaddrecord() {
    let divObj = document.getElementById("div1");
    divObj.style.display = "none";
    // let buttonObj = document.getElementById("showbutton");
    // buttonObj.style.display = "block";
}

function removeRow(inputobj) {
    if (inputobj == null) return;
    let parentTD = inputobj.parentNode;
    let parentTR = parentTD.parentNode;
    let parentTBODY = parentTR.parentNode;
    parentTBODY.removeChild(parentTR);
}