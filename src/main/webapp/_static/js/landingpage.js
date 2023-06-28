// HTML Elements
const navLinkContainer = document.getElementById("nav-links");

function changeLinkWhenLoggedIn() {
  // Changes the Register and login links to home link in the navbar

  if (isLoggedIn()) {
    // If user is logged in change link
    navLinkContainer.innerHTML =
      '<li class="nav-link"><a href="/home.html">Home</a></li>';
  }
}

// Run on page load
changeLinkWhenLoggedIn();
