print("getDouble")
if(isdefined(Main, :getDouble) && !isnothing(getDouble)){
	error("getDouble already defined!")
}
getDouble = putDouble
print(typeof(getDouble))
print(getDouble)
if(typeof(getDouble) != Float64){
	error("getDouble not Float64!")
}

print("getDoubleVector")
if(isdefined(Main, :getDoubleVector) && !isnothing(getDoubleVector)){
	error("getDoubleVector already defined!")
}
getDoubleVector = putDoubleVector
print(typeof(getDoubleVector))
print(getDoubleVector)
if(typeof(getDoubleVector) != Float64){
	error("getDoubleVector not Float64!")
}

print("getDoubleVectorAsList")
if(isdefined(Main, :getDoubleVectorAsList) && !isnothing(getDoubleVectorAsList)){
	error("getDoubleVectorAsList already defined!")
}
getDoubleVectorAsList = putDoubleVectorAsList
print(typeof(getDoubleVectorAsList))
print(getDoubleVectorAsList)
if(typeof(getDoubleVectorAsList) != Float64){
	error("getDoubleVectorAsList not Float64!")
}

print("getDoubleMatrix")
if(isdefined(Main, :getDoubleMatrix) && !isnothing(getDoubleMatrix)){
	error("getDoubleMatrix already defined!")
}
getDoubleMatrix = putDoubleMatrix
print(typeof(getDoubleMatrix))
print(getDoubleMatrix)
if(typeof(getDoubleMatrix) != Float64){
	error("getDoubleMatrix not Float64!")
}

print("getDoubleMatrixAsList")
if(isdefined(Main, :getDoubleMatrixAsList) && !isnothing(getDoubleMatrixAsList)){
	error("getDoubleMatrixAsList already defined!")
}
getDoubleMatrixAsList = putDoubleMatrixAsList
print(typeof(getDoubleMatrixAsList))
print(getDoubleMatrixAsList)
if(typeof(getDoubleMatrixAsList) != Float64){
	error("getDoubleMatrixAsList not Float64!")
}
