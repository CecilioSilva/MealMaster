// HTML Elements
const shoppingListContainer = document.getElementById("shopping-list-container")
const pageTitle = document.getElementById("shopping-list-name");
const shoppingListTitle = document.getElementById("shopping-list-title");
const shoppingListIngredients = document.getElementById("shopping-list-ingredients");
const loader = document.getElementById("loader");
const errorMsg = document.getElementById("error-msg");

// Globals
let SHOPPING_LIST_ID;

function getShoppingListId(){
    // Gets the meal id from search url
    const urlParams = new URLSearchParams(window.location.search);
    const shopping_list_id = urlParams.get("id");

    if(!shopping_list_id){
        // if the url does not have a search param redirect the user
        window.location.replace("/");
    }

    SHOPPING_LIST_ID = shopping_list_id;
}

function getShoppingListData() {
    // gets the public shopping list data
    errorMsg.innerHTML = "";

    serverGetRequest(`/shopping-list/public/${SHOPPING_LIST_ID}`, (data) => {
        // Fill fields with shopping list data
        pageTitle.innerHTML = data.name;
        shoppingListTitle.innerHTML = data.name;

        let ingredients = [];

        data.days.forEach(day => {
            const meal = day.meal;

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
                      <span class="ingredient-name">• ${i.name}</span>
                      <div>
                        <span class="ingredient-amount">${i.amount}</span>
                        <span class="ingredient-unit">${i.unit}</span>
                      </div>
                    </li>
                `;
        })
        shoppingListContainer.classList.toggle("hidden",  false);
    }, (err) => {
        errorMsg.innerHTML = "Shopping list could not be found or is not public";
    })


    loader.classList.toggle("hidden", true);
}

// Run on load
getStatusMessage()
getShoppingListId()
getShoppingListData()