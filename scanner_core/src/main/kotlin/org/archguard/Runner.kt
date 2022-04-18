package org.archguard

class Runner {
    // 作为探针, 根据 archguard 的调用参数创建不同的context
    // context包含了scanner执行过程中所需的上下文信息, 还有数据存储的回调方法等
    // executor启动后, 会根据上下文信息拉取指定的scanner类, 并按照树状结构执行

    // 官方实现的scanner会提前写入official_scanner_specs
    // 自定义实现的scanner需使用已有的context, 实现LanguageScanner or FeatureScanner
    // 将其打包后, 通过spec参数通过上述流程自动加载
    // 自动加载通过下载jar, 读取外部class进行执行
}
