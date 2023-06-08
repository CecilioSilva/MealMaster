
const registerForm = document.getElementById("register-form");
const errorMsg = document.getElementById("error-msg");
const registerButton = document.getElementById("register-button");

function register(){
    let formData = new FormData(registerForm);
    let jsonRequestBody = {}

    // Clears previous error message if exists
    errorMsg.innerHTML = "";


    // turns all the data from the form into an object
    formData.forEach((key, value) => jsonRequestBody[value] = key);

    // Checks if users passwords match
    if(jsonRequestBody.repeatPassword !== jsonRequestBody.password){
        errorMsg.innerHTML = "Passwords don't match";
        return;
    }

    // Checks if username is between the min and max length
    if(jsonRequestBody.name.length < 3 || jsonRequestBody.name.length > 15){
        errorMsg.innerHTML = "Name should be between 3 and 15 characters";
        return;
    }

    // Checks if email is valid
    if(!jsonRequestBody.email.includes("@")){
        errorMsg.innerHTML = "Email should include an '@' symbol";
        return;
    }

    // Send auth post request
    fetch("api/auth/register", {
        method: "POST",
        body: JSON.stringify(jsonRequestBody),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(res => {
        if(res.ok){
            // if the request was successfull redirect to login page with success status
            window.location = "/login.html?status=success";
        } else {
            return Promise.reject(res)
        }
    }).catch(err => {
        err.json().then(jsn => {
            console.log(jsn)
            // Set the error message to the response msg
            errorMsg.innerText = jsn.msg;
        })
    })
}

registerButton.addEventListener("click", register);