import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo


class InterfaceProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(ImplementInterface::class.qualifiedName!!)
        val unableToProcess = symbols.filterNot { it.validate() }
        symbols.filter { it is KSClassDeclaration && it.validate() }.forEach {
            it.accept(Visitor(), Unit)
        }
        return unableToProcess.toList()
    }

    private inner class Visitor : KSVisitorVoid() {
        @OptIn(KotlinPoetKspPreview::class)
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

//            logger.error("Class ${classDeclaration.modifiers} is annotated with @ImplementInterface")
            val typeClassName = classDeclaration.asType(emptyList())
            val packageName = classDeclaration.packageName.asString()
            classDeclaration.getAllProperties().forEach {
//                logger.error("Property ${it.simpleName} of type ${it.type.resolve().declaration.qualifiedName}")
                it.accept(this, Unit)
            }
            // obtener el nombre de la clase que fue anotada
            val classSimpleName = classDeclaration.simpleName.asString()


            // definir el nombre de la clase

            val fileSpec = FileSpec.builder(
                packageName = "mela.pela",
                fileName = "${classSimpleName}Impl"
            ).apply {
                // definir el nombre de la clase
                addType(
                    TypeSpec.classBuilder("${classSimpleName}Impl")
//                            TypeSpec.classBuilder("${classDeclaration::class.java.simpleName}Impl")
                        .addSuperinterface(
                            ClassName(classDeclaration.packageName.asString(), "$classDeclaration")
                        )
                        .addFunction(
                            FunSpec.builder("total")
//                                .addStatement("val venta = %T()", typeClassName::class.java)
                                .addStatement("this.vender()")
                                .build()
                        )
                        .addFunction(
                            FunSpec.builder("vender")
                                .addModifiers(KModifier.OVERRIDE)
                                .addStatement("TODO()")
                                .build()
                        )
                        .addFunction(
                            FunSpec.builder("toString")
                                .addModifiers(KModifier.OVERRIDE)
                                .addStatement("return StringBuilder().append(\"suck\").toString()")
                                .returns(String::class)
                                .build()
                        )
                        .build()
                )
            }.build()

            fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)

        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            logger.error("Property ${property.simpleName} of type ${property.type.resolve().declaration.qualifiedName}")


        }
    }

}

@AutoService(SymbolProcessorProvider::class)
class InterfaceProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return InterfaceProcessor(environment.options, environment.codeGenerator, environment.logger)
    }
}