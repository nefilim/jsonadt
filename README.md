# Schema Examples

```json
   {
      "Header":{
         "Status":200
      },
      "ResponseType":"MultiplePerson",
      "Body":{
         "Persons":[
            {
               "ID":1,
               "FirstName":"Peter",
               "LastName":"van Rensburg"
            }
         ]
      }
   }
```

```json
  {
      "Header":{
         "Status":200
      },
      "ResponseType":"OneFish",
      "Body":{
         "fish":{
            "id":1,
            "species":"Barracuda"
         }
      }
   }
```