// something about klipper
// and exposing the core functionalities to the network interface
// maybe even have a config DEFAULT that implements a TCP method of comms

// slicer though... maybe a default "just works" set, and some other software on top of that to detect if its just not going to work

import chisel3._
import circt.stage.ChiselStage

class Printer extends Module {
  val io = IO(new Bundle {})
  printf("hello world\n")
}

object VerilogMain extends App {
//   ChiselStage.emitSystemVerilog(new Printer)
  ChiselStage.emitCHIRRTL(new Printer)
}
