print("getDecimal")
if(isdefined(Main, :getDecimal) && !isnothing(getDecimal)){
	error("getDecimal already defined!")
}
getDecimal = putDecimal
print(typeof(getDecimal))
print(getDecimal)
if(typeof(getDecimal) != Float64){
	error("getDecimal not Float64!")
}

print("getDecimalVector")
if(isdefined(Main, :getDecimalVector) && !isnothing(getDecimalVector)){
	error("getDecimalVector already defined!")
}
getDecimalVector = putDecimalVector
print(typeof(getDecimalVector))
print(getDecimalVector)
if(typeof(getDecimalVector) != Float64){
	error("getDecimalVector not Float64!")
}

print("getDecimalVectorAsList")
if(isdefined(Main, :getDecimalVectorAsList) && !isnothing(getDecimalVectorAsList)){
	error("getDecimalVectorAsList already defined!")
}
getDecimalVectorAsList = putDecimalVectorAsList
print(typeof(getDecimalVectorAsList))
print(getDecimalVectorAsList)
if(typeof(getDecimalVectorAsList) != Float64){
	error("getDecimalVectorAsList not Float64!")
}

print("getDecimalMatrix")
if(isdefined(Main, :getDecimalMatrix) && !isnothing(getDecimalMatrix)){
	error("getDecimalMatrix already defined!")
}
getDecimalMatrix = putDecimalMatrix
print(typeof(getDecimalMatrix))
print(getDecimalMatrix)
if(typeof(getDecimalMatrix) != Float64){
	error("getDecimalMatrix not Float64!")
}

print("getDecimalMatrixAsList")
if(isdefined(Main, :getDecimalMatrixAsList) && !isnothing(getDecimalMatrixAsList)){
	error("getDecimalMatrixAsList already defined!")
}
getDecimalMatrixAsList = putDecimalMatrixAsList
print(typeof(getDecimalMatrixAsList))
print(getDecimalMatrixAsList)
if(typeof(getDecimalMatrixAsList) != Float64){
	error("getDecimalMatrixAsList not Float64!")
}
