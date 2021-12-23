print("getInteger")
if(isdefined(Main, :getInteger) && !isnothing(getInteger)){
	error("getInteger already defined!")
}
getInteger = putInteger
print(typeof(getInteger))
print(getInteger)
if(typeof(getInteger) != Int32){
	error("getInteger not Int32!")
}

print("getIntegerVector")
if(isdefined(Main, :getIntegerVector) && !isnothing(getIntegerVector)){
	error("getIntegerVector already defined!")
}
getIntegerVector = putIntegerVector
print(typeof(getIntegerVector))
print(getIntegerVector)
if(typeof(getIntegerVector) != Int32){
	error("getIntegerVector not Int32!")
}

print("getIntegerVectorAsList")
if(isdefined(Main, :getIntegerVectorAsList) && !isnothing(getIntegerVectorAsList)){
	error("getIntegerVectorAsList already defined!")
}
getIntegerVectorAsList = putIntegerVectorAsList
print(typeof(getIntegerVectorAsList))
print(getIntegerVectorAsList)
if(typeof(getIntegerVectorAsList) != Int32){
	error("getIntegerVectorAsList not Int32!")
}

print("getIntegerMatrix")
if(isdefined(Main, :getIntegerMatrix) && !isnothing(getIntegerMatrix)){
	error("getIntegerMatrix already defined!")
}
getIntegerMatrix = putIntegerMatrix
print(typeof(getIntegerMatrix))
print(getIntegerMatrix)
if(typeof(getIntegerMatrix) != Int32){
	error("getIntegerMatrix not Int32!")
}

print("getIntegerMatrixAsList")
if(isdefined(Main, :getIntegerMatrixAsList) && !isnothing(getIntegerMatrixAsList)){
	error("getIntegerMatrixAsList already defined!")
}
getIntegerMatrixAsList = putIntegerMatrixAsList
print(typeof(getIntegerMatrixAsList))
print(getIntegerMatrixAsList)
if(typeof(getIntegerMatrixAsList) != Int32){
	error("getIntegerMatrixAsList not Int32!")
}
