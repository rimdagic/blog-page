function submitForm() {
    var formData = {
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    var loginIsSuccessful = false;

    if (!email.checkValidity()) {
        alert('Fyll i en giltig email-adress.');
        return;
    }

    if (!password.checkValidity()) {
        alert('Fyll i ditt lösenord.');
        return;
    }

    console.log("Sending request")

    //var csrfToken = getCsrfToken();



    fetch('http://localhost:8080/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
  //          'X-CSRF-TOKEN': csrfToken // Add this line to include the CSRF token
        },
         "credentials": "include", 
        body: JSON.stringify(formData),
    })
        .then(response => response.text()) // Läs svaret som text
        .then(data => {
            console.log('Server Response:', data);

            if (data === "Login successful") {
               window.location.href = "http://localhost:5500/home.html";
            }else if(data === "Login successful admin"){
                window.location.href = "http://localhost:5500/admin-panel.html";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
// Function to retrieve the CSRF token from the cookie or wherever it's stored
// function getCsrfToken() {
//     return document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$)|^.*$/, '$1');
// }