val x = List(1, 2, 3, 4, 5)

val res = x.zip(x).map({ case (a, b) => a + b })
println(res)
