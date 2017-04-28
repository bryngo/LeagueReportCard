
$(document).ready(function() {
    $.ajax({
        type: "GET",
        url: "BubblyBryan_reportCard.csv",
        dataType: "text",
        success: function(data) {processData(data);}
    });
});

function processData(allText) {
    var allTextLines = allText.split(/\r\n|\n/);
    var headers = allTextLines[0].split(',');
    var lines = [];

    for (var i=1; i<allTextLines.length; i++) {
        var data = allTextLines[i].split(',');
        if (data.length === headers.length) {

            var tarr = [];
            for (var j=0; j<headers.length; j++) {
                tarr.push(data[j]);
            }
            lines.push(tarr);
        }
    }
    // alert(lines);

    console.log(lines[0][0]);
}

function graphHistograms() {

    var x1 = [];
    var y1 = [];
    var y2 = [];
    for (var i = 1; i < 500; i++) {
        k = Math.random();
        x1.push(k);
        y1.push(Math.random() + 1);
        y2.push(Math.random() + 2);
    }
    var win = {
        x: x1,
        y: y1,
        type: "histogram",
        name: "win",
        marker: {
            color: 'pink'
        }
    };
    var lose = {
        x: x1,
        y: y2,
        type: "histogram",
        name: "lose",
        marker: {
            color: 'lightblue'
        }
    };
    var data = [win, lose];
    var layout = {barmode: "stack", title: "BubblyBryan's Stats"};
    Plotly.newPlot("myDiv", data, layout);
    Plotly.newPlot("bryan", data, layout);
}

