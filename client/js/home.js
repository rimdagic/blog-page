var postsList = document.getElementById("posts-list");
var emailDisplay = document.getElementById("email-display");


adjustToAuth();





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

// Function to render blog posts in the HTML.
function renderBlogPosts(blogPosts) {
    for (var i = 0; i < blogPosts.length; i++) {
        var listItem = document.createElement('li');

        // Create a clickable link with an onclick event to navigate to the post page.
        var link = document.createElement('a');
        link.href = 'post.html?id=' + blogPosts[i].id; // Assuming you have a page named 'post.html'
        link.textContent = blogPosts[i].headline;

        // Append the link to the list item.
        listItem.appendChild(link);

        // Append the list item to the posts list.
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

function deleteAll(){
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
    // Fetch a new CSRF token
    fetch('http://localhost:8080/csrf-token', {
        method: 'GET',
        credentials: 'include',
    })
    .then(response => response.json())
    .then(data => {
        // Use the newly obtained CSRF token in the logout request
        var csrfToken = data.csrfToken;
        console.log(csrfToken)



        var requestOptions = {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': "O0m7WrjPVE6hQvSIrOiW_SvpXpd18YU2-hVCC_qZn26etiXSC3iJY939YnyMcMLsncWizk2Kc_YRwrAbwyUmM876rgvC00Ow"
            },
        };

        
    fetch("http://localhost:8080/user/logout", requestOptions)
    .then(response => {
        response.text()
        if(response.status === 200){
        //    window.location.href = "http://localhost:5500/home.html";
        }
    })
    .then(result => console.log(result))
    .catch(error => console.log('error', error));
})
}


      //console.log(csrfToken)


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
            //emailDisplay.appendChild;
        }
            
            )
        .catch(error => console.log('error', error));
}



