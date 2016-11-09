// Globale Variablen
var g_mapHandler, g_webserviceURL, g_dataType;
var IN_LOCAL_MODE = false;

window.onload = function () {
    console.log("Kartenprogramm wird gestartet ...");

    // URL bestimmen
    if (IN_LOCAL_MODE) {
        g_webserviceURL = "http://localhost:8080/v1";
    } else {
        // g_webserviceURL = "http://141.64.5.203:44445/v1";
        g_webserviceURL = "http://dbl43.beuth-hochschule.de/excell-heatmap-api/v1";
    }
    // Initialen Datentyp festlegen
    g_dataType = "density";

    // Karte anlegen (mit initialen Werten)
    g_mapHandler = new MapHandler("map");
    g_mapHandler.initializeMap(51.05924, 13.73958, 12);

    // Webservice initialisieren
    var wsHandler = new WebserviceHandler();
    wsHandler.startRequest(2014,12,24,18);

    // Controller für User-Interaktionen
    var controller = new HTMLController(wsHandler);

    // Spinner als Ladeanimation einbinden
    $("#spinner").bind("ajaxSend", function() {
        $(this).show();
    }).bind("ajaxStop", function() {
        $(this).hide();
    }).bind("ajaxError", function () {
        $(this).hide();
    });

    console.log("Kartenprogramm geladen.");
};