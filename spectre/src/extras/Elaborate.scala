// import circt.stage._

// object Elaborate extends App {
//   def top       = new GCD()
//   val useMFC    = true
//   val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
//   if (useMFC) {
//     (new ChiselStage).execute(args, generator :+ CIRCTTargetAnnotation(CIRCTTarget.Verilog))
//   } else {
//     (new chisel3.stage.ChiselStage).execute(args, generator)
//   }
// }
