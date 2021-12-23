print("getShort")
if isdefined(Main, :getShort) && !isnothing(getShort)
	error("getShort already defined!")
end
getShort = putShort
print(typeof(getShort))
print(getShort)
if typeof(getShort) != Int16
	error("getShort not Int16!")
end

print("getShortVector")
if isdefined(Main, :getShortVector) && !isnothing(getShortVector)
	error("getShortVector already defined!")
end
getShortVector = putShortVector
print(eltype(getShortVector))
print(getShortVector)
if eltype(getShortVector) != Int16
	error("getShortVector not Int16!")
end

print("getShortVectorAsList")
if isdefined(Main, :getShortVectorAsList) && !isnothing(getShortVectorAsList)
	error("getShortVectorAsList already defined!")
end
getShortVectorAsList = putShortVectorAsList
print(eltype(getShortVectorAsList))
print(getShortVectorAsList)
if eltype(getShortVectorAsList) != Int16
	error("getShortVectorAsList not Int16!")
end

print("getShortMatrix")
if isdefined(Main, :getShortMatrix) && !isnothing(getShortMatrix)
	error("getShortMatrix already defined!")
end
getShortMatrix = putShortMatrix
print(eltype(getShortMatrix))
print(getShortMatrix)
if eltype(getShortMatrix) != Int16
	error("getShortMatrix not Int16!")
end

print("getShortMatrixAsList")
if isdefined(Main, :getShortMatrixAsList) && !isnothing(getShortMatrixAsList)
	error("getShortMatrixAsList already defined!")
end
getShortMatrixAsList = putShortMatrixAsList
print(eltype(getShortMatrixAsList))
print(getShortMatrixAsList)
if eltype(getShortMatrixAsList) != Int16
	error("getShortMatrixAsList not Int16!")
end
