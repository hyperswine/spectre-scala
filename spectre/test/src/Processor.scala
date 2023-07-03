import spinal.core.sim._
import spinal.core._

object VectorProcessorSim extends App {
  SimConfig.withWave.compile(new VectorProcessor(4, 64)).doSim { dut =>
    // Initialization
    // dut.io.src1.zipWithIndex.foreach { case (wire, i) => wire #= i }
    // dut.io.src2.zipWithIndex.foreach { case (wire, i) => wire #= i * 2 }

    // dut.io.src1 := Vec(0, 1, 2, 4)
    // dut.io.src2 := Vec(0, 1, 2, 4)

    // dut.io.src1 #= Vec(Vector(0, 1, 2, 3).map(U(_, 64 bits)))
    // dut.io.src2 #= Vec(Vector(0, 1, 2, 3).map(U(_, 64 bits)))

    Vector(0, 1, 2, 3).zipWithIndex.foreach {
      case (value, index) =>
        dut.io.src1(index) #= value
        dut.io.src2(index) #= value
    }

    // dut.clockDomain.forkStimulus(period = 10)
    // dut.clockDomain.waitSampling()

    // Simulation for each instruction
    // for (instruction <- VectorInstruction.elements) {
    //   dut.io.instruction #= instruction
    //   println(s"Instruction: $instruction")
    //   println("Output: " + dut.io.output.toVector.map(_.toBigInt).mkString(", "))
    // }

    dut.io.instruction #= VectorInstruction.add

    sleep(1)

    val res = dut.io.output

    println("Output: " + res.mkString(", "))

    // sleep(1)
  }
}

// Identity takes n bits in a and gives them back in z
class Identity(n: Int) extends Component {
  val io = new Bundle {
    val a = in.Bits(n bits)
    val z = out.Bits(n bits)
  }

  io.z := io.a
}

object TestIdentity extends App {
  // Use the component with n = 3 bits as "dut" (device under test)
  SimConfig.withWave.compile(new Identity(3)).doSim { dut =>
    // For each number from 3'b000 to 3'b111 included
    for (a <- 0 to 7) {
      // Apply input
      dut.io.a #= a
      // Wait for a simulation time unit
      sleep(1)
      // Read output
      val z = dut.io.z.toInt
      // Check result
      assert(z == a, s"Got $z, expected $a")
    }
  }
}
