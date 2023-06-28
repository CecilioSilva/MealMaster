//HTML Elements
const shoppingListName = document.getElementById("name");
const mealContainer = document.getElementById("meal-container");
const mealForm = document.getElementById("meal-form");
const addShoppingListButton = document.getElementById("add-button");
const mealSelect = document.getElementById("meal-select");

//region Initialization
//List of meals fetched from user
const serverMeals = [];

function getUserMeals() {
  serverGetRequest(
    "/meals",
    (data) => {
      // If request succeeded add the meals to the global list
      serverMeals.push(...data);

      mealSelect.innerHTML = "";

      // Add all the meals to the selection box
      serverMeals.forEach((i) => {
        mealSelect.innerHTML += `<option value="${i.id}">${i.name}</option>`;
      });
    },
    (err) => {
      alert(err);
    }
  );
}

//endregion

//region Meal Form
// Global list of user added WeeklyMeals
let meals = [];

function renderMeals() {
  // Renders all meals in user added list
  mealContainer.innerHTML = "";
  meals.forEach((meal) => {
    mealContainer.innerHTML += `
           <li class="meal-item">
            <div class="meal-details">
              <span class="meal-date">${meal.date}</span>
              <span class="meal-name">${meal.name}</span>
            </div>
            <button class="meal-remove" onclick="removeMeal(${meal.uid})">
              Remove
            </button>
          </li>
        `;
  });
}

function removeMeal(uid) {
  // Removes meal from users local list by id
  meals = meals.filter((i) => i.uid !== uid);
  renderMeals();
}

function submitMeal(e) {
  // Gets meal from form and ads it to local meal list

  // Stops button from refreshing page
  e.preventDefault();

  // Gets data from form
  const formData = new FormData(mealForm);

  const id = formData.get("meal-select");
  const date = formData.get("date");
  const uid = new Date().getTime();

  // Validates if date is not empty
  if (!date) {
    alert("Date can't be empty");
    return;
  }

  const name = serverMeals.find((m) => m.id === id).name;

  //Adds meal to global meal list
  meals.push({ id, date, name, uid });

  // Re-renders ingredient list
  renderMeals();
}
//endregion

//region Shopping-list form
function submitShoppingList(e) {
  const name = shoppingListName.value;
  if (name.length < 1) {
    return alert("Name is required");
  }

  if (meals.length < 1) {
    return alert("At least one meal is required");
  }

  const payload = {
    name,
    days: meals.map((meal) => ({ id: meal.id, date: meal.date })),
  };

  fetch("/api/shopping-list/add", {
    method: "POST",
    body: JSON.stringify(payload),
    headers: {
      "Content-Type": "application/json",
      ...getAuthorizationHeader(),
    },
  })
    .then((res) => {
      if (res.ok) {
        // If request was successfully redirect user to home page
        window.location = "/home.html?status=Shopping-list added successfully!";
        return;
      }
      // If there was an error reject the promise
      return Promise.reject(res);
    })
    .catch((err) => {
      // Alert user of error
      err.json().then((data) => alert(data.msg));
    });
}

// Event listeners
mealForm.addEventListener("submit", submitMeal);
addShoppingListButton.addEventListener("click", submitShoppingList);

// Run on load
checkLoginStatus();
getStatusMessage();
getUserMeals();
