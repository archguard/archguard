package org.archguard.scanner.ctl.loader

import org.archguard.scanner.core.Analyser
import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.core.context.Context

// TODO load the scanner via classloader
// 扫描指定包/路径/类
object AnalyserLoader {

    fun load(context: Context, spec: AnalyserSpec): Analyser<Context> {
        // isInstalled
        // install
        // get with class for name

        // support full class name or short class name
        return Class.forName("org.archguard.scanner.ctl." + spec.className)
            .declaredConstructors[0]
            .newInstance(context) as Analyser<Context>
    }
}
