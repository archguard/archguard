
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

`javap -c QualityGateClientImpl.class`

```class
public class com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl implements com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient {
  public com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl(java.lang.String);
    Code:
       0: aload_1
       1: ldc           #14                 // String baseUrl
       3: invokestatic  #20                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
       6: aload_0
       7: invokespecial #23                 // Method java/lang/Object."<init>":()V
      10: aload_0
      11: aload_1
      12: putfield      #26                 // Field baseUrl:Ljava/lang/String;
      15: return

  public java.lang.String getBaseUrl();
    Code:
       0: aload_0
       1: getfield      #26                 // Field baseUrl:Ljava/lang/String;
       4: areturn

  public com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate getQualityGate(java.lang.String);
    Code:
       0: aload_1
       1: ldc           #34                 // String qualityGateName
       3: invokestatic  #20                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
       6: new           #36                 // class org/springframework/web/client/RestTemplate
       9: dup
      10: invokespecial #37                 // Method org/springframework/web/client/RestTemplate."<init>":()V
      13: new           #39                 // class java/lang/StringBuilder
      16: dup
      17: invokespecial #40                 // Method java/lang/StringBuilder."<init>":()V
      20: aload_0
      21: invokevirtual #42                 // Method getBaseUrl:()Ljava/lang/String;
      24: invokevirtual #46                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      27: ldc           #48                 // String /api/quality-gate-profile/
      29: invokevirtual #46                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      32: aload_1
      33: invokevirtual #46                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      36: invokevirtual #51                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      39: ldc           #53                 // class com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate
      41: iconst_0
      42: anewarray     #4                  // class java/lang/Object
      45: invokevirtual #57                 // Method org/springframework/web/client/RestTemplate.getForObject:(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
      48: checkcast     #53                 // class com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate
      51: astore_2
      52: aload_2
      53: dup
      54: ifnonnull     72
      57: pop
      58: new           #53                 // class com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate
      61: dup
      62: aconst_null
      63: aload_1
      64: invokestatic  #63                 // Method kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
      67: aconst_null
      68: aconst_null
      69: invokespecial #66                 // Method com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate."<init>":(Ljava/lang/Long;Ljava/lang/String;Ljava/util/List;Ljava/util/Date;Ljava/util/Date;)V
      72: areturn
}
```