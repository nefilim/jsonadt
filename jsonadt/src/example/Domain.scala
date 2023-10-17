package example

import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, Encoder, Json}

import scala.util.Try

object Domain {
  // DISCUSS "enumeration"
  sealed abstract class Status(val code: Int, val name: String)
  object Status {
    case object Ok extends Status(200, "Ok")
    case object BadRequest extends Status(400, "Bad Request")
    case object Error extends Status(500, "Internal Error")
  }

  sealed trait Header
  case class ResponseHeader(status: Status)

  sealed trait ResponseBody
  object ResponseBody {
    // DISCUSS style - match JSON case in the variable names or manually create a Codec?
    case class Human(ID: Int, FirstName: String, LastName: String)
    case class MultiplePersonResponseBody(Persons: List[Human]) extends ResponseBody

    case class Animal(id: Int, name: String)
    case class MultiplePetResponseBody(pets: List[Animal]) extends ResponseBody

    case class Fish(id: Int, species: String)
    case class MultipleFishResponseBody(school: List[Fish]) extends ResponseBody
    case class OneFishResponseBody(fish: Fish) extends ResponseBody

    case class UnspecifiedJSON(body: Json) extends ResponseBody
    case class DecodeError(cause: Throwable) extends ResponseBody
  }

  sealed abstract class ResponseType(val name: String)
  object ResponseType {
    case object MultiplePerson extends ResponseType("MultiplePerson")
    case object MultiplePet extends ResponseType("MultiplePet")
    case object MultipleFish extends ResponseType("MultipleFish")
    case object OneFish extends ResponseType("OneFish")
  }

  // DISCUSS invariant .. is there an application for covariance here?
  case class Response[T <: ResponseBody](
    header: ResponseHeader,
    responseType: String, // DISCUSS discriminator
    body: Option[T] = None,
  )

  // DISCUSS alternative implementation
  case class ResponseAlt(
    header: ResponseHeader,
    responseType: String,
    body: Option[ResponseBody] = None, // DISCUSS Option is covariant
  )
}

object codecs {
  object circe {
    import Domain.*

    // DISUCSS
    // implicit val statusCodec: Codec[Status] = deriveCodec // kekw
    implicit val statusEncoder: Encoder[Status] = Encoder.encodeInt.contramap(s => s.code)
    implicit val statusDecoder: Decoder[Status] = Decoder.decodeInt.emapTry {
      case Status.Ok.code => Try(Status.Ok)
      // case Status.BadRequest.code => Try(Status.BadRequest) // DISCUSS exhaustive match?
      case Status.Error.code => Try(Status.Ok)
      // DISCUSS what if this function is not total?
    }

    implicit val responseHeaderDecoder: Decoder[ResponseHeader] =
      Decoder.forProduct1[ResponseHeader, Status]("Status")(ResponseHeader)

    object responseBody {
      import Domain.ResponseBody.*
      implicit val humanDecoder: Decoder[Human] = deriveDecoder
      implicit val multiplePersonResponseDecoder: Decoder[MultiplePersonResponseBody] = deriveDecoder
      implicit val animalDecoder: Decoder[Animal] = deriveDecoder
      implicit val multipleAnimalResponseDecoder: Decoder[MultiplePetResponseBody] = deriveDecoder
      implicit val fishDecoder: Decoder[Fish] = deriveDecoder
      implicit val multipleFishResponseDecoder: Decoder[MultipleFishResponseBody] = deriveDecoder
      implicit val oneFishDecoder: Decoder[OneFishResponseBody] = deriveDecoder
    }

    // DISCUSS Possible Decoders for Response variants

    implicit def decoderCompiler[T <: ResponseBody: Decoder]: Decoder[Response[T]] = deriveDecoder

    implicit val decoder: Decoder[Response[ResponseBody]] = {
      import responseBody.*

      Decoder.forProduct3[Response[ResponseBody], ResponseHeader, String, Json]("Header", "ResponseType", "Body") { (h, rt, b) =>
        val body = rt match {
          case ResponseType.MultiplePerson.name => b.as[ResponseBody.MultiplePersonResponseBody]
          case ResponseType.MultiplePet.name => b.as[ResponseBody.MultiplePetResponseBody]
          case ResponseType.MultipleFish.name => b.as[ResponseBody.MultipleFishResponseBody]
          case ResponseType.OneFish.name => b.as[ResponseBody.OneFishResponseBody]
          case _ => Right(ResponseBody.UnspecifiedJSON(b))
        }
        Response[ResponseBody](h, rt, body.fold(e => Some(ResponseBody.DecodeError(e)), rb => Some(rb)))
      }
    }

    implicit val decoderAlt: Decoder[ResponseAlt] = {
      import responseBody.*

      Decoder.forProduct3[ResponseAlt, ResponseHeader, String, Json]("Header", "ResponseType", "Body") { (h, rt, b) =>
        val body = rt match {
          case ResponseType.MultiplePerson.name => b.as[ResponseBody.MultiplePersonResponseBody]
          case ResponseType.MultiplePet.name => b.as[ResponseBody.MultiplePetResponseBody]
          case ResponseType.MultipleFish.name => b.as[ResponseBody.MultipleFishResponseBody]
          case ResponseType.OneFish.name => b.as[ResponseBody.OneFishResponseBody]
          case _ => Right(ResponseBody.UnspecifiedJSON(b))
        }
        ResponseAlt(h, rt, body.fold(e => Some(ResponseBody.DecodeError(e)), rb => Some(rb)))
      }
    }
  }
}