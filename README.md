# Stylyts2
**STYLUS BACKEND DOCUMENTATION**
----

 - User
	 - [Регистрация](#регистрация)
	 - [Логин](#логин)
	 - [Забыли пароль](#забыли-пароль)
	 - [Профиль](#профиль)
	 - [Изменить данные](#изменить-данные)
	 - [Стандартная модель пользователя](#стандартная-модель-пользователя)
	 - [Расширенная Модель пользователя](#расширенная-модель-пользователя)
- Магазин Детализация (6 модуль)
- [Конструктор (8 модуль)](#конструктор)

**Регистрация**
----
 - **/api/auth/registration/*

	Регистрация  
	 
 - **Method:**
	  `POST`
  
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| email(required) |  string | Электронная почта | -
	| password(required) |  string | Пароль | -
	| name(required) | string  |   Имя | -
	| last_name (required) |string | Фамилия |    -  |
	| username | string | Никнейм |    -  |
	| should_send_mail (required) | boolean | Получать рассылки по почте  |  true  |
	| date_of_birth (required) |string (YYYY-MM-DD) | День рождениия |    - |


 - **Success Response:**
	 После успешеного запроса берете поле **token** для дальнейшей Авторизации, и передаете в **headers** как `"Authorization": "JWT <token>"`
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		 {
             "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo4LCJ1c2VybmFtZSI6ImV4YW1wbGUxQG1haWwuY29tIiwiZXhwIjoxNjE1NTQ0MTg1LCJlbWFpbCI6ImV4YW1wbGUxQG1haWwuY29tIn0.NjW_BiIqhT-cT_32A_sJOaAzKiqGNb5_CLxSPHmpqts",
             "user": {
                 "id": 8,
                 "avatar": null,
                 "username": "username",
                 "name": "Galizhan",
                 "last_name": "Tolybayev",
                 "email": "galix.kz@gmail.kz133",
                 "brand": false,
                 "date_of_birth": "2001-01-01",
                 "gender": "",
                 "is_active": true,
                 "verification_uuid": "9ca7f67c-05ee-431a-a0fb-460f03993f29"
             }
         }
 
 - **Error Response:**

	 - **Code:** 400 invalid  <br />
	 - **Content:**
	  ```
	  {
          "email": [
              "user с таким email уже существует."
          ],
          "username": [
              "user с таким username уже существует."
          ]
      }

**Логин**
----
 - **/api/auth/login/**

	  проверка логина и пароля и возвращает токен
	   

 - **Method:**
	  `POST`
  
  - **Required:**
	   `username=[string]`
	   `password=[string]`
	   

 - **Success Response:**
	  Добавьте полученный токен в headers `"Authorization": "JWT <token>"`
	  
	 - **Code:** 200 <br />
	 - **Content:** 
	 ```
	 {
         "id": 8,
         "avatar": null,
         "username": "username",
         "name": "Galizhan",
         "last_name": "Tolybayev",
         "email": "galix.kz@gmail.kz133",
         "brand": false,
         "date_of_birth": "2001-01-01",
         "gender": "",
         "is_active": true,
         "verification_uuid": "9ca7f67c-05ee-431a-a0fb-460f03993f29"
    }
 - **Error Response:**

	 - **Code:** 400 Value Error <br />
	 -   **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "username",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
			}
	
	
	---
	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		```
		{
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}
**Забыли пароль**
----
Нужно сперва сперва запросить [сбор пароля](#сброс-пароля), после придет на почту ссылка на восставновление пароля который проверяет через какое устройтсво человек заходит.<br/>
Если ПК то открывается веб интерфейс <br/>
Если Мобильное устройство то перенаправляет по `kz.Stylyts://recover/<token>`, дальше вам надо [установить новый пароль](#установка-нового-пароля)

**Сброс пароля**
----
 - **/api/auth/generate_forgot_password/*

	Сброс пароля для пользователя  
	 
 - **Method:**
	  `POST`
  
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| email(required) |  string | Электронная почта | -
	


 - **Success Response:**
	В целом тут не важно что придет пока что ставлю токен
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		{
		    "token": "efff0bf4-123d-485e-9ce1-2f2dd3373918"
		}
 
 - **Error Response:**

	 - **Code:** 404 user_not_found  <br />
	 - **Content:**
	  ```
			{
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "user_not_found",
		            "message": "Пользователь не найден"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Пользователь не найден",
		    "error_code": "user_not_found"
		}

**Установка нового пароля**
----
 - **/api/auth/set_new_password/*

	Установка нового пароля для пользователя
	 
 - **Method:**
	  `POST`
  
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| token(required) |  string | Токен который вы получили с прошлого роута | -
	| password(required) |  string | Новый пароль | -
	

[Профиль](#профиль)
 - **Success Response:**
	В целом тут не важно что придет 
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		{
		    "ok": "ok"
		}
 - **Error Response:**

	 - **Code:** 400 user_not_found  <br />
	 - **Content:**
	  ```
			{
			    "errors": [
			        {
			            "field": "token",
			            "error_code": "invalid",
			            "message": "Must be a valid UUID."
			        }
			    ],
			    "status_code": 400,
			    "detail": "Invalid input.",
			    "error_code": "invalid"
			}

**Информация о себе**
----
 - **/api/auth/profile/me/**

	  Информация о пользователя который залогинился

 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "JWT <token>"`
	   

 - **Success Response:**
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
         {
             "id": 8,
             "avatar": null,
             "username": "username",
             "name": "Galizhan",
             "last_name": "Tolybayev",
             "email": "galix.kz@gmail.kz133",
             "brand": false,
             "date_of_birth": "2001-01-01",
             "gender": "",
             "is_active": true,
             "verification_uuid": "9ca7f67c-05ee-431a-a0fb-460f03993f29"
         }
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
             "detail": "Учетные данные не были предоставлены."
         }

         {
             "detail": "Signature has expired."
         }

**Изменить данные**
----
 - **/api/auth/profile/edit/*

	Изменить данные
	 
 - **Method:**
	  `PATCH`
  
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id пользователя | -
	| [Любое поле из стандартной модели пользователя](#стандартная-модель-пользователя) |  - |  [Любое поле из стандартной модели пользователя](#стандартная-модель-пользователя) | -
	

 - **Success Response:**
	Изменные данные 
	 - **Code:** 200 <br />
	 - **Content:** 
		```
		{
             "id": 8,
             "avatar": null,
             "username": "username",
             "name": "Galizhan",
             "last_name": "Tolybayev",
             "email": "galix.kz@gmail.kz133",
             "brand": false,
             "date_of_birth": "2001-01-01",
             "gender": "",
             "is_active": true,
             "verification_uuid": "9ca7f67c-05ee-431a-a0fb-460f03993f29"
        }
 - **Error Response:**

	 - **Code:** 400 user_not_found  <br />
	 - **Content:**
	  ```
			{
			    "errors": [
			        {
			            "field": "token",
			            "error_code": "invalid",
			            "message": "Must be a valid UUID."
			        }
			    ],
			    "status_code": 400,
			    "detail": "Invalid input.",
			    "error_code": "invalid"
			}

**Информация о другом пользователе**
----
 - **/api/auth/profile/:id/**

	  Информация о другом пользователе по айдишке. Тут важно учесть поле "role", она может быть user/brand и в зависимости от него выводить отображать.

 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "JWT <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id пользователя | -
 - **Success Response:**
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		 {
              "id": 8,
              "avatar": null,
              "username": "username",
              "name": "Galizhan",
              "last_name": "Tolybayev",
              "email": "galix.kz@gmail.kz133",
              "brand": false,
              "date_of_birth": "2001-01-01",
              "gender": "",
              "is_active": true,
              "verification_uuid": "9ca7f67c-05ee-431a-a0fb-460f03993f29"
         }
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_authenticated",
	            "message": "Учетные данные не были предоставлены."
	        }
	    ],
	    "status_code": 401,
	    "detail": "Учетные данные не были предоставлены.",
	    "error_code": "not_authenticated"
	}

**Профиль пользователя**
----
 Получаете [информацию о cебе](#информация-о-себе) либо о [другому пользователе](#информация-о-другом-пользователе), если поле `role="user"` потом запрашиваете [свои образы](#получить-список-образов) передав поле `author=<user_id>`, так же можете получить свои [вещи в гардеробе](#получить-вещи-в-гардеробе)

 **Профиль пользователя**
----
 Получаете [информацию о cебе](#информация-о-себе) либо о [другому пользователе](#информация-о-другом-пользователе), если поле `role="brand"` потом запрашиваете [свои образы](#получить-список-образов) передав поле `author=<user_id>`, потом можете фильтровать [вещи по бренду](#cписок-предметов) передав поле `brand=<user_id>`
 
**Стандартная Модель пользователя**
----
	```
	{
        "id": 1,
        "is_brand": false,
        "last_login": null,
        "role": "user",
        "email": "galix.kz@gmail.com1",
        "first_name": "Galizhan",
        "last_name": "Tolybayev",
        "date_of_birth": "1998-12-31",
        "is_verified": false,
        "should_send_mail": false,
        "verification_uuid": "c1f4c5b4-11c9-4d92-b7d4-96e6424e1ed8",
        "created": "2020-11-23T11:46:02.025895+06:00",
        "modified": "2020-11-23T11:46:02.025931+06:00",
        "avatar": null
    },
**Расширенная Модель пользователя**
----
	```
	{
	    "id": 17,
	    "followings_count": 0,
	    "followers_count": 0,
	    "is_brand": false,
	    "last_login": null,
	    "role": "user",
	    "email": "galix.kz@gmail.kz133",
	    "first_name": "Galizhan",
	    "last_name": "Tolybayev",
	    "date_of_birth": "1998-12-31",
	    "is_verified": false,
	    "should_send_mail": false,
	    "verification_uuid": "3cf02311-d8fc-42ea-9a64-83a9fb33d880",
	    "created": "2020-12-25T09:31:30.874573+06:00",
	    "modified": "2020-12-25T09:31:30.874598+06:00",
	    "avatar": null
	},
**Магазин Детализация**
----
пути для получения данных по 6 модулю
В первой странице вам нужно отобразить категории, товары со скидками и бренды

**Получить список категории**
----
 - **/api/clothes/category/**

	 Тут вы получаете список категории
	 
 - **Method:**
	  `GET`
	   

 - **Success Response:**
	 После успешеного запроса выбираете нужный вам данной **под категории** `clothes_types` вы можете пройти получить всю одежду с данной **под категорией**
	 - **Code:** 200 <br />
	 - **Content:**
		  ```
		 {
		    "M": [
		        {
		            "id": 1,
		            "clothes_types": [
		                {
		                    "id": 1,
		                    "title": "Куртка",
		                    "clothes_category": 1,
		                    "body_part": null
		                }
		            ],
		            "cover_image": "http://localhost:8000/media/odejda.png",
		            "constructor_icon": null,
		            "title": "одежда"
		        }
		    ],
		    "F": [
		        {
		            "id": 1,
		            "clothes_types": [],
		            "cover_image": "http://localhost:8000/media/odejda_9ZHEgFs.png",
		            "constructor_icon": null,
		            "title": "одежда"
		        }
		    ]
		}
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		  ```
		  {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
		
**Полная информация по под категории**
----
 - **/api/clothes/type/:id**

	 Тут вы получаете детальную информацию по под категориям 
	 
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id под категории | -
	| gender(optional) |  enum("M", "F") | пол | -
	|page(optional)  | integer  |   страница с вещами | 1
	| page_size |integer | количество вещей за страницу |    10 |


 - **Success Response:**
	 После успешеного запроса выбираете нужную **вещь**  `clothes["data"][:id]` и переходите для более детальной информации
	 - **Code:** 200 <br />
	 - **Content:** 
	 ```
		 {
	    "id": 1,
	    "clothes": {
	        "current_page": 1,
	        "page_size": "2",
	        "total_pages": 3,
	        "count": 3,
	        "data": [
	            {
	                "id": 3,
	                "title": "123213",
	                "cover_photo": null,
	                "cost": 0.0,
	                "sale_cost": 0.0,
	                "currency": "kzt",
	                "clothes_type": {
	                    "id": 1,
	                    "title": "Куртка",
	                    "clothes_category": 1,
	                    "body_part": null
	                },
	                "gender": "M",
	                "constructor_photo": null,
	                "new_arrival": true
	            },
	            {
	                "id": 2,
	                "title": "123",
	                "cover_photo": null,
	                "cost": 0.0,
	                "sale_cost": 0.0,
	                "currency": "kzt",
	                "clothes_type": {
	                    "id": 1,
	                    "title": "Куртка",
	                    "clothes_category": 1,
	                    "body_part": null
	                },
	                "gender": "M",
	                "constructor_photo": null,
	                "new_arrival": true
	            }
	        ]
	    },
	    "title": "Куртка",
	    "clothes_category": 1,
	    "body_part": null
	}
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	- **Code:** 404 not_found  <br />
	 - **Content:** 
		 ```
		 {
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_found",
	            "message": "Не найдено."
	        }
	    ],
	    "status_code": 404
		}

**Фильтр аутфитов**
----
 - **/api/clothes/item/

	 Здесь вы можете получать нужные результаты по фильтрам
	 Чтобы получить id категории, цветов, размеров и т.д можете обращаться к роутам /clothes/type,/clothes/category, /clothes/brands, /clothes/styles 
	 
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id вещи | -
	| gender(optional) |  enum("M", "F") | пол | -
	| cost_range(optional) |  range(min_price,max_price) | указывате цены через запятую, пример: 100,1000 | -
	| clothes_type(optional)  |  integers | указывате подкатегорию через запятую, пример: 1,2,3,4| -
	|sale(optional) |  boolean | показать товары со скидкой | false
	| clothes_category(optional) |  integer | указывате категорию через запятую, пример: 1,2,3,4 | -
	| brand  |  integer | id под бренда | -
	| clothes_color  |  integers | указываете цвета через запятую, пример: 1,2,3,4 | -
	| clothes_categories  |  integers | указываете категории через запятую: 1,2,3,4 | -

 - **Success Response:**
	 После успешеного запроса выбираете нужную **вещь**  `clothes["data"][:id]` и переходите для более детальной информации
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		 {
            "id": 45,
            "clothes_type": {
                "id": 5,
                "title": "Шапки",
                "clothes_category": 4,
                "body_part": 8
            },
            "cover_photo": "http://178.170.221.31:8000/media/27.png",
            "constructor_photo": "http://178.170.221.31:8000/media/27.png",
            "images": [
                "http://178.170.221.31:8000/media/27.png"
            ],
            "clothes_sizes": [
                {
                    "id": 2,
                    "size": "S"
                }
            ],
            "clothes_colors": [
                {
                    "id": 1,
                    "color": "Белый"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "шапка 1",
            "description": "шапка",
            "gender": "M",
            "cost": 5000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T16:36:03+06:00"
        },
        {
            "id": 44,
            "clothes_type": {
                "id": 2,
                "title": "Футболка",
                "clothes_category": 1,
                "body_part": 2
            },
            "cover_photo": "http://178.170.221.31:8000/media/24.png",
            "constructor_photo": "http://178.170.221.31:8000/media/24.png",
            "images": [
                "http://178.170.221.31:8000/media/24.png"
            ],
            "clothes_sizes": [
                {
                    "id": 3,
                    "size": "XL"
                }
            ],
            "clothes_colors": [
                {
                    "id": 1,
                    "color": "Белый"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "Футболка белая",
            "description": "Футболка белая",
            "gender": "M",
            "cost": 5000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T16:16:28+06:00"
        },
        {
            "id": 43,
            "clothes_type": {
                "id": 2,
                "title": "Футболка",
                "clothes_category": 1,
                "body_part": 2
            },
            "cover_photo": "http://178.170.221.31:8000/media/22.png",
            "constructor_photo": "http://178.170.221.31:8000/media/22.png",
            "images": [
                "http://178.170.221.31:8000/media/22.png"
            ],
            "clothes_sizes": [
                {
                    "id": 4,
                    "size": "L"
                }
            ],
            "clothes_colors": [
                {
                    "id": 1,
                    "color": "Белый"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "Рубашка голубая",
            "description": "Рубашка голубая",
            "gender": "M",
            "cost": 5500.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T16:12:05+06:00"
        },
        {
            "id": 42,
            "clothes_type": {
                "id": 4,
                "title": "Обувь",
                "clothes_category": 3,
                "body_part": 5
            },
            "cover_photo": "http://178.170.221.31:8000/media/21.png",
            "constructor_photo": "http://178.170.221.31:8000/media/21.png",
            "images": [
                "http://178.170.221.31:8000/media/21.png"
            ],
            "clothes_sizes": [
                {
                    "id": 1,
                    "size": "XS"
                }
            ],
            "clothes_colors": [
                {
                    "id": 3,
                    "color": "Черный"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "Кеды черные",
            "description": "Кеды черные",
            "gender": "M",
            "cost": 9500.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T16:10:00+06:00"
        },
        {
            "id": 41,
            "clothes_type": {
                "id": 3,
                "title": "брюки",
                "clothes_category": 2,
                "body_part": 4
            },
            "cover_photo": "http://178.170.221.31:8000/media/20.png",
            "constructor_photo": "http://178.170.221.31:8000/media/20.png",
            "images": [
                "http://178.170.221.31:8000/media/20.png"
            ],
            "clothes_sizes": [
                {
                    "id": 2,
                    "size": "S"
                }
            ],
            "clothes_colors": [
                {
                    "id": 1,
                    "color": "Белый"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "брюки белые",
            "description": "брюки белые",
            "gender": "M",
            "cost": 8500.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T16:06:41+06:00"
        },
        {
            "id": 39,
            "clothes_type": {
                "id": 3,
                "title": "брюки",
                "clothes_category": 2,
                "body_part": 4
            },
            "cover_photo": "http://178.170.221.31:8000/media/19.png",
            "constructor_photo": "http://178.170.221.31:8000/media/19.png",
            "images": [
                "http://178.170.221.31:8000/media/19.png"
            ],
            "clothes_sizes": [
                {
                    "id": 1,
                    "size": "XS"
                }
            ],
            "clothes_colors": [
                {
                    "id": 3,
                    "color": "Черный"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "брюки черные",
            "description": "брюки черные",
            "gender": "M",
            "cost": 5600.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "4607078200927",
            "created_at": "2021-01-12T15:48:43+06:00"
        },
        {
            "id": 38,
            "clothes_type": {
                "id": 4,
                "title": "Обувь",
                "clothes_category": 3,
                "body_part": 5
            },
            "cover_photo": "http://178.170.221.31:8000/media/18.png",
            "constructor_photo": "http://178.170.221.31:8000/media/18.png",
            "images": [
                "http://178.170.221.31:8000/media/18.png"
            ],
            "clothes_sizes": [
                {
                    "id": 2,
                    "size": "S"
                }
            ],
            "clothes_colors": [
                {
                    "id": 3,
                    "color": "Черный"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "Кроссовки 4",
            "description": "Кроссовки 4",
            "gender": "M",
            "cost": 12000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T15:45:52+06:00"
        },
        {
            "id": 37,
            "clothes_type": {
                "id": 4,
                "title": "Обувь",
                "clothes_category": 3,
                "body_part": 5
            },
            "cover_photo": "http://178.170.221.31:8000/media/17.png",
            "constructor_photo": "http://178.170.221.31:8000/media/17.png",
            "images": [
                "http://178.170.221.31:8000/media/17.png"
            ],
            "clothes_sizes": [
                {
                    "id": 1,
                    "size": "XS"
                }
            ],
            "clothes_colors": [
                {
                    "id": 1,
                    "color": "Белый"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "кроссовки 3",
            "description": "кроссовки 3",
            "gender": "M",
            "cost": 15000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T15:44:21+06:00"
        },
        {
            "id": 36,
            "clothes_type": {
                "id": 4,
                "title": "Обувь",
                "clothes_category": 3,
                "body_part": 5
            },
            "cover_photo": "http://178.170.221.31:8000/media/15.png",
            "constructor_photo": "http://178.170.221.31:8000/media/15.png",
            "images": [
                "http://178.170.221.31:8000/media/15.png"
            ],
            "clothes_sizes": [
                {
                    "id": 2,
                    "size": "S"
                }
            ],
            "clothes_colors": [
                {
                    "id": 3,
                    "color": "Черный"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "кроссовки 2",
            "description": "кроссовки 2",
            "gender": "M",
            "cost": 17000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T15:39:00+06:00"
        },
        {
            "id": 35,
            "clothes_type": {
                "id": 4,
                "title": "Обувь",
                "clothes_category": 3,
                "body_part": 5
            },
            "cover_photo": "http://178.170.221.31:8000/media/13.png",
            "constructor_photo": "http://178.170.221.31:8000/media/13.png",
            "images": [
                "http://178.170.221.31:8000/media/13.png"
            ],
            "clothes_sizes": [
                {
                    "id": 3,
                    "size": "XL"
                }
            ],
            "clothes_colors": [
                {
                    "id": 3,
                    "color": "Черный"
                }
            ],
            "brand": {
                "id": 10,
                "is_brand": true,
                "last_login": null,
                "role": "brand",
                "email": "Taipov779@gmail.com",
                "first_name": "Руслан",
                "last_name": "Таипов",
                "date_of_birth": "1991-11-21",
                "is_verified": false,
                "should_send_mail": false,
                "verification_uuid": "3c1d35cc-3751-4cf9-b793-b1d2a365b622",
                "created": "2020-12-22T13:53:46.345171+06:00",
                "modified": "2020-12-25T17:30:49.554308+06:00",
                "avatar": null
            },
            "title": "Кроссовки 1",
            "description": "Кроссовки 1",
            "gender": "M",
            "cost": 7000.0,
            "sale_cost": 0.0,
            "currency": "KZT",
            "product_code": "",
            "created_at": "2021-01-12T15:33:20+06:00"
        }
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** `{
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_authenticated",
	            "message": "Учетные данные не были предоставлены."
	        }
	    ],
	    "status_code": 401,
	    "detail": "Учетные данные не были предоставлены.",
	    "error_code": "not_authenticated"
	}`
	---
	- **Code:** 404 not_found  <br />
	 - **Content:** 
		 ```
		 {
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_found",
	            "message": "Не найдено."
	        }
	    ],
	    "status_code": 404
		}

**Список предметов**
----
 - **/api/clothes/item/**

	 Тут вы можете искать предметы и фильтровать их
	 
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| gender(optional) |  enum("M", "F") | пол | -
	| cost_range(optional) |  range(min_price,max_price) | указывате цены через запятую, пример: 100,1000 | -
	| clothes_type(optional)  |  integers | указывате подкатегорию через запятую, пример: 1,2,3,4| -
	|sale(optional) |  boolean | показать товары со скидкой | false
	| clothes_category(optional) |  integer | указывате категорию через запятую, пример: 1,2,3,4 | -
	| brand  |  integer | id бренда | -
	| page |  integer | страница | -
	| page_size |  integer | количество вещей на страницу | -
	| search  |  string | поиск по description и title | -
 - **Success Response:**
	 
	 - **Code:** 200 <br />
	 - **Content:** 
		 ```
		{
		    "current_page": 1,
		    "total_pages": 1,
		    "page_size": 10,
		    "count": 1,
		    "results": [
		        {
		            "id": 1,
		            "clothes_type": {
		                "id": 1,
		                "title": "Куртка",
		                "clothes_category": 1,
		                "body_part": null
		            },
		            "cover_photo": null,
		            "constructor_photo": "http://localhost:8000/media/suit-115309787575q8zja3fwo.png",
		            "images": [],
		            "clothes_sizes": [
		                {
		                    "id": 1,
		                    "size": "small"
		                }
		            ],
		            "clothes_colors": [
		                {
		                    "id": 1,
		                    "color": "white"
		                }
		            ],
		            "brand": {
		                "id": 8,
		                "is_brand": true,
		                "last_login": null,
		                "role": "brand",
		                "email": "shop@gmail.com",
		                "first_name": "Zara",
		                "last_name": "",
		                "date_of_birth": null,
		                "is_verified": false,
		                "should_send_mail": false,
		                "verification_uuid": "37a9d8e5-b6fa-43b2-aaf8-a0fe53c7aef7",
		                "created": "2020-12-24T07:32:07.905757+06:00",
		                "modified": "2020-12-24T07:35:21.223909+06:00",
		                "avatar": "http://localhost:8000/media/Screenshot_from_2020-12-11_22-06-39.png"
		            },
		            "title": "Rehnrf rhenfz",
		            "description": "asdasd",
		            "gender": "M",
		            "cost": 54.0,
		            "sale_cost": 0.0,
		            "currency": "KZT",
		            "product_code": "123123",
		            "created_at": "2020-12-02T03:25:26+06:00"
		        }
		    ]
		}
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** `{
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_authenticated",
	            "message": "Учетные данные не были предоставлены."
	        }
	    ],
	    "status_code": 401,
	    "detail": "Учетные данные не были предоставлены.",
	    "error_code": "not_authenticated"
	}`
	---
	- **Code:** 404 not_found  <br />
	 - **Content:** 
		 ```
		 {
	    "errors": [
	        {
	            "field": "detail",
	            "error_code": "not_found",
	            "message": "Не найдено."
	        }
	    ],
	    "status_code": 404
		}

**Список брендов**
----
Получить список брендов
 - **/api/clothes/brands/**

	 Тут вы получаете детальную информацию по предмету
	 
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| liked |  boolean | понравившиеся бренды | false'
 - **Success Response:**
	 После успешеного запроса можете получить полную информацию о магазине
	 - **Code:** 200 <br />
	 - **Content:** 
		```
		[
		    {
		        "id": 8,
		        "is_brand": true,
		        "liked": false,
		        "last_login": null,
		        "role": "brand",
		        "email": "shop@gmail.com",
		        "first_name": "Zara",
		        "last_name": "",
		        "date_of_birth": null,
		        "is_verified": false,
		        "should_send_mail": false,
		        "verification_uuid": "37a9d8e5-b6fa-43b2-aaf8-a0fe53c7aef7",
		        "created": "2020-12-24T07:32:07.905757+06:00",
		        "modified": "2020-12-24T07:35:21.223909+06:00",
		        "avatar": "http://localhost:8000/media/Screenshot_from_2020-12-11_22-06-39.png"
		    }
		]
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
		
**Детальная информация по бренду**
----

 - **/api/clothes/brands/:id**

	 Тут вы получаете детальную информацию по бренду
	 
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id бренда | -
 - **Success Response:**
	 После успешеного запроса можете получить полную информацию о магазине
	 - **Code:** 200 <br />
	 - **Content:** 
		```
		{
	    "id": 8,
	    "followings_count": 0,
	    "followers_count": 0,
	    "is_brand": true,
	    "last_login": null,
	    "role": "brand",
	    "email": "shop@gmail.com",
	    "first_name": "Zara",
	    "last_name": "",
	    "date_of_birth": null,
	    "is_verified": false,
	    "should_send_mail": false,
	    "verification_uuid": "37a9d8e5-b6fa-43b2-aaf8-a0fe53c7aef7",
	    "created": "2020-12-24T07:32:07.905757+06:00",
	    "modified": "2020-12-24T07:35:21.223909+06:00",
	    "avatar": "http://localhost:8000/media/Screenshot_from_2020-12-11_22-06-39.png"
		}
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	
**Добавить бренд в список любимых**
----

 - **/api/clothes/brands/:id/liked**

	 Тут вы сохранить бренд в любимых
	  
 - **Method:**
	  `POST | DELETE`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id бренда | -
 - **Success Response:**
	 Если метод `DELETE`
	 - **Code:** 200 <br />
	 - **Content:** 
		```
		{
		    "message": "brand successfully removed"
		}
	Если метод `POST`
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "message": "brand successfully added"
		}
 
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`


**Получить список стилей**
----

 - **/api/clothes/styles/**

	 Получить список стилей
	  
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   

 - **Success Response:**
	
	 - **Code:** 200 <br />
	 - **Content:** 
		```
		[
		    {
		        "id": 1,
		        "title": "Улица"
		    }
		]
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`

**Конструктор**
----
Получаете сперва список категорий и сделайте [фильтрацию вещи по нужной категории или под категории](#cписок-предметов). 


**Создать Образ**
----

 - **/api/outfit/**

	 Тут вы создаете образ. **Проверяйте body_part id обязательно в подкатегории( clothes_type.** Нужно чтобы вещь с определнным body_part был только 1. 
	  
 - **Method:**
	  `POST`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| text(required) |  string | Описание образа | -
	| clothes(required)| array | id вещей. Пример: [1,2,3]  | -
	| clothes_location(required)| json | Вещи с позицией   | -
	| style(required)| integer | id стилей   | -
	| cover_photo(required)| image | скрин экрана с расположенными вещами   | -
	| is_published(optional)| boolean | публиковать в ленте   | false
	```
	Пример:
	"clothes_location": [
        {
        	"clothes_id": 3,
        	"point_x": 123.33,
        	"point_y": 321.33,
        	"width": 100.00,
        	"height": 101.10,
        	"degree": 99.55
        }
    ]
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 8,
		    "author": {
		        "id": 1,
		        "is_brand": false,
		        "last_login": null,
		        "role": "user",
		        "email": "galix.k@gmail.com",
		        "first_name": "Galizhan",
		        "last_name": "Tolybayev",
		        "date_of_birth": "1998-12-31",
		        "is_verified": false,
		        "should_send_mail": false,
		        "verification_uuid": "cf1f7651-e73a-429f-a88f-81c746488b23",
		        "created": "2020-11-23T10:37:34.867859+06:00",
		        "modified": "2020-11-23T10:37:34.867877+06:00",
		        "avatar": null
		    },
		    "clothes_location": [
		        {
		            "clothes_id": 3,
		            "point_x": 123.33,
		            "point_y": 321.33,
		            "width": 100.0,
		            "height": 101.1,
		            "degree": 99.55
		        }
		    ],
		    "likes_count": 0,
		    "comments_count": 0,
		    "already_liked": false,
		    "title": "",
		    "gender": null,
		    "total_price": 17.0,
		    "total_price_currency": "KZT",
		    "cover_photo": null,
		    "live_photo": null,
		    "constructor_code": null,
		    "created_at": "2020-12-25T12:49:13.575045+06:00",
		    "updated_at": "2020-12-25T12:49:13.581859+06:00",
		    "saved": false,
		    "style": 1,
		    "clothes": [
		        {
		            "id": 3,
		            "clothes_type": {
		                "id": 1,
		                "title": "Куртка",
		                "clothes_category": 1,
		                "body_part": null
		            },
		            "cover_photo": null,
		            "constructor_photo": null,
		            "images": [],
		            "clothes_sizes": [
		                {
		                    "id": 1,
		                    "size": "small"
		                }
		            ],
		            "clothes_colors": [
		                {
		                    "id": 1,
		                    "color": "white"
		                }
		            ],
		            "brand": {
		                "id": 2,
		                "is_brand": false,
		                "last_login": "2020-12-23T16:08:40.127773+06:00",
		                "role": "user",
		                "email": "galix.kz@gmail.com",
		                "first_name": "Galizhan",
		                "last_name": "Tolybayev",
		                "date_of_birth": "1998-12-31",
		                "is_verified": true,
		                "should_send_mail": false,
		                "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		                "created": "2020-11-23T10:41:03.751501+06:00",
		                "modified": "2020-12-02T03:22:05.976166+06:00",
		                "avatar": null
		            },
		            "title": "123213",
		            "description": "ashdjkd21312",
		            "gender": "M",
		            "cost": 17.0,
		            "sale_cost": 0.0,
		            "currency": "kzt",
		            "product_code": "",
		            "created_at": "2020-12-10T11:44:17+06:00"
		        }
		    ],
		    "liked_by": []
		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
**Получить список образов**
----

 - **/api/outfit/**

	 Тут вы можете получить список образов и отфильтровать их 
	  
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| autor | integer | id пользователя который создал | -


 - **Success Response:**
	 - **Code:** 200<br />
	 - **Content:** 
		```
		{
		    "current_page": 1,
		    "total_pages": 1,
		    "page_size": 10,
		    "count": 9,
		    "results": [
		        {
		            "id": 11,
		            "author": {
		                "id": 1,
		                "is_brand": false,
		                "last_login": null,
		                "role": "user",
		                "email": "galix.k@gmail.com",
		                "first_name": "Galizhan",
		                "last_name": "Tolybayev",
		                "date_of_birth": "1998-12-31",
		                "is_verified": false,
		                "should_send_mail": false,
		                "verification_uuid": "cf1f7651-e73a-429f-a88f-81c746488b23",
		                "created": "2020-11-23T10:37:34.867859+06:00",
		                "modified": "2020-11-23T10:37:34.867877+06:00",
		                "avatar": null
		            },
		            "clothes_location": [
		                {
		                    "clothes_id": 3,
		                    "point_x": 123.33,
		                    "point_y": 321.33,
		                    "width": 100.0,
		                    "height": 101.1,
		                    "degree": 99.55
		                }
		            ],
		            "likes_count": 0,
		            "comments_count": 0,
		            "already_liked": false,
		            "title": "My new outlook",
		            "gender": null,
		            "total_price": 71.0,
		            "total_price_currency": "KZT",
		            "cover_photo": null,
		            "live_photo": null,
		            "constructor_code": null,
		            "created_at": "2020-12-26T16:26:58.943504+06:00",
		            "updated_at": "2020-12-26T16:26:58.994152+06:00",
		            "saved": false,
		            "style": 1,
		            "clothes": [
		                {
		                    "id": 3,
		                    "clothes_type": {
		                        "id": 1,
		                        "title": "Куртка",
		                        "clothes_category": 1,
		                        "body_part": null
		                    },
		                    "cover_photo": null,
		                    "constructor_photo": null,
		                    "images": [],
		                    "clothes_sizes": [
		                        {
		                            "id": 1,
		                            "size": "small"
		                        }
		                    ],
		                    "clothes_colors": [
		                        {
		                            "id": 1,
		                            "color": "white"
		                        }
		                    ],
		                    "brand": {
		                        "id": 2,
		                        "is_brand": false,
		                        "last_login": "2020-12-26T16:21:49.055907+06:00",
		                        "role": "user",
		                        "email": "galix.kz@gmail.com",
		                        "first_name": "Galizhan",
		                        "last_name": "Tolybayev",
		                        "date_of_birth": "1998-12-31",
		                        "is_verified": true,
		                        "should_send_mail": false,
		                        "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		                        "created": "2020-11-23T10:41:03.751501+06:00",
		                        "modified": "2020-12-02T03:22:05.976166+06:00",
		                        "avatar": null
		                    },
		                    "title": "123213",
		                    "description": "ashdjkd21312",
		                    "gender": "M",
		                    "cost": 17.0,
		                    "sale_cost": 0.0,
		                    "currency": "kzt",
		                    "product_code": "123",
		                    "created_at": "2020-12-10T11:44:17+06:00"
		                },
		                {
		                    "id": 1,
		                    "clothes_type": {
		                        "id": 1,
		                        "title": "Куртка",
		                        "clothes_category": 1,
		                        "body_part": null
		                    },
		                    "cover_photo": null,
		                    "constructor_photo": "http://localhost:8000/media/suit-115309787575q8zja3fwo.png",
		                    "images": [],
		                    "clothes_sizes": [
		                        {
		                            "id": 1,
		                            "size": "small"
		                        }
		                    ],
		                    "clothes_colors": [
		                        {
		                            "id": 1,
		                            "color": "white"
		                        }
		                    ],
		                    "brand": {
		                        "id": 8,
		                        "is_brand": true,
		                        "last_login": null,
		                        "role": "brand",
		                        "email": "shop@gmail.com",
		                        "first_name": "Zara",
		                        "last_name": "",
		                        "date_of_birth": null,
		                        "is_verified": false,
		                        "should_send_mail": false,
		                        "verification_uuid": "37a9d8e5-b6fa-43b2-aaf8-a0fe53c7aef7",
		                        "created": "2020-12-24T07:32:07.905757+06:00",
		                        "modified": "2020-12-24T07:35:21.223909+06:00",
		                        "avatar": "http://localhost:8000/media/Screenshot_from_2020-12-11_22-06-39.png"
		                    },
		                    "title": "Rehnrf rhenfz",
		                    "description": "asdasd",
		                    "gender": "M",
		                    "cost": 54.0,
		                    "sale_cost": 0.0,
		                    "currency": "KZT",
		                    "product_code": "123123",
		                    "created_at": "2020-12-02T03:25:26+06:00"
		                }
		            ],
		            "liked_by": []
		        },
		        ]
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
**Изменить Образ**
----

 - **/api/outfit/:id/**

	 Тут вы изменяте образ, если вы изменяете чужой образ сперва [сохраните его к себе](#cохранить-образ-к-себе)
	  
 - **Method:**
	  `PATCH`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) | integer | id образа | -
	| [любые поля из создать образ](#создать-образ) | array |   | -
	
	```
	Пример:
	"clothes_location": [
        {
        	"clothes_id": 3,
        	"point_x": 123.33,
        	"point_y": 321.33,
        	"width": 100.00,
        	"height": 101.10,
        	"degree": 99.55
        }
    ]
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 8,
		    "author": {
		        "id": 1,
		        "is_brand": false,
		        "last_login": null,
		        "role": "user",
		        "email": "galix.k@gmail.com",
		        "first_name": "Galizhan",
		        "last_name": "Tolybayev",
		        "date_of_birth": "1998-12-31",
		        "is_verified": false,
		        "should_send_mail": false,
		        "verification_uuid": "cf1f7651-e73a-429f-a88f-81c746488b23",
		        "created": "2020-11-23T10:37:34.867859+06:00",
		        "modified": "2020-11-23T10:37:34.867877+06:00",
		        "avatar": null
		    },
		    "clothes_location": [
		        {
		            "clothes_id": 3,
		            "point_x": 123.33,
		            "point_y": 321.33,
		            "width": 100.0,
		            "height": 101.1,
		            "degree": 99.55
		        }
		    ],
		    "likes_count": 0,
		    "comments_count": 0,
		    "already_liked": false,
		    "title": "",
		    "gender": null,
		    "total_price": 17.0,
		    "total_price_currency": "KZT",
		    "cover_photo": null,
		    "live_photo": null,
		    "constructor_code": null,
		    "created_at": "2020-12-25T12:49:13.575045+06:00",
		    "updated_at": "2020-12-25T12:49:13.581859+06:00",
		    "saved": false,
		    "style": 1,
		    "clothes": [
		        {
		            "id": 3,
		            "clothes_type": {
		                "id": 1,
		                "title": "Куртка",
		                "clothes_category": 1,
		                "body_part": null
		            },
		            "cover_photo": null,
		            "constructor_photo": null,
		            "images": [],
		            "clothes_sizes": [
		                {
		                    "id": 1,
		                    "size": "small"
		                }
		            ],
		            "clothes_colors": [
		                {
		                    "id": 1,
		                    "color": "white"
		                }
		            ],
		            "brand": {
		                "id": 2,
		                "is_brand": false,
		                "last_login": "2020-12-23T16:08:40.127773+06:00",
		                "role": "user",
		                "email": "galix.kz@gmail.com",
		                "first_name": "Galizhan",
		                "last_name": "Tolybayev",
		                "date_of_birth": "1998-12-31",
		                "is_verified": true,
		                "should_send_mail": false,
		                "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		                "created": "2020-11-23T10:41:03.751501+06:00",
		                "modified": "2020-12-02T03:22:05.976166+06:00",
		                "avatar": null
		            },
		            "title": "123213",
		            "description": "ashdjkd21312",
		            "gender": "M",
		            "cost": 17.0,
		            "sale_cost": 0.0,
		            "currency": "kzt",
		            "product_code": "",
		            "created_at": "2020-12-10T11:44:17+06:00"
		        }
		    ],
		    "liked_by": []
		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
**Сохранить образ к себе**
----

 - **/api/outfit/:id/save**

	 Сохраняете образ к себе
	  
 - **Method:**
	  `GET`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| id(required) |  integer | id образа  | -
	
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 8,
		    "author": {
		        "id": 1,
		        "is_brand": false,
		        "last_login": null,
		        "role": "user",
		        "email": "galix.k@gmail.com",
		        "first_name": "Galizhan",
		        "last_name": "Tolybayev",
		        "date_of_birth": "1998-12-31",
		        "is_verified": false,
		        "should_send_mail": false,
		        "verification_uuid": "cf1f7651-e73a-429f-a88f-81c746488b23",
		        "created": "2020-11-23T10:37:34.867859+06:00",
		        "modified": "2020-11-23T10:37:34.867877+06:00",
		        "avatar": null
		    },
		    "clothes_location": [
		        {
		            "clothes_id": 3,
		            "point_x": 123.33,
		            "point_y": 321.33,
		            "width": 100.0,
		            "height": 101.1,
		            "degree": 99.55
		        }
		    ],
		    "likes_count": 0,
		    "comments_count": 0,
		    "already_liked": false,
		    "title": "",
		    "gender": null,
		    "total_price": 17.0,
		    "total_price_currency": "KZT",
		    "cover_photo": null,
		    "live_photo": null,
		    "constructor_code": null,
		    "created_at": "2020-12-25T12:49:13.575045+06:00",
		    "updated_at": "2020-12-25T12:49:13.581859+06:00",
		    "saved": false,
		    "style": 1,
		    "clothes": [
		        {
		            "id": 3,
		            "clothes_type": {
		                "id": 1,
		                "title": "Куртка",
		                "clothes_category": 1,
		                "body_part": null
		            },
		            "cover_photo": null,
		            "constructor_photo": null,
		            "images": [],
		            "clothes_sizes": [
		                {
		                    "id": 1,
		                    "size": "small"
		                }
		            ],
		            "clothes_colors": [
		                {
		                    "id": 1,
		                    "color": "white"
		                }
		            ],
		            "brand": {
		                "id": 2,
		                "is_brand": false,
		                "last_login": "2020-12-23T16:08:40.127773+06:00",
		                "role": "user",
		                "email": "galix.kz@gmail.com",
		                "first_name": "Galizhan",
		                "last_name": "Tolybayev",
		                "date_of_birth": "1998-12-31",
		                "is_verified": true,
		                "should_send_mail": false,
		                "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		                "created": "2020-11-23T10:41:03.751501+06:00",
		                "modified": "2020-12-02T03:22:05.976166+06:00",
		                "avatar": null
		            },
		            "title": "123213",
		            "description": "ashdjkd21312",
		            "gender": "M",
		            "cost": 17.0,
		            "sale_cost": 0.0,
		            "currency": "kzt",
		            "product_code": "",
		            "created_at": "2020-12-10T11:44:17+06:00"
		        }
		    ],
		    "liked_by": []
		}
 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
		
**Добавить вещь по фотографии**
----

 - **/api/clothes/items/**

	 Тут вы добавляете свои вещи по фотографиям
	  
 - **Method:**
	  `POST`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| image(required) |  file | Фотографию вещи | -
	| decsription(required)| text | Описание  | -
	| clothes_type(required)| integer | Под категория   | -
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 26,
		    "title": "ajksdljasld",
		    "cover_photo": "http://localhost:8000/media/Group_188_1_6TMpv6J.png",
		    "cost": 0.0,
		    "sale_cost": 0.0,
		    "currency": "KZT",
		    "clothes_type": null,
		    "gender": null,
		    "constructor_photo": "http://localhost:8000/media/Group_188_1_YwlILdK.png",
		    "new_arrival": true,
		    "description": "ajksdljasld"
		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
		
**Добавить вещь по баркоду в гардероб**
----
 - **/api/wardrobe/**

	 [Найдите вещь по баркоду](#найти-вещь-по-баркоду) и добавьте к себе в гардероб
	  
 - **Method:**
	  `POST`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| cloth(required) |  integer | id вещи| -
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{

		"id": 34,

		"cloth": 1,

		"user": 2

		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`

**Найти вещь по баркоду**
----
 - **/api/clothes/items/**

	 Тут вы ищите вещь по баркоду
	  
 - **Method:**
	  `POST`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| barcode(required) |  text | данные из баркода | -
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 3,
		    "clothes_type": {
		        "id": 1,
		        "title": "Куртка",
		        "clothes_category": 1,
		        "body_part": null
		    },
		    "cover_photo": null,
		    "constructor_photo": null,
		    "images": [],
		    "clothes_sizes": [
		        {
		            "id": 1,
		            "size": "small"
		        }
		    ],
		    "clothes_colors": [
		        {
		            "id": 1,
		            "color": "white"
		        }
		    ],
		    "brand": {
		        "id": 2,
		        "is_brand": false,
		        "last_login": "2020-12-23T16:08:40.127773+06:00",
		        "role": "user",
		        "email": "galix.kz@gmail.com",
		        "first_name": "Galizhan",
		        "last_name": "Tolybayev",
		        "date_of_birth": "1998-12-31",
		        "is_verified": true,
		        "should_send_mail": false,
		        "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		        "created": "2020-11-23T10:41:03.751501+06:00",
		        "modified": "2020-12-02T03:22:05.976166+06:00",
		        "avatar": null
		    },
		    "title": "123213",
		    "description": "ashdjkd21312",
		    "gender": "M",
		    "cost": 17.0,
		    "sale_cost": 0.0,
		    "currency": "kzt",
		    "product_code": "123",
		    "created_at": "2020-12-10T11:44:17+06:00"
		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}

**Получить вещи в гардеробе**
----
 - **/api/wardrobe/**

	 Получите вещи по баркоду
	  
 - **Method:**
	  `POST`
  
- **Headers**
	- `"Authorization": "Bearer <token>"`
	   
- **Params**
	| Параметр  | Тип данных |      Описание      |  Default |
	|----------|:-------------:|:-------------:|------:|
	| page(optional) |  integer | страница | 1
	| page_size(optional) |  integer | количество вещей в странице | 10
 - **Success Response:**
	 - **Code:** 201 <br />
	 - **Content:** 
		```
		{
		    "id": 3,
		    "clothes_type": {
		        "id": 1,
		        "title": "Куртка",
		        "clothes_category": 1,
		        "body_part": null
		    },
		    "cover_photo": null,
		    "constructor_photo": null,
		    "images": [],
		    "clothes_sizes": [
		        {
		            "id": 1,
		            "size": "small"
		        }
		    ],
		    "clothes_colors": [
		        {
		            "id": 1,
		            "color": "white"
		        }
		    ],
		    "brand": {
		        "id": 2,
		        "is_brand": false,
		        "last_login": "2020-12-23T16:08:40.127773+06:00",
		        "role": "user",
		        "email": "galix.kz@gmail.com",
		        "first_name": "Galizhan",
		        "last_name": "Tolybayev",
		        "date_of_birth": "1998-12-31",
		        "is_verified": true,
		        "should_send_mail": false,
		        "verification_uuid": "3a96c0ba-ec66-4efd-99b8-5a8b886774a1",
		        "created": "2020-11-23T10:41:03.751501+06:00",
		        "modified": "2020-12-02T03:22:05.976166+06:00",
		        "avatar": null
		    },
		    "title": "123213",
		    "description": "ashdjkd21312",
		    "gender": "M",
		    "cost": 17.0,
		    "sale_cost": 0.0,
		    "currency": "kzt",
		    "product_code": "123",
		    "created_at": "2020-12-10T11:44:17+06:00"
		}

 - **Error Response:**

	 - **Code:** 401 not_authenticated  <br />
	 - **Content:** 
		 ```
		 {
		    "errors": [
		        {
		            "field": "detail",
		            "error_code": "not_authenticated",
		            "message": "Учетные данные не были предоставлены."
		        }
		    ],
		    "status_code": 401,
		    "detail": "Учетные данные не были предоставлены.",
		    "error_code": "not_authenticated"
		}`
	---
	 - **Code:** 400 invalid  <br />
	 - **Content:** 
		 ```
		{
		    "errors": [
		        {
		            "field": "description",
		            "error_code": "required",
		            "message": "Это поле обязательно для заполнения"
		        }
		    ],
		    "status_code": 400,
		    "detail": "Invalid input.",
		    "error_code": "invalid"
		}
