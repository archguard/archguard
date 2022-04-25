package org.archguard.doc.generator.compiler

import com.intellij.core.CoreApplicationEnvironment
import com.intellij.mock.MockComponentManager
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiNameHelper
import com.intellij.psi.impl.PsiNameHelperImpl
import com.intellij.psi.impl.source.javadoc.JavadocManagerImpl
import com.intellij.psi.javadoc.CustomJavadocTagProvider
import com.intellij.psi.javadoc.JavadocManager
import com.intellij.psi.javadoc.JavadocTagInfo
import com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.analyzer.ResolverForSingleModuleProject
import org.jetbrains.kotlin.analyzer.common.CommonAnalysisParameters
import org.jetbrains.kotlin.analyzer.common.CommonResolverForModuleFactory
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoot
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJavaSourceRoot
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.container.ComponentProvider
import org.jetbrains.kotlin.container.tryGetService
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.platform.CommonPlatforms
import org.jetbrains.kotlin.resolve.CompilerEnvironment
import org.jetbrains.kotlin.resolve.LazyTopDownAnalyzer
import org.jetbrains.kotlin.resolve.TopDownAnalysisContext
import org.jetbrains.kotlin.resolve.TopDownAnalysisMode
import org.jetbrains.kotlin.utils.PathUtil
import org.slf4j.LoggerFactory
import java.io.File

class KotlinAnalysis {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val classPath: ArrayList<File> = run {
        val classpath = arrayListOf<File>()
        PathUtil.getResourcePathForClass(AnnotationTarget.CLASS.javaClass)
        classpath
    }

    private val configuration = CompilerConfiguration()

    init {
        val messageCollector = DocMessageCollector(logger)
        configuration.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
    }

    fun parse(vararg files: File): TopDownAnalysisContext {
        return createEnvironmentAndFacade(files)
    }

    private fun createEnvironmentAndFacade(files: Array<out File>): TopDownAnalysisContext {
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


        val environment = createCoreEnvironment()
        val container = createSingleModuleResolver(environment)

        val lazyTopDownAnalyzer = container.tryGetService(LazyTopDownAnalyzer::class.java) as LazyTopDownAnalyzer
        val analysisContext = lazyTopDownAnalyzer.analyzeDeclarations(TopDownAnalysisMode.TopLevelDeclarations,
            environment.getSourceFiles())
        return analysisContext
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


    private fun createSingleModuleResolver(environment: KotlinCoreEnvironment): ComponentProvider {
//        val projectContext = ProjectContext(environment.project, "Doc")
        val sourceFiles = environment.getSourceFiles()

        val moduleInfo = SourceModuleInfo(Name.special("<main"), mapOf(), false)
        val project = sourceFiles.firstOrNull()?.project ?: throw AssertionError("No files to analyze")

        val multiplatformLanguageSettings = object : LanguageVersionSettings by configuration.languageVersionSettings {
            override fun getFeatureSupport(feature: LanguageFeature): LanguageFeature.State =
                if (feature == LanguageFeature.MultiPlatformProjects) LanguageFeature.State.ENABLED
                else configuration.languageVersionSettings.getFeatureSupport(feature)
        }

        val resolverForModuleFactory = CommonResolverForModuleFactory(
            CommonAnalysisParameters { content ->
                environment.createPackagePartProvider(content.moduleContentScope)
            },
            CompilerEnvironment,
            CommonPlatforms.defaultCommonPlatform,
            shouldCheckExpectActual = false
        )

        val resolver = ResolverForSingleModuleProject(
            "sources for metadata serializer",
            ProjectContext(project, "metadata serializer"),
            moduleInfo,
            resolverForModuleFactory,
            GlobalSearchScope.allScope(project),
            languageVersionSettings = multiplatformLanguageSettings,
            syntheticFiles = sourceFiles
        )

        val container = resolver.resolverForModule(moduleInfo).componentProvider

        return container
    }
}