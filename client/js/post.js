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