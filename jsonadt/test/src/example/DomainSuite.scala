package example

import example.Domain.{Response, ResponseBody, Status}
import io.circe.Json
import io.circe.parser.*

class DomainSuite extends munit.FunSuite {
  test("StatusCodec") {
    import codecs.circe.{statusDecoder, statusEncoder}

    val jsonString =
      """200""".stripMargin

    assertEquals(
      decode[Status](jsonString),
      Right(Status.Ok)
    )
    assertEquals(
      statusEncoder(Status.Ok),
      Json.fromInt(200)
    )
  }

  test("ResponseHeader") {
    import Domain.ResponseHeader
    import codecs.circe.*
    val jsonString =
      """
        |{"Status": 200}
        |""".stripMargin

    assertEquals(
      decode[ResponseHeader](jsonString),
      Right(ResponseHeader(Status.Ok))
    )
  }

  test("Response") {
    import codecs.circe.*
    val jsonString1 =
      """
        |{
        | "Header": { "Status": 200 },
        | "ResponseType": "OneFish",
        | "Body": {
        |   "fish":
        |     { "id": 1, "species": "Barracuda" }
        |
        | }
        |}
        |""".stripMargin
    println(decode[Response[ResponseBody]](jsonString1))

//    val jsonString =
//      """
//        |{
//        | "Header": { "Status": 200 },
//        | "ResponseType": "MultiplePerson",
//        | "Body": {
//        |   "Persons": [
//        |     { "ID": 1, "FirstName": "Peter", "LastName": "van Rensburg" }
//        |   ]
//        | }
//        |}
//        |""".stripMargin
    val jsonString =
      """
        |{"Header":{"Status":200},"ResponseType":"MultiplePerson","Body":{"Persons":[{"ID":1,"FirstName":"Peter","LastName":"van Rensburg"}]}}
        |""".stripMargin
    println(decode[Response[ResponseBody]](jsonString))
  }
}
