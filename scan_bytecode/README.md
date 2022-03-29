
##  Code Samples for test

source code in Kotlin

```kotlin
package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class QualityGateClientImpl(@Value("\${client.host}") val baseUrl: String) : QualityGateClient {
    override fun getQualityGate(qualityGateName: String): CouplingQualityGate {
        val couplingQualityGate = RestTemplate().getForObject("$baseUrl/api/quality-gate-profile/$qualityGateName", CouplingQualityGate::class.java)
        return couplingQualityGate ?: CouplingQualityGate(null, qualityGateName, emptyList(), null, null)
    }
}
```

decompiler in JD-GUI

```java
import com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate;
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient;
import com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Metadata(mv = {1, 6, 0}, k = 1, xi = 48, d1 = {"\000\032\n\002\030\002\n\002\030\002\n\000\n\002\020\016\n\002\b\004\n\002\030\002\n\002\b\002\b\027\030\0002\0020\001B\017\022\b\b\001\020\002\032\0020\003\006\002\020\004J\020\020\007\032\0020\b2\006\020\t\032\0020\003H\026R\024\020\002\032\0020\003X\004\006\b\n\000\032\004\b\005\020\006\006\n"}, d2 = {"Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;", "Lcom/thoughtworks/archguard/report/domain/qualitygate/QualityGateClient;", "baseUrl", "", "(Ljava/lang/String;)V", "getBaseUrl", "()Ljava/lang/String;", "getQualityGate", "Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;", "qualityGateName", "archguard"})
public class QualityGateClientImpl implements QualityGateClient {
  @NotNull
  private final String baseUrl;
  
  public QualityGateClientImpl(@Value("${client.host}") @NotNull String baseUrl) {
    this.baseUrl = baseUrl;
  }
  
  @NotNull
  public String getBaseUrl() {
    return this.baseUrl;
  }
  
  @NotNull
  public CouplingQualityGate getQualityGate(@NotNull String qualityGateName) {
    Intrinsics.checkNotNullParameter(qualityGateName, "qualityGateName");
    CouplingQualityGate couplingQualityGate = (CouplingQualityGate)(new RestTemplate()).getForObject(getBaseUrl() + "/api/quality-gate-profile/" + qualityGateName, CouplingQualityGate.class, new Object[0]);
    if (couplingQualityGate == null);
    return new CouplingQualityGate(null, qualityGateName, CollectionsKt.emptyList(), null, null);
  }
}
```
``