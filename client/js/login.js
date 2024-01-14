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

    console.log("Sending request")


    fetch('http://localhost:8080/user/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => response.text()) // Läs svaret som text
        .then(data => {
            console.log('Server Response:', data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}