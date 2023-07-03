import spinal.core._

class Processor extends Component {
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

  // do the operation on a and b and output to c

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
  val add, sub, mult, div = newElement()
}

// val reg = Reg(UInt((lanes * width) bits))

// no width for now
class VectorProcessor(lanes: Int, width: Int) extends Component {
  val io = new Bundle {
    val src1        = in.Vec[UInt](lanes)
    val src2        = in.Vec[UInt](lanes)
    val instruction = in(VectorInstruction)
    val output      = out.Vec[UInt](lanes)
  }

  switch(io.instruction) {
    is(VectorInstruction.add) { io.output := Vec(io.src1.zip(io.src2).map({ case (a, b) => a + b })) }
    is(VectorInstruction.sub) { io.output := Vec(io.src1.zip(io.src2).map({ case (a, b) => a - b })) }
    is(VectorInstruction.mult) { io.output := Vec(io.src1.zip(io.src2).map({ case (a, b) => a * b })) }
    is(VectorInstruction.div) { io.output := Vec(io.src1.zip(io.src2).map({ case (a, b) => a / b })) }
  }
}

class Memory(size: Int) extends Component {
  val io = new Bundle {
    val addr        = in.UInt(log2Up(size) bits)
    val writeEnable = in.Bool()
    val writeData   = in.Bits(32 bits)
    val readData    = out.Bits(32 bits)
  }

  val mem = Mem(Bits(32 bits), size)

  mem.write(
    enable  = io.writeEnable,
    address = io.addr,
    data    = io.writeData
  )

  io.readData := mem.readSync(io.addr)
}

object Playground {
  // Let's go
  def main(args: Array[String]): Unit =
    println("Hi Verilog")
  // SpinalVhdl(new Playground)
  // SpinalVerilog(new VectorProc(2, 16))
}
