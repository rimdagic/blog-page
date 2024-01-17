function submitForm() {
    var formData = {
        headline: document.getElementById('headline').value,
        content: document.getElementById('content').value,
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
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
        .then(response => {response.text()
        if(response.status === 200){
            window.location.href = "http://localhost:5500/home.html";
        }
    })

        .then(data => {

            console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}