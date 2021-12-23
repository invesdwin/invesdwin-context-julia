print("getPercent")
if isdefined(Main, :getPercent) && !isnothing(getPercent)
	error("getPercent already defined!")
end
getPercent = putPercent
print(typeof(getPercent))
print(getPercent)
if typeof(getPercent) != Float64
	error("getPercent not Float64!")
end

print("getPercentVector")
if isdefined(Main, :getPercentVector) && !isnothing(getPercentVector)
	error("getPercentVector already defined!")
end
getPercentVector = putPercentVector
print(eltype(getPercentVector))
print(getPercentVector)
if eltype(getPercentVector) != Float64
	error("getPercentVector not Float64!")
end

print("getPercentVectorAsList")
if isdefined(Main, :getPercentVectorAsList) && !isnothing(getPercentVectorAsList)
	error("getPercentVectorAsList already defined!")
end
getPercentVectorAsList = putPercentVectorAsList
print(eltype(getPercentVectorAsList))
print(getPercentVectorAsList)
if eltype(getPercentVectorAsList) != Float64
	error("getPercentVectorAsList not Float64!")
end

print("getPercentMatrix")
if isdefined(Main, :getPercentMatrix) && !isnothing(getPercentMatrix)
	error("getPercentMatrix already defined!")
end
getPercentMatrix = putPercentMatrix
print(eltype(getPercentMatrix))
print(getPercentMatrix)
if eltype(getPercentMatrix) != Float64
	error("getPercentMatrix not Float64!")
end

print("getPercentMatrixAsList")
if isdefined(Main, :getPercentMatrixAsList) && !isnothing(getPercentMatrixAsList)
	error("getPercentMatrixAsList already defined!")
end
getPercentMatrixAsList = putPercentMatrixAsList
print(eltype(getPercentMatrixAsList))
print(getPercentMatrixAsList)
if eltype(getPercentMatrixAsList) != Float64
	error("getPercentMatrixAsList not Float64!")
end
