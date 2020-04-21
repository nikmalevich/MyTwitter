let posts = [
    {
        id: '1',
        description: 'aaa',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'a',
        photoLink: 'https://www.pressball.by/images/stories/2020/03/20200310231542.jpg',
        hashTags: ['covid-19'],
        likes: ['b', 'c']
    },
    {
        id: '2',
        description: 'bbb',
        createdAt: new Date('2020-03-21T12:58:00'),
        author: 'nikmalevich',
        photoLink: 'https://images.aif.ru/017/670/9a6e8711e058b9c97bcbe0ef1061c82c.jpg',
        hashTags: ['cba', 'abc'],
        likes: ['a']
    },
];

class Model {
    _posts;

    static _postSchema = {
        id: val => typeof val === 'string',
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => ((typeof val === 'string') || (typeof val === 'undefined')),
        hashTags: val => Array.isArray(val),
        likes: val => Array.isArray(val)
    };

    constructor(posts = []) {
        this._posts = posts.filter(post => Model.validate(post));
    }

    static _validateSchema(validateOver = {}, post = {}) {
        if ((Object.keys(validateOver).length !== Object.keys(post).length) && (Object.keys(validateOver).length !== Object.keys(post).length + 1)) {
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
                if (Array.isArray(filterConfig[key])) {
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

        return filteredPosts.sort((a, b) => a.createdAt > b.createdAt ? 1 : -1).slice(skip, skip + top);
    }

    get(id = '') {
        return this._posts.find(post => post.id === id);
    }

    add(post = {}) {
        if (Model.validate(post)) {
            this._posts.push(post);

            return true;
        }

        return false;
    }

    edit(id = '', post = {}) {
        let oldPost = this.get(id);

        if (oldPost && Model._validateSchema(post, post)) {
            Object.keys(post).forEach(key => oldPost[key] = post[key]);

            return true;
        }

        return false;
    }

    remove(id = '') {
        let length = this._posts.length;
        this._posts = this._posts.filter(post => post.id !== id);

        return length !== this._posts.length;
    }

    addAll(posts = []) {
        return posts.filter(post => !this.add(post));
    }

    clear() {
        this._posts = [];
    }

    like(id = '', user = '') {
        let post = this.get(id);

        if (post.hashTags.find(hashTag => hashTag === user) === undefined) {
            post.hashTags.push(user);

            return true;
        }

        this._posts = post.hashTags.filter(hashTag => hashTag !== user);

        return false;
    }
}

class View {
    _postTemplate;
    _postContainer;
    _filterName;
    _filterTag;
    _filterDateFrom;
    _filterDateTo;
    _currentUser;

    constructor() {
        this._postTemplate = document.getElementById('postTemplate');
        this._postContainer = document.getElementById('postContainer');
        this._filterName = document.getElementById('filterName');
        this._filterTag = document.getElementById('filterTag');
        this._filterDateFrom = document.getElementById('filterDateFrom');
        this._filterDateTo = document.getElementById('filterDateTo');
        this._currentUser = 'nikmalevich';
    }

    _setPostView(postView = {}, post = {}) {
        if (this._currentUser !== post.author) {
            const children = postView.querySelector('div.post-action').children;

            children[0].setAttribute('style', 'display: none');
            children[1].setAttribute('style', 'display: none');
        }

        if (post.hashTags.find(hashTag => hashTag === post.author) === undefined) {
            postView.querySelector('div.post-action').lastElementChild.setAttribute('style', 'background-color: transparent');
        }

        postView.firstElementChild.id = post.id;
        postView.querySelector('p.post-text').textContent = post.description;
        postView.querySelector('p.post-tags').textContent = '#' + post.hashTags.join(' #');
        postView.querySelector('p.post-info').textContent = post.createdAt.toLocaleString() + ' ' + post.author;

        let image = postView.querySelector('img.post-image');

        if (post.photoLink) {
            image.setAttribute('src', post.photoLink);
        } else {
            image.setAttribute('style', 'display: none');
        }
    }

    displayPost(post = {}) {
        let postView = document.importNode(this._postTemplate.content, true);

        this._setPostView(postView, post);

        this._postContainer.insertBefore(postView, this._postContainer.lastElementChild);
    }

    editPost(id = '', post = {}) {
        let postView = document.importNode(this._postTemplate.content, true);

        this._setPostView(postView, post);

        document.getElementById(id)?.replaceWith(postView);
    }

    removePost(id = '') {
        document.getElementById(id)?.remove();
    }

    likePost(id = '') {
        document.getElementById(id)?.querySelector('div.post-action').lastElementChild.setAttribute('style', 'background-color: #000000');
    }

    dislikePost(id = '') {
        document.getElementById(id)?.querySelector('div.post-action').lastElementChild.setAttribute('style', 'background-color: transparent');
    }

    clearView() {
        let first = this._postContainer.firstElementChild;

        while (first !== this._postContainer.lastElementChild) {
            first.remove();

            first = this._postContainer.firstElementChild;
        }
    }
}

let model;
let view;

window.onload = () => {
    model = new Model(posts);
    view = new View();

    getPage(0, 10)
};

function addPost(post = {}) {
    if (model.add(post)) {
        view.displayPost(post);
    }
}

function editPost(id = '', post = {}) {
    if (model.edit(id, post)) {
        view.editPost(id, model.get(id));
    }
}

function removePost(id = '') {
    if (model.remove(id)) {
        view.removePost(id);
    }
}

function likePost(id = '', user = '') {
    if (model.like(id, user)) {
        view.likePost(id);
    } else {
        view.dislikePost(id);
    }
}

function getPage(skip = 0, top = 10, filters = {}) {
    view.clearView();

    model.getPage(skip, top, filters).forEach(post => view.displayPost(post));
}