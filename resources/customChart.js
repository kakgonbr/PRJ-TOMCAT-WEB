function createChart(t_elementId, t_type, t_label, t_labels, t_data) {
    new Chart(document.getElementById(t_elementId),
    {
        type: t_type,
        data: {
            labels: t_labels,
            datasets: [{
            label: t_label,
            data: t_data,
            borderWidth: 1,
            fill: true,
            borderColor: 'rgb(75, 192, 192)',
            backgroundColor: 'rgb(75, 192, 192, 0.3)'
            }]
        },
        options: {
            scales: {
            y: {
                beginAtZero: true
            }
            }
        }
        }
    );
}