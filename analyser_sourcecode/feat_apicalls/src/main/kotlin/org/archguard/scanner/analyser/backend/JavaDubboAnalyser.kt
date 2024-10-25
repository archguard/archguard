package org.archguard.scanner.analyser.backend

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.core.sourcecode.ContainerDemand
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply
import org.archguard.scanner.core.sourcecode.ServiceSupplyType

/**
 * Author By [@wertycn](https://github.com/wertycn)
 */
class JavaDubboAnalyser : ApiAnalyser {
    override var resources: List<ContainerSupply> = listOf()
    var demands: List<ContainerDemand> = listOf()

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
        // 当前类导入的Dubbo相关注解存入map,
        val packagePrefix = "org.apache.dubbo.config.annotation."
        val dubboAnnotationMap = node.Imports.filter { it.Source.startsWith(packagePrefix) }.associate {
            Pair(it.Source.replaceFirst(packagePrefix, ""), it.Source)
        }

        // 导入的包为空不进行分析
        if (dubboAnnotationMap.isEmpty()) {
            return
        }

        // 判断是否存在 DubboService ,Service 为Dubbo旧版本注解
        val serviceAnnotation = node.filterAnnotations("DubboService", "Service")
        // 筛选出包含DubboService注解且导入了官方包的Class
        val dubboServiceAnnotation = serviceAnnotation.filter {
            dubboAnnotationMap.containsKey(it.Name) || dubboAnnotationMap.containsKey("*")
        }

        // 获取DubboService的接口
        if (dubboServiceAnnotation.isNotEmpty() && node.Implements.isNotEmpty()) {
            node.Implements.forEach {
                val dubboInterface = it
                /*
                 * 基于dubbo接口一定实现Interface的方法，因此存在Override注解
                 * TODO:
                 *   存在例外情况： 方法在父类中实现，子类只继承则无法识别 (存在跨文件甚至跨项目的情况，如父类可能基于maven等依赖工具引入，精准识别需要基于编译后包含所有依赖的完整jar包的扫描结果);
                 *   此外，还需要判断方法的可见性 ，只针对public方法生效
                 */
                node.Functions
                    //
                    .filter { it.Annotations.filter { it.Name == "Override" }.isNotEmpty() }
                    .forEach { this.createResource(it, dubboInterface, node) }
            }
        }

        node.Fields.filter { field ->
            // 注解为DubboReference 或Reference 且在Import 的Dubbo注解列表内
            field.Annotations
                .filter { annotation -> annotation.Name == "DubboReference" || annotation.Name == "Reference" }
                .any { dubboAnnotationMap.containsKey(it.Name) || dubboAnnotationMap.containsKey("*") }
        }.associate { Pair(it.TypeValue, "") }
    }

    private fun createResource(func: CodeFunction, service: String, node: CodeDataStruct) {
        // TODO: demo暂时借用HTTP API 的对象申明 ，后续需要调整为更标准的服务提供者对象
        resources = resources + ContainerSupply(
            sourceUrl = service + ":" + func.Name,
            sourceHttpMethod = "rpc",
            packageName = node.Package,
            className = node.NodeName,
            methodName = func.Name,
            supplyType = ServiceSupplyType.DUBBO_API
        )
        return
    }

    override fun toContainerServices(): List<ContainerService> {
        val componentCalls: MutableList<ContainerService> = mutableListOf()

        val componentRef = ContainerService(name = "")
        componentRef.resources = this.resources
        componentRef.demands = this.demands

        componentCalls += componentRef
        return componentCalls
    }
}