println("getLong")
if isdefined(Main, :getLong) && !isnothing(getLong)
	error("getLong already defined!")
end
getLong = callJava("getLong")
println(typeof(getLong))
println(getLong)
if typeof(getLong) != Int64
	error("getLong not Int64!")
end
callJava("setLong",getLong)

println("getLongVector")
if isdefined(Main, :getLongVector) && !isnothing(getLongVector)
	error("getLongVector already defined!")
end
getLongVector = callJava("getLongVector")
println(eltype(getLongVector))
println(getLongVector)
if eltype(getLongVector) != Int64
	error("getLongVector not Int64!")
end
callJava("setLongVector",getLongVector)

println("getLongVectorAsList")
if isdefined(Main, :getLongVectorAsList) && !isnothing(getLongVectorAsList)
	error("getLongVectorAsList already defined!")
end
getLongVectorAsList = callJava("getLongVectorAsList")
println(eltype(getLongVectorAsList))
println(getLongVectorAsList)
if eltype(getLongVectorAsList) != Int64
	error("getLongVectorAsList not Int64!")
end
callJava("setLongVectorAsList",getLongVectorAsList)

println("getLongMatrix")
if isdefined(Main, :getLongMatrix) && !isnothing(getLongMatrix)
	error("getLongMatrix already defined!")
end
getLongMatrix = callJava("getLongMatrix")
println(eltype(getLongMatrix))
println(getLongMatrix)
if eltype(getLongMatrix) != Int64
	error("getLongMatrix not Int64!")
end
callJava("setLongMatrix",getLongMatrix)

println("getLongMatrixAsList")
if isdefined(Main, :getLongMatrixAsList) && !isnothing(getLongMatrixAsList)
	error("getLongMatrixAsList already defined!")
end
getLongMatrixAsList = callJava("getLongMatrixAsList")
println(eltype(getLongMatrixAsList))
println(getLongMatrixAsList)
if eltype(getLongMatrixAsList) != Int64
	error("getLongMatrixAsList not Int64!")
end
callJava("setLongMatrixAsList",getLongMatrixAsList)
