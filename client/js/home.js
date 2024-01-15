
var postsList = document.getElementById("posts-list");


// MOCKDATA
var blogPosts = [
    {
        headline: "Denna lista är mockdata från js-filen",
        content: "Lorem ipsum dolor sit amet",
        id: 3
    },
    {
        headline: "Bästa hamburgerstället",
        content: "content, loremv ipsum",
        id: 1
    },
    {
        headline: "Hur man bakar den godaste kladdkakan",
        content: "Lorem ipsum osv",
        id:2
    },
    {
        headline: "Annanas på pizza, är det verkligen gott?",
        content: "Lorem ipsum dolor sit amet",
        id: 3
    }
]




for (var i = 0; i < blogPosts.length; i++) {
    var listItem = document.createElement('li');
    
    listItem.innerHTML = blogPosts[i].headline;

    postsList.appendChild(listItem);
}