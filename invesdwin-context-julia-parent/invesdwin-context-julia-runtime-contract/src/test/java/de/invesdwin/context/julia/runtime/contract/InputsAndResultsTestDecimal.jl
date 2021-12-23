print("getDecimal")
if isdefined(Main, :getDecimal) && !isnothing(getDecimal)
	error("getDecimal already defined!")
end
getDecimal = putDecimal
print(typeof(getDecimal))
print(getDecimal)
if typeof(getDecimal) != Float64
	error("getDecimal not Float64!")
end

print("getDecimalVector")
if isdefined(Main, :getDecimalVector) && !isnothing(getDecimalVector)
	error("getDecimalVector already defined!")
end
getDecimalVector = putDecimalVector
print(eltype(getDecimalVector))
print(getDecimalVector)
if eltype(getDecimalVector) != Float64
	error("getDecimalVector not Float64!")
end

print("getDecimalVectorAsList")
if isdefined(Main, :getDecimalVectorAsList) && !isnothing(getDecimalVectorAsList)
	error("getDecimalVectorAsList already defined!")
end
getDecimalVectorAsList = putDecimalVectorAsList
print(eltype(getDecimalVectorAsList))
print(getDecimalVectorAsList)
if eltype(getDecimalVectorAsList) != Float64
	error("getDecimalVectorAsList not Float64!")
end

print("getDecimalMatrix")
if isdefined(Main, :getDecimalMatrix) && !isnothing(getDecimalMatrix)
	error("getDecimalMatrix already defined!")
end
getDecimalMatrix = putDecimalMatrix
print(eltype(getDecimalMatrix))
print(getDecimalMatrix)
if eltype(getDecimalMatrix) != Float64
	error("getDecimalMatrix not Float64!")
end

print("getDecimalMatrixAsList")
if isdefined(Main, :getDecimalMatrixAsList) && !isnothing(getDecimalMatrixAsList)
	error("getDecimalMatrixAsList already defined!")
end
getDecimalMatrixAsList = putDecimalMatrixAsList
print(eltype(getDecimalMatrixAsList))
print(getDecimalMatrixAsList)
if eltype(getDecimalMatrixAsList) != Float64
	error("getDecimalMatrixAsList not Float64!")
end
