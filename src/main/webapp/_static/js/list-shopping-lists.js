// HTML Elements
const shoppingListContainer = document.getElementById("shopping-list-container");

function getShoppingLists(){
    // All the ingredients from the user
    serverGetRequest("/shopping-list",
        (data) => {
            setTimeout(() => {
                // Clear the ingredient container
                shoppingListContainer.innerHTML = "";

                // For every ingredient in results append it to the meal container as card html
                data.forEach(shoppingList => {

                    shoppingListContainer.innerHTML += `
                            <div class="item-card" style="height: 200px; overflow-y: hidden" onclick="openShoppingList('${shoppingList.id}')">
                              <h2 class="item-title">${shoppingList.name}</h2>
                                <ul class="item-list">
                                   ${shoppingList.days.map(meal => `<li class="item-list-item">${meal.meal.name}</li>`).join("")}
                                </ul>
                              <img onclick="deleteShoppingList('${shoppingList.id}')" class="item-close" src="/_static/images/trash.svg" alt="Delete shopping list" aria-label="Delete item" aria-roledescription="Deletes the item">
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

function deleteShoppingList(ingredientId){
    // Deletes the ingredient from the user
    serverDeleteRequest(`/shopping-list/delete/${ingredientId}`, () => {
        // Reload the window
        window.location.reload();
    }, (errMsg) => {
        alert(errMsg);
    })
}

function openShoppingList(id){
    window.location = `/shopping-list.html?id=${id}`;
}

// Run on page load
checkLoginStatus();
getShoppingLists();