println("getBoolean")
if isdefined(Main, :getBoolean) && !isnothing(getBoolean)
	error("getBoolean already defined!")
end
getBoolean = callJava("getBoolean")
println(typeof(getBoolean))
println(getBoolean)
if typeof(getBoolean) != Bool
	error("getBoolean not Bool!")
end
callJava("setBoolean",getBoolean)

println("getBooleanVector")
if isdefined(Main, :getBooleanVector) && !isnothing(getBooleanVector)
	error("getBooleanVector already defined!")
end
getBooleanVector = callJava("getBooleanVector")
println(eltype(getBooleanVector))
println(getBooleanVector)
if eltype(getBooleanVector) != Bool 
	error("getBooleanVector not Bool!")
end
callJava("setBooleanVector",getBooleanVector)

println("getBooleanVectorAsList")
if isdefined(Main, :getBooleanVectorAsList) && !isnothing(getBooleanVectorAsList)
	error("getBooleanVectorAsList already defined!")
end
getBooleanVectorAsList = callJava("getBooleanVectorAsList")
println(eltype(getBooleanVectorAsList))
println(getBooleanVectorAsList)
if eltype(getBooleanVectorAsList) != Bool
	error("getBooleanVectorAsList not Bool!")
end
callJava("setBooleanVectorAsList",getBooleanVectorAsList)

println("getBooleanMatrix")
if isdefined(Main, :getBooleanMatrix) && !isnothing(getBooleanMatrix)
	error("getBooleanMatrix already defined!")
end
getBooleanMatrix = callJava("getBooleanMatrix")
println(eltype(getBooleanMatrix))
println(getBooleanMatrix)
if eltype(getBooleanMatrix) != Bool
	error("getBooleanMatrix not Bool!")
end
callJava("setBooleanMatrix",getBooleanMatrix)

println("getBooleanMatrixAsList")
if isdefined(Main, :getBooleanMatrixAsList) && !isnothing(getBooleanMatrixAsList)
	error("getBooleanMatrixAsList already defined!")
end
getBooleanMatrixAsList = callJava("getBooleanMatrixAsList")
println(eltype(getBooleanMatrixAsList))
println(getBooleanMatrixAsList)
if eltype(getBooleanMatrixAsList) != Bool
	error("getBooleanMatrixAsList not Bool!")
end
callJava("setBooleanMatrixAsList",getBooleanMatrixAsList)
