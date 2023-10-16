import mill.scalalib._

object Dependencies {
  object plugins {
    val betterMonadicFor = ivy"com.olegpy::better-monadic-for:0.3.1"
    val kindProjector = ivy"org.typelevel:::kind-projector:0.13.2"
  }

  object cats {
    private val catsCoreVersion = "2.10.0"
    private val catsEffectVersion = "3.5.2"
    private val log4CatsVersion = "2.6.0"

    val core = ivy"org.typelevel::cats-core:$catsCoreVersion"
    val effect = ivy"org.typelevel::cats-effect:$catsEffectVersion"
    val log4Cats = ivy"org.typelevel::log4cats-slf4j:$log4CatsVersion"
    val log4CatsNoop = ivy"org.typelevel::log4cats-noop:$log4CatsVersion"
  }

  object fs2 {
    val fs2Version = "3.9.2"
    val core = ivy"co.fs2::fs2-core:$fs2Version"
    val io = ivy"co.fs2::fs2-io:$fs2Version"

    object data {
      val circe = ivy"org.gnieh::fs2-data-json-circe:1.9.0"
    }
  }

  object logback {
    private val logbackVersion = "1.4.11"

    val classic = ivy"ch.qos.logback:logback-classic:$logbackVersion"
  }

  object circe {
    private val circeVersion = "0.14.6"

    val core = ivy"io.circe::circe-core:$circeVersion"
    val generic = ivy"io.circe::circe-generic:$circeVersion"
    val genericExtras = ivy"io.circe::circe-generic-extras:0.14.3"
    val parser = ivy"io.circe::circe-parser:$circeVersion"
  }

  object test {
    private val munitVersion = "0.7.29"
    private val munitVersionCatsEffect = "1.0.7"

    val munit = ivy"org.scalameta::munit:$munitVersion"
    val munitCatsEffect = ivy"org.typelevel::munit-cats-effect-3:$munitVersionCatsEffect"
  }
}
