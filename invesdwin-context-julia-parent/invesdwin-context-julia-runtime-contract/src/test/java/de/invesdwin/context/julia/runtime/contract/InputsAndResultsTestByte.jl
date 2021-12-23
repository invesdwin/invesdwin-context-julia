print("getByte")
if isdefined(Main, :getByte) && !isnothing(getByte)
	error("getByte already defined!")
end
getByte = putByte
print(typeof(getByte))
print(getByte)
if typeof(getByte) != Int8
	error("getByte not Int8!")
end

print("getByteVector")
if isdefined(Main, :getByteVector) && !isnothing(getByteVector)
	error("getByteVector already defined!")
end
getByteVector = putByteVector
print(eltype(getByteVector))
print(getByteVector)
if eltype(getByteVector) != Int8
	error("getByteVector not Int8!")
end

print("getByteVectorAsList")
if isdefined(Main, :getByteVectorAsList) && !isnothing(getByteVectorAsList)
	error("getByteVectorAsList already defined!")
end
getByteVectorAsList = putByteVectorAsList
print(eltype(getByteVectorAsList))
print(getByteVectorAsList)
if eltype(getByteVectorAsList) != Int8
	error("getByteVectorAsList not Int8!")
end

print("getByteMatrix")
if isdefined(Main, :getByteMatrix) && !isnothing(getByteMatrix)
	error("getByteMatrix already defined!")
end
getByteMatrix = putByteMatrix
print(eltype(getByteMatrix))
print(getByteMatrix)
if eltype(getByteMatrix) != Int8
	error("getByteMatrix not Int8!")
end

print("getByteMatrixAsList")
if isdefined(Main, :getByteMatrixAsList) && !isnothing(getByteMatrixAsList)
	error("getByteMatrixAsList already defined!")
end
getByteMatrixAsList = putByteMatrixAsList
print(eltype(getByteMatrixAsList))
print(getByteMatrixAsList)
if eltype(getByteMatrixAsList) != Int8
	error("getByteMatrixAsList not Int8!")
end
