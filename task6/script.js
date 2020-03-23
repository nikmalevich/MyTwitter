let posts = [
    {
        id: '1',
        description: 'Более 76 тыс. человек во всем мире уже излечились от заболевания, спровоцированного новым коронавирусом, тогда как количество смертей превысило 6,4 тыс.',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Иванов Иван',
        photoLink: 'https://www.pressball.by/images/stories/2020/03/20200310231542.jpg',
        hashTags: ['covid-19', 'смерть'],
        likes: ['Иванов Иван']
    },
    {
        id: '2',
        description: 'Я люблю есть',
        createdAt: new Date('2020-03-21T12:58:00'),
        author: 'Никита Малевич',
        photoLink: 'https://images.aif.ru/017/670/9a6e8711e058b9c97bcbe0ef1061c82c.jpg',
        hashTags: ['бургер', 'вкуснота'],
        likes: ['Марк Цукерберг']
    },
    {
        id: '3',
        description: 'Я люблю пить',
        createdAt: new Date('2020-02-21T13:02:00'),
        author: 'Андрей Малевич',
        photoLink: 'https://upload.wikimedia.org/wikipedia/commons/3/3e/Weizenbier.jpg',
        hashTags: ['пенное', 'Германия'],
        likes: ['Никита Малевич', 'Иванов Иван']
    },
    {
        id: '4',
        description: 'Я люблю играть в футбол',
        createdAt: new Date('2019-02-21T13:03:00'),
        author: 'Александр Дубиковский',
        photoLink: 'https://www.soccer.ru/sites/default/files/styles/content_image/public/blogs/records/news.88423.720x4071.jpg?itok=XvpSs4Ml',
        hashTags: ['мяч', 'гол'],
        likes: ['Никита Малевич', 'Лионель Месси']
    },
    {
        id: '5',
        description: 'Я люблю мыться',
        createdAt: new Date('2020-01-01T00:00:00'),
        author: 'Просто гена',
        hashTags: ['шампунь'],
        likes: []
    },
    {
        id: '6',
        description: 'Я люблю мыться',
        createdAt: new Date('2020-01-01T00:00:00'),
        author: 'Просто гена',
        hashTags: ['шампунь'],
        likes: []
    },
    {
        id: '7',
        description: 'Я люблю читать',
        createdAt: new Date('2017-05-15T20:00:00'),
        author: 'Николай Добролюбов',
        photoLink: 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/a6/Dobrolyubov.jpg/274px-Dobrolyubov.jpg',
        hashTags: ['книга', 'критика', 'знания'],
        likes: ['Николай Гумилев']
    },
    {
        id: '8',
        description: 'Моргенштерн - лучший',
        createdAt: new Date('2020-02-15T00:00:00'),
        author: 'Леша из 3Б',
        hashTags: ['качает', 'бит'],
        likes: ['Никита Малевич']
    },
    {
        id: '9',
        description: 'Лучший кофе в кофебаре Горячо',
        createdAt: new Date('2020-03-19T19:00:00'),
        author: 'Stasy How',
        photoLink: 'https://image.shutterstock.com/image-photo/cup-fresh-coffee-on-blue-600w-523523743.jpg',
        hashTags: ['капуч', 'латте', 'эспрессо'],
        likes: ['Никита Малевич', 'Александр Дубиковский', 'Кофеман']
    },
    {
        id: '10',
        description: 'Делаю задание по УП',
        createdAt: new Date('2020-03-21T13:26:00'),
        author: 'nikmalevich',
        hashTags: ['классно', 'интересно', 'захватывающе'],
        likes: ['Жанна Витальевна']
    },
    {
        id: '11',
        description: 'Сделал новую прическу',
        createdAt: new Date('2020-01-15T13:29:00'),
        author: 'barber',
        photoLink: 'https://vokrug.tv/pic/news/8/1/4/4/814454b9d6e78412ec9077fcf0fb3afd.jpeg',
        hashTags: [],
        likes: ['Гоша Гурщенков']
    },
    {
        id: '12',
        description: 'Смотрим с друзьями футбол',
        createdAt: new Date('2020-02-01T14:29:00'),
        author: 'Неймар',
        hashTags: ['пиво', 'чипсы', 'гол'],
        likes: ['Росс Баркли']
    },
    {
        id: '13',
        description: 'Купили батут',
        createdAt: new Date('2019-07-01T11:29:00'),
        author: 'Сергей Сергеев',
        photoLink: 'https://www.oma.by/upload/Sh/imageCache/21f/442/1f4553f7a2796b9bbe7957675cc1fee2.jpg',
        hashTags: ['отдых', 'relax'],
        likes: ['Просто гена']
    },
    {
        id: '14',
        description: 'Жарим шашлыки с мужиками и обмываем батут',
        createdAt: new Date('2019-07-01T13:29:00'),
        author: 'Сергей Сергеев',
        photoLink: 'https://www.koolinar.ru/all_image/recipes/54/54941/recipe_9109f8d8-43ca-4311-8927-9c75b74cb9bc_large.jpg',
        hashTags: ['мясо'],
        likes: ['Просто гена', 'Сергей Сергеев']
    },
    {
        id: '15',
        description: 'Гена пошел пробовать батут',
        createdAt: new Date('2019-07-01T17:29:00'),
        author: 'Сергей Сергеев',
        hashTags: [],
        likes: ['Просто гена', 'Андрей Малевич']
    },
    {
        id: '16',
        description: 'Гену увезли на скорой и нужен новый батут',
        createdAt: new Date('2019-07-01T17:30:00'),
        author: 'Сергей Сергеев',
        hashTags: ['минуснога', 'отпуск'],
        likes: ['Минздрав', 'Андрей Малевич', 'Дарвин']
    },
    {
        id: '17',
        description: 'Романтик на 14 февраля',
        createdAt: new Date('2020-02-14T18:30:00'),
        author: 'Дима Минский',
        photoLink: 'https://img3.imgbb.ru/a/4/0/a4023118d37399a5d193403fd0998738.jpg',
        hashTags: ['свечи', 'любовь', 'вместенавсегда'],
        likes: ['Святой Валентин']
    },
    {
        id: '18',
        description: 'Покоряем Эверест вместе',
        createdAt: new Date('2019-12-14T11:30:00'),
        author: 'Саша Смирнов',
        hashTags: ['горы'],
        likes: []
    },
    {
        id: '19',
        description: 'Воюем с короновирусом!',
        createdAt: new Date('2020-03-17T15:37:00'),
        author: 'Александр Лукашенко',
        photoLink: 'https://cdn1.img.inosmi.ru/images/23851/66/238516650.jpg',
        hashTags: ['против всех полезней'],
        likes: ['Владимир Путин']
    },
    {
        id: '20',
        description: 'Учиться, учиться и еще раз учиться',
        createdAt: new Date('1917-03-17T15:37:00'),
        author: 'Владимир Ильич Ленин',
        hashTags: [],
        likes: []
    }
];

let postsOperations = (function () {
    let postSchema = {
        id: val => typeof val === 'string',
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => ((typeof val === 'string') || (typeof val === 'undefined')),
        hashTags: val => Array.isArray(val),
        likes: val => Array.isArray(val)
    };

    function validateSchema(validateOver, post) {
        if ((Object.keys(validateOver).length !== Object.keys(post).length) && (Object.keys(validateOver).length !== Object.keys(post).length + 1)) {
            console.log('Mismatching number of keys!');

            return false;
        }

        let errors = Object.keys(validateOver)
            .filter(key => !postSchema[key]?.(post[key]))
            .map(key => new Error(key + ' is invalid!'));

        if (errors.length > 0) {
            errors.forEach(error => console.log(error.message));

            return false;
        }

        return true;
    }

    function validatePost(post) {
        return validateSchema(postSchema, post);
    }

   function getPosts(skip, top, filterConfig) {
       let filteredPosts = posts;

       if (filterConfig) {
            if (!validateSchema(filterConfig, filterConfig)) {
                console.log("Wrong filterConfig");

                return [];
            }

            filteredPosts = posts.filter(post => {
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
            })
        }

        skip = skip || 0;
        top = top || 10;
        
        return filteredPosts.
            sort((a, b) => a.createdAt > b.createdAt ? 1 : -1).
            slice(skip, skip + top);
   }

    function getPost(id) {
        return posts.find(post => post.id === id);
    }

    function addPost(post) {
        if (validatePost(post)) {
            posts.push(post);

            return true;
        }

        return false;
    }
    
    function editPost(id, post) {
        let oldPost = getPost(id);

        if (oldPost && validateSchema(post, post)) {
            Object.keys(post).forEach(key => oldPost[key] = post[key]);

            return true;
        }

        return false;
    }

    function removePost(id) {
        let length = posts.length;

        posts = posts.filter(post => post.id !== id);

        return length !== posts.length;
    }

    return {
        getPosts: getPosts,
        getPost: getPost,
        validatePost: validatePost,
        addPost: addPost,
        editPost: editPost,
        removePost: removePost
    }
}());

console.log(postsOperations.getPosts());
console.log(postsOperations.getPosts(3, 4));
console.log(postsOperations.getPosts(0, 10, {description: 'Я люблю есть'}));
console.log(postsOperations.getPosts(0, 4, {
    hashTags: ['гол']
}));
console.log(postsOperations.getPost('15'));
console.log(postsOperations.validatePost(posts[4]));
console.log(postsOperations.validatePost({
    author: 'Сергей Сергеев',
    hashTags: ['мясо', 'отпуск']
}));
console.log(postsOperations.addPost({
    id: '21',
    description: 'Учиться, учиться и еще раз учиться',
    createdAt: new Date('1917-03-17T15:37:00'),
    author: 'Владимир Ильич Ленин',
    hashTags: [],
    likes: []
}));
console.log(postsOperations.editPost('3', {createdAt: new Date('2020-02-21T13:03:00')}));
console.log(postsOperations.removePost('1'));
console.log(posts);