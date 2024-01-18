function submitForm() {
    var formData = {
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    if (!email.checkValidity()) {
        alert('Fyll i en giltig email-adress.');
        return;
    }

    if (!password.checkValidity()) {
        alert('Fyll i ditt lösenord.');
        return;
    }



    console.log("Fetching CSRF token");

        fetch('http://localhost:8080/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include', // Include credentials to ensure cookies are sent
            body: JSON.stringify(formData),
        })
        .then(response => response.text())
        .then(data => {
            console.log('Server Response:', data);

            if (data === "Login successful") {
                window.location.href = "http://localhost:5500/home.html";
            } else if (data === "Login successful admin") {
                window.location.href = "http://localhost:5500/admin-panel.html";
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

}