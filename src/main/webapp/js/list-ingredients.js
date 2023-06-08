checkLoginStatus();
const ingredientContainer = document.getElementById("ingredients-container");

function getIngredients(){
    ingredientContainer.innerHTML = "";

    fetch("/api/ingredients", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then((res) => {
        if (res.ok) {
            res.json().then(data => {
                data.forEach(ingredient => {
                    ingredientContainer.innerHTML += `
                    <div class="ingredient-card">
                      <h2>${ingredient.name}</h2>
                      <img src="${ingredient.image}" alt="image" class="card-image">
                      <p class="card-description">${ingredient.description}</p>
                    </div>
                    `
                })
            });
            return
        }
        return Promise.reject(res);
    }).catch(err => {
        err.json().then(data => {
            errorMsg.innerHTML = data.msg;
        }).catch(err => {
            errorMsg.innerHTML = "Error getting ingredients"
        })
    });

}

getIngredients();