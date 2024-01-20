var postsList = document.getElementById("posts-list");
var emailDisplay = document.getElementById("email-display");

adjustToAuth();
var csrfToken = getCookie("XSRF-TOKEN")

fetch('http://localhost:8080/all-posts')
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(blogPosts => {
        blogPosts.sort((a, b) => Date.parse(b.date) - Date.parse(a.date));
        renderBlogPosts(blogPosts);
    })
    .catch(error => {
        console.error('Error fetching blog posts:', error);
    });

function adjustToAuth() {
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

function renderBlogPosts(blogPosts) {
    for (var i = 0; i < blogPosts.length; i++) {
        var listItem = document.createElement('li');
        var link = document.createElement('a');
        link.href = 'post.html?id=' + blogPosts[i].id;
        link.textContent = blogPosts[i].headline;

        listItem.appendChild(link);
        postsList.appendChild(listItem);
    }
}


function search() {
    var searchWord = document.getElementById("search-field").value
    removeAllChildren(postsList)

    fetch(`http://localhost:8080/search-posts?searchWord=${searchWord}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(blogPosts => {
            blogPosts.sort((a, b) => Date.parse(b.date) - Date.parse(a.date));
            renderBlogPosts(blogPosts);
        })
        .catch(error => {
            console.error('Error fetching blog posts:', error);
        });
}

function removeAllChildren(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}

function deleteAll() {
    var requestOptions = {
        method: 'DELETE',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        }
    };

    fetch("http://localhost:8080/delete-posts", requestOptions)
        .then(response => response.text())
        .then(result => location.reload())
        .catch(error => console.log('error', error));
}

function logout() {
            var requestOptions = {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken,
                }}
            


            fetch("http://localhost:8080/user/logout", requestOptions)
                .then(response => {
                    response.text()
                    if (response.status === 200) {
                        window.location.href = "http://localhost:5500/home.html";
                    }
                })
                .then(result => console.log(result))
                .catch(error => console.log('error', error));
        }


function renderPage(authenticated) {
    var loginLink = document.getElementById("login-link")
    var logoutLink = document.getElementById("logout-link")
    var createPostButton = document.getElementById("create-post-btn")

    if (authenticated == "true") {
        loginLink.style.display = "none"
        logoutLink.style.display = "flex"
        getEmail();
        getCsrfToken()
    }
    else if (authenticated == "false") {
        loginLink.style.display = "flex"
        logoutLink.style.display = "none"
        createPostButton.style.display = "none"

    }
}

function getEmail() {
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
        })
        .catch(error => console.log('error', error));
}

function getCsrfToken() {
    fetch('http://localhost:8080/csrf-token', {
        method: 'GET',
        credentials: 'include',
    })
        .then(response => response.json())
        .then(data => {
            console.log(data.csrfToken)
        })
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}