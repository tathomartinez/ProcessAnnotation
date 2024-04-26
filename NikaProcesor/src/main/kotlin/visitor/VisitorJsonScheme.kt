package visitor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.writeTo

class VisitorJsonScheme(val codeGenerator: CodeGenerator) : KSVisitorVoid() {
    @OptIn(KotlinPoetKspPreview::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val properties = classDeclaration.getAllProperties()
        val classSimpleName = classDeclaration.simpleName.asString()
        val requiredProperties = properties.filterNot { property -> property.type.resolve().isMarkedNullable }
            .joinToString(", ") { "\"${it.simpleName.asString()}\"" }

        val jsonSchema = StringBuilder().apply {
            append("{\n")
            append("\"type\": \"object\",\n")
            append("\"properties\": {\n")
            properties.forEach { property ->
                val propertyName = property.simpleName.asString()
                val propertyType = when (property.type.resolve().declaration.qualifiedName?.asString()) {
                    "kotlin.String" -> "\"string\""
                    "kotlin.Boolean" -> "\"boolean\""
                    "kotlin.Float", "kotlin.Double" -> "\"number\""
                    "kotlin.Int", "kotlin.Long", "kotlin.Short", "kotlin.Byte" -> "\"integer\""
                    else -> "\"${property.type}\""
                }
                property.type.resolve().isMarkedNullable
                append("\"$propertyName\": {\"type\": $propertyType},\n")
            }
            append("},\n")
            append("\"required\": [$requiredProperties]\n")
            append("}")
        }.toString()

        val fileSpec = FileSpec.builder(
            packageName = "cl.tbk.scheme.generator",
            fileName = "${classSimpleName}Extension"
        ).apply {
            addFunction(
                FunSpec.builder("getScheme")
                    .receiver(classDeclaration.toClassName())
                    .returns(String::class)
                    .addStatement("return \"\"\"$jsonSchema\"\"\"")
                    .build()
            )
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }
}
