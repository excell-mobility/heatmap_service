var WebserviceHandler = function() {};

WebserviceHandler.prototype.startRequest = function(year, month, day, hour, resolution) {
    if (resolution == undefined) {resolution = 0;}
    // URL kombinieren mit Zeit und Datum
    const url = g_webserviceURL + "/" + year + "/" + month + "/" + day + "/" + hour + "/" + resolution;
    // Anfrage an Server schicken
    $.ajax({
        url: url,
        type: "GET",
        dataType: 'json',
        crossDomain: true,
        success: this.onSuccess,
        error: this.onError
    });
};

WebserviceHandler.prototype.onSuccess = function(data, status, request) {
    // kurzes Speichern der aktuellen Kartenansicht
    const zoom = g_mapHandler.map._zoom;
    const latlng = g_mapHandler.map._lastCenter;

    // Fehler abfangen
    if (latlng == null) {
        alert("Fehler beim Verarbeiten der Daten, bitte erneut versuchen.");
        return;
    }

    // Karte neu initialisieren mit der gespeicherten Ansicht
    g_mapHandler.map.remove();
    g_mapHandler.initializeMap(latlng.lat, latlng.lng, zoom);

    if (g_dataType == "density") {
        var sumValues = 0;
        for (var i = 0; i < data.length; i++) {
            sumValues += data[i]["vehicle_count"];
        }

        for (i = 0; i < data.length; i++) {
            var value = data[i]["vehicle_count"];
            var density = value / sumValues;
            var lat1 = data[i]["lat1"];
            var lat2 = data[i]["lat2"];
            var lng1 = data[i]["lng1"];
            var lng2 = data[i]["lng2"];
            var colorCode = g_mapHandler.getDensityColorCode(density * 2000);
            g_mapHandler.paintDensityRectangle(lat1, lng1, lat2, lng2, colorCode, density * 100);
        }
    } else if (g_dataType == "velocity") {
        for (i = 0; i < data.length; i++) {
            var velo = data[i]["average_velocity"];
            var lat1 = data[i]["lat1"];
            var lat2 = data[i]["lat2"];
            var lng1 = data[i]["lng1"];
            var lng2 = data[i]["lng2"];
            var colorCode = g_mapHandler.getSpeedColorCode(velo);

            g_mapHandler.paintVelocityRectangle(lat1, lng1, lat2, lng2, colorCode, velo);
        }
    }
};

WebserviceHandler.prototype.onError = function(request, status, error) {
    // Bei Fehler nur den Fehler ausgeben und sonst nichts tun.
    console.log(status);
    console.log(request);
    console.log(error);
    alert("Daten konnten nicht geladen werden.");
};
