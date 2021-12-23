print("getInteger")
if isdefined(Main, :getInteger) && !isnothing(getInteger)
	error("getInteger already defined!")
end
getInteger = putInteger
print(typeof(getInteger))
print(getInteger)
if typeof(getInteger) != Int32
	error("getInteger not Int32!")
end

print("getIntegerVector")
if isdefined(Main, :getIntegerVector) && !isnothing(getIntegerVector)
	error("getIntegerVector already defined!")
end
getIntegerVector = putIntegerVector
print(eltype(getIntegerVector))
print(getIntegerVector)
if eltype(getIntegerVector) != Int32
	error("getIntegerVector not Int32!")
end

print("getIntegerVectorAsList")
if isdefined(Main, :getIntegerVectorAsList) && !isnothing(getIntegerVectorAsList)
	error("getIntegerVectorAsList already defined!")
end
getIntegerVectorAsList = putIntegerVectorAsList
print(eltype(getIntegerVectorAsList))
print(getIntegerVectorAsList)
if eltype(getIntegerVectorAsList) != Int32
	error("getIntegerVectorAsList not Int32!")
end

print("getIntegerMatrix")
if isdefined(Main, :getIntegerMatrix) && !isnothing(getIntegerMatrix)
	error("getIntegerMatrix already defined!")
end
getIntegerMatrix = putIntegerMatrix
print(eltype(getIntegerMatrix))
print(getIntegerMatrix)
if eltype(getIntegerMatrix) != Int32
	error("getIntegerMatrix not Int32!")
end

print("getIntegerMatrixAsList")
if isdefined(Main, :getIntegerMatrixAsList) && !isnothing(getIntegerMatrixAsList)
	error("getIntegerMatrixAsList already defined!")
end
getIntegerMatrixAsList = putIntegerMatrixAsList
print(eltype(getIntegerMatrixAsList))
print(getIntegerMatrixAsList)
if eltype(getIntegerMatrixAsList) != Int32
	error("getIntegerMatrixAsList not Int32!")
end
