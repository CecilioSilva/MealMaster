checkLoginStatus();

const ingredientForm = document.getElementById("ingredient-form");
const errorMsg = document.getElementById("error-msg");
const addButton = document.getElementById("add-button");

function addIngredient(){
    let formData = new FormData(ingredientForm);
    let jsonRequestBody = {};

    // Clears previous error message if exists
    errorMsg.innerHTML = "";

    // turns all the data from the form into an object
    formData.forEach((key, value) => (jsonRequestBody[value] = key));

    if(jsonRequestBody.name.length < 1){
        errorMsg.innerHTML = "Name is invalid";
        return
    }

    console.log(jsonRequestBody);

    // Send add ingredient request
    fetch("/api/ingredients/add", {
        method: "POST",
        body: JSON.stringify(jsonRequestBody),
        headers: {
            "Content-Type": "application/json",
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            window.location = "/home.html?status=Ingredient added";
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        err.json().then(data => {
            errorMsg.innerHTML = data.msg;
        }).catch(err => {
            errorMsg.innerHTML = "Error submitting"
        })
    });
}

getStatusMessage();
addButton.addEventListener("click", addIngredient);