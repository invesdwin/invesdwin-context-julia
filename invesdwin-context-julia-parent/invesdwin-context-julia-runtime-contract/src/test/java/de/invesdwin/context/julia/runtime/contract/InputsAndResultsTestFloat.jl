print("getFloat")
if(isdefined(Main, :getFloat) && !isnothing(getFloat)){
	error("getFloat already defined!")
}
getFloat = putFloat
print(typeof(getFloat))
print(getFloat)
if(typeof(getFloat) != Float32){
	error("getFloat not Float32!")
}

print("getFloatVector")
if(isdefined(Main, :getFloatVector) && !isnothing(getFloatVector)){
	error("getFloatVector already defined!")
}
getFloatVector = putFloatVector
print(typeof(getFloatVector))
print(getFloatVector)
if(typeof(getFloatVector) != Float32){
	error("getFloatVector not Float32!")
}

print("getFloatVectorAsList")
if(isdefined(Main, :getFloatVectorAsList) && !isnothing(getFloatVectorAsList)){
	error("getFloatVectorAsList already defined!")
}
getFloatVectorAsList = putFloatVectorAsList
print(typeof(getFloatVectorAsList))
print(getFloatVectorAsList)
if(typeof(getFloatVectorAsList) != Float32){
	error("getFloatVectorAsList not Float32!")
}

print("getFloatMatrix")
if(isdefined(Main, :getFloatMatrix) && !isnothing(getFloatMatrix)){
	error("getFloatMatrix already defined!")
}
getFloatMatrix = putFloatMatrix
print(typeof(getFloatMatrix))
print(getFloatMatrix)
if(typeof(getFloatMatrix) != Float32){
	error("getFloatMatrix not Float32!")
}

print("getFloatMatrixAsList")
if(isdefined(Main, :getFloatMatrixAsList) && !isnothing(getFloatMatrixAsList)){
	error("getFloatMatrixAsList already defined!")
}
getFloatMatrixAsList = putFloatMatrixAsList
print(typeof(getFloatMatrixAsList))
print(getFloatMatrixAsList)
if(typeof(getFloatMatrixAsList) != Float32){
	error("getFloatMatrixAsList not Float32!")
}
