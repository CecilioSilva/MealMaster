// HTML Elements
const ingredientForm = document.getElementById("ingredient-form");
const ingredientList = document.getElementById("ingredients-list");
const mealForm = document.getElementById("meal-form");
const addMealButton = document.getElementById("add-button");
const ingredientSelect = document.getElementById("ingredient");
const unitSelect = document.getElementById("unit");


//region Initialization
// List of ingredients fetched from the server
const serverIngredients = [

]

function getUserIngredients() {
    // Gets all the ingredients from the user
    fetch("/api/ingredients", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            // If request succeeded add the ingredients to the global list
            res.json().then(data => {
               serverIngredients.push(...data);

                ingredientSelect.innerHTML = "";
                // Add all the ingredients to the selection box
                serverIngredients.forEach((i) => {
                    ingredientSelect.innerHTML += `<option value="${i.id}">${i.name}</option>`;
                })
            });
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        err.json().then(data => alert(data.msg));
    });
}

function getMeasurementUnits() {
    // Gets all measurment units from the server

    fetch("/api/unit", {
        method: "GET",
    }).then(res => {
        if(res.ok){
            // If request was successfully serialize it to json
            res.json().then(data => {
                unitSelect.innerHTML = "";

                // Add all units to select box on pagw
                data.forEach(unit => {
                    unitSelect.innerHTML += `<option value="${unit}">${unit}</option>`;
                })
            })
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        // Alert user on error
        err.json().then(data => alert(data.msg));
    });
}
//endregion

//region Ingredient form
// Global list of user added ingredients
let ingredients = [];

function renderIngredients() {
    // Renders all ingredients in user added list
    ingredientList.innerHTML = "";
    ingredients.forEach((ingredient) => {
        ingredientList.innerHTML += `<li class="ingredient-item"><span>${ingredient.amount} ${ingredient.unit} - ${ingredient.name} </span><button class="remove-button" onclick="removeIngredient(${ingredient.uid})">remove</button></li>`
    })
}

function removeIngredient(uid) {
    // Removes ingredient from users local list by id
    ingredients = ingredients.filter((i) => i.uid !== uid);
    renderIngredients();
}

function submitIngredient(e)  {
    // Gets ingredient from form and ads it to local ingredient list

    // Stops button from refreshing page
    e.preventDefault();

    // Gets data from form
    const formData = new FormData(ingredientForm);

    const id = formData.get("ingredient");
    const amount = formData.get("amount");
    const unit = formData.get("unit");
    const uid = new Date().getTime();

    // Validates if amount is not empty
    if(!amount) {
        alert("Amount can't be empty");
        return
    }

    // Gets the name from server ingredient list with id
    const name = serverIngredients.find((i) => i.id === id ).name

    // Adds ingredient to global meal ingredient list
    ingredients.push({name, amount, unit, uid, id});

    // Re-renders ingredient list
    renderIngredients();
}


//endregion

//region Meal form
function submitMeal(e) {
    // Submits meal with ingredients to users data

    // Prevents page from reloading
    e.preventDefault();

    // Gets data from the meal form
    const formData = new FormData(mealForm);

    const name = formData.get("name");
    const description = formData.get("description");
    const image = formData.get("image");
    const numberOfPeople = formData.get("people")

    // Validates if name is not too short and not empty
    if(name.length < 1 || !name){
        alert("Name cant be empty");
        return;
    }

    // Validates if number of people is not empty or 0
    if(numberOfPeople < 1 || !numberOfPeople){
        alert("Number of people needs to be at least 1")
        return;
    }


    // Creates the Object to be sent to server
    // noinspection JSCheckFunctionSignatures
    const payload = {
        name,
        description,
        image,
        numberOfPeople: parseInt(numberOfPeople),
        // Sends the id amount and unit from every ingredient to server in list of objects
        ingredients: ingredients.map((i) => ({
            amount: parseInt(i.amount),
            id: i.id,
            unit: i.unit,
        }))
    }

    fetch("/api/meals/add", {
        method: "POST",
        body: JSON.stringify(payload),
        headers: {
            "Content-Type": "application/json",
            ...getAuthorizationHeader(),
        },
    }).then(res => {
        if (res.ok) {
            // If request was successfully redirect user to home page
            window.location = "/home.html?status=Meal added successfully!"
            return
        }
        // If there was an error reject the promise
        return Promise.reject(res);
    }).catch(err => {
        // Alert user of error
        err.json().then(data => alert(data.msg));
    });
}
//endregion


// Event listers
ingredientForm.addEventListener("submit", submitIngredient)
addMealButton.addEventListener("click", submitMeal)

// Run on page load
checkLoginStatus();
getStatusMessage();
setTimeout(() => {
    getUserIngredients();
    getMeasurementUnits();
}, 1000)