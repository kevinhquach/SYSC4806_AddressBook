$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const addressBookID = urlParams.get('id');

    //check for existing address book for current id query
    $.ajax({url: "http://localhost:8080/addressBooks/" + addressBookID,
                type: 'GET',
                error: function() { //create a new address book if one doesn't exist
                    $.ajax({url: 'http://localhost:8080/addressBooks',
                        type: 'POST',
                        data: JSON.stringify({"id": addressBookID, "buddies": null}),
                        dataType: 'json',
                        contentType: 'application/json'
                    });
                }
    });

    $("<p id='addText'>Add Buddy</p>").insertAfter('#header');
    $("<label for='addName'>Name:</label><input type='text' id='addName' name='addName'><br id='afterAddName'>").insertAfter('#addText');
    $("<label for='number'>Phone Number:</label><input type='text' id='number' name='number'><br id='afterNumber'>").insertAfter('#afterAddName');
    $("<button type='button' id='add'>Add</button>").insertAfter('#afterNumber');

    $("<p id='removeText'>Remove Buddy</p>").insertAfter('#add');
    $("<label for='removeName'>Name:</label><input type='text' id='removeName' name='removeName'><br>").insertAfter('#removeText');
    $("<button type='button' id='remove'>Remove</button>").insertAfter('#removeName');

    $("<button type='button' id='update'>Update Table (from external HTTP requests)</button>").insertBefore('table');

    //defining adding behaviour
    $('#add').click(function(){
        $.ajax({url: 'http://localhost:8080/buddyInfoes', //add a buddy first (should really check if one with the same properties exists first)
            type: 'POST',
            data: JSON.stringify({"name": $('#addName').val(), "phoneNumber": $('#number').val()}),
            dataType: 'json',
            contentType: 'application/json',
            success: function(data) { //added buddy? now add buddy to address book
                $.ajax({url:"http://localhost:8080/addressBooks/" + addressBookID + "/buddies",
                    type: 'PATCH',
                    data: data._links.self.href,
                    contentType: 'text/uri-list',
                    success: getAddressBookInfo //update table
                });
            }
        });
    });

    //defining removing behaviour
    $('#remove').click(function(){
        let removeName = $('#removeName').val();
        $.ajax({url: "http://localhost:8080/addressBooks/" + addressBookID + "/buddies", //get the buddies in the address book
                    type: 'GET',
                    success: function(data) {
                        let buddies = data._embedded.buddyInfoes;
                        let idCount = 1;
                        buddies.forEach(function(bud) {
                            if (bud.name === removeName) { //look for the buddy (assuming unique names)
                                $.ajax({url: "http://localhost:8080/addressBooks/1/buddies/" + idCount, //delete them
                                            type: 'DELETE',
                                            success: getAddressBookInfo //update table
                                });
                            }
                            idCount++;
                        })
                    }
        });
    });

    //defining update behaviour
    $('#update').click(getAddressBookInfo);

    //getting data
    function getAddressBookInfo() {
        $.ajax({
            datatype: 'json',
            url: "http://localhost:8080/addressBooks/" + addressBookID + "/buddies",
            success: showAddressBookInfo //actually change the table now
            });
        return false; //override any other behaviour
    }
    function showAddressBookInfo(jsonData) {
        $('table').replaceWith("<table><tr><th>Name</th><th>Phone Number</th></tr></table>");
        jsonData._embedded.buddyInfoes.forEach(function(bud) {
            $('table tbody').append('<tr><td>' + bud.name + '</td><td>' + bud.phoneNumber + "</td></tr>");
        });

        return false;
    }

    getAddressBookInfo(); //update the table after loading
});