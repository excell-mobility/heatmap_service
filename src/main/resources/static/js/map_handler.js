/**
 * Created by rhintho on 26.10.16.
 */
var MapHandler = function (mapid) {
    this.mapid = mapid;
};

MapHandler.prototype.initializeMap = function (centerLat, centerLng, zoom) {
    console.log(centerLat + " / " + centerLng + " / " + zoom);
    this.map = L.map(this.mapid).setView([centerLat, centerLng], zoom);

    L.tileLayer('http://tile.stamen.com/toner/{z}/{x}/{y}.png', {
        attribution: 'Map tiles by <a href="http://stamen.com">Stamen Design</a>, under <a href="http://creativecommons.org/licenses/by/3.0">CC BY 3.0</a>. Data by <a href="http://openstreetmap.org">OpenStreetMap</a>, under <a href="http://www.openstreetmap.org/copyright">ODbL</a>.',
        maxZoom: 18
    }).addTo(this.map);

    L.control.sidebar('sidebar').addTo(this.map);
};

MapHandler.prototype.paintDensityRectangle = function(x1, y1, x2, y2, color, density) {
    var bounds = [[x1, y1], [x2, y2]];
    var tooltip = density.toFixed(3) + " %";
    L.rectangle(bounds, {color: color, weight: 0.0, fillOpacity: 0.5}).bindTooltip(tooltip).addTo(this.map);
};

MapHandler.prototype.paintVelocityRectangle = function(x1, y1, x2, y2, color, velo) {
    var bounds = [[x1, y1], [x2, y2]];
    var tooltip = velo.toFixed(0) + " km/h";
    L.rectangle(bounds, {color: color, weight: 0.0, fillOpacity: 0.5}).bindTooltip(tooltip).addTo(this.map);
};

MapHandler.prototype.getDensityColorCode = function(density) {
    // Color-Codes definieren
    const level5 = "#FF0000";
    const level4 = "#EE5500";
    const level3 = "#BB6600";
    const level2 = "#777700";
    const level1 = "#225500";
    const level0 = "#222222";

    // Abgleich und entsprechenden Color Code zurückschicken
    if (density > 8) {
        return level5;
    } else if (density > 4) {
        return level4;
    } else if (density > 1) {
        return level3;
    } else if (density > 0.5) {
        return level2;
    } else if (density > 0) {
        return level1;
    } else {
        return level0;
    }
};

MapHandler.prototype.getSpeedColorCode = function(speed) {
    // Color-Codes definieren
    const level5 = "#FF0000";
    const level4 = "#EE5500";
    const level3 = "#BB6600";
    const level2 = "#777700";
    const level1 = "#225500";
    const level0 = "#222222";

    // Ableich und entsprechenden Color Code zurückschicken
    if (speed > 90) {
        return level5;
    } else if (speed > 80) {
        return level4;
    } else if (speed > 70) {
        return level3;
    } else if (speed > 50) {
        return level2;
    } else if (speed > 25) {
        return level1;
    } else {
        return level0;
    }
};