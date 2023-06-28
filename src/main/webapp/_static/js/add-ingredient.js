// HTML Elements
const ingredientForm = document.getElementById("ingredient-form");
const errorMsg = document.getElementById("error-msg");
const addButton = document.getElementById("add-button");

function addIngredient() {
  // Adds an ingredient to the user

  // Get data from form
  let formData = new FormData(ingredientForm);
  let jsonRequestBody = {};

  // Clears previous error message if exists
  errorMsg.innerHTML = "";

  // turns all the data from the form into an object
  formData.forEach((key, value) => (jsonRequestBody[value] = key));

  // Validates if name is not empty
  if (jsonRequestBody.name.length < 1) {
    errorMsg.innerHTML = "Name is invalid";
    return;
  }

  // Send add ingredient request
  fetch("/api/ingredients/add", {
    method: "POST",
    body: JSON.stringify(jsonRequestBody),
    headers: {
      "Content-Type": "application/json",
      ...getAuthorizationHeader(),
    },
  })
    .then((res) => {
      if (res.ok) {
        // If the ingredient got added successfully redirect user to home page
        window.location = "/home.html?status=Ingredient added successfully!";
        return;
      }

      // If there was an error reject the promise
      return Promise.reject(res);
    })
    .catch((err) => {
      // Set the error banner to given error
      err
        .json()
        .then((data) => {
          errorMsg.innerHTML = data.msg;
        })
        .catch((err) => {
          errorMsg.innerHTML = "Error submitting";
        });
    });
}

// Set event listeners
addButton.addEventListener("click", addIngredient);

// Run on page load
getStatusMessage();
checkLoginStatus();
