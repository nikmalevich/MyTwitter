class Controller {
    static _view;
    static _model;
    static _curUser;
    static _curNumVisiblePosts;
    static _curFilter;
    static _curPost;
    static _users;

    constructor() {
        Controller._model = new Model();
        Controller._view = new View();

        Controller._view._logo.addEventListener('click', Controller.logo);
        Controller._view._logInOutButton.addEventListener('click', Controller.logInOut);
        Controller._view._filterForm.addEventListener('input', Controller.filterInput);
        Controller._view._postContainer.addEventListener('click', Controller.postAction);
        Controller._view._seeMoreButton.addEventListener('click', Controller.seeMore);
        Controller._view._addPostButton.addEventListener('click', Controller.addPost);
        Controller._view._donePostButton.addEventListener('click', Controller.donePost);
        Controller._view._logInButton.addEventListener('click', Controller.logIn);

        Controller._curUser = document.getElementById('username').textContent;
        Controller._curNumVisiblePosts = 10;
        Controller._curFilter = {};
        Controller._curPost = null;
        Controller._users = new Map();
        Controller._users.set('nikmalevich', '1111');
        Controller._users.set('anna', '2222');

        Controller.getPage(0, Controller._curNumVisiblePosts);
    }

    static getPage(skip = 0, top = 10, filters = {}) {
        Controller._view.clearView();

        Controller._model.getPage(skip, top, filters).forEach(post => Controller._view.displayPost(post));

        if (Controller._model._curNumFilterPosts <= Controller._curNumVisiblePosts) {
            Controller._view._seeMoreButton.setAttribute('style', 'display: none');
        } else {
            Controller._view._seeMoreButton.setAttribute('style', 'display: block');
        }
    }

    static logo(event) {
        Controller._view._mainPage.setAttribute('style', 'display: block');
        Controller._view._postPage.setAttribute('style', 'display: none');
        Controller._view._logInPage.setAttribute('style', 'display: none');
        Controller._view._incorrectPostData.setAttribute('style', 'visibility: hidden');
        Controller._view._incorrectLogInData.setAttribute('style', 'visibility: hidden');
        Controller._view._postText.value = '';
        Controller._view._postTags.value = '';
        Controller._view._userLogin.value = '';
        Controller._view._userPassword.value = '';

        Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
    }

    static logInOut(event) {
        if (Controller._curUser === '') {
            Controller._view._mainPage.setAttribute('style', 'display: none');
            Controller._view._postPage.setAttribute('style', 'display: none');
            Controller._view._logInPage.setAttribute('style', 'display: block');
        } else {
            Controller._curUser = '';
            Controller._view._logInOutButton.textContent = 'Log in';
            Controller._view._currentUser.textContent = '';
            Controller._view._addPostButton.setAttribute('style', 'display: none');

            Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
        }
    }

    static filterInput(event) {
        let formElements = event.currentTarget.elements;

        let name = formElements[0].value;
        let hashTags = formElements[1].value;
        let dateFrom = formElements[2].value;
        let dateTo = formElements[3].value;

        Controller._curFilter = {};

        if (name !== '') {
            Controller._curFilter.author = name;
        }
        if (hashTags !== '') {
            Controller._curFilter.hashTags = hashTags.split(' ');
        }

        Controller._curFilter.createdFromTo = [new Date(dateFrom), new Date(dateTo)];

        Controller.getPage(0, 10, Controller._curFilter);
    }

    static postAction(event) {
        let id = event.target.parentElement.parentElement.id;

        switch (event.target.className) {
            case 'fas fa-times delete':
                if (Controller._model.remove(id)) {
                    Controller._view.removePost(id);
                }

                break;
            case 'fas fa-ellipsis-v edit':
                Controller._view._mainPage.setAttribute('style', 'display: none');
                Controller._view._postPage.setAttribute('style', 'display: block');

                Controller._curPost = Controller._model.get(id);

                Controller._view._postText.value = Controller._curPost.description;
                Controller._view._postTags.value = Controller._curPost.hashTags.join(' ');

                break;
            case 'fas fa-heart like':
                Controller._model.dislike(id, Controller._curUser);
                Controller._view.dislikePost(id);

                break;
            case 'far fa-heart like':
                Controller._model.like(id, Controller._curUser);
                Controller._view.likePost(id);

                break;
        }
    }

    static seeMore(event) {
        Controller._curNumVisiblePosts += 10;

        Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
    }

    static addPost(event) {
        Controller._curPost = null;

        Controller._view._mainPage.setAttribute('style', 'display: none');
        Controller._view._postPage.setAttribute('style', 'display: block');

        event.preventDefault();
    }

    static donePost(event) {
        let description = Controller._view._postText.value;
        let hashTags = Controller._view._postTags.value.split(' ');

        if (description === '' || description.length > 200) {
            Controller._view._incorrectPostData.setAttribute('style', 'visibility: visible');

            return;
        }

        Controller._view._incorrectPostData.setAttribute('style', 'visibility: hidden');

        let post = {
            description: description,
            hashTags: hashTags
        };

        if (Controller._curPost === null) {
            post.id = Math.random().toString(36).substr(2, 9);
            post.createdAt = new Date(Date.now());
            post.author = Controller._curUser;
            post.likes = [];

            Controller._model.add(post);
        } else {
            Controller._model.edit(Controller._curPost.id, post);
        }

        Controller._view._postText.value = '';
        Controller._view._postTags.value = '';

        Controller._view._postPage.setAttribute('style', 'display: none');
        Controller._view._mainPage.setAttribute('style', 'display: block');

        Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
    }

    static logIn() {
        let login = Controller._view._userLogin.value;
        let password = Controller._view._userPassword.value;

        Controller._view._userLogin.value = '';
        Controller._view._userPassword.value = '';

        if (Controller._users.get(login) === password) {
            Controller._curUser = login;
            Controller._view._currentUser.textContent = login;

            Controller._view._incorrectLogInData.setAttribute('style', 'visibility: hidden');
            Controller._view._logInPage.setAttribute('style', 'display: none');
            Controller._view._mainPage.setAttribute('style', 'display: block');
            Controller._view._logInOutButton.textContent = 'Log out';
            Controller._view._addPostButton.setAttribute('style', 'display: block');

            Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
        } else {
            Controller._view._incorrectLogInData.setAttribute('style', 'visibility: visible');
        }
    }
}

window.onload = () => {
    new Controller();
};