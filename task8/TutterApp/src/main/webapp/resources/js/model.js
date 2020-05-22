class Model {
    async getPage(filterConfig = {}) {
        let response = await fetch('/posts', {
            method: 'POST',
            body: JSON.stringify(filterConfig),
            headers: {
                'Content-Type': 'application/json',
            }
        });

        return response.json();
    }

    async countPosts() {
        let response = await fetch('/posts');

        return response.json();
    }

    async get(id = 0) {
        let response = await fetch(`/post?id=${id}`);

        return response.json();
    }

    async add(post = {}) {
        let response = await fetch('/post', {
            method: 'POST',
            body: JSON.stringify(post),
            headers: {
                'Content-Type': 'application/json',
            }
        });

        return response.json();
    }

    async edit(post = {}) {
        let response = await fetch('/post', {
            method: 'PUT',
            body: JSON.stringify(post),
            headers: {
                'Content-Type': 'application/json',
            }
        });

        return response.json();
    }

    async remove(id = 0) {
        let response = await fetch(`/post?id=${id}`, {
            method: 'DELETE'
        });

        return response.json();
    }

    async like(postID = 0, userID = 0) {
        let response = await fetch(`/like?postID=${postID}&userID=${userID}`);

        return response.json();
    }

    async dislike(postID = 0, userID = 0) {
        let response = await fetch(`/dislike?postID=${postID}&userID=${userID}`);

        return response.json();
    }

    async getUserID(name = '') {
        let response = await fetch(`/user?name=${name}`);

        return response.json();
    }
}