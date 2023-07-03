import spinal.core.sim._

// object VectorProcessorSim {
//   def main(args: Array[String]): Unit = {
//     SimConfig.withWave.doSim(new VectorProcessor(4, 8)) { dut =>
//       // Initialization
//       dut.io.src1.zipWithIndex.foreach { case (wire, i) => wire #= i }
//       dut.io.src2.zipWithIndex.foreach { case (wire, i) => wire #= i * 2 }
//       dut.clockDomain.forkStimulus(period = 10)

//       // Simulation for each instruction
//       for (instruction <- VectorInstruction.elements) {
//         dut.io.instruction #= instruction
//         dut.clockDomain.waitSampling()
//         println(s"Instruction: $instruction")
//         println("Output: " + dut.io.output.toVector.map(_.toBigInt).mkString(", "))
//       }
//     }
//   }
// }

object VectorProcessorSim {
  def main(args: Array[String]): Unit = {
    SimConfig.withWave.doSim(new VectorProcessor(4, 8)) { dut =>
      // Initialization
      dut.clockDomain.forkStimulus(period = 10)

      // Simulation for each instruction
      for (instruction <- VectorInstruction.elements) {
        dut.io.src1.zipWithIndex.foreach { case (wire, i) => wire #= i }
        dut.io.src2.zipWithIndex.foreach { case (wire, i) => wire #= i * 2 }
        dut.io.instruction #= instruction
        dut.clockDomain.waitSampling()
        println(s"Instruction: $instruction")
        println("Output: " + dut.io.output.toVector.map(_.toBigInt).mkString(", "))
      }
    }
  }
}
