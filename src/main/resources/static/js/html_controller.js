/**
 * Created by rhintho on 26.10.16.
 */
var HTMLController = function(wsHandler) {

    var radioDensity = document.getElementById("radio-density");
    var radioVelocity = document.getElementById("radio-velocity");
    var timestamp = document.getElementById("timestamp");

    radioDensity.checked = true;
    timestamp.value = "2014-12-24T18:00:00";
    var savedTimestamp = timestamp.value;

    var radioRes10 = document.getElementById("resolution-10");
    var radioRes20 = document.getElementById("resolution-20");
    var radioRes100 = document.getElementById("resolution-100");
    var radioRes200 = document.getElementById("resolution-200");
    var radioRes1000 = document.getElementById("resolution-1000");

    radioRes100.checked = true;

    var that = this;
    var resolution = 100;

    radioDensity.onclick = function(event) {
        g_dataType = "density";
        const dc = that.getDateComponents(timestamp.value);
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, resolution);
    };

    radioVelocity.onclick = function(event) {
        g_dataType = "velocity";
        const dc = that.getDateComponents(timestamp.value);
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, resolution);
    };

    timestamp.onchange = function(event) {
        if (timestamp.value.substring(0, 13) == savedTimestamp.substring(0, 13)) {
            // Tue nichts
        } else {
            const dc = that.getDateComponents(timestamp.value);
            // if (dc.hour < 10) {
            //     dc.hour = "0" + dc.hour;
            // }
            wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, resolution);
            savedTimestamp = timestamp.value;
        }
    };

    radioRes10.onclick = function(event) {
        const dc = that.getDateComponents(timestamp.value);
        resolution = 10;
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, radioRes10.value);
    };

    radioRes20.onclick = function(event) {
        const dc = that.getDateComponents(timestamp.value);
        resolution = 20;
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, radioRes20.value);
    };

    radioRes100.onclick = function(event) {
        const dc = that.getDateComponents(timestamp.value);
        resolution = 100;
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, radioRes100.value);
    };

    radioRes200.onclick = function(event) {
        const dc = that.getDateComponents(timestamp.value);
        resolution = 200;
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, radioRes200.value);
    };

    radioRes1000.onclick = function(event) {
        const dc = that.getDateComponents(timestamp.value);
        resolution = 1000;
        wsHandler.startRequest(dc.year, dc.month, dc.day, dc.hour, radioRes1000.value);
    };
};

HTMLController.prototype.getDateComponents = function(datestring) {
    const year = datestring.substring(0, 4);
    const month = datestring.substring(5, 7);
    const day = datestring.substring(8, 10);
    const hour = datestring.substring(11, 13);

    return {
        year: year,
        month: month,
        day: day,
        hour: hour
    }
};