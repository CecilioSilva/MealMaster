checkLoginStatus();
const mealsContainer = document.getElementById("meals-container");

function getIngredients(){
    fetch("/api/meals", {
        method: "GET",
        headers: {
            ...getAuthorizationHeader(),
        },
    }).then(res => {
        if (res.ok) {
            res.json().then(data => {
                setTimeout(() => {
                    mealsContainer.innerHTML = "";
                    data.forEach(meal => {
                        mealsContainer.innerHTML += `
                    <div class="meals-card">
                      <h2>${meal.name}</h2>
                      <img src="${meal.image}" alt="image" class="card-image">
                      <p class="card-description">${meal.description}</p>
                    </div>
                    `
                    })
                }, 500);
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