# Microservicios
Microservicios con SpringBoot

Para la utilización de este proyecto debemos importar el modulo completo en Intellij Idea y cargar las dependencias gradle para el correcto funcionamiento.

Una vez importado el proyecto con sus módulos y dependencias respectivas debemos ir a /microservice-commons y ejecutar gradlew build en consola de la siguiente forma :
Microservicios\microservice-commons>gradlew build

Esto porque otros microservicios requieren de microservice-commons como dependencia para su funcionamiento. Cabe mencionar que microservice-commons No se debe levantar.

Posterior a eso debemos proceder a ejecutar cada microservicio en el siguiente orden
1. Eureka
2. Users
3. Oauth
4. Zuul Gateway; para este ultimo es importante esperar (puede demorar hasta 15 seg) que en la consola aparezcan las siguientes líneas:

2020-10-05 23:21:12.792  INFO 7824 --- [erListUpdater-0] c.netflix.config.ChainedDynamicProperty  : Flipping property: users-service.ribbon.ActiveConnectionsLimit to use NEXT property: niws.loadbalancer.availabilityFilteringRule.activeConnectionsLimit = 2147483647
2020-10-05 23:21:12.889  INFO 7824 --- [erListUpdater-1] c.netflix.config.ChainedDynamicProperty  : Flipping property: oauth-service.ribbon.ActiveConnectionsLimit to use NEXT property: niws.loadbalancer.availabilityFilteringRule.activeConnectionsLimit = 2147483647

Una vez aparezcan ambas sabremos que ya está disponible, de lo contrario, en caso que solo apareciera una, es posible que pueda haber error ya que el servidor de eureka aun no lo registra.

Una vez validemos que nuestros microservicios están levantados podremos ejecutar nuestros servicios REST.

------------------------------
localhost:8090/api/users (persiste el usuario) (Unica ruta que no necesita autenticación)
-Para probar podemos ir a PostMan y agregar el end-point mencionado (POST)
-Luego vamos donde dice Body, seleccionamos raw , formato JSON y agregamos el siguiente request de ejemplo

{
    "name": "benjamin",
    "email": "benjamin.salazar8@gmail.com",
    "password": "1234",
    "phones": [
        {
            "number": "123123",
            "cityCode": "123",
            "countryCode": "1"
        },
         {
            "number": "123123",
            "cityCode": "123",
            "countryCode": "12"
        }
    ]
}

Ejecutamos y ya podríamos ver un response con el siguiente formato

{
    "created": "2020-10-06T01:45:20.497+00:00",
    "last_login": "2020-10-06T01:45:20.269+00:00",
    "modified": "2020-10-06T01:45:20.269+00:00",
    "id": 3,
    "isActive": "Y",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MDE5NTIzMjAsInVzZXJfbmFtZSI6ImJlbmphbWluLnNhbGF6YXIxN2dtYWlsLmNvbSIsImp0aSI6ImMzMTA2YmIxLTMwMmMtNGQ1Ni05ZDc2LTU5ZjVjMDZiZTY2MiIsImNsaWVudF9pZCI6ImJhY2tlbmRBcHAiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.a19XFYS9Q6mR5EKqBKPh3fkpdOJmUJEghE9Mb9Xg3EQ"
}

--------------------------------------------------
localhost:8090/api/security/oauth/token -> A través de este servicio obtendremos el token para consultar los demás servicios, lo que debemos hacer es:
-Ir a postman, agregar el end-point y seleccionar POST
-Ir a la pestaña Authorization y seleccionar el type como "Basic Auth" y agregar las siguientes credenciales 
Username = backendApp
Password = 12345
-Una ves ingresamos las credenciales no dirigimos a la pestaña Body, seleccionamos application/x-www-form-urlencoded
y agregamos las siguientes llave/valor
username = benjamin.salazar17gmail.com
password = 123
grant_type = password

cuando ya tengamos esto podemos presionar send para obtener nuestro token y consultar las demas apis que piden autorización como por ejemplo
localhost:8090/api/users/1 (GET)
o

localhost:8090/api/users/1 (PUT)
con el request ejemplo para modificar,
{
    "id": 1,
    "name": "benjamin",
    "email": "benjamin.salazar178gmail.com",
    "password": "1234",
    "lastLogin": "2020-10-06T01:43:29.111+00:00",
    "token": "",
    "isActive": "Y",
    "createdAt": "2020-10-06T01:43:29.111+00:00",
    "updatedAt": "2020-10-06T01:46:26.455+00:00",
    "phones": [
        {
            "id": 5,
            "number": "123123",
            "cityCode": "123",
            "countryCode": "1"
        },
        {
            "id": 6,
            "number": "123123",
            "cityCode": "123",
            "countryCode": "12"
        }
    ]
}



-para agregar el token y consultar o modificar debemos ir a la pestaña Authorization, seleccionar el tipo Bearer Token y agregar el token obtenido desde el servicio de autenticación.
