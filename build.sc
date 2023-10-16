import mill._
import mill.scalalib._
import $file.dependencies
import dependencies.Dependencies

import $ivy.`io.github.davidgregory084::mill-tpolecat::0.3.5`
import io.github.davidgregory084.TpolecatModule

object jsonadt extends TpolecatModule {

  def scalaVersion = "2.13.12"

  override def scalacPluginIvyDeps: T[Agg[Dep]] = super.scalacPluginIvyDeps() ++ Agg(
    Dependencies.plugins.betterMonadicFor,
    Dependencies.plugins.kindProjector,
  )

  override def scalacOptions: T[Seq[String]] = (super.scalacOptions() ++ Seq(
    "-Ymacro-annotations",
  )).filterNot(Set(
    "-Xfatal-warnings",
  ))

  override def ivyDeps = Agg(
    Dependencies.circe.core,
    Dependencies.circe.generic,
    Dependencies.circe.genericExtras,
    Dependencies.circe.parser,
    Dependencies.fs2.core,
    Dependencies.fs2.io,
    Dependencies.fs2.data.circe,
    Dependencies.cats.core,
    Dependencies.cats.effect,
    Dependencies.cats.log4Cats,
    Dependencies.logback.classic,
    Dependencies.test.munit,
    Dependencies.test.munitCatsEffect,
  )

  object test extends ScalaTests with TestModule.Munit {
    override def ivyDeps = Agg(
      ivy"org.scalameta::munit::0.7.29"
    )
  }
}

