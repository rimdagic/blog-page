
document.addEventListener('DOMContentLoaded', function () {
    // Get the post ID from the URL parameter
    const params = new URLSearchParams(window.location.search);
    const postId = params.get('id');
    console.log("postId " + postId);

    // Fetch the specific blog post data
    fetch(`http://localhost:8080/post?id=${postId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(blogPost => {

            const postTitleElement = document.getElementById('post-title');
            const postEmailElement = document.getElementById('post-email');
            const postDateElement = document.getElementById('post-date');
            const postContentElement = document.getElementById('post-content');

            postTitleElement.textContent = blogPost.headline;
            postDateElement.textContent = new Date(blogPost.date).toLocaleString();
            postContentElement.innerText = blogPost.content;
            postEmailElement.innerText = blogPost.user.email;
        })
        .catch(error => {
            console.error('Error fetching blog post details:', error);
        });
});

function goBack(){
    window.history.back()
}

adjustToAuth()
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

        if(authenticated == "true"){
            loginLink.style.display = "none"
            logoutLink.style.display = "flex"
        } 
        else if (authenticated == "false") {
            loginLink.style.display = "flex"
            logoutLink.style.display = "none"
   
        } 
    }

    function logout(){
        var requestOptions = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
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