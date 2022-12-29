
// servlet ValidLoginL
// Checks the login for validity.
$("#login").change(function() {
    if ($('#login').val() != "") {

        $.ajax({
            url: "/task03/validation/login_l",
            type: "POST",
            contentType: 'application/json',
            data: JSON.stringify({ login: $('#login').val() }),
            dataType: 'JSON',
            success: function (resp) {
                if (!resp.exists) {
                    warn("This login is not registered!");
                } else {
                    $('#login').css('border', '2px solid #74b0eb');
                }
            }
        });

    }
});

// Mark fields red if they are invalid.
// Otherwise, sends data to the server for login.
$('.form').submit(function(event) {
    event.preventDefault();

    let faults = $('input').filter(function() {

        return $(this).data('required') && $(this).val() === "";
    }).css("border-color", "crimson");

    if (faults.length) return false;

    submitForm();
});

// servlet Login
// Relocates to todo.html.
function submitForm() {
    let up = JSON.stringify({

        login: $('#login').val(),
        password: $('#password').val()

    });

    $.ajax({
        url: "/task03/login",
        type: "POST",
        contentType: 'application/json',
        data: up,
        dataType: 'JSON',
        statusCode: {
            400: function (e) {
                console.log("bad request - " + e.status);
                alert("bad request - " + e.status);
            },
            500: function(e) {
                console.log("Server error - " + e.status);
                alert("Server error - " + e.status);
            },
            200: console.log("relocated to todo-page"),
        },
        success: function (resp) {
            if (resp.user) {
                document.location.href = "../task03/todo.html";
            } else {
                console.log("Wrong login or password!");
                warn("Wrong login or password!");
            }
        }
    });

}

// Animates invalid information.
function warn(text) {
    $('#warn').append(`${text}`).animate({opacity: "show", top: '45%'}, "slow");
    setTimeout(function() { $('#warn').animate({opacity: "hide", top: '55%'}, "fast").empty(); }, 2000);
}