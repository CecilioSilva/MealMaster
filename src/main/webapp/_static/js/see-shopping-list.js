//HTML Elements
const shoppingListContainer = document.getElementById("shopping-list-container")
const pageTitle = document.getElementById("shopping-list-name");
const shoppingListTitle = document.getElementById("shopping-list-title");
const shoppingListIngredients = document.getElementById("shopping-list-ingredients");
const mealsItems = document.getElementById("meals-items");
const loader = document.getElementById("loader");
const shareButton = document.getElementById("share-button");

// Globals
let SHOPPING_LIST_ID;

function getShoppingListId(){
    // Gets the meal id from search url
    const urlParams = new URLSearchParams(window.location.search);
    const shopping_list_id = urlParams.get("id");

    if(!shopping_list_id){
        // if the url does not have a search param redirect the user
        window.location.replace("/home.html");
    }

    SHOPPING_LIST_ID = shopping_list_id;
}

function combineObjects(objArr) {
    const combinedObj = {};

    objArr.forEach(obj => {
        const { unit, id, ...rest } = obj;

        const key = unit + '_' + id;
        if (!combinedObj[key]) {
            combinedObj[key] = { unit, id, ...rest };
        } else {
            combinedObj[key].amount += obj.amount;
        }
    });

    return Object.values(combinedObj);
}

function getShoppingListData(){
    // Gets the shopping list from the user
    serverGetRequest(`/shopping-list/${SHOPPING_LIST_ID}`, (data) => {
            // Fill fields with shopping list data
            pageTitle.innerHTML = data.name;
            shoppingListTitle.innerHTML = data.name;

            const meals = [];
            let ingredients = [];

            data.days.forEach(day => {
                const meal = day.meal;
                // Creates meal object for html page
                meals.push({id: meal.id, image: meal.image, name: meal.name});

                // Creates ingredient for html page
                meal.ingredients.forEach(ing => {
                    ingredients.push({
                        amount: ing.amount,
                        unit: ing.measurementUnit,
                        id: ing.id,
                        bought: ing.bought,
                        name: ing.ingredient.name,
                    })
                })
            })

            ingredients = combineObjects(ingredients);

            shoppingListIngredients.innerHTML = "";
            ingredients.forEach(i => {
                shoppingListIngredients.innerHTML += `
                    <li class="list-ingredient">
                      <span class="ingredient-name">${i.name}</span>
                      <div>
                        <span class="ingredient-amount">${i.amount}</span>
                        <span class="ingredient-unit">${i.unit}</span>
                      </div>
                    </li>
                `;
            })

            mealsItems.innerHTML = "";
            meals.forEach(m => {
                mealsItems.innerHTML += `
                    <div class="meal-item" style="background-image: url('${m.image}')" onclick="openMeal('${m.id}')">
                      <h4 class="meal-item-title">${m.name}</h4>
                    </div>
                `;
            })
        },
        (err) => {
            alert(err);
            errMsg.innerHTML = err;
        })


    // Shows the shopping list and hides the loader icon
    shoppingListContainer.classList.toggle("hidden",  false);
    loader.classList.toggle("hidden", true);
}

// Event listeners
shareButton.addEventListener("click", () => {
    alert("Alert pressed");
})

// Run on load
checkLoginStatus()
getStatusMessage()
getShoppingListId()
getShoppingListData()
