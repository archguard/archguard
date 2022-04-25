package org.archguard.doc.generator.compiler

import com.intellij.core.CoreApplicationEnvironment
import com.intellij.mock.MockComponentManager
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.psi.PsiNameHelper
import com.intellij.psi.impl.PsiNameHelperImpl
import com.intellij.psi.impl.source.javadoc.JavadocManagerImpl
import com.intellij.psi.javadoc.CustomJavadocTagProvider
import com.intellij.psi.javadoc.JavadocManager
import com.intellij.psi.javadoc.JavadocTagInfo
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.analyzer.AbstractResolverForProject
import org.jetbrains.kotlin.analyzer.LibraryModuleInfo
import org.jetbrains.kotlin.analyzer.ModuleContent
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.analyzer.ResolverForModule
import org.jetbrains.kotlin.analyzer.ResolverForProject
import org.jetbrains.kotlin.analyzer.common.CommonPlatformAnalyzerServices
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.builtins.jvm.JvmBuiltIns
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoot
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.JvmPackagePartProvider
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.cli.jvm.config.addJavaSourceRoot
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.config.jvmClasspathRoots
import org.jetbrains.kotlin.cli.jvm.index.JavaRoot
import org.jetbrains.kotlin.config.ApiVersion
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.LanguageVersion
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.container.tryGetService
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.context.withModule
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.library.KotlinLibrary
import org.jetbrains.kotlin.load.java.structure.impl.JavaClassImpl
import org.jetbrains.kotlin.load.java.structure.impl.classFiles.BinaryJavaClass
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.resolve.CliSealedClassInheritorsProvider
import org.jetbrains.kotlin.resolve.CompilerEnvironment
import org.jetbrains.kotlin.resolve.LazyTopDownAnalyzer
import org.jetbrains.kotlin.resolve.PlatformDependentAnalyzerServices
import org.jetbrains.kotlin.resolve.TopDownAnalysisContext
import org.jetbrains.kotlin.resolve.TopDownAnalysisMode
import org.jetbrains.kotlin.resolve.jvm.JvmPlatformParameters
import org.jetbrains.kotlin.resolve.jvm.JvmResolverForModuleFactory
import org.jetbrains.kotlin.utils.PathUtil
import org.slf4j.LoggerFactory
import java.io.File

class KotlinAnalysis {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var messageCollector: DocMessageCollector = DocMessageCollector(logger)
    private val classPath: List<File> get() = configuration.jvmClasspathRoots
    private val configuration = CompilerConfiguration()

    init {
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
    }

    fun parse(vararg files: File): TopDownAnalysisContext {
        val environment = createEnvironmentAndFacade(files)

        val container = createSingleModuleResolver(environment).componentProvider

        val lazyTopDownAnalyzer = container.tryGetService(LazyTopDownAnalyzer::class.java) as LazyTopDownAnalyzer
        val analysisContext = lazyTopDownAnalyzer.analyzeDeclarations(TopDownAnalysisMode.TopLevelDeclarations,
            environment.getSourceFiles())
        return analysisContext
    }

    private fun createEnvironmentAndFacade(files: Array<out File>): KotlinCoreEnvironment {
        configuration.addJvmClasspathRoots(PathUtil.getJdkClassesRootsFromCurrentJre())

        // todo: add source sets

        // add source sets
        files.forEach {
            configuration.addKotlinSourceRoot(it.path)
            if (it.extension == ".java") {
                configuration.addJavaSourceRoot(it)
            }
        }

        // Configuring Kotlin class path
        configuration.addJvmClasspathRoots(classPath)

        // todo: loadLanguageVersionSettings
        loadLanguageVersionSettings()

        return createCoreEnvironment()
    }

    private fun loadLanguageVersionSettings() {
        val languageVersion = LanguageVersion.LATEST_STABLE
        val apiVersion = ApiVersion.createByLanguageVersion(languageVersion)
        configuration.languageVersionSettings = LanguageVersionSettingsImpl(
            languageVersion = languageVersion,
            apiVersion = apiVersion
        )
    }

    private fun createCoreEnvironment(): KotlinCoreEnvironment {
        val configFiles = EnvironmentConfigFiles.JVM_CONFIG_FILES
        val rootDisposable = Disposer.newDisposable()

        val environment = KotlinCoreEnvironment.createForProduction(rootDisposable, configuration, configFiles)
        val projectComponentManager = environment.project as MockComponentManager

        CoreApplicationEnvironment.registerExtensionPoint(
            environment.project.extensionArea,
            JavadocTagInfo.EP_NAME, JavadocTagInfo::class.java
        )

        CoreApplicationEnvironment.registerExtensionPoint(
            Extensions.getRootArea(),
            CustomJavadocTagProvider.EP_NAME, CustomJavadocTagProvider::class.java
        )

        projectComponentManager.registerService(
            JavadocManager::class.java,
            JavadocManagerImpl(environment.project)
        )

        projectComponentManager.registerService(
            PsiNameHelper::class.java,
            PsiNameHelperImpl(environment.project)
        )

        projectComponentManager.registerService(
            CustomJavadocTagProvider::class.java,
            CustomJavadocTagProvider { emptyList() }
        )

        return environment
    }


