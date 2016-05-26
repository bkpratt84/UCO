
function spaceElements() {

    var horizontalSpace = 2;
    var verticalSpace   = 10;

    var v = 1;
    var h = 1;

    while ( true ) {

        $( "#crs-" + v + "-" + h ).css( "left", horizontalSpace + "em" );
        $( "#crs-" + v + "-" + h ).css( "top", verticalSpace + "em" );

        if ( $( "#crs-" + v + "-" + h ).length ) {
            h++;
            horizontalSpace += 15;
        }
        else {
            h = 1;
            v++;
            horizontalSpace = 2;
            verticalSpace += 10;
        }

        if ( !($( "#crs-" + v + "-1" ).length) )  {
            break;
        }

    }

}

function colorElements() {
    var courses = $(".chart-elements .window .pre").toArray();
    var courses1 = $(".chart-elements .window").toArray();

    for (var i = 0; i < courses.length; i++) {
        var c = $(courses[i]).attr("id").substring(0,4);

        if ( (c === "MATH") || (c === "STAT") ) {
            $(courses1[i]).css({"background-color":"lightblue"});
        }
        else if ( (c === "CMSC") ) {
            //$(courses1[i]).css({"background-color":"lightgreen"});
        }
        else if ( (c.substring(0,2) === "SE") ) {
            $(courses1[i]).css({"background-color":"lightsalmon"});
        }
        else if ( c.substring(0,2) === "HS" ) {
            $(courses1[i]).css({"background-color":"lightyellow"});
        }
    }
}

spaceElements();
colorElements();
var instance;

jsPlumb.ready(function () {

    var color = "gray";

    instance = jsPlumb.getInstance({

        Connector: ["Bezier", {curviness: 50}],
        DragOptions: {cursor: "pointer", zIndex: 2000},
        PaintStyle: {strokeStyle: color, lineWidth: 2},
        EndpointStyle: {},
        HoverPaintStyle: {},
        EndpointHoverStyle: {},
        Container: "chart-elements"

    });

    instance.batch(function () {

        var arrowCommon = {foldback: 1, fillStyle: color, width: 14, id:"Overlay"},

        overlays = [
            ["Arrow", {location: 0.80}, arrowCommon]
        ];

        var windows = jsPlumb.getSelector(".chart-elements .window .pre");

        for (var i = 0; i < windows.length; i++) {
            instance.addEndpoint(windows[i], {
                uuid: windows[i].getAttribute("id") + "-bottom",
                anchor: "AutoDefault",
                //anchor:["Perimeter", {shape:"Circle"}],
                maxConnections: -1
            });
            instance.addEndpoint(windows[i], {
                uuid: windows[i].getAttribute("id") + "-top",
                anchor: "AutoDefault",
                //anchor:["Perimeter", {shape:"Circle"}],
                maxConnections: -1
            });
        }

        var courses = $(".chart-elements .window").toArray();

        for (var i = 0; i < courses.length; i++) {
            var c   = $(courses[i]).find("div");
            var id  = c.attr("id");
            var pre = c.attr("class").split(/\s+/);
            pre.splice(0,1);
            pre.splice(pre.length-1,1);

            for (var j = 0; j < pre.length; j++) {
                var preReq = pre[j] + "-bottom";
                var current = id + "-top";

                if ( pre[j] !== "None1" ) {
                    instance.connect({
                        uuids: [preReq, current], 
                        overlays: overlays,
                        parameters: {"id": pre}
                    });
                }
            }
        }

        instance.draggable(jsPlumb.getSelector(".chart-elements .window"));
    });
    
    jsPlumb.fire("jsPlumbDemoLoaded", instance);


});

var vis = true;

function getIDList(id, prev) {
    var pre = $("#"+id).attr("class").split(/\s+/);
    pre.splice(0,1);
    pre.splice(pre.length-2,2);

    var ret = [];

    for (var i = 0; i < pre.length; i++) {
        if ( (pre[i] !== prev) && $("#"+pre[i]).length ) {

            var v = getIDList(pre[i], id);

            for (var j = 0; j < v.length; j++) {
                if ( v[j] !== "None1" ) {
                    ret.push(v[j]);
                }
            }
        }
    }

    return ret.concat(pre);
}

var hasMoved = false;

$(".chart-elements .window").mousedown(function(e){
    hasMoved = false;
});

$(".chart-elements .window").mouseup(function(e){
    if ( hasMoved === false ) {
        selectClass( $(this) );
        $( "#filter-buttons #year .year" ).prop("disabled", !vis);
        $( "#filter-buttons #season .season" ).prop("disabled", !vis);
        filterBy(selectedSeason, selectedYear);
    }
});

$(".chart-elements .window").mousemove(function(e){
    hasMoved = true;
});

function selectClass(selectedID) {
    var c   = selectedID.find("div");
    var id  = c.attr("id");

    instance.select().setPaintStyle({ strokeStyle:"gray", lineWidth:2 });

    if ( vis ) {
        var p;
        var matches = getIDList(id);

        matches.push(id);

        var connections = instance.select();
        connections.setVisible(false);

        for (var i = 0; i < matches.length; i++) {
            p = instance.select({target:matches[i]});
            p.setPaintStyle({ strokeStyle:"blue", lineWidth:5 });
            p.setVisible(true);
        }

        connections.each(function(c) {
            c.showOverlay("Overlay");
        });

        var courses  = $(".chart-elements .window .pre").toArray();
        var courses1 = $(".chart-elements .window").toArray();

        for (var i = 0; i < courses.length; i++) {
            if ( $(courses[i]).attr("id") !== id ) {
                if ( $.inArray($(courses[i]).attr("id").trim(), matches) < 0 ) {
                    $(courses1[i]).hide();
                }
            }
        }
        vis = false;
    }
    else {
        var connections = instance.select();
        connections.setVisible(true);

        var courses1 = $(".chart-elements .window").toArray();

        for (var i = 0; i < courses1.length; i++) {
            $(courses1[i]).show();
        }
        vis = true;
    }
}

function filterBy(season, year) {

    var courses  = $(".chart-elements .window .avail").toArray();
    var courses1 = $(".chart-elements .window").toArray();
    var courses2 = $(".chart-elements .window .pre").toArray();

    var hide;
    
    for (var i = 0; i < courses.length; i++) {
        
        var c = $(courses[i]).attr("class").split(/\s+/);
        c.splice(0,1);

        hide = true;
        
        for (var j = 0; j < c.length; j++) {
            var date = c[j].split("-");
            
            if ( date.length > 1 ) {
                if ( !(((date[0] !== season) && (season !== "All")) ||
                        ((date[1] !== year) && (year !== "All"))) ) {
                    hide = false;
                }
            }
        }

        if ( c.length > 1 ) {
            if ( hide ) {
                        
                $(courses1[i]).hide();
                
                var hideID = $(courses2[i]).attr("id");
                
                p = instance.select({target:hideID});
                p.setVisible(false);
                
                p = instance.select({source:hideID});
                p.setVisible(false);
                
            }
            else if ( vis === true ) {
                $(courses1[i]).show();
                
                var hideID = $(courses2[i]).attr("id");
                
                p = instance.select({target:hideID});
                p.setVisible(true);
                
                p = instance.select({source:hideID});
                p.setVisible(true);
                
                var connections = instance.select();
                connections.each(function(c) {
                    c.showOverlay("Overlay");
                });
            }
        }
    }
        
}

var selectedSeason = "All";
var selectedYear   = "All";

$( "#filter-buttons #season .season" ).click(function (event) {
    var season = $(this).html();
    selectedSeason = season;

    $( "#filter-buttons #season .season" ).removeClass("btn-success");
    $( "#filter-buttons #season .season" ).removeClass("btn-primary");
    $( "#filter-buttons #season .season" ).addClass("btn-primary");

    $(this).addClass("btn-success");

    filterBy(selectedSeason, selectedYear);
});

$( "#filter-buttons #year .year" ).click(function (event) {
    var year = $(this).html();
    selectedYear = year;

    $( "#filter-buttons #year .year" ).removeClass("btn-success");
    $( "#filter-buttons #year .year" ).removeClass("btn-primary");
    $( "#filter-buttons #year .year" ).addClass("btn-primary");

    $(this).addClass("btn-success");

    filterBy(selectedSeason, selectedYear);
});