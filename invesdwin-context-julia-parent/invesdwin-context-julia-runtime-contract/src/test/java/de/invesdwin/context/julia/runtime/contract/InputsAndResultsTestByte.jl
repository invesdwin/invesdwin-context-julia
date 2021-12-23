print("getByte")
if(isdefined(Main, :getByte) && !isnothing(getByte)){
	error("getByte already defined!")
}
getByte = putByte
print(typeof(getByte))
print(getByte)
if(typeof(getByte) != Int8){
	error("getByte not Int8!")
}

print("getByteVector")
if(isdefined(Main, :getByteVector) && !isnothing(getByteVector)){
	error("getByteVector already defined!")
}
getByteVector = putByteVector
print(typeof(getByteVector))
print(getByteVector)
if(typeof(getByteVector) != Int8){
	error("getByteVector not Int8!")
}

print("getByteVectorAsList")
if(isdefined(Main, :getByteVectorAsList) && !isnothing(getByteVectorAsList)){
	error("getByteVectorAsList already defined!")
}
getByteVectorAsList = putByteVectorAsList
print(typeof(getByteVectorAsList))
print(getByteVectorAsList)
if(typeof(getByteVectorAsList) != Int8){
	error("getByteVectorAsList not Int8!")
}

print("getByteMatrix")
if(isdefined(Main, :getByteMatrix) && !isnothing(getByteMatrix)){
	error("getByteMatrix already defined!")
}
getByteMatrix = putByteMatrix
print(typeof(getByteMatrix))
print(getByteMatrix)
if(typeof(getByteMatrix) != Int8){
	error("getByteMatrix not Int8!")
}

print("getByteMatrixAsList")
if(isdefined(Main, :getByteMatrixAsList) && !isnothing(getByteMatrixAsList)){
	error("getByteMatrixAsList already defined!")
}
getByteMatrixAsList = putByteMatrixAsList
print(typeof(getByteMatrixAsList))
print(getByteMatrixAsList)
if(typeof(getByteMatrixAsList) != Int8){
	error("getByteMatrixAsList not Int8!")
}
