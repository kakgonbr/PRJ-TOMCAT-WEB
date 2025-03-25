function refreshLogs() {
    fetch(contextPath + '/logs')
        .then(response => response.text())
        .then(text => {
            let log = document.getElementById("logContainer");
            let wasScrolled = log.scrollTop == log.scrollHeight;
            log.innerText = text;
            if (wasScrolled) {
                log.scrollTop = log.scrollHeight;
            }
        });
}
setInterval(refreshLogs, 10000);
window.onload = refreshLogs;
