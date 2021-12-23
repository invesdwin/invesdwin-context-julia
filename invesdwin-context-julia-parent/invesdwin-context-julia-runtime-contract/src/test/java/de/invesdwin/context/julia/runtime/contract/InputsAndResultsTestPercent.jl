print("getPercent")
if(isdefined(Main, :getPercent) && !isnothing(getPercent)){
	error("getPercent already defined!")
}
getPercent = putPercent
print(typeof(getPercent))
print(getPercent)
if(typeof(getPercent) != Float64){
	error("getPercent not Float64!")
}

print("getPercentVector")
if(isdefined(Main, :getPercentVector) && !isnothing(getPercentVector)){
	error("getPercentVector already defined!")
}
getPercentVector = putPercentVector
print(typeof(getPercentVector))
print(getPercentVector)
if(typeof(getPercentVector) != Float64){
	error("getPercentVector not Float64!")
}

print("getPercentVectorAsList")
if(isdefined(Main, :getPercentVectorAsList) && !isnothing(getPercentVectorAsList)){
	error("getPercentVectorAsList already defined!")
}
getPercentVectorAsList = putPercentVectorAsList
print(typeof(getPercentVectorAsList))
print(getPercentVectorAsList)
if(typeof(getPercentVectorAsList) != Float64){
	error("getPercentVectorAsList not Float64!")
}

print("getPercentMatrix")
if(isdefined(Main, :getPercentMatrix) && !isnothing(getPercentMatrix)){
	error("getPercentMatrix already defined!")
}
getPercentMatrix = putPercentMatrix
print(typeof(getPercentMatrix))
print(getPercentMatrix)
if(typeof(getPercentMatrix) != Float64){
	error("getPercentMatrix not Float64!")
}

print("getPercentMatrixAsList")
if(isdefined(Main, :getPercentMatrixAsList) && !isnothing(getPercentMatrixAsList)){
	error("getPercentMatrixAsList already defined!")
}
getPercentMatrixAsList = putPercentMatrixAsList
print(typeof(getPercentMatrixAsList))
print(getPercentMatrixAsList)
if(typeof(getPercentMatrixAsList) != Float64){
	error("getPercentMatrixAsList not Float64!")
}
