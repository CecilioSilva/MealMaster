const ingredientForm = document.getElementById("ingredient-form");
const ingredientList = document.getElementById("ingredients-list");
const mealForm = document.getElementById("meal-form");
const addMealButton = document.getElementById("add-button");
const ingredientSelect = document.getElementById("ingredient");
const unitSelect = document.getElementById("unit");


//region Initialization
const serverIngredients = [

]

function getUserIngredients() {
    fetch("/api/ingredients", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            res.json().then(data => {
               serverIngredients.push(...data);

                ingredientSelect.innerHTML = "";
                serverIngredients.forEach((i) => {
                    ingredientSelect.innerHTML += `<option value="${i.id}">${i.name}</option>`;
                })
            });
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        // err.json().then(data => alert(data));
        console.log(err)
    });


}

function getMeasurementUnits() {
    fetch("/api/unit", {
        method: "GET",
    }).then(res => {
        if(res.ok){
            res.json().then(data => {
                unitSelect.innerHTML = "";

                data.forEach(unit => {
                    unitSelect.innerHTML += `<option value="${unit}">${unit}</option>`;
                })
            })
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        // err.json().then(data => alert(data));
        console.log(err)
    });
}
//endregion

//region Ingredient form
let ingredients = [];

function renderIngredients() {
    ingredientList.innerHTML = "";
    ingredients.forEach((ingredient) => {
        ingredientList.innerHTML += `<li class="ingredient-item"><span>${ingredient.amount} ${ingredient.unit} - ${ingredient.name} </span><button class="remove-button" onclick="removeIngredient(${ingredient.uid})">remove</button></li>`
    })
}

function removeIngredient(uid) {
    ingredients = ingredients.filter((i) => i.uid !== uid);
    renderIngredients();
}

ingredientForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(ingredientForm);

    const id = formData.get("ingredient");
    const amount = formData.get("amount");
    const unit = formData.get("unit");
    const uid = new Date().getTime();

    if(!amount) {
       alert("Amount can't be empty");
       return
    }

    const name = serverIngredients.find((i) => i.id === id ).name
    ingredients.push({name, amount, unit, uid, id});
    renderIngredients();
})

//endregion

//region Meal form
addMealButton.addEventListener("click", (e) => {
    e.preventDefault();

    const formData = new FormData(mealForm);

    const name = formData.get("name");
    const description = formData.get("description");
    const image = formData.get("image");
    const numberOfPeople = formData.get("people")

    if(name.length < 1 || !name){
        alert("Name cant be empty");
        return;
    }

    if(numberOfPeople < 1 || !numberOfPeople){
        alert("Number of people needs to be at least 1")
        return;
    }


    // noinspection JSCheckFunctionSignatures
    const payload = {
        name,
        description,
        image,
        numberOfPeople: parseInt(numberOfPeople),
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
            window.location = "/home.html"
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        err.json().then(data => alert(data.msg));
    });
})
//endregion


checkLoginStatus();
getStatusMessage();

setTimeout(() => {
    getUserIngredients();
    getMeasurementUnits();
}, 1000)