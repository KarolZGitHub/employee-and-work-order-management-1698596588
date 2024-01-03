document.addEventListener("DOMContentLoaded", function () {
    // Select all elements that have IDs starting with 'overallWorkingTime'
    let overallWorkingTimeElements = document.querySelectorAll('[id^="overallWorkingTime"]');

    function updateWorkingTimes() {
        overallWorkingTimeElements.forEach(function (element) {
            let overallWorkingTimeValue = parseInt(element.innerText, 10);
            let formattedOverallWorkingTime = formatTime(overallWorkingTimeValue); // Format time
            element.textContent = formattedOverallWorkingTime; // Display overall working time on the page
        });
    }

    updateWorkingTimes();
});