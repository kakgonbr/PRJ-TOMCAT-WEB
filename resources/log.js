function refreshLogs() {
    fetch(contextPath + '/logs')
        .then(response => response.text())
        .then(text => {
            let log = document.getElementById("logContainer");
            log.innerText = text;
            log.scrollTop = log.scrollHeight;
        });
}
setInterval(refreshLogs, 10000);
window.onload = refreshLogs;
