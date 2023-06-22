// HTML Elements
const ingredientContainer = document.getElementById("ingredients-container");

function getIngredients(){
    // All the ingredients from the user
    fetch("/api/ingredients", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            // If request was a success serialize the result into JSON
            res.json().then(data => {
                // After 500 ms delay render the cards
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
                    </div>
                    `
                   })
               }, 500)
            });
            return
        }
        // If there was an error reject the promise
        return Promise.reject(res);
    }).catch(err => {
        // Set the error banner to given error
        err.json().then(data => {
            errorMsg.innerHTML = data.msg;
        }).catch(err => {
            errorMsg.innerHTML = "Error getting ingredients"
        })
    });

}

// Run on page load
checkLoginStatus();
getIngredients();