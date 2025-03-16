function setTheme(mode = 'system') {
    const userMode = localStorage.getItem('bs-theme');
    const sysMode = window.matchMedia(
        '(prefers-color-scheme: light)'
    ).matches;
    const useSystem = mode === 'system' && !userMode;
    const modeChosen = useSystem
        ? 'system'
        : mode === 'dark' || mode === 'light'
            ? mode
            : userMode;

    if (useSystem) {
        localStorage.removeItem('bs-theme');
    } else {
        localStorage.setItem('bs-theme', modeChosen);
    }

    document.documentElement.setAttribute(
        'data-bs-theme',
        useSystem ? (sysMode ? 'light' : 'dark') : modeChosen
    );
    document
        .querySelectorAll('.button-switch-theme')
        .forEach((e) => e.classList.remove('active'));
    document.getElementById("button-theme-" + mode).classList.add('active');
}

function startupTheme() {
    setTheme();
    document
        .querySelectorAll('.button-switch-theme')
        .forEach((e) => e.addEventListener('click', () => setTheme(e.getAttribute("data-bs-theme-value"))));
    window
        .matchMedia('(prefers-color-scheme: light)')
        .addEventListener('change', () => setTheme());
}

// can be dangerous
document.addEventListener("DOMContentLoaded", startupTheme);