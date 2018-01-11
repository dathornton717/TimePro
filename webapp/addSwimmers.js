function addIndividualSwimmer() {

    var name = document.getElementById('name').value;
    var teamName = document.getElementById('team-name').value;

    if (name === "") {
        alert("A swimmer name must be entered!");
        return;
    }

    if (teamName === "") {
        alert("A team name must be entered!");
        return;
    }

    name = name.trim();
    teamName = teamName.trim();
    var spaceIndex = name.indexOf(' ');
    if (spaceIndex === -1) {
        alert("Please enter the swimmer's first and last name separated by a space.");
    }
    var firstName = name.substring(0, spaceIndex);
    var lastName = name.substring(spaceIndex + 1);

    var toSend = {};
    toSend.firstName = firstName;
    toSend.lastName = lastName;
    toSend.teamName = teamName;

    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "http://localhost:8080/TimePro/rest/add-swimmer",
        data: JSON.stringify(toSend)
    });

    alert("Give the server approximately 30 seconds to load the given swimmer's times.");
}

function addMultipleSwimmers() {
    var fileToLoad = document.getElementById("file").files[0];

    var fileReader = new FileReader();
    fileReader.onload = function(fileLoadedEvent){
        var textFromFileLoaded = fileLoadedEvent.target.result;
        console.log(textFromFileLoaded);
        $.ajax({
            type: "PUT",
            contentType: "text/plain",
            url: "http://localhost:8080/TimePro/rest/add-swimmers",
            data: textFromFileLoaded
        });

        alert("Give the server approximately 30 seconds per swimmer to load the times.");
    };

    fileReader.readAsText(fileToLoad, "UTF-8");
}


