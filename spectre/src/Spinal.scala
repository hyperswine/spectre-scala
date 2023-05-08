import spinal.core._

class Playground extends Component {
  val io = new Bundle {
    val a = in.Bool()
    val b = in.Bool()
    val c = out.Bool()
    val readData = out.Bits()
    val writeValid = in.Bool()
    val readValid = in.Bool()

    val writeAddress = in.UInt()
    val writeData = in.Bits()
    val readAddress = in.UInt()
  }

  val cond = Bool()
  val mem = Mem(Bits(32 bits), wordCount = 256)

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

class Dataflow extends Component {

}

object Playground {
  // Let's go
  def main(args: Array[String]): Unit =
    SpinalVhdl(new Playground)
}
