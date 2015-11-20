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
function createBarChart(data, tagname, range) {
    var margin = { top: 20, right: 20, bottom: 70, left: 40 };
    var selection = d3.select(tagname);
    var width = selection[0][0].clientWidth - margin.left - margin.right;
    var height = 300 - margin.top - margin.bottom;

    // Parse the date / time
    var parseDate = d3.time.format("%d/%m/%Y %H:%M:%S").parse;
    data.forEach(function (d) {
        d.date = parseDate(d.activityDate);
        d.value = +d.value;
    });

    if (range == "month") {
        var today = new Date();
        var lastMonth = new Date();
        var barWidth = 20;
        lastMonth.setMonth(lastMonth.getMonth() - 1);
    } else {
        var today = new Date();
        var lastMonth = new Date();
        var barWidth = 140;
        lastMonth = new Date(lastMonth.setDate(lastMonth.getDate() - 7));
    }

    var x = d3.time.scale()
        .domain([lastMonth, today])
        .rangeRound([0, width - margin.left - margin.right]);

    var y = d3.scale.linear()
        .domain([0, d3.max(data, function (d) { return d.value; })])
        .range([height - margin.top - margin.bottom, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient('bottom')
        .ticks(d3.time.days, 1)
        .tickFormat(d3.time.format('%d/%m'))
        .tickPadding(8);

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient('left')
        .tickPadding(8);

    var svg = d3.select(tagname).append('svg')
        .attr('class', 'chart')
        .attr('width', width)
        .attr('height', height)
      .append('g')
        .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');

   svg.selectAll('.chart')
        .data(data)
      .enter().append('rect')
        .attr('class', 'bar')
        .attr('x', function (d) { return x(new Date(d.date)); })
        .attr('y', function (d) { return height - margin.top - margin.bottom - (height - margin.top - margin.bottom - y(d.value)) })
        .attr('width', barWidth)
        .attr('height', function (d) { return height - margin.top - margin.bottom - y(d.value) });

    svg.selectAll(".bar").append("text")
        .attr("dy", ".75em")
        .attr("y", 6)
        .attr("x", barWidth / 2)
        .attr("text-anchor", "middle")
        .text(function (d) { return d.value; });

    svg.append('g')
        .attr('class', 'x axis')
        .attr('transform', 'translate(-5, ' + (height - margin.top - margin.bottom) + ')')
        .call(xAxis);

    svg.append('g')
      .attr('class', 'y axis')
      .attr('transform', 'translate(-5, 0)')
      .call(yAxis);

}