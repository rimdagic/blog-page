function submitForm() {
    var formData = {
        headline: document.getElementById('headline').value,
       postContent: document.getElementById('post-content').value
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