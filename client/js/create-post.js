function submitForm() {
    var formData = {
        headline: document.getElementById('headline').value,
        content: document.getElementById('content').value,
        date: new Date(),
        token: "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYTc0NDM2NS02ZDQ0LTQwMTQtODMxZC01MzVmZDZhNTQ5NjEiLCJleHAiOjE3MDUyNjc5MjgsImVtYWlsIjoib25lQG9uZS5jb20ifQ.yNlpj1l4P5Y972M6fBeMTeiQmTiNWK666d9ptRepQu"
    };


    if (!headline.checkValidity()) {
        alert('Ange blogginläggets rubrik.');
        return;
    }

    if (!content.checkValidity()) {
        alert('Fyll i blogginläggets innehåll.');
        return;
    }

    console.log("Sending request")


    fetch('http://localhost:8080/blog-post', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => response.text())

        .then(data => {

            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}