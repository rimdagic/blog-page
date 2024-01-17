var postsList = document.getElementById("posts-list");

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
        .then(result => console.log(result))
        .catch(error => console.log('error', error));
}