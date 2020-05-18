class Model {
    _curNumFilterPosts;

    async getPage(filterConfig = {}) {
        let response = await fetch('/posts', {
            method: 'POST',
            body: JSON.stringify(filterConfig),
            headers: {
                'Content-Type': 'application/json',
            }
        });

        let result = response.json();
        this._curNumFilterPosts = result.length;

        return result;
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

    like(id = '', user = '') {
        let post = this.get(id);

        post.likes.push(user);

        localStorage.removeItem(id);
        localStorage.setItem(id, JSON.stringify(post));
    }

    dislike(id = '', user = '') {
        let post = this.get(id);

        post.likes = post.likes.filter(like => like !== user);

        localStorage.removeItem(id);
        localStorage.setItem(id, JSON.stringify(post));
    }
}