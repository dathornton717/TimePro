function updateGraph(event, params) {
    var svg = d3.select("svg");
    var top = 50;
    var right = 100;
    var bottom = 10;
    var left = 60;
    var width = 960 - left - right;
    var height = 500 - top - bottom;

    // Setup axes
    var x = d3.scaleTime().range([0, width]);
    var y = d3.scaleLinear().range([height, 0]);

    var line = d3.line()
        //.curve(d3.curveStepAfter)
        .x(function(d) { return x(d.date); })
        .y(function(d) { return y(d.time); });

    var startDate = new Date(2000, 01, 01).getTime();
    var endDate = new Date().getTime();

    var isStartDate = $("#datepicker-1").datepicker('getDate') !== null;
    var isEndDate = $("#datepicker-2").datepicker('getDate') !== null;

    if (isStartDate) {
        startDate = $("#datepicker-1").datepicker('getDate').getTime();
        $('#datepicker-2').datepicker( "option", "minDate", new Date(startDate) );
    }

    if (isEndDate) {
        endDate = $("#datepicker-2").datepicker('getDate').getTime();
        $('#datepicker-1').datepicker( "option", "maxDate", new Date(endDate) );
    }

    //Get the event selected
    var event = document.getElementById("events_chosen").getElementsByTagName("span")[0].innerHTML;
    event = event.replace(/\s/g, '');

    // Get the names selected
    var nameElements = document.getElementById("names_chosen").getElementsByTagName("span");
    var names = [];
    var graphData = [];
    var namesToColors = new Map();
    for (var i = 0; i < nameElements.length; i++) {
        names.push(nameElements[i].innerHTML);
    }

    var color = function(i) {
        return d3.schemeCategory20b[(i * 3) % 20];
    }

    if (params && params.deselected) {
        var index = names.indexOf(params.deselected);
        names.splice(index, 1);
    }

    if (names.length === 0) {
        svg.selectAll("g").remove();
        svg.selectAll("rect").remove();
        svg.selectAll("text").remove();
        return;
    }

    for (var i = 0; i < names.length; i++) {
        var name = names[i];
        var firstName = name.split(", ")[1];
        var lastName = name.split(", ")[0];
        var url = "http://localhost:8080/rest/times-between-dates-name/" +
            firstName + "/" +
            lastName + "/" +
            event + "/" +
            startDate + "/" +
            endDate;

        $.ajax({
            type: "GET",
            url:url,
            dateType: "json",
            async: false,
            success: function(data) {
                data.sort(function(a, b) {
                    return a.date - b.date;
                });

                data = data.map(function(obj) {
                    return {date: new Date(obj.date), time: obj.time};
                });
                for (var j = 0; j < data.length; j++) {
                    data[j].color = color(i);
                    data[j].name = firstName + " " + lastName;
                }

                namesToColors.set(name, color(i));

                graphData.push(data);
            }
        });
    }

    var ticks = function(d) {
        var minutes = Math.floor(d / 60 / 100).toString();
        d = d - minutes * 60 * 100;
        var seconds = Math.floor(d / 100).toString();
        d = (d - seconds * 100).toString();

        if (seconds.length === 1) {
            seconds = "0" + seconds;
        }
        if (d.length == 1) {
            d = d + "0";
        }

        if (minutes === "0") {
            return seconds + "." + d;
        }
        else {
            return minutes + ":" + seconds + "." + d;
        }
    }

    svg.selectAll("g").remove();
    svg.selectAll("rect").remove();
    svg.selectAll("text").remove();
                var g = svg.append("g").attr("transform", "translate(" + left + "," + top + ")");

                if (!isStartDate && !isEndDate) {
                                    x.domain([
                                        d3.min(graphData, function(c) { return d3.min(c, function(d) { return d.date.getTime(); }); }),
                                        d3.max(graphData, function(c) { return d3.max(c, function(d) { return d.date.getTime(); }); })
                                    ]);
                }
                else if (!startDate) {
                    x.domain([
                        d3.min(graphData, function(c) { return d3.min(c, function(d) { return d.date.getTime(); }); }),
                        endDate
                    ]);
                }
                else if (!endDate) {
                                    x.domain([
                                        d3.min(graphData, function(c) { return d3.min(c, function(d) { return d.date.getTime(); }); }),
                                        endDate
                                    ]);
                }
                else {
                    x.domain([startDate, endDate]);
                }



                y.domain([
                    d3.min(graphData, function(c) { return d3.min(c, function(d) { return d.time; }); }) - 25,
                    d3.max(graphData, function(c) { return d3.max(c, function(d) { return d.time; }); }) + 25
                ]);

                g.append("g")
                    .attr("class", "axis axis--x")
                    .attr("transform", "translate(0," + height + ")")
                    .call(d3.axisBottom(x));

    var yAxis = d3.axisLeft(y)
        .tickFormat(ticks);

                g.append("g")
                    .attr("class", "axis axis--y")
                    .call(yAxis);

                var graph = g.selectAll(".times")
                    .data(graphData)
                    .enter().append("g");

                graph.append("path")
                    .attr("class", "line")
                    .attr("d", function(d) { return line(d); })
                    .attr("fill", "none")
                    .attr("stroke-width", "1.5px")
                    .attr("stroke", function(d, i) { return color(i); });

                var legendXOffset = 10;
                var legend = svg.append("rect")
                    .attr("x", width + left + legendXOffset)
                    .attr("y", top)
                    .attr("width", 200)
                    .attr("height", namesToColors.size * 20 + 5)
                    .style("fill", "none")
                    .style("stroke", "black");

                var colorXOffset = legendXOffset + 5;
                var colorYOffset = 20;
                var colorWidth = 50;
                var colorHeight = 5;
                var textXOffset = 5;
                var legendColors = svg.selectAll(".rect")
                    .data(Array.from(namesToColors.values()));

                legendColors.enter().append("rect")
                    .attr("class", "rect")
                    .attr("x", function() { return width + left + colorXOffset; })
                    .attr("y", function(d, i) { return top + (colorYOffset / 2) + i * colorYOffset; })
                    .attr("width", function() { return colorWidth; })
                    .attr("height", function() { return colorHeight; })
                    .style("fill", function(d) { return d; });

                var legendText = svg.selectAll(".text")
                    .data(Array.from(namesToColors.keys()));

                legendText.enter().append("text")
                    .attr("class", "text")
                    .attr("x", function() { return width + left + colorXOffset + colorWidth + textXOffset; })
                    .attr("y", function(d, i) { return top + 5 + (colorYOffset / 2) + i * colorYOffset; })
                    .attr("font-size", "14px")
                    .text(function(d) { return d.split(", ")[1] + " " + d.split(", ")[0]; });

                var dots = g.selectAll(".dots")
                    .data(graphData)
                    .enter().append("g")
                    .attr("class", "dots");

                dots.selectAll("circle")
                    .data(function(d, i){return d;})
                    .enter()
                    .append("circle")
                            .attr("class", function(d) { return d.name; })
                            .attr("r", 3.5)
                            .attr("cx", function(d) { return x(d.date); })
                            .attr("cy", function(d) { return y(d.time); })
                            .attr("stroke", function(d) { return d.color; })
                            .attr("fill", function(d) { return d.color; })
                            .on("mouseover", handleMouseOver)
                            .on("mouseout", handleMouseOut);




              // Create Event Handlers for mouse
              function handleMouseOver(d, i) {  // Add interactivity

                    // Specify where to put label of text
                    svg.append("text")
                        .attr("id", "t" + d.date.getTime() + "-" + d.time + "-" + i)
                        .attr("x", function() { return x(d.date) + left; })
                        .attr("y", function() { return y(d.time) + top - 5; })
                        .attr("font-size", "12px")
                        .text(function() {
                            var month = d.date.getMonth() + 1;
                            var day = d.date.getDate();
                            var year = d.date.getFullYear();
                            var toPrintDate = month + "/" + day + "/" + year;
                            return d.name + ": " + toPrintDate + ", " + ticks(d.time);  // Value of the text
                        });
                  }

              function handleMouseOut(d, i) {
                    // Select text by id and then remove
                    d3.select("#t" + d.date.getTime() + "-" + d.time + "-" + i).remove();  // Remove text location
                  }
}

function setUpNameSelector(data) {
    var names = [];
    for (var i = 0; i < data.length; i++) {
        var name = data[i];
        var firstName = name.split(" ")[0];
        var lastName = name.split(" ")[1];
        names.push(lastName + ", " + firstName);
        names.sort();
    }
    $html = '<select name="names" id="names" multiple="multiple" size="1" class="chosenElement">';

    for (var i = 0; i < names.length; i++) {
        var name = names[i];
        $html += '<option value="' + name + '">' + name + '</option>';
    }
    $html += '</select>';

    $('.selectors').append($html);
    $('.chosenElement').chosen({ width: "210px" });
    $('.html-multi-chosen-select').chosen({ width: "210px" });
    $('.simple-select').chosen({ width: "210px" });
    $('#names').chosen().change(updateGraph);
}

function setUpEventSelector() {
    var data = [
        "50 Free",
        "100 Free",
        "200 Free",
        "500 Free",
        "1000 Free",
        "1650 Free",
        "50 Back",
        "100 Back",
        "200 Back",
        "50 Breast",
        "100 Breast",
        "200 Breast",
        "50 Fly",
        "100 Fly",
        "200 Fly",
        "100 IM",
        "200 IM",
        "400 IM"
    ];

    $html = '<select name="events" id="events" size="1" class="chosenElement">';
    for (var i = 0; i < data.length; i++) {
        var name = data[i];
        $html += '<option value="' + name + '">' + name + '</option>';
    }
    $html += '</select>';
    $('.selectors').prepend($html);
    $('.chosenElement').chosen({ width: "210px" });
    $('.simple-select').chosen({ width: "210px" });
    $('#events').chosen().change(updateGraph);
}

$("#datepicker-1").datepicker({
   onSelect: function(dateText, inst) {
      updateGraph();
   }
});

$("#datepicker-2").datepicker({
   onSelect: function(dateText, inst) {
      updateGraph();
   }
});

$.ajax({
    type: "GET",
    url:"http://localhost:8080/rest/names",
    dateType: "json",
    success: setUpNameSelector
}).done(function() {
    setUpEventSelector();
});


//
//// Create the svg canvas in the "graph" div
//var svg = d3.select("#graph")
//        .append("svg")
//        .style("width", width + margin.left + margin.right + "px")
//        .style("height", height + margin.top + margin.bottom + "px")
//        .attr("width", width + margin.left + margin.right)
//        .attr("height", height + margin.top + margin.bottom)
//        .append("g")
//        .attr("transform","translate(" + margin.left + "," + margin.top + ")")
//        .attr("class", "svg");
//
//// Import the CSV data
//d3.csv("Example4.csv", function(error, data) {
//  if (error) throw error;
//
//   // Format the data
//  data.forEach(function(d) {
//      d.Month = parseMonth(d.Month);
//      d.Sales = +d.Sales;
//      d.Fruit = d.Fruit;
//      d.Year = formatYear(parseYear(+d.Year));
//  });
//
//  var nest = d3.nest()
//	  .key(function(d){
//	    return d.Fruit;
//	  })
//	  .key(function(d){
//	  	return d.Year;
//	  })
//	  .entries(data)
//
//  // Scale the range of the data
//  x.domain(d3.extent(data, function(d) { return d.Month; }));
//  y.domain([0, d3.max(data, function(d) { return d.Sales; })]);
//
//  // Set up the x axis
//  var xaxis = svg.append("g")
//       .attr("transform", "translate(0," + height + ")")
//       .attr("class", "x axis")
//       .call(d3.axisBottom(x)
//          .ticks(d3.timeMonth)
//          .tickSize(0, 0)
//          .tickFormat(d3.timeFormat("%B"))
//          .tickSizeInner(0)
//          .tickPadding(10));
//
//  // Add the Y Axis
//   var yaxis = svg.append("g")
//       .attr("class", "y axis")
//       .call(d3.axisLeft(y)
//          .ticks(5)
//          .tickSizeInner(0)
//          .tickPadding(6)
//          .tickSize(0, 0));
//
//  // Add a label to the y axis
//  svg.append("text")
//        .attr("transform", "rotate(-90)")
//        .attr("y", 0 - 60)
//        .attr("x", 0 - (height / 2))
//        .attr("dy", "1em")
//        .style("text-anchor", "middle")
//        .text("Monthly Sales")
//        .attr("class", "y axis label");
//
//  // Create a dropdown
//    var fruitMenu = d3.select("#fruitDropdown")
//
//    fruitMenu
//		.append("select")
//		.selectAll("option")
//        .data(nest)
//        .enter()
//        .append("option")
//        .attr("value", function(d){
//            return d.key;
//        })
//        .text(function(d){
//            return d.key;
//        })
//
//
//
// 	// Function to create the initial graph
// 	var initialGraph = function(fruit){
//
// 		// Filter the data to include only fruit of interest
// 		var selectFruit = nest.filter(function(d){
//                return d.key == fruit;
//              })
//
//	    var selectFruitGroups = svg.selectAll(".fruitGroups")
//		    .data(selectFruit, function(d){
//		      return d ? d.key : this.key;
//		    })
//		    .enter()
//		    .append("g")
//		    .attr("class", "fruitGroups")
//
//		var initialPath = selectFruitGroups.selectAll(".line")
//			.data(function(d) { return d.values; })
//			.enter()
//			.append("path")
//
//		initialPath
//			.attr("d", function(d){
//				return valueLine(d.values)
//			})
//			.attr("class", "line")
//
// 	}
//
// 	// Create initial graph
// 	initialGraph("strawberry")
//
//
// 	// Update the data
// 	var updateGraph = function(fruit){
//
// 		// Filter the data to include only fruit of interest
// 		var selectFruit = nest.filter(function(d){
//                return d.key == fruit;
//              })
//
// 		// Select all of the grouped elements and update the data
//	    var selectFruitGroups = svg.selectAll(".fruitGroups")
//		    .data(selectFruit)
//
//		    // Select all the lines and transition to new positions
//            selectFruitGroups.selectAll("path.line")
//               .data(function(d){
//                  return (d.values);
//                })
//                .transition()
//                  .duration(1000)
//                  .attr("d", function(d){
//                    return valueLine(d.values)
//                  })
//
//
// 	}
//
//
// 	// Run update function when dropdown selection changes
// 	fruitMenu.on('change', function(){
//
// 		// Find which fruit was selected from the dropdown
// 		var selectedFruit = d3.select(this)
//            .select("select")
//            .property("value")
//
//        // Run update function with the selected fruit
//        updateGraph(selectedFruit)
//
//
//    });
//
//
//
//})