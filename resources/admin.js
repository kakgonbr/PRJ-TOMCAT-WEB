// Expected json from the server:
// [{
//     "for" : "chartTotalMoney",
//     "type" : "bar",
//     "label" : "Money Earned"
//     "labels" : ["c", "b", "c"],
//     "values" : [1, 2, 3]
// }]

document.addEventListener("DOMContentLoaded", function () {
    fetchChartData();
});

function fetchChartData() {
    fetch(contextPath + "/ajax/admin/data")
        .then(response => response.json())
        .then(data => {
            data.forEach(chart => {
                createChart(chart.for, chart.type, chart.label, chart.labels, chart.values);
            });
            
        })
        .catch(error => console.error("Error fetching chart data:", error));
}
