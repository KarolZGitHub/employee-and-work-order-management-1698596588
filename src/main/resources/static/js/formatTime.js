function formatTime(milliseconds) {
    let days = Math.floor(milliseconds / (24 * 3600 * 1000)); // 1 day = 24 hours
    let hours = Math.floor((milliseconds % (24 * 3600 * 1000)) / 3600000); // Remaining hours
    let minutes = Math.floor((milliseconds % 3600000) / 60000); // Remaining minutes
    let seconds = Math.floor((milliseconds % 60000) / 1000); // Remaining seconds

    // Format time in "dd:hh:mm:ss" format
    return (
        (days < 10 ? "0" : "") + days + ":" +
        (hours < 10 ? "0" : "") + hours + ":" +
        (minutes < 10 ? "0" : "") + minutes + ":" +
        (seconds < 10 ? "0" : "") + seconds
    );
}
