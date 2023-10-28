println("getByte")
if isdefined(Main, :getByte) && !isnothing(getByte)
	error("getByte already defined!")
end
getByte = callJava("getByte")
println(typeof(getByte))
println(getByte)
if typeof(getByte) != Int8
	error("getByte not Int8!")
end
callJava("setByte",getByte)

println("getByteVector")
if isdefined(Main, :getByteVector) && !isnothing(getByteVector)
	error("getByteVector already defined!")
end
getByteVector = callJava("getByteVector")
println(eltype(getByteVector))
println(getByteVector)
if eltype(getByteVector) != Int8
	error("getByteVector not Int8!")
end
callJava("setByteVector",getByteVector)

println("getByteVectorAsList")
if isdefined(Main, :getByteVectorAsList) && !isnothing(getByteVectorAsList)
	error("getByteVectorAsList already defined!")
end
getByteVectorAsList = callJava("getByteVectorAsList")
println(eltype(getByteVectorAsList))
println(getByteVectorAsList)
if eltype(getByteVectorAsList) != Int8
	error("getByteVectorAsList not Int8!")
end
callJava("setByteVectorAsList",getByteVectorAsList)

println("getByteMatrix")
if isdefined(Main, :getByteMatrix) && !isnothing(getByteMatrix)
	error("getByteMatrix already defined!")
end
getByteMatrix = callJava("getByteMatrix")
println(eltype(getByteMatrix))
println(getByteMatrix)
if eltype(getByteMatrix) != Int8
	error("getByteMatrix not Int8!")
end
callJava("setByteMatrix",getByteMatrix)

println("getByteMatrixAsList")
if isdefined(Main, :getByteMatrixAsList) && !isnothing(getByteMatrixAsList)
	error("getByteMatrixAsList already defined!")
end
getByteMatrixAsList = callJava("getByteMatrixAsList")
println(eltype(getByteMatrixAsList))
println(getByteMatrixAsList)
if eltype(getByteMatrixAsList) != Int8
	error("getByteMatrixAsList not Int8!")
end
callJava("setByteMatrixAsList",getByteMatrixAsList)
