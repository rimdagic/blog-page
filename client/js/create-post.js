adjustToAuth();
var emailDisplay = document.getElementById("email-display");

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
            //'X-CSRF-TOKEN':
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


function adjustToAuth(){
    var requestOptions = {
        method: 'GET',
        redirect: 'follow',
        credentials: "include"
      };
      
      fetch("http://localhost:8080/user/auth", requestOptions)
        .then(response => response.text())
        .then(result => renderPage(result)
        )
        .catch(error => console.log('error', error));
    }

    function renderPage(authenticated){
        var loginLink = document.getElementById("login-link")
        var logoutLink = document.getElementById("logout-link")
        var createPostButton = document.getElementById("create-post-btn")
    
    if(authenticated == "true"){
            loginLink.style.display = "none"
            logoutLink.style.display = "flex"
            getEmail();
        } 
        else if (authenticated == "false") {
            loginLink.style.display = "flex"
            logoutLink.style.display = "none"
            createPostButton.style.display = "none"
           // emailDisplay.innerText = "EPOST"
    
        } 
    }

    function getEmail(){
    
        var requestOptions = {
            method: 'GET',
            credentials: 'include',
            redirect: 'follow'
          };
          
          fetch("http://localhost:8080/user/email", requestOptions)
            .then(response => response.text())
            .then(result => {
                console.log(result)
                emailDisplay.innerText = result;
            }
                
                )
            .catch(error => console.log('error', error));
    }

    function logout(){
        var requestOptions = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                //'X-CSRF-TOKEN':
            }
          };
    
        fetch("http://localhost:8080/user/logout", requestOptions)
        .then(response => {
            response.text()
            if(response.status === 200){
                window.location.href = "http://localhost:5500/home.html";
            }
        })
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
    }