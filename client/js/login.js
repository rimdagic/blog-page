function submitForm() {
    var formData = {
        email: document.getElementById('email').value,
        password: document.getElementById('password').value
    };

    console.log("Sending request")
    fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => response.json())
        .then(data => {

            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}