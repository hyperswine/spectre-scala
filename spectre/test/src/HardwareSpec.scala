
import chisel3._
import chiseltest._
import chisel3.experimental.BundleLiterals._

import utest._

object HardwareSpec extends ChiselUtestTester {
  val tests = Tests {
    test("Hardware") {
      testCircuit(new Hardware) { hw =>
        hw.io.mem_input.poke(1.asUInt)
        hw.clock.step(1)
        hw.io.mem_output.expect(1)
      }
    }
  }
}
