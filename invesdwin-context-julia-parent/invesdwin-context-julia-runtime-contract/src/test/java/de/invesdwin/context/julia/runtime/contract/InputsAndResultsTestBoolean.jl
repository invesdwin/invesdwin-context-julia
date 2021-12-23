print("getBoolean")
if(isdefined(Main, :getBoolean) && !isnothing(getBoolean)){
	error("getBoolean already defined!")
}
getBoolean = putBoolean
print(typeof(getBoolean))
print(getBoolean)
if(typeof(getBoolean) != Bool){
	error("getBoolean not Bool!")
}

print("getBooleanVector")
if(isdefined(Main, :getBooleanVector) && !isnothing(getBooleanVector(){
	error("getBooleanVector already defined!")
}
getBooleanVector = putBooleanVector
print(typeof(getBooleanVector))
print(getBooleanVector)
if(typeof(getBooleanVector) != Bool){
	error("getBooleanVector not Bool!")
}

print("getBooleanVectorAsList")
if(isdefined(Main, :getBooleanVectorAsList) && !isnothing(getBooleanVectorAsList)){
	error("getBooleanVectorAsList already defined!")
}
getBooleanVectorAsList = putBooleanVectorAsList
print(typeof(getBooleanVectorAsList))
print(getBooleanVectorAsList)
if(typeof(getBooleanVectorAsList) != Bool){
	error("getBooleanVectorAsList not Bool!")
}

print("getBooleanMatrix")
if(isdefined(Main, :getBooleanMatrix) && !isnothing(getBooleanMatrix)){
	error("getBooleanMatrix already defined!")
}
getBooleanMatrix = putBooleanMatrix
print(typeof(getBooleanMatrix))
print(getBooleanMatrix)
if(typeof(getBooleanMatrix) != Bool){
	error("getBooleanMatrix not Bool!")
}

print("getBooleanMatrixAsList")
if(isdefined(Main, :getBooleanMatrixAsList) && !isnothing(getBooleanMatrixAsList)){
	error("getBooleanMatrixAsList already defined!")
}
getBooleanMatrixAsList = putBooleanMatrixAsList
print(typeof(getBooleanMatrixAsList))
print(getBooleanMatrixAsList)
if(typeof(getBooleanMatrixAsList) != Bool){
	error("getBooleanMatrixAsList not Bool!")
}
