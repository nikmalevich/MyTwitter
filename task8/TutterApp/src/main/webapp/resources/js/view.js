class View {
    _logo;
    _logInOutButton;
    _mainPage;
    _postTemplate;
    _postContainer;
    _filterForm;
    _filterName;
    _filterTag;
    _filterDateFrom;
    _filterDateTo;
    _seeMoreButton;
    _addPostButton;
    _postPage;
    _postText;
    _postTags;
    _donePostButton;
    _incorrectPostData;
    _logInPage;
    _userLogin;
    _userPassword;
    _logInButton;
    _incorrectLogInData;
    _currentUser;

    constructor() {
        this._logo = document.getElementById('logo');
        this._logInOutButton = document.getElementById('logInOut');
        this._mainPage = document.getElementById('mainPage');
        this._postTemplate = document.getElementById('postTemplate');
        this._postContainer = document.getElementById('postContainer');
        this._filterForm = document.getElementById('filterForm');
        this._filterName = document.getElementById('filterName');
        this._filterTag = document.getElementById('filterTag');
        this._filterDateFrom = document.getElementById('filterDateFrom');
        this._filterDateTo = document.getElementById('filterDateTo');
        this._seeMoreButton = document.getElementById('seeMore');
        this._addPostButton = document.getElementById('addPost');
        this._postPage = document.getElementById('postPage');
        this._postText = document.getElementById('postText');
        this._postTags = document.getElementById('postTags');
        this._donePostButton = document.getElementById('done');
        this._incorrectPostData = document.getElementById('incorrectPostData');
        this._logInPage = document.getElementById('logInPage');
        this._userLogin = document.getElementById('userLogin');
        this._userPassword = document.getElementById('userPassword');
        this._logInButton = document.getElementById('login');
        this._incorrectLogInData = document.getElementById('incorrectLogInData');
        this._currentUser = document.getElementById('username');

        this._postPage.setAttribute('style', 'display: none');
        this._logInPage.setAttribute('style', 'display: none');
    }

    _setPostView(postView = {}, post = {}) {
        if (Controller._curUser !== post.author) {
            const children = postView.querySelector('div.post-action').children;

            children[0].setAttribute('style', 'display: none');
            children[1].setAttribute('style', 'display: none');

            if (Controller._curUser === '') {
                children[2].setAttribute('style', 'display: none');
            }
        }

        if (post.likes.find(like => like === Controller._curUser) === undefined) {
            postView.querySelector('div.post-action').lastElementChild.className = 'far fa-heart like';
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
        document.getElementById(id)?.querySelector('div.post-action').lastElementChild.classList.replace('far', 'fas');
    }

    dislikePost(id = '') {
        document.getElementById(id)?.querySelector('div.post-action').lastElementChild.classList.replace('fas', 'far');
    }

    clearView() {
        let first = this._postContainer.firstElementChild;

        while (first !== this._postContainer.lastElementChild) {
            first.remove();

            first = this._postContainer.firstElementChild;
        }
    }
}