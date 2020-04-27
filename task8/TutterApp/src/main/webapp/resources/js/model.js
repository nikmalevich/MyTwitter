class Model {
    _posts;
    _curNumFilterPosts;

    static _postSchema = {
        id: val => typeof val === 'string',
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        createdFromTo: val => (Array.isArray(val) || typeof val === 'undefined'),
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => ((typeof val === 'string') || (typeof val === 'undefined')),
        hashTags: val => Array.isArray(val),
        likes: val => Array.isArray(val)
    };

    constructor() {
        let keyPosts = Object.keys(localStorage);
        this._posts = [];

        for (let i = 0; i < keyPosts.length; i++) {
            let post = JSON.parse(localStorage.getItem(keyPosts[i]));

            post.createdAt = new Date(post.createdAt);

            this._posts.push(post);
        }
    }

    static _validateSchema(validateOver = {}, post = {}) {
        if ((Object.keys(post).length > Object.keys(validateOver).length) || (Object.keys(post).length < Object.keys(validateOver).length - 2)) {
            console.log('Mismatching number of keys!');

            return false;
        }

        let errors = Object.keys(validateOver)
            .filter(key => !(Model._postSchema)[key]?.(post[key]))
            .map(key => new Error(key + ' is invalid!'));

        if (errors.length > 0) {
            errors.forEach(error => console.log(error.message));

            return false;
        }

        return true;
    }

    static validate(post = {}) {
        return Model._validateSchema(Model._postSchema, post);
    }

    getPage(skip = 0, top = 10, filterConfig = {}) {
        if (!Model._validateSchema(filterConfig, filterConfig)) {
            console.log('Wrong filterConfig');

            return [];
        }

        let filteredPosts = this._posts.filter(post => {
            for (let key in filterConfig) {
                if (key === 'createdFromTo') {
                    return (post.createdAt >= filterConfig[key][0]) && (post.createdAt <= filterConfig[key][1]);
                } else if (Array.isArray(filterConfig[key])) {
                    for (let property in filterConfig[key]) {
                        if (!post[key].find(elem => elem === filterConfig[key][property])) {
                            return false;
                        }
                    }
                } else {
                    if (post[key] !== filterConfig[key]) {
                        return false;
                    }
                }
            }

            return true;
        });

        this._curNumFilterPosts = filteredPosts.length;

        return filteredPosts.sort((a, b) => a.createdAt > b.createdAt ? 1 : -1).slice(skip, skip + top);
    }

    get(id = '') {
        return this._posts.find(post => post.id === id);
    }

    add(post = {}) {
        console.log(post);

        if (Model.validate(post)) {
            this._posts.push(post);
            localStorage.setItem(post.id, JSON.stringify(post));

            return true;
        }

        return false;
    }

    edit(id = '', post = {}) {
        let oldPost = this.get(id);

        if (oldPost && Model._validateSchema(post, post)) {
            Object.keys(post).forEach(key => oldPost[key] = post[key]);
            localStorage.removeItem(id);
            localStorage.setItem(id, JSON.stringify(oldPost));

            return true;
        }

        return false;
    }

    remove(id = '') {
        let length = this._posts.length;
        this._posts = this._posts.filter(post => post.id !== id);

        if (length !== this._posts.length) {
            localStorage.removeItem(id);

            return true;
        }

        return false;
    }

    addAll(posts = []) {
        return posts.filter(post => !this.add(post));
    }

    clear() {
        this._posts = [];
        localStorage.clear();
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