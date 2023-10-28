println("getShort")
if isdefined(Main, :getShort) && !isnothing(getShort)
	error("getShort already defined!")
end
getShort = callJava("getShort")
println(typeof(getShort))
println(getShort)
if typeof(getShort) != Int16
	error("getShort not Int16!")
end
callJava("setShort",getShort)

println("getShortVector")
if isdefined(Main, :getShortVector) && !isnothing(getShortVector)
	error("getShortVector already defined!")
end
getShortVector = callJava("getShortVector")
println(eltype(getShortVector))
println(getShortVector)
if eltype(getShortVector) != Int16
	error("getShortVector not Int16!")
end
callJava("setShortVector",getShortVector)

println("getShortVectorAsList")
if isdefined(Main, :getShortVectorAsList) && !isnothing(getShortVectorAsList)
	error("getShortVectorAsList already defined!")
end
getShortVectorAsList = callJava("getShortVectorAsList")
println(eltype(getShortVectorAsList))
println(getShortVectorAsList)
if eltype(getShortVectorAsList) != Int16
	error("getShortVectorAsList not Int16!")
end
callJava("setShortVectorAsList",getShortVectorAsList)

println("getShortMatrix")
if isdefined(Main, :getShortMatrix) && !isnothing(getShortMatrix)
	error("getShortMatrix already defined!")
end
getShortMatrix = callJava("getShortMatrix")
println(eltype(getShortMatrix))
println(getShortMatrix)
if eltype(getShortMatrix) != Int16
	error("getShortMatrix not Int16!")
end
callJava("setShortMatrix",getShortMatrix)

println("getShortMatrixAsList")
if isdefined(Main, :getShortMatrixAsList) && !isnothing(getShortMatrixAsList)
	error("getShortMatrixAsList already defined!")
end
getShortMatrixAsList = callJava("getShortMatrixAsList")
println(eltype(getShortMatrixAsList))
println(getShortMatrixAsList)
if eltype(getShortMatrixAsList) != Int16
	error("getShortMatrixAsList not Int16!")
end
callJava("setShortMatrixAsList",getShortMatrixAsList)
