import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule
import mill.scalalib.TestModule.Utest
import mill.bsp._

object spectre extends ScalaModule with ScalafmtModule { m =>
  override def scalaVersion = "2.13.8"
  override def scalacOptions = Seq(
    "-language:reflectiveCalls",
    "-deprecation",
    "-feature",
    "-Xcheckinit",
  )
  override def ivyDeps = Agg(
    ivy"edu.berkeley.cs::chisel3:3.6.0",
    // ivy"com.sifive::chisel-circt:0.6.0",
    ivy"com.github.spinalhdl::spinalhdl-core:1.6.4",
    ivy"com.github.spinalhdl::spinalhdl-lib:1.6.4"
  )
  override def scalacPluginIvyDeps = Agg(
    ivy"edu.berkeley.cs:::chisel3-plugin:3.6.0",
    ivy"com.github.spinalhdl::spinalhdl-idsl-plugin:1.6.4"
  )
  object test extends Tests with Utest {
    override def ivyDeps = m.ivyDeps() ++ Agg(
      ivy"com.lihaoyi::utest:0.7.10",
      ivy"edu.berkeley.cs::chiseltest:0.5.4"
    )
  }
}