    private fun createSingleModuleResolver(environment: KotlinCoreEnvironment): ResolverForModule {
        val projectContext = ProjectContext(environment.project, "Doc")
        val sourceFiles = environment.getSourceFiles()

        val targetPlatform = JvmPlatforms.defaultJvmPlatform

        val kotlinLibraries: Map<String, KotlinLibrary> = emptyMap()

        val library = object : LibraryModuleInfo {
            override val analyzerServices: PlatformDependentAnalyzerServices = CommonPlatformAnalyzerServices
            override val name: Name = Name.special("<library>")
            override val platform: TargetPlatform = targetPlatform
            override fun dependencies(): List<ModuleInfo> = listOf(this)
            override fun getLibraryRoots(): Collection<String> = classPath
                .map { libraryFile -> libraryFile.absolutePath }
                .filter { path -> path !in kotlinLibraries }
        }

        val moduleInfo = object : ModuleInfo {
            override val analyzerServices: PlatformDependentAnalyzerServices = CommonPlatformAnalyzerServices
            override val name: Name = Name.special("<module>")
            override val platform: TargetPlatform = targetPlatform
            override fun dependencies(): List<ModuleInfo> = listOf(this, library)
        }

        val sourcesScope = TopDownAnalyzerFacadeForJVM.newModuleSearchScope(environment.project, sourceFiles)

        val modulesContent: (ModuleInfo) -> ModuleContent<ModuleInfo> = {
            when (it) {
                library -> ModuleContent(it, emptyList(), GlobalSearchScope.notScope(sourcesScope))
                moduleInfo -> ModuleContent(it, emptyList(), GlobalSearchScope.allScope(environment.project))
                else -> null
            } ?: throw IllegalArgumentException("Unexpected module info")
        }

        val builtIns = JvmBuiltIns(projectContext.storageManager, JvmBuiltIns.Kind.FROM_CLASS_LOADER)

        val resolverForProject = createJvmResolverForProject(
            projectContext,
            moduleInfo,
            library,
            modulesContent,
            sourcesScope,
            builtIns
        )

        val moduleDescriptor = resolverForProject.descriptorForModule(moduleInfo)
        builtIns.initialize(moduleDescriptor, true)

        val resolverForModule = resolverForProject.resolverForModule(moduleInfo)
        return resolverForModule
    }

    private fun createJvmResolverForProject(
        projectContext: ProjectContext,
        module: ModuleInfo,
        library: LibraryModuleInfo,
        modulesContent: (ModuleInfo) -> ModuleContent<ModuleInfo>,
        sourcesScope: GlobalSearchScope,
        builtIns: KotlinBuiltIns,
    ): ResolverForProject<ModuleInfo> {
        val javaRoots = classPath
            .mapNotNull { file ->
                val rootFile = when (file.extension) {
                    "jar" -> StandardFileSystems.jar().findFileByPath("${file.absolutePath}$JAR_SEPARATOR")
                    else -> StandardFileSystems.local().findFileByPath(file.absolutePath)
                }

                rootFile?.let { JavaRoot(it, JavaRoot.RootType.BINARY) }
            }

        class ModuleInfoAbstractResolverForProject :
            AbstractResolverForProject<ModuleInfo>("Doc", projectContext, modules = listOf(module, library)) {
            override fun modulesContent(module: ModuleInfo): ModuleContent<ModuleInfo> =
                when (module) {
                    library -> ModuleContent(module, emptyList(), GlobalSearchScope.notScope(sourcesScope))
                    module -> ModuleContent(module, emptyList(), sourcesScope)
                    else -> throw IllegalArgumentException("Unexpected module info")
                }

            override fun builtInsForModule(module: ModuleInfo): KotlinBuiltIns = builtIns

            override fun createResolverForModule(
                descriptor: ModuleDescriptor,
                moduleInfo: ModuleInfo,
            ): ResolverForModule {
                val platformParameters = JvmPlatformParameters(
                    packagePartProviderFactory = { content ->
                        JvmPackagePartProvider(configuration.languageVersionSettings, content.moduleContentScope)
                            .apply { addRoots(javaRoots, messageCollector) }
                    },
                    moduleByJavaClass = {
                        val file = (it as? BinaryJavaClass)?.virtualFile
                            ?: (it as JavaClassImpl).psi.containingFile.virtualFile
                        if (file in sourcesScope) module else library
                    },
                    resolverForReferencedModule = null,
                    useBuiltinsProviderForModule = { false }
                )

                val jvmResolverForModuleFactory = JvmResolverForModuleFactory(
                    platformParameters,
                    CompilerEnvironment,
                    JvmPlatforms.unspecifiedJvmPlatform
                )

                return jvmResolverForModuleFactory.createResolverForModule(
                    descriptor as ModuleDescriptorImpl,
                    projectContext.withModule(descriptor),
                    modulesContent(moduleInfo),
                    this,
                    configuration.languageVersionSettings,
                    CliSealedClassInheritorsProvider,
                )
            }

            override fun sdkDependency(module: ModuleInfo): ModuleInfo? = null
        }

        return ModuleInfoAbstractResolverForProject()
    }

    companion object {
        const val JAR_SEPARATOR = "!/"
    }
}