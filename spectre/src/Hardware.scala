import chisel3._
import chisel3.util._

// implicit class LoopOps(val i: Int) extends AnyVal {
//   def ..(j: Int) = (i, j)
//   def loop(func: Int => Unit) = {
//     val (start, end) = i
//     def loopRec(start: Int, end: Int)(func: Int => Unit): Unit = {
//       if (start <= end) {
//         func(start)
//         loopRec(start + 1, end)(func)
//       }
//     }
//     loopRec(start, end)(func)
//   }
// }

def loop(start: Int, end: Int)(func: Int => Unit): Unit = {
  if (start <= end) {
    func(start)
    loop(start + 1, end)(func)
  }
}


class Hardware extends Module {
  val io = IO(new Bundle {
    val mem_input  = Input(UInt(16.W))
    val mem_output = Output(UInt(16.W))
  })

  io.mem_output := io.mem_input
}

// 2048-bit vector unit
class VectorUnitIO(val dataWidth: Int, val vecSize: Int) extends Bundle {
  val op       = Input(UInt(3.W))
  val dataType = Input(UInt(3.W))
  val src1     = Input(UInt(vecSize.W))
  val src2     = Input(UInt(vecSize.W))
  val src3     = Input(UInt(vecSize.W))
  val result   = Output(UInt(vecSize.W))
}

class VectorUnit(val dataWidth: Int = 2048) extends Module {
  val io = IO(new VectorUnitIO(dataWidth, dataWidth))

  // Your vector unit implementation will go here
  // Define the different operations and data types
  val OP_ADD = 0.U(3.W)
  val OP_SUB = 1.U(3.W)
  val OP_MUL = 2.U(3.W)
  val OP_DIV = 3.U(3.W)
  val OP_MAC = 4.U(3.W)

  val DATA_TYPE_8   = 0.U(3.W)
  val DATA_TYPE_16  = 1.U(3.W)
  val DATA_TYPE_32  = 2.U(3.W)
  val DATA_TYPE_64  = 3.U(3.W)
  val DATA_TYPE_128 = 4.U(3.W)

  def processElements(op: UInt, src1: UInt, src2: UInt, src3: UInt): UInt = {
    val result = WireDefault(src1)
    switch(op) {
      is(OP_ADD) { result := src1 + src2 }
      is(OP_SUB) { result := src1 - src2 }
      is(OP_MUL) { result := src1 * src2 }
      is(OP_DIV) { result := src1 / src2 }
      is(OP_MAC) { result := (src1 * src2) + src3 }
    }
    result
  }

  val resultVec = Wire(Vec(dataWidth / 8, UInt(8.W)))
  for (i <- 0 until dataWidth by 8) {
    switch(io.dataType) {
      is(DATA_TYPE_8) {
        resultVec(i / 8) := processElements(
          io.op,
          io.src1(8 * i + 7, 8 * i),
          io.src2(8 * i + 7, 8 * i),
          io.src3(8 * i + 7, 8 * i)
        )
      }
      is(DATA_TYPE_16) {
        when((i % 16 == 0).asBool) {
          resultVec(i / 8) := processElements(
            io.op,
            io.src1(16 * i + 15, 16 * i),
            io.src2(16 * i + 15, 16 * i),
            io.src3(16 * i + 15, 16 * i)
          )
        }
      }
      is(DATA_TYPE_32) {
        when((i % 32 == 0).asBool) {
          resultVec(i / 8)
        }
      }
      is(DATA_TYPE_64) {
        when((i % 64 == 0).asBool) {
          resultVec(i / 8) := processElements(
            io.op,
            io.src1(64 * i + 63, 64 * i),
            io.src2(64 * i + 63, 64 * i),
            io.src3(64 * i + 63, 64 * i)
          )
        }
      }
      is(DATA_TYPE_128) {
        when((i % 128 == 0).asBool) {
          resultVec(i / 8) := processElements(
            io.op,
            io.src1(128 * i + 127, 128 * i),
            io.src2(128 * i + 127, 128 * i),
            io.src3(128 * i + 127, 128 * i)
          )
        }
      }
    }
  }

  io.result := resultVec.asUInt

}

class TaggedMem

class ContentAddressableMemory(val dataWidth: Int, val numEntries: Int) extends Module {
  val io = IO(new Bundle {
    val readAddr      = Input(UInt(log2Ceil(numEntries).W))
    val readData      = Output(UInt(dataWidth.W))
    val writeEnable   = Input(Bool())
    val writeData     = Input(UInt(dataWidth.W))
    val writeAddr     = Input(UInt(log2Ceil(numEntries).W))
    val searchKey     = Input(UInt(dataWidth.W))
    val searchHit     = Output(Bool())
    val searchHitAddr = Output(UInt(log2Ceil(numEntries).W))
  })

  val mem = RegInit(VecInit(Seq.fill(numEntries)(0.U(dataWidth.W))))

  // Read data from memory
  io.readData := mem(io.readAddr)

  // Write data to memory
  when(io.writeEnable) {
    mem(io.writeAddr) := io.writeData
  }

  // Search for the given key
  val searchHits = Wire(Vec(numEntries, Bool()))
  for (i <- 0 until numEntries) {
    searchHits(i) := mem(i) === io.searchKey
  }
  io.searchHit     := searchHits.reduce(_ || _)
  io.searchHitAddr := PriorityEncoder(searchHits)
}
