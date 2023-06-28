// HTML Elements
const mealNameElement = document.getElementById("meal-name");
const amountInput = document.getElementById("amount");
const mealTitleElement = document.getElementById("meal-title");
const amountCountElement = document.getElementById("amount-count");
const mealIngredientContainer = document.getElementById("meal-ingredients");
const mealImage = document.getElementById("meal-image");
const mealDescription = document.getElementById("meal-description");
const mealContainer = document.getElementById("meal-container");
const loader = document.getElementById("loader");
const errMsg = document.getElementById("error-msg");

// Globals
let AMOUNT_PEOPLE = 4;
let MEAL_ID;
let MEAL;

function handleAmountInputChange(e) {
  // Runs when amount input gets changed
  AMOUNT_PEOPLE = e.currentTarget.value;
  amountCountElement.innerHTML = AMOUNT_PEOPLE;

  // Rerender ingredients
  getIngredients();
}

function getIngredients() {
  if (MEAL) {
    // Calculates the ratio of ingredients from the ones that were given from the recipe and that were selected
    const ratio = (AMOUNT_PEOPLE - MEAL.numberOfPeople) / AMOUNT_PEOPLE + 1;

    // Clears previous ingredient listings
    mealIngredientContainer.innerHTML = "";

    // Goes over every ingredient in the Meal
    for (let i = 0; i < MEAL.ingredients.length; i++) {
      let ingredient = MEAL.ingredients[i];

      // Sets the value of how many decimals the values should have
      let roundDigits = 0;
      switch (ingredient.measurementUnit) {
        case "milliliter":
        case "liter":
        case "gram":
          roundDigits = 1;
          break;
        case "count":
          roundDigits = 0;
          break;
        case "lbs":
          roundDigits = 2;
          break;
      }

      // Calculates the current amount with ratio and amount of decimals
      let amount = (ingredient.amount * ratio).toFixed(roundDigits);
      if (amount.endsWith(".0")) {
        // removes .0
        amount = parseInt(amount);
      }

      // Ads filed HTML to webpage
      mealIngredientContainer.innerHTML += `
                <li class="meal-ingredient">
                    <span class="ingredient-name">- ${ingredient.ingredient.name}</span>
                    <div class="ingredient-amounts">
                        <span class="ingredient-amount">${amount}</span>
                        <span class="ingredient-measurement">${ingredient.measurementUnit}</span>
                    </div>
                </li>
            `;
    }
  }
}

function getMealId() {
  // Gets the meal id from search url
  const urlParams = new URLSearchParams(window.location.search);
  const meal_id = urlParams.get("id");

  if (!meal_id) {
    // if the url does not have a search param redirect the user
    window.location.replace("/home.html");
  }

  MEAL_ID = meal_id;
}

function getMealData() {
  // Gets the meal from the user
  serverGetRequest(
    `/meals/${MEAL_ID}`,
    (data) => {
      MEAL = data;

      // Sets the fields with the meal data
      mealNameElement.innerHTML = data.name;
      mealTitleElement.innerHTML = data.name;
      mealImage.setAttribute("src", data.image);
      mealDescription.innerHTML = data.description;
      getIngredients();
    },
    (err) => {
      alert(err);
      errMsg.innerHTML = err;
    }
  );

  // Shows the meal and hides the loader icon
  mealContainer.classList.toggle("hidden", false);
  loader.classList.toggle("hidden", true);
}

// Event Handlers
amountInput.addEventListener("change", handleAmountInputChange);

// Run on load
checkLoginStatus();
getStatusMessage();
getMealId();
getMealData();
