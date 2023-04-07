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
    test("Vector") {
      testCircuit(new VectorUnit) { vec_unit =>
        def testOperation(
          op:             UInt,
          dataType:       UInt,
          src1:           BigInt,
          src2:           BigInt,
          src3:           BigInt,
          expectedResult: BigInt
        ): Unit = {
          vec_unit.io.op.poke(dataType)
          vec_unit.io.dataType.poke(dataType)
          vec_unit.io.src1.poke(src1)
          vec_unit.io.src2.poke(src2)
          vec_unit.io.src2.poke(src2)

          vec_unit.clock.step(1)
          vec_unit.io.result.expect(expectedResult)
        }

        // Example: Add two 8-bit integers
        testOperation(0.U, 8.U, 1, 2, 0, 3)
      }
    }
  }
}
