// HTML ELements
const mealsContainer = document.getElementById("meals-container");

function getMeals(){
    // All the meals from the user
    serverGetRequest("/meals", (data) => {
        setTimeout(() => {
            // Clear the meals container
            mealsContainer.innerHTML = "";

            // For every meal in results append it to the meal container as card html
            data.forEach(meal => {
                mealsContainer.innerHTML += `
                    <div class="item-card" onclick="openMeal('${meal.id}')">
                      <h2 class="item-title">${meal.name}</h2>
                      <img src="${meal.image}" alt="image" class="card-image">
                      <p class="card-description">${clipText(meal.description, 50)}</p>
                      <img onclick="deleteMeal('${meal.id}')" class="item-close" src="/_static/images/trash.svg" alt="Delete meal" aria-label="Delete meal" aria-roledescription="Deletes the meal">
                    </div>
                    `
            })
        }, 100);
    }, (errMsg) => {
        errorMsg.innerHTML = errMsg;
    })
}

function deleteMeal(mealId) {
    // Deletes the meal from the user
    serverDeleteRequest(`/meals/delete/${mealId}`, () => {
        // Reload the window
        window.location.reload();
    }, (errMsg) => {
        alert(errMsg);
    });
}

// Run on page load
checkLoginStatus();
getMeals();