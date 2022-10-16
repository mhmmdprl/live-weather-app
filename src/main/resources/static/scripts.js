var stompClient = null;

$(document).ready(function () {
    getLiveWeather();
    connect();
    getSettingsAndList();
    $('#cities').on('change', function () {
        changeCity(this.value);
    });
    $('#langs').on('change', function () {
        changeLang(this.value);
        changeFieldLang(this.value)
    });
    $('#units').on('change', function () {
        changeUnits(this.value);
    });


});

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/topic/user-messages', function (message) {
            console.log(message);
            showMessage(JSON.parse(message.body));
        });
    });
}

function showMessage(message) {
    console.log(message)
    $("#title-id").text(message.city + "," + message.countryCode);
    $("#temp-id").text(message.temp + "°" + message.unitChar);
    $("#feel-temp-id").text(message.feelTemp + "°" + message.unitChar);
    $("#weather-desc-id").text(message.description);
    $("#date-id").text(message.date);
}


function getLiveWeather() {
    var webUrl = "http://localhost:8080/weather";

    $.ajax({
        type: "GET",
        url: webUrl,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            showMessage(msg.result);
        },
        error: function (e) {
            console.log(e.message);
        }
    });
}

function changeCity(city) {
    stompClient.send("/ws/user-message", {}, JSON.stringify({'key': 'city', 'value': city}));
}

function changeUnits(unit) {
    stompClient.send("/ws/user-message", {}, JSON.stringify({'key': 'units', 'value': unit}));
}

function changeLang(lang) {
    stompClient.send("/ws/user-message", {}, JSON.stringify({'key': 'lang', 'value': lang}));
}

function getSettingsAndList() {
    var webUrl = "http://localhost:8080/settings";

    $.ajax({
        type: "GET",
        url: webUrl,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (msg) {
            console.log(msg.result);
            fillAllList(msg.result);
        },
        error: function (e) {
            console.log(e.message);
        }
    });
}

function fillAllList(result) {
    $.each(result.cities, function (val, text) {
        $('#cities').append(
            $('<option></option>').val(text).html(text)
        );
    });

    $.each(result.languages, function (val, text) {
        $('#langs').append(
            $('<option></option>').val(text).html(getLangs(text))
        );
    });

    $.each(result.units, function (val, text) {
        $('#units').append(
            $('<option></option>').val(text).html(getUnits(text))
        );
    });
}

function getLangs(langCode) {
    var lang;
    switch (langCode) {
        case "TR" :
            lang = "TÜRKÇE";
            break;
        default :
            lang = "İNGİLİZCE";
            break;
    }

    return lang;
}

function getUnits(unitCode) {
    var unit;

    switch (unitCode) {
        case "IMPERIAL" :
            unit = "FAHRENHEIT";
            break;
        case "METRIC" :
            unit = "CELSIUS";
            break;
        default :
            unit = "KELVIN";
            break;
    }

    return unit;

}


function changeFieldLang(langCode) {
    var lang;
    switch (langCode) {
        case "TR" :
            $("#feels-like").text("Hissedilen : ");
            $("#city-tag").text("Şehir Seçiniz");
            $("#lang-tag").text("Dil Seçiniz");
            $("#unit-tag").text("Birim Seçiniz");
            break;
        case "EN" :
            $("#feels-like").text("Feels Like : ");
            $("#city-tag").text("Chose City");
            $("#lang-tag").text("Chose Language");
            $("#unit-tag").text("Chose Unit");
            break;
        case  "BG":
            $("#feels-like").text("Feels Like : ");
            $("#city-tag").text("Chose City");
            $("#lang-tag").text("Chose Language");
            $("#unit-tag").text("Chose Unit");
            break;
        default :
            $("#feels-like").text("Feels Like : ");
            $("#city-tag").text("Chose City");
            $("#lang-tag").text("Chose Language");
            $("#unit-tag").text("Chose Unit");
            break;
    }

    return lang;
}