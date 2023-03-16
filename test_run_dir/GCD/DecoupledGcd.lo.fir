circuit DecoupledGcd :
  module DecoupledGcd :
    input clock : Clock
    input reset : UInt<1>
    output input_ready : UInt<1>
    input input_valid : UInt<1>
    input input_bits_value1 : UInt<16>
    input input_bits_value2 : UInt<16>
    input output_ready : UInt<1>
    output output_valid : UInt<1>
    output output_bits_value1 : UInt<16>
    output output_bits_value2 : UInt<16>
    output output_bits_gcd : UInt<16>

    reg xInitial : UInt<16>, clock with :
      reset => (UInt<1>("h0"), xInitial) @[DecoupledGCD.scala 19:24]
    reg yInitial : UInt<16>, clock with :
      reset => (UInt<1>("h0"), yInitial) @[DecoupledGCD.scala 20:24]
    reg x : UInt<16>, clock with :
      reset => (UInt<1>("h0"), x) @[DecoupledGCD.scala 21:24]
    reg y : UInt<16>, clock with :
      reset => (UInt<1>("h0"), y) @[DecoupledGCD.scala 22:24]
    reg busy : UInt<1>, clock with :
      reset => (UInt<1>("h0"), busy) @[DecoupledGCD.scala 23:28]
    reg resultValid : UInt<1>, clock with :
      reset => (UInt<1>("h0"), resultValid) @[DecoupledGCD.scala 24:28]
    node _input_ready_T = eq(busy, UInt<1>("h0")) @[DecoupledGCD.scala 26:19]
    node _T = gt(x, y) @[DecoupledGCD.scala 31:12]
    node _x_T = sub(x, y) @[DecoupledGCD.scala 32:14]
    node _x_T_1 = tail(_x_T, 1) @[DecoupledGCD.scala 32:14]
    node _y_T = sub(y, x) @[DecoupledGCD.scala 34:14]
    node _y_T_1 = tail(_y_T, 1) @[DecoupledGCD.scala 34:14]
    node _GEN_0 = mux(_T, _x_T_1, x) @[DecoupledGCD.scala 31:17 21:24 32:9]
    node _GEN_1 = mux(_T, y, _y_T_1) @[DecoupledGCD.scala 31:17 22:24 34:9]
    node _T_1 = eq(x, UInt<1>("h0")) @[DecoupledGCD.scala 36:12]
    node _T_2 = eq(y, UInt<1>("h0")) @[DecoupledGCD.scala 36:25]
    node _T_3 = or(_T_1, _T_2) @[DecoupledGCD.scala 36:20]
    node _T_4 = eq(x, UInt<1>("h0")) @[DecoupledGCD.scala 37:14]
    node _GEN_2 = mux(_T_4, y, x) @[DecoupledGCD.scala 37:23 38:25 40:25]
    node _T_5 = and(output_ready, resultValid) @[DecoupledGCD.scala 47:25]
    node _GEN_3 = mux(_T_5, UInt<1>("h0"), busy) @[DecoupledGCD.scala 47:41 48:21 23:28]
    node _GEN_4 = mux(_T_5, UInt<1>("h0"), UInt<1>("h1")) @[DecoupledGCD.scala 47:41 49:21 45:26]
    node _GEN_5 = validif(_T_3, _GEN_2) @[DecoupledGCD.scala 36:34]
    node _GEN_6 = validif(_T_3, xInitial) @[DecoupledGCD.scala 36:34 43:26]
    node _GEN_7 = validif(_T_3, yInitial) @[DecoupledGCD.scala 36:34 44:26]
    node _GEN_8 = mux(_T_3, _GEN_4, resultValid) @[DecoupledGCD.scala 24:28 36:34]
    node _GEN_9 = mux(_T_3, _GEN_3, busy) @[DecoupledGCD.scala 23:28 36:34]
    node _GEN_10 = mux(input_valid, UInt<1>("h1"), _input_ready_T) @[DecoupledGCD.scala 53:23 Decoupled.scala 83:20 DecoupledGCD.scala 26:16]
    node _GEN_11 = mux(input_valid, input_bits_value1, x) @[DecoupledGCD.scala 53:23 55:16 21:24]
    node _GEN_12 = mux(input_valid, input_bits_value2, y) @[DecoupledGCD.scala 53:23 56:16 22:24]
    node _GEN_13 = mux(input_valid, input_bits_value1, xInitial) @[DecoupledGCD.scala 53:23 57:16 19:24]
    node _GEN_14 = mux(input_valid, input_bits_value2, yInitial) @[DecoupledGCD.scala 53:23 58:16 20:24]
    node _GEN_15 = mux(input_valid, UInt<1>("h1"), busy) @[DecoupledGCD.scala 53:23 59:16 23:28]
    node _GEN_16 = mux(busy, _GEN_0, _GEN_11) @[DecoupledGCD.scala 30:14]
    node _GEN_17 = mux(busy, _GEN_1, _GEN_12) @[DecoupledGCD.scala 30:14]
    node _GEN_18 = validif(busy, _GEN_5) @[DecoupledGCD.scala 30:14]
    node _GEN_19 = validif(busy, _GEN_6) @[DecoupledGCD.scala 30:14]
    node _GEN_20 = validif(busy, _GEN_7) @[DecoupledGCD.scala 30:14]
    node _GEN_21 = mux(busy, _GEN_8, resultValid) @[DecoupledGCD.scala 30:14 24:28]
    node _GEN_22 = mux(busy, _GEN_9, _GEN_15) @[DecoupledGCD.scala 30:14]
    node _GEN_23 = mux(busy, _input_ready_T, _GEN_10) @[DecoupledGCD.scala 30:14 26:16]
    node _GEN_24 = mux(busy, xInitial, _GEN_13) @[DecoupledGCD.scala 30:14 19:24]
    node _GEN_25 = mux(busy, yInitial, _GEN_14) @[DecoupledGCD.scala 30:14 20:24]
    input_ready <= _GEN_23
    output_valid <= resultValid @[DecoupledGCD.scala 27:16]
    output_bits_value1 <= _GEN_19
    output_bits_value2 <= _GEN_20
    output_bits_gcd <= _GEN_18
    xInitial <= _GEN_24
    yInitial <= _GEN_25
    x <= _GEN_16
    y <= _GEN_17
    busy <= mux(reset, UInt<1>("h0"), _GEN_22) @[DecoupledGCD.scala 23:{28,28}]
    resultValid <= mux(reset, UInt<1>("h0"), _GEN_21) @[DecoupledGCD.scala 24:{28,28}]
