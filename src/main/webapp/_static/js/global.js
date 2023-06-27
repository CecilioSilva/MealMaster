function logout(){
    // Logs the user out

    // Removes users JWT
    window.localStorage.removeItem("userJWT");

    // Redirects user to landing page
    window.location.replace("/");
}

function parseJwt(token) {
    // Parses the jwt token to object
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
        window
            .atob(base64)
            .split("")
            .map(function (c) {
                return "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2);
            })
            .join("")
    );

    return JSON.parse(jsonPayload);
}

function isLoggedIn() {
    // Checks if user is logged in

    // Gets users jwt from local storage
    const userJWT = window.localStorage.getItem("userJWT");

    // If user does not have a jwt (not authenticated) redirect to login page
    if(!userJWT){
        return false;
    }

    const user = parseJwt(userJWT);

    // Checks if the jwt is still valid
    let currentTimeInSeconds = new Date().getTime() / 1000;
    if(currentTimeInSeconds > user.exp){
        // if token is expired remove it
        window.localStorage.removeItem("userJWT");
        return false;
    }

    return true;
}

function checkLoginStatus() {
    // Returns user to login page if their not logged in

    // Checks if user is authenticated
    const loggedIn = isLoggedIn();

    if(!loggedIn){
        // If user is not logged in redirect them to login page
        window.location.replace("/login.html?status=Not Logged in")
    }
}

function getAuthorizationHeader(){
    // Creates a header for requests with the logged-in users JWT token

    return {
        "Authorization": `Bearer ${window.localStorage.getItem("userJWT")}`
    }
}

function getStatusMessage() {
    // Displays the status message in status banner

    const statusMsg = document.getElementById("status-msg");
    // Clears status message
    statusMsg.innerHTML = "";

    // Gets the search string from the url
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    statusMsg.innerHTML = urlParams.get("status");
}

function clipText(text, maxLength) {
    // Makes it so the length of a string is not longer than maxLength
    return (text.length > maxLength) ? text.slice(0, maxLength-1) + '&hellip;' : text;
}

function serverGetRequest(route, success, failure){
    fetch(`/api${route}`, {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            // If request was a success serialize the result into JSON
            res.json().then(success);
            return
        }
        // If there was an error reject the promise
        return Promise.reject(res);
    }).catch(err => {
        // Set the error banner to given error
        err.json().then(data => {
            data.msg ??= "";
            failure(data.msg)
        }).catch(_ => {
            alert(`Error get sending request to: ${route}`)
        })
    });
}

function serverDeleteRequest(route, success, failure){
    fetch(`/api${route}`, {
        method: "DELETE",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            // If request was a success serialize the result into JSON
            res.json().then(success);
            return
        }
        // If there was an error reject the promise
        return Promise.reject(res);
    }).catch(err => {
        // Set the error banner to given error
        err.json().then(data => {
            data.msg ??= "";
            failure(data.msg)
        }).catch(_ => {
            alert(`Error delete sending request to: ${route}`)
        })
    });
}

function openMeal(mealId){
    window.location = `/meal.html?id=${mealId}`;
}