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
            $("#"+ parameters.tagname +"-loading-fails").text(msg);
        }
    });
}

function setSummaryValues(data, prefix, dateFormat) {
    if (typeof (dateFormat) === 'undefined') dateFormat = 'MMMM Do';
    $("#" + prefix + "-total").text(data.Total);
    $("#" + prefix + "-average").text(parseFloat(Math.round(data.Average * 100) / 100).toFixed(2));
    $("#" + prefix + "-min-date").text(moment(data.MinDate).format(dateFormat));
    $("#" + prefix + "-min-value").text(data.Min);
    $("#" + prefix + "-max-date").text(moment(data.MaxDate).format(dateFormat));
    $("#" + prefix + "-max-value").text(data.Max);
}


/* Create a bar chart of activity data
 Based on tutorial at:
 http://www.d3noob.org/2014/02/making-bar-chart-in-d3js.html
*/
function createBarChartSummary(data, params) {
    if (data.length === 0) {
        return;
    }

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

    var barWidth = (width / (dateDiffInDays(dateRange.startDate, dateRange.endDate)));

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
        .attr('width', barWidth -10)
        .attr('height', function (d) { return height - margin.top - margin.bottom - y(d.value) });

   var emptyOffset = dateDiffInDays(dateRange.startDate, d3.min(data, function (d) { return d.date; }));

   svg.selectAll("text")
        .data(data)
      .enter()
        .append("text")
        .text(function (d) {
          return d.value;
        })
        .attr("x", function (d, i) { return x(new Date(d.date)) + (barWidth/2) - 13; })
        .attr("y", function (d) {
            return y(d.value) + 15;
        })
        .attr("font-family", "sans-serif")
        .attr("font-size", "11px")
        .attr("fill", "white")
        .attr("text-anchor", "left");

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
        dateRange.startDate.setMonth(dateRange.startDate.getMonth() - 1);
    } else {
        // create a date range for one week ago
        dateRange.startDate = new Date(dateRange.startDate.setDate(dateRange.startDate.getDate() - 7));
    }

    return dateRange;
}

var _MS_PER_DAY = 1000 * 60 * 60 * 24;

// Calculate the difference between two dates
// Solution from: http://stackoverflow.com/questions/3224834
function dateDiffInDays(a, b) {
    // Discard the time and time-zone information.
    var utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
    var utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

    return Math.floor((utc2 - utc1) / _MS_PER_DAY);
}