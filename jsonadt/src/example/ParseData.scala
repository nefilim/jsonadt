package example

import cats.effect.{IO, IOApp}
import example.Domain.{Response, ResponseBody}
import fs2.data.json.*
import fs2.data.json.circe.*
import fs2.io.file.{Files, Path}
import fs2.text

object ParseData extends IOApp.Simple {

  override def run: IO[Unit] = {
    import codecs.circe.decoder

    Files[IO].readAll(Path("data.json"))
      .through(text.utf8.decode)
      .through(text.lines)
      .debug()
      .through(ast.parse)
      .through(ast.tokenize)
      .through(codec.deserialize)
      .evalTap {
        case _: Response[ResponseBody.MultiplePersonResponseBody] => IO.println("peeps!")
        case _: Response[ResponseBody.OneFishResponseBody] => IO.println("¡un fish!")
      }
      .debug()
      .compile
      .drain
  }
}
