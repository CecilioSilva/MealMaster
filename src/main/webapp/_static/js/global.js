function logout(){
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
    // Checks if user is authenticated
    const loggedIn = isLoggedIn();

    if(!loggedIn){
        window.location.replace("/login.html?status=Not Logged in")
    }
}

function getAuthorizationHeader(){
    return {
        "Authorization": `Bearer ${window.localStorage.getItem("userJWT")}`
    }
}

function getStatusMessage() {
    const statusMsg = document.getElementById("status-msg");
    // Clears status message
    statusMsg.innerHTML = "";

    // Gets the search string from the url
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    statusMsg.innerHTML = urlParams.get("status");
}