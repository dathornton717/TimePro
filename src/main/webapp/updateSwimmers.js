function updateSwimmers() {
    $.ajax({
        type: "GET",
        url:"http://localhost:8080/TimePro/rest/update-times"
    });

    alert("Give the server approximately 30 seconds per swimmer to update the times. " +
        "The graph can be used in the meantime");
}