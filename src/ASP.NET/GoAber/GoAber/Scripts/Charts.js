/* JavaScript code to display activity data summary statistics
*/


/* Setup the page ready to handle ajax loading
*/
function initiliseAjax() {
    $(document).ajaxStart(function () {
        $('.ajax-loading').show();  // show loading indicator
    });

    $(document).ajaxStop(function () {
        $('.ajax-loading').hide();  // hide loading indicator
    });
}

/* Request data from an API endpoint
*/
function requestData(endpoint, parameters, callback) {
    $.ajax({
        type: "GET",
        url: endpoint,
        contentType: "application/json; charset=utf-8",
        data: parameters,
        dataType: "json",
        success: callback,
        error: function (msg) {
            $("#result").text(msg);
        }
    });
}

/* Create a bar chart of activity data
 Based on tutorial at:
 http://www.d3noob.org/2014/02/making-bar-chart-in-d3js.html
*/
function createBarChart(data, tagname) {
    var margin = { top: 20, right: 20, bottom: 70, left: 40 };
    var selection = d3.select(tagname);
    var width = selection[0][0].clientWidth - margin.left - margin.right;
    var height = 300 - margin.top - margin.bottom;

    // Parse the date / time
    var parseDate = d3.time.format("%d/%m/%Y %H:%M:%S").parse;
    var x = d3.scale.ordinal().rangeRoundBands([0, width], .05);
    var y = d3.scale.linear().range([height, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom")
        .tickFormat(d3.time.format("%d/%m"));

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(10);

    var svg = d3.select(tagname).append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
      .append("g")
        .attr("transform",
              "translate(" + margin.left + "," + margin.top + ")");

    data.forEach(function (d) {
        d.date = parseDate(d.activityDate);
        d.value = +d.value;
    });

    x.domain(data.map(function (d) { return d.date; }));
    y.domain([0, d3.max(data, function (d) { return d.value; })]);

    svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0," + height + ")")
        .call(xAxis)
        .selectAll("text")
        .style("text-anchor", "end")
        .attr("dx", "-.8em")
        .attr("dy", "-.55em")
        .attr("transform", "rotate(-90)");

    svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
        .append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 6)
        .attr("dy", ".71em")
        .style("text-anchor", "end")
        .text("Value");

    svg.selectAll("bar")
        .data(data)
        .enter().append("rect")
        .style("fill", "steelblue")
        .attr("x", function (d) { return x(d.date); })
        .attr("width", x.rangeBand())
        .attr("y", function (d) { return y(d.value); })
        .attr("height", function (d) { return height - y(d.value); });
}