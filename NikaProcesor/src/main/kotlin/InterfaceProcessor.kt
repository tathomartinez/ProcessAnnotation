import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import visitor.VisitorJsonScheme
import visitor.VisitorParams

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
        val symbolsFactura = resolver.getSymbolsWithAnnotation(FacturaColombianas::class.qualifiedName!!)
        symbolsFactura.filter { it is KSClassDeclaration && it.validate() }.forEach {
            it.accept(VisitorFactura(), Unit)
        }
        val symbolsParameter = resolver.getSymbolsWithAnnotation(ParamPax::class.qualifiedName!!)
        symbolsParameter.filter { it is KSClassDeclaration && it.validate() }.forEach {
            it.accept(VisitorParams(codeGenerator = codeGenerator), Unit)
        }

        val nikaJsonClass = resolver.getSymbolsWithAnnotation(NikaJsonClass::class.qualifiedName!!)
        nikaJsonClass.filter { it is KSClassDeclaration && it.validate() && it.modifiers.contains(Modifier.DATA) }
            .forEach {
                it.accept(VisitorJsonScheme(codeGenerator = codeGenerator), Unit)
            }
        return unableToProcess.toList()
    }


    private inner class VisitorFactura : KSVisitorVoid() {
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

//            val companion = TypeSpec.companionObjectBuilder()
//                .addProperty(
//                    PropertySpec.builder("buzz", String::class)
//                        .initializer("%S", "buzz")
//                        .build()
//                )
//                .addFunction(
//                    FunSpec.builder("beep")
//                        .addStatement("println(%S)", "Beep!")
//                        .build()
//                )
//                .build()
//            // definir el nombre de la clase
//            val helloWorld = TypeSpec.objectBuilder("HelloWorld")
//                .addProperty(
//                    PropertySpec.builder("buzz", String::class)
//                        .initializer("%S", "buzz")
//                        .build()
//                )
//                .addFunction(
//                    FunSpec.builder("beep")
//                        .addStatement("println(%S)", "Beep!")
//                        .build()
//                )
//                .build()
            val createInterfaz = TypeSpec.interfaceBuilder("I${classSimpleName}")
//                    .addSuperinterface(classDeclaration.javaClass).
                .addProperty(
                    PropertySpec.builder("uid", String::class)
                        .build()
                )
                .build()

//            override val uid: String,

            val android = PropertySpec.builder("uid", String::class)
//                ParameterSpec.builder("uid", String::class)
                .addModifiers(KModifier.OPEN)
                .addModifiers(KModifier.OVERRIDE)
                .initializer("%S", "123")
                .build()

            val parameterspec = ParameterSpec
                .builder("uid", String::class)
//                ParameterSpec.builder("uid", String::class)
//                .addModifiers(KModifier.OVERRIDE)
                .build()

            val flux = FunSpec.constructorBuilder()
                .addParameter(parameterspec)
//                .addStatement("this.%N = %N", "greeting", "greeting")
                .build()
            val fileSpec = FileSpec.builder(
                packageName = "mela.pela",
                fileName = "${classSimpleName}ColombianaImpl"
            ).apply {
                // definir el nombre de la clase
                addType(
                    TypeSpec.classBuilder("${classSimpleName}Impl")
                        .addModifiers(KModifier.OPEN)
//                        .addSuperinterface(
//                            ClassName( classDeclaration.packageName.asString(), "$classDeclaration")
//                        )
                        .addSuperinterface(
                            ClassName("mela.pela", "I${classSimpleName}")
                        )
//                        .primaryConstructor(
//                           flux
//                        )
//                        .primaryConstructor(
//                            FunSpec.constructorBuilder()
//                                .addParameter(parameterspec)
//                                .build()
//                        )
//                        .addSuperclassConstructorParameter("%S", "123123123asd")
//                        .addEnumConstant(
//                            "FACTURA",
//                            TypeSpec.anonymousClassBuilder()
//                                .addSuperclassConstructorParameter("%S", "123123123asd")
//                                .build())

                        .addProperty(
                            PropertySpec.builder("nit", String::class)
                                .initializer("\"123\"")
                                .build()
                        )
                        .addProperty(
                            android
                        )
//                        .addType(companion)
//                        .addType(helloWorld)
                        .build()
                )

                addType(createInterfaz)

//                addBodyComment("Generated by InterfaceProcessor")
//                addNamedCode(
//
//                    format = """
//                    |fun main() {
//                    |   var factura = ${classSimpleName}Impl()
//                    |   println(factura.nit)
//                    |}
//                """.trimMargin(),
//                    args = emptyMap<String,String>()
//                )
//                addAnnotation(ClassName("com.google.auto.service", "AutoService"))
//                addBodyComment("Generated by InterfaceProcessor")
//                addCode(
//                    """
//                    |fun main() {
//                    |   var factura = ${classSimpleName}Impl()
//                    |   println(factura.nit)
//                    |}
//                """.trimMargin()
//                )
//                addTypeAlias(
//                    TypeAliasSpec.builder("${classSimpleName}Colombiana", typeClassName::class.java)
//                        .addModifiers(KModifier.PUBLIC)
//                        .build()
//                )
//                addStatement("fun main() {")
                addImport(StringBuilder::class.java.packageName, StringBuilder::class.java.simpleName)
            }.build()

            fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)

        }
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
                        .addModifiers(KModifier.OPEN)
                        .addSuperinterface(
                            ClassName(classDeclaration.packageName.asString(), "$classDeclaration")
                        )
                        .addFunction(
                            FunSpec.builder("total")
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
                addImport(StringBuilder::class.java.packageName, StringBuilder::class.java.simpleName)
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