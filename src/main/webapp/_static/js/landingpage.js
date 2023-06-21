const navLinkContainer = document.getElementById("nav-links");

function changeLinkWhenLoggedIn() {
    if(isLoggedIn()){
        navLinkContainer.innerHTML = '<li class="nav-link"><a href="/home.html">Home</a></li>';
    }
}

changeLinkWhenLoggedIn();