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
function createBarChartSummary(data, params) {
    var margin = { top: 20, right: 20, bottom: 70, left: 40 };
    var selection = d3.select(params.tagname);
    var width = selection[0][0].clientWidth - margin.left - margin.right;
    var height = 300 - margin.top - margin.bottom;

    var dateRange = createDateRange(params.rangeType);
    data = formatResponseData(data);

    var x = d3.time.scale()
        .domain([dateRange.startDate, dateRange.endDate])
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

    var svg = d3.select(params.tagname).append('svg')
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
        .attr('width', dateRange.barWidth)
        .attr('height', function (d) { return height - margin.top - margin.bottom - y(d.value) });

    svg.selectAll(".bar").append("text")
        .attr("dy", ".75em")
        .attr("y", 6)
        .attr("x", dateRange.barWidth / 2)
        .attr("text-anchor", "middle")
        .text(function (d) { return d.value; });

    svg.append('g')
        .attr('class', 'x axis')
        .attr('transform', 'translate(0, ' + (height - margin.top - margin.bottom) + ')')
        .call(xAxis);

    svg.append('g')
      .attr('class', 'y axis')
      .attr('transform', 'translate(0, 0)')
      .call(yAxis);

}

/* Parse the reaw JSON response returned from the API.
*/
function formatResponseData(data) {
    // Parse the date / time
    var parseDate = d3.time.format("%d/%m/%Y %H:%M:%S").parse;

    data.forEach(function (d) {
        d.date = parseDate(d.activityDate);
        d.value = +d.value;
    });

    return data;
}

/* Create an object representing a date range form 
   for either a week or a month
*/
function createDateRange(rangeType) {
    var dateRange = new Object();

    dateRange.endDate = new Date();
    dateRange.startDate = new Date();

    dateRange.startDate.setHours(0, 0, 0, 0);
    dateRange.endDate.setHours(0, 0, 0, 0);

    if (rangeType == "month") {
        // create a date range for one month ago
        dateRange.barWidth = 20;
        dateRange.startDate.setMonth(dateRange.startDate.getMonth() - 1);
    } else {
        // create a date range for one week ago
        dateRange.barWidth = 140;
        dateRange.startDate = new Date(dateRange.startDate.setDate(dateRange.startDate.getDate() - 7));
    }

    return dateRange;
}