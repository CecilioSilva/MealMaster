function logout(){
    // Removes users JWT
    window.localStorage.removeItem("userJWT");

    // Redirects user to landing page
    window.location.replace("/");
}

function parseJwt(token) {
    // Parses the jwt token to object
    var base64Url = token.split(".")[1];
    var base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    var jsonPayload = decodeURIComponent(
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

function checkLoginStatus() {
    // Checks if user is authenticated

    // Gets users jwt from local storage
    const userJWT = window.localStorage.getItem("userJWT");

    // If user does not have a jwt (not authenticated) redirect to login page
    if(!userJWT){
        window.location.replace("/login.html")
        return;
    }

    const user = parseJwt(userJWT);

    // Checks if the jwt is still valid
    let currentTimeInSeconds = new Date().getTime() / 1000;
    if(currentTimeInSeconds > user.exp){
        // if token is expired remove it and redirect to login page
        window.localStorage.removeItem("userJWT");
        window.location.replace("/login.html?status=Session expired");
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