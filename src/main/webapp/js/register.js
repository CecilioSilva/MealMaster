async function register(){
    let formData = new FormData(document.getElementById("register-form"));
    let jsonRequestBody = {}

    formData.forEach((key, value) => jsonRequestBody[value] = key);

    try {
        const res = await fetch("api/auth/register", {
            method: "POST",
            body: JSON.stringify(jsonRequestBody),
            headers: {
                'Content-Type': 'application/json'
            }
        })
        const data = await res.json();

        if(res.ok){
            window.location = "/login.html?status=success"
        }
    } catch (err) {
        console.log(data.msg);
    }
}

document.getElementById("register-button").addEventListener("click", register);