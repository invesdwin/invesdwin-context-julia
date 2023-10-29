println("putUuid")
println(putUuid)

getSecretStaticCallJava = callJava("getSecretStatic", putUuid)
println("getSecretStaticCallJava")
println(getSecretStaticCallJava)

getSecretCallJava = callJava("getSecret", putUuid)
println("getSecretCallJava")
println(getSecretCallJava)

getSecretExpressionCallJava = callJava("getSecretExpression", putUuid)
println("getSecretExpressionCallJava")
println(getSecretExpressionCallJava)

callJava("voidMethod")