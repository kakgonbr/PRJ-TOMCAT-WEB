function refreshLogs() {
    fetch(contextPath + '/logs')
        .then(response => response.text())
        .then(text => document.getElementById("logContainer").innerText = text);
}
setInterval(refreshLogs, 10000);
window.onload = refreshLogs;
