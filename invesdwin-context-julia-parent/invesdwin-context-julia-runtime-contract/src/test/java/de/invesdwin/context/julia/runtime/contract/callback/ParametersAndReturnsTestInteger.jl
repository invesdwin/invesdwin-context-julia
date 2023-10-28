println("getInteger")
if isdefined(Main, :getInteger) && !isnothing(getInteger)
	error("getInteger already defined!")
end
getInteger = callJava("getInteger")
println(typeof(getInteger))
println(getInteger)
if typeof(getInteger) != Int32
	error("getInteger not Int32!")
end
callJava("setInteger",getInteger)

println("getIntegerVector")
if isdefined(Main, :getIntegerVector) && !isnothing(getIntegerVector)
	error("getIntegerVector already defined!")
end
getIntegerVector = callJava("getIntegerVector")
println(eltype(getIntegerVector))
println(getIntegerVector)
if eltype(getIntegerVector) != Int32
	error("getIntegerVector not Int32!")
end
callJava("setIntegerVector",getIntegerVector)

println("getIntegerVectorAsList")
if isdefined(Main, :getIntegerVectorAsList) && !isnothing(getIntegerVectorAsList)
	error("getIntegerVectorAsList already defined!")
end
getIntegerVectorAsList = callJava("getIntegerVectorAsList")
println(eltype(getIntegerVectorAsList))
println(getIntegerVectorAsList)
if eltype(getIntegerVectorAsList) != Int32
	error("getIntegerVectorAsList not Int32!")
end
callJava("setIntegerVectorAsList",getIntegerVectorAsList)

println("getIntegerMatrix")
if isdefined(Main, :getIntegerMatrix) && !isnothing(getIntegerMatrix)
	error("getIntegerMatrix already defined!")
end
getIntegerMatrix = callJava("getIntegerMatrix")
println(eltype(getIntegerMatrix))
println(getIntegerMatrix)
if eltype(getIntegerMatrix) != Int32
	error("getIntegerMatrix not Int32!")
end
callJava("setIntegerMatrix",getIntegerMatrix)

println("getIntegerMatrixAsList")
if isdefined(Main, :getIntegerMatrixAsList) && !isnothing(getIntegerMatrixAsList)
	error("getIntegerMatrixAsList already defined!")
end
getIntegerMatrixAsList = callJava("getIntegerMatrixAsList")
println(eltype(getIntegerMatrixAsList))
println(getIntegerMatrixAsList)
if eltype(getIntegerMatrixAsList) != Int32
	error("getIntegerMatrixAsList not Int32!")
end
callJava("setIntegerMatrixAsList",getIntegerMatrixAsList)
