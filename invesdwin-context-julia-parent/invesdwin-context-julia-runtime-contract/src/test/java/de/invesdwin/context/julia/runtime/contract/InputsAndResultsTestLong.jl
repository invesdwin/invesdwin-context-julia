print("getLong")
if(isdefined(Main, :getLong) && !isnothing(getLong)){
	error("getLong already defined!")
}
getLong = putLong
print(typeof(getLong))
print(getLong)
if(typeof(getLong) != Float64){
	error("getLong not Float64!")
}

print("getLongVector")
if(isdefined(Main, :getLongVector) && !isnothing(getLongVector)){
	error("getLongVector already defined!")
}
getLongVector = putLongVector
print(typeof(getLongVector))
print(getLongVector)
if(typeof(getLongVector) != Float64){
	error("getLongVector not Float64!")
}

print("getLongVectorAsList")
if(isdefined(Main, :getLongVectorAsList) && !isnothing(getLongVectorAsList)){
	error("getLongVectorAsList already defined!")
}
getLongVectorAsList = putLongVectorAsList
print(typeof(getLongVectorAsList))
print(getLongVectorAsList)
if(typeof(getLongVectorAsList) != Float64){
	error("getLongVectorAsList not Float64!")
}

print("getLongMatrix")
if(isdefined(Main, :getLongMatrix) && !isnothing(getLongMatrix)){
	error("getLongMatrix already defined!")
}
getLongMatrix = putLongMatrix
print(typeof(getLongMatrix))
print(getLongMatrix)
if(typeof(getLongMatrix) != Float64){
	error("getLongMatrix not Float64!")
}

print("getLongMatrixAsList")
if(isdefined(Main, :getLongMatrixAsList) && !isnothing(getLongMatrixAsList)){
	error("getLongMatrixAsList already defined!")
}
getLongMatrixAsList = putLongMatrixAsList
print(typeof(getLongMatrixAsList))
print(getLongMatrixAsList)
if(typeof(getLongMatrixAsList) != Float64){
	error("getLongMatrixAsList not Float64!")
}
