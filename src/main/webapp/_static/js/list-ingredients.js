// HTML Elements
const ingredientContainer = document.getElementById("ingredients-container");

function getIngredients(){
    // All the ingredients from the user
    serverGetRequest("/ingredients",
    (data) => {
                setTimeout(() => {
                    // Clear the ingredient container
                    ingredientContainer.innerHTML = "";

                    // For every ingredient in results append it to the meal container as card html
                    data.forEach(ingredient => {
                        ingredientContainer.innerHTML += `
                            <div class="item-card">
                              <h2 class="item-title">${ingredient.name}</h2>
                              <img src="${ingredient.image}" alt="image" class="card-image">
                              <p class="card-description">${clipText(ingredient.description, 50)}</p>
                              <img onclick="deleteIngredient('${ingredient.id}')" class="item-close" src="/_static/images/trash.svg" alt="Delete ingredient" aria-label="Delete item" aria-roledescription="Deletes the item">
                            </div>
                            `
                    })
                }, 100)
            },
        (errMsg) => {
            errorMsg.innerHTML = errMsg;
        }
    )
}

function deleteIngredient(ingredientId){
    // Deletes the ingredient from the user
    serverDeleteRequest(`/ingredients/delete/${ingredientId}`, () => {
        // Reload the window
        window.location.reload();
    }, (errMsg) => {
        alert(errMsg);
    })
}

// Run on page load
checkLoginStatus();
getIngredients();