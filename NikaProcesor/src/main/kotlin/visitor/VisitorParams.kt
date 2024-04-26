package visitor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo

class VisitorParams(val codeGenerator: CodeGenerator) : KSVisitorVoid() {
    @OptIn(KotlinPoetKspPreview::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        classDeclaration.getAllProperties().forEach {
            it.accept(this, Unit)
        }
        val classSimpleName = classDeclaration.simpleName.asString()
        val fileSpec = FileSpec.builder(
            packageName = "cl.tbk.paxparam.generator",
            fileName = "${classSimpleName}Param"
        ).apply {
            addType(
                TypeSpec.classBuilder("${classSimpleName}Param")
                    .addModifiers(KModifier.OPEN)
                    .build()
            )
            addImport(StringBuilder::class.java.packageName, StringBuilder::class.java.simpleName)
        }.build()

        fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
    }

}
