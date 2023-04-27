// something about klipper
// and exposing the core functionalities to the network interface
// maybe even have a config DEFAULT that implements a TCP method of comms

// slicer though... maybe a default "just works" set, and some other software on top of that to detect if its just not going to work

import spinal.core._

// define a minimal working interface, like wifi?

class Printer extends Component {
  val io = new Bundle {
    val b = in.Bool()
    val c = out.Bool()
  }

  io.c := io.b
}
