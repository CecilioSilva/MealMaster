function parseJwt(token) {
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

function isLoggedIn() {
  const userJWT = window.localStorage.getItem("userJWT");

  //{sub: 'cjsilvamon@gmail.com', exp: 1686218994, name: 'Cecilio Silva'}
  const user = parseJwt(userJWT);

  // Get local time

  // check if jwt is still valid

  // redirect to login if user is not authed (aka no valid jwt)
}

isLoggedIn();
