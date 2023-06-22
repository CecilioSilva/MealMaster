// HTML ELements
const mealsContainer = document.getElementById("meals-container");

function getMeals(){
    // All the meals from the user
    fetch("/api/meals", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then(res => {
        if (res.ok) {
            // If request was a success serialize the result into JSON
            res.json().then(data => {
                // After 500 ms delay render the cards
                setTimeout(() => {
                    // Clear the meals container
                    mealsContainer.innerHTML = "";

                    // For every meal in results append it to the meal container as card html
                    data.forEach(meal => {
                        mealsContainer.innerHTML += `
                    <div class="item-card">
                      <h2 class="item-title">${meal.name}</h2>
                      <img src="${meal.image}" alt="image" class="card-image">
                      <p class="card-description">${clipText(meal.description, 50)}</p>
                    </div>
                    `
                    })
                }, 500);
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
getMeals();