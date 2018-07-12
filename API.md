# 1. Login
Used to collect a Token for a registered User.

**URL** : `/auth/login`

**Method** : `POST`

**Auth required** : NO

**Data constraints**

```json
{
    "username": "[valid username]",
    "password": "[password in plain text]"
}
```

**Data example**

```json
{
    "username": "markoa",
    "password": "heril123"
}
```

## Success Response

**Code** : `200 OK`

**Content example**

```json
{
    "token": "93144b288eb1fdccbe46d6fc0f241a51766ecd3d"
}
```

## Error Response

**Condition** : If 'username' and 'password' combination is wrong.

**Code** : `400 BAD REQUEST`
** **


# 2. Create User
Used to create an user

**URL** : `/api/users`

**Method** : `POST`

**Auth required** : YES

**Permissions required** : Admin

**Data constraints**

```json
{
    "firstName": "[string]",
    "lastName": "[string]",
    "address": "[string]",
    "age": [int],
    "username": "[valid username]",
    "password": "[password in plain text]",
    "email": "[valid email]",
    "isActive": [boolean]
}
```
**Data example**

```json
{
    "firstName": "John",
    "lastName": "Steward",
    "address": "address11",
    "age": 28,
    "username": "johns",
    "password": "john123",
    "email": "john94@gmail.com",
    "isActive": true
}
```
## Success Response

**Code** : `201 CREATED`

**Content example**

```json
{
    "id": 50,
    "firstName": "John",
    "lastName": "Steward",
    "address": "address11",
    "age": 28,
    "username": "johns",
    "password": "$2a$10$NlIxUR3hqdsqg59/Kc2QV.8JEFLGnjaIBBR4/8PvAxQnqgPpHSA9C",
    "email": "john94@gmail.com",
    "isActive": true,
    "roles": [
        {
            "id": 2,
            "name": "ROLE_USER",
            "description": "User"
        }
    ]
}
```
## Error Response

**Condition** : If 'id' exists in request body.

**Code** : `400 BAD REQUEST`

### Or
**Condition** : If User already exists

**Code** : `500 Internal Server Error`

# 3. Show Categories
Used to show all categories

**URL** : `/api/categories`

**Method** : `GET`

**Auth required** : YES

**Permissions required** : Admin

**Data constraints** : `{}`

## Success Responses

**Condition** : User can not see any Category.

**Code** : `200 OK`

**Content** : `{[]}`

### OR

**Condition** : User can see one or more Category.

**Code** : `200 OK`

**Content** : In this example, the User can see some Cateogires:

```json
[
    {
        "id": 1,
        "name": "Programiranje",
        "opis": "Analiza programerskih vjestina",
        "isActive": true
    },
    {
        "id": 2,
        "name": "Web",
        "opis": "HTML, CSS i Javascript",
        "isActive": true
    },
    {
        "id": 6,
        "name": "Spring Boot",
        "opis": "Opis Springa",
        "isActive": true
    }
]
```
# 4. Create Category
Used to create a category

**URL** : `/api/categories`

**Method** : `POST`

**Auth required** : YES

**Permissions required** : Admin

**Data constraints**

```json
{
    "name": "[string]",
    "opis": "[string]",
    "isActive": [boolean]
}
```
**Data example**

```json
{
    "name": "Programiranje",
    "opis": "Analiza programerskih vjestina",
    "isActive": true
}
```

## Success Response

**Code** : `201 CREATED`

**Content example**

```json
{
	"id": 5,
    "name": "Programiranje",
    "opis": "Analiza programerskih vjestina",
    "isActive": true
}
```
## Error Response

**Condition** : If 'id' exists in request body.

**Code** : `400 BAD REQUEST`


# 5. Update Category
Allow Admin to update Category details.

**URL** : `/api/categories/{id}`

**Method** : `PUT`

**Auth required** : YES

**Permissions required** : Admin

**Data constraints**

```json
{
	"id" : [int],
    "name": "[string]",
    "opis": "[string]",
    "isActive": [boolean]
}
```
**Data example**

```json
{
	"id": 29,
    "name": "JAVA Programiranje",
    "opis": "Analiza programerskih vjestina u Javi",
    "isActive": true
}
```
## Success Response

**Code** : `200 OK`

**Content example**

```json
{
	"id": 29,
    "name": "JAVA Programiranje",
    "opis": "Analiza programerskih vjestina u Javi",
    "isActive": true
}
```
## Error Response

**Condition** : If 'id' does not exist in request body.

**Code** : `400 BAD REQUEST`
