const loginForm = document.getElementById("login-form");
const errorMsg = document.getElementById("error-msg");

const loginButton = document.getElementById("login-button");


function login() {
  let formData = new FormData(loginForm);
  let jsonRequestBody = {};

  // Clears previous error message if exists
  errorMsg.innerHTML = "";

  // turns all the data from the form into an object
  formData.forEach((key, value) => (jsonRequestBody[value] = key));

  // Checks if fields are empty
  if (jsonRequestBody.email.length < 1 || jsonRequestBody.password.length < 1) {
    errorMsg.innerHTML = "Please fill all fields";
    return;
  }

  // Send auth request
  fetch("api/auth/login", {
    method: "POST",
    body: JSON.stringify(jsonRequestBody),
    headers: {
      "Content-Type": "application/json",
    },
  }).then((res) => {
    if (res.ok) {
    return res
      .json()
      .then((data) => {

          window.localStorage.setItem("userJWT", data.JWT);
          window.location = "/home.html";

      });
    }

    return Promise.reject(res);
  }).catch(err => {
    err.json().then(data => {
      errorMsg.innerHTML = data.msg;
    })
  });
}

getStatusMessage();
loginButton.addEventListener("click", login);
