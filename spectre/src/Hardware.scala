import chisel3._

class Hardware extends Module {
  val io = IO(new Bundle {
    val mem_input  = Input(UInt(16.W))
    val mem_output = Output(UInt(16.W))
  })

  io.mem_output := io.mem_input
}
