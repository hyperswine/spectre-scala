import chisel3._
import chiseltest._
import chisel3.experimental.BundleLiterals._

import utest._

object GCDSpec extends ChiselUtestTester {
  val tests = Tests {
    test("GCD") {
      testCircuit(new DecoupledGcd(16)) { dut =>
        dut.input.initSource()
        dut.input.setSourceClock(dut.clock)
        dut.output.initSink()
        dut.output.setSinkClock(dut.clock)
        val testValues = for { x <- 0 to 10; y <- 0 to 10 } yield (x, y)
        val inputSeq   = testValues.map { case (x, y) => (new GcdInputBundle(16)).Lit(_.value1 -> x.U, _.value2 -> y.U) }
        val resultSeq = testValues.map {
          case (x, y) =>
            (new GcdOutputBundle(16)).Lit(_.value1 -> x.U, _.value2 -> y.U, _.gcd -> BigInt(x).gcd(BigInt(y)).U)
        }
        fork {
          val (seq1, seq2) = inputSeq.splitAt(resultSeq.length / 3)
          dut.input.enqueueSeq(seq1)
          dut.clock.step(11)
          dut.input.enqueueSeq(seq2)
        }.fork {
          val (seq1, seq2) = resultSeq.splitAt(resultSeq.length / 2)
          dut.output.expectDequeueSeq(seq1)
          dut.clock.step(10)
          dut.output.expectDequeueSeq(seq2)
        }.join()
      }
    }
  }
}
