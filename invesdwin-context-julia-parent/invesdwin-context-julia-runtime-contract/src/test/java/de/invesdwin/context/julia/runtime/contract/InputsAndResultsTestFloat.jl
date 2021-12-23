print("getFloat")
if isdefined(Main, :getFloat) && !isnothing(getFloat)
	error("getFloat already defined!")
end
getFloat = putFloat
print(typeof(getFloat))
print(getFloat)
if typeof(getFloat) != Float32
	error("getFloat not Float32!")
end

print("getFloatVector")
if isdefined(Main, :getFloatVector) && !isnothing(getFloatVector)
	error("getFloatVector already defined!")
end
getFloatVector = putFloatVector
print(eltype(getFloatVector))
print(getFloatVector)
if eltype(getFloatVector) != Float32
	error("getFloatVector not Float32!")
end

print("getFloatVectorAsList")
if isdefined(Main, :getFloatVectorAsList) && !isnothing(getFloatVectorAsList)
	error("getFloatVectorAsList already defined!")
end
getFloatVectorAsList = putFloatVectorAsList
print(eltype(getFloatVectorAsList))
print(getFloatVectorAsList)
if eltype(getFloatVectorAsList) != Float32
	error("getFloatVectorAsList not Float32!")
end

print("getFloatMatrix")
if isdefined(Main, :getFloatMatrix) && !isnothing(getFloatMatrix)
	error("getFloatMatrix already defined!")
end
getFloatMatrix = putFloatMatrix
print(eltype(getFloatMatrix))
print(getFloatMatrix)
if eltype(getFloatMatrix) != Float32
	error("getFloatMatrix not Float32!")
end

print("getFloatMatrixAsList")
if isdefined(Main, :getFloatMatrixAsList) && !isnothing(getFloatMatrixAsList)
	error("getFloatMatrixAsList already defined!")
end
getFloatMatrixAsList = putFloatMatrixAsList
print(eltype(getFloatMatrixAsList))
print(getFloatMatrixAsList)
if eltype(getFloatMatrixAsList) != Float32
	error("getFloatMatrixAsList not Float32!")
end
