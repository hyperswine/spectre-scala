import spinal.core._

class Playground extends Component {
  val io = new Bundle {
    val a          = in.Bool()
    val b          = in.Bool()
    val c          = out.Bool()
    val readData   = out.Bits()
    val writeValid = in.Bool()
    val readValid  = in.Bool()

    val writeAddress = in.UInt()
    val writeData    = in.Bits()
    val readAddress  = in.UInt()
  }

  val cond = Bool()
  val mem  = Mem(Bits(32 bits), wordCount = 256)

  mem.write(
    enable  = io.writeValid,
    address = io.writeAddress,
    data    = io.writeData
  )

  io.readData := mem.readSync(
    enable  = io.readValid,
    address = io.readAddress
  )

  // Here we define some asynchronous logic
  io.c := io.a & io.b

  // so like read or something...
  // do stuff like you know that algorithm??
  // wait that algo is useless

  // just add and vectors

  // do I want graph reduction?
  // do I want to try out dataflow? like that graph reduction one which kinda used it

  // https://spinalhdl.github.io/SpinalDoc-RTD/master/SpinalHDL/Sequential%20logic/memory.html#mixed-width-ram

}

object VectorInstruction extends SpinalEnum {
  val add, sub = newElement()
}

// elementwise or array - scalar
/*
  1. v1 + v2
  [1, 2, 3] + [2, 3, 4]

  2. v1 + s
  [1, 2, 3] + 3
  [1, 2, 3] + [3, 3, 3]

  3. v1 ** v2
  [1, 2, 3] ** [2, 3, 4]
*/

class VectorProc(lanes: Int, width: Int) extends Component {
  val io = new Bundle {
    val a = in.Vec[UInt](lanes * width)
    val b = in.Vec[UInt](lanes * width)
    val instruction = in.Bits()
    val output = out.Vec[UInt](lanes * width)
  }

  val reg = Reg(UInt((lanes * width) bits))

  io.output(0) := 0
}

object Playground {
  // Let's go
  def main(args: Array[String]): Unit =
    // SpinalVhdl(new Playground)
    SpinalVerilog(new VectorProc(2, 16))
}
