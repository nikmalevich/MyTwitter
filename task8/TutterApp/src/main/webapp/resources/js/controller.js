class Controller {
    static _view;
    static _model;
    static _curUser;
    static _curFilter;
    static _curPost;
    static _curPostID;
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

        Controller._curUser = {};
        Controller._curUser.id = 0;
        Controller._curFilter = {};
        Controller._curFilter.fromDate = new Date('1995-12-17T03:24:00');
        Controller._curFilter.toDate = new Date(Date.now());
        Controller._curFilter.quantity = 10;
        Controller._curPost = null;
        Controller._users = new Map();
        Controller._users.set('nikmalevich', '1111');
        Controller._users.set('anna', '2222');

        Controller.getPage(Controller._curFilter);
    }

    static getPage(filters = {}) {
        Controller._view.clearView();

        Controller._model.getPage(filters).forEach(post => Controller._view.displayPost(post));

        if (Controller._model._curNumFilterPosts <= Controller._curFilter.quantity) {
            Controller._view._seeMoreButton.setAttribute('style', 'display: none');
        } else {
            Controller._view._seeMoreButton.setAttribute('style', 'display: block');
        }
    }

    static logo() {
        Controller._view._mainPage.setAttribute('style', 'display: block');
        Controller._view._postPage.setAttribute('style', 'display: none');
        Controller._view._logInPage.setAttribute('style', 'display: none');
        Controller._view._incorrectPostData.setAttribute('style', 'visibility: hidden');
        Controller._view._incorrectLogInData.setAttribute('style', 'visibility: hidden');
        Controller._view._postText.value = '';
        Controller._view._postTags.value = '';
        Controller._view._userLogin.value = '';
        Controller._view._userPassword.value = '';

        Controller.getPage(Controller._curFilter);
    }

    static logInOut() {
        if (Controller._curUser.id === 0) {
            Controller._view._mainPage.setAttribute('style', 'display: none');
            Controller._view._postPage.setAttribute('style', 'display: none');
            Controller._view._logInPage.setAttribute('style', 'display: block');
        } else {
            Controller._curUser.id = 0;
            Controller._view._logInOutButton.textContent = 'Log in';
            Controller._view._currentUser.textContent = '';
            Controller._view._addPostButton.setAttribute('style', 'display: none');
            Controller._view._addPostButton.disabled = true;

            Controller.getPage(Controller._curFilter);
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
            Controller._curFilter.descriptionHashTags = hashTags.split(' ');
        }

        Controller._curFilter.fromDate = new Date(dateFrom);
        Controller._curFilter.toDate = new Date(dateTo);

        Controller.getPage(Controller._curFilter);
    }

    static postAction(event) {
        Controller._curPostID = event.target.parentElement.parentElement.id;

        switch (event.target.className) {
            case 'fas fa-times delete':
                if (Controller._model.remove(Controller._curPostID)) {
                    Controller._view.removePost(Controller._curPostID);
                }

                break;
            case 'fas fa-ellipsis-v edit':
                Controller._view._mainPage.setAttribute('style', 'display: none');
                Controller._view._postPage.setAttribute('style', 'display: block');

                Controller._curPost = Controller._model.get(Controller._curPostID);

                let strHashTags = '#';

                Controller._curPost.hashTags.forEach(hashTag => strHashTags += hashTag.description.concat(' #'));
                strHashTags = strHashTags.substr(0, strHashTags.length - 2);

                Controller._view._postText.value = Controller._curPost.description;
                Controller._view._postTags.value = strHashTags;

                break;
            case 'fas fa-heart like':
                Controller._model.dislike(Controller._curPostID, Controller._curUser);
                Controller._view.dislikePost(Controller._curPostID);

                break;
            case 'far fa-heart like':
                Controller._model.like(Controller._curPostID, Controller._curUser);
                Controller._view.likePost(Controller._curPostID);

                break;
        }
    }

    static seeMore() {
        Controller._curFilter.quantity += 10;

        Controller.getPage(Controller._curFilter);
    }

    static addPost(event) {
        Controller._curPost = null;

        Controller._view._mainPage.setAttribute('style', 'display: none');
        Controller._view._postPage.setAttribute('style', 'display: block');

        event.preventDefault();
    }

    static donePost() {
        let description = Controller._view._postText.value;
        let hashTags = Controller._view._postTags.value.split(' ');

        if (description === '' || description.length > 200) {
            Controller._view._incorrectPostData.setAttribute('style', 'visibility: visible');

            return;
        }

        Controller._view._incorrectPostData.setAttribute('style', 'visibility: hidden');

        let post = {
            description: description,
            descriptionHashTags: hashTags
        };

        if (Controller._curPost === null) {
            post.author = Controller._curUser;

            Controller._model.add(post);
        } else {
            post.id = Controller._curPostID;

            Controller._model.edit(post);
        }

        Controller._view._postText.value = '';
        Controller._view._postTags.value = '';

        Controller._view._postPage.setAttribute('style', 'display: none');
        Controller._view._mainPage.setAttribute('style', 'display: block');

        Controller.getPage(Controller._curFilter);
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
            Controller._view._addPostButton.disabled = false;

            Controller.getPage(0, Controller._curNumVisiblePosts, Controller._curFilter);
        } else {
            Controller._view._incorrectLogInData.setAttribute('style', 'visibility: visible');
        }
    }
}

window.onload = () => {
    new Controller();
};