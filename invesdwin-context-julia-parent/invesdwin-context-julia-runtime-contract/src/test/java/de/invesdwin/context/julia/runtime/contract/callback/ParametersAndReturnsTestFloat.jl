println("getFloat")
if isdefined(Main, :getFloat) && !isnothing(getFloat)
	error("getFloat already defined!")
end
getFloat = callJava("getFloat")
println(typeof(getFloat))
println(getFloat)
if typeof(getFloat) != Float32
	error("getFloat not Float32!")
end
callJava("setFloat",getFloat)

println("getFloatVector")
if isdefined(Main, :getFloatVector) && !isnothing(getFloatVector)
	error("getFloatVector already defined!")
end
getFloatVector = callJava("getFloatVector")
println(eltype(getFloatVector))
println(getFloatVector)
if eltype(getFloatVector) != Float32
	error("getFloatVector not Float32!")
end
callJava("setFloatVector",getFloatVector)

println("getFloatVectorAsList")
if isdefined(Main, :getFloatVectorAsList) && !isnothing(getFloatVectorAsList)
	error("getFloatVectorAsList already defined!")
end
getFloatVectorAsList = callJava("getFloatVectorAsList")
println(eltype(getFloatVectorAsList))
println(getFloatVectorAsList)
if eltype(getFloatVectorAsList) != Float32
	error("getFloatVectorAsList not Float32!")
end
callJava("setFloatVectorAsList",getFloatVectorAsList)

println("getFloatMatrix")
if isdefined(Main, :getFloatMatrix) && !isnothing(getFloatMatrix)
	error("getFloatMatrix already defined!")
end
getFloatMatrix = callJava("getFloatMatrix")
println(eltype(getFloatMatrix))
println(getFloatMatrix)
if eltype(getFloatMatrix) != Float32
	error("getFloatMatrix not Float32!")
end
callJava("setFloatMatrix",getFloatMatrix)

println("getFloatMatrixAsList")
if isdefined(Main, :getFloatMatrixAsList) && !isnothing(getFloatMatrixAsList)
	error("getFloatMatrixAsList already defined!")
end
getFloatMatrixAsList = callJava("getFloatMatrixAsList")
println(eltype(getFloatMatrixAsList))
println(getFloatMatrixAsList)
if eltype(getFloatMatrixAsList) != Float32
	error("getFloatMatrixAsList not Float32!")
end
callJava("setFloatMatrixAsList",getFloatMatrixAsList)
