
document.addEventListener("DOMContentLoaded", function () {
    // Get the HTML element where you want to display working time
    let workingTimeElement = document.querySelector("#workingTime");
    let workingTimeMillis = parseInt(workingTimeElement.innerText, 10);

    // Function to update time every second
    function updateWorkingTime() {
        workingTimeMillis += 1000; // Add 1 second of work
        let formattedWorkingTime = formatTime(workingTimeMillis); // Format time
        workingTimeElement.innerText = formattedWorkingTime; // Display working time on the page
    }

    // Call the function and start updating every second
    updateWorkingTime();
    setInterval(updateWorkingTime, 1000); // Update every second
});
