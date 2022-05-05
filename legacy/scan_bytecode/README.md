
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



javap -verbose QualityGateClientImpl.class

```
Classfile QualityGateClientImpl.class
  Last modified Mar 28, 2022; size 2566 bytes
  SHA-256 checksum 47d32bebf5a8a18aa2f310b00e9a056aeadd14dbe6cbe7529ee18e4a4aa646c0
  Compiled from "QualityGateClientImpl.kt"
public class com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl implements com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient
  minor version: 0
  major version: 52
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #2                          // com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl
  super_class: #4                         // java/lang/Object
  interfaces: 1, fields: 1, methods: 3, attributes: 2
Constant pool:
   #1 = Utf8               com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl
   #2 = Class              #1             // com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl
   #3 = Utf8               java/lang/Object
   #4 = Class              #3             // java/lang/Object
   #5 = Utf8               com/thoughtworks/archguard/report/domain/qualitygate/QualityGateClient
   #6 = Class              #5             // com/thoughtworks/archguard/report/domain/qualitygate/QualityGateClient
   #7 = Utf8               <init>
   #8 = Utf8               (Ljava/lang/String;)V
   #9 = Utf8               baseUrl
  #10 = Utf8               Lorg/springframework/beans/factory/annotation/Value;
  #11 = Utf8               value
  #12 = Utf8               ${client.host}
  #13 = Utf8               Lorg/jetbrains/annotations/NotNull;
  #14 = String             #9             // baseUrl
  #15 = Utf8               kotlin/jvm/internal/Intrinsics
  #16 = Class              #15            // kotlin/jvm/internal/Intrinsics
  #17 = Utf8               checkNotNullParameter
  #18 = Utf8               (Ljava/lang/Object;Ljava/lang/String;)V
  #19 = NameAndType        #17:#18        // checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
  #20 = Methodref          #16.#19        // kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
  #21 = Utf8               ()V
  #22 = NameAndType        #7:#21         // "<init>":()V
  #23 = Methodref          #4.#22         // java/lang/Object."<init>":()V
  #24 = Utf8               Ljava/lang/String;
  #25 = NameAndType        #9:#24         // baseUrl:Ljava/lang/String;
  #26 = Fieldref           #2.#25         // com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl.baseUrl:Ljava/lang/String;
  #27 = Utf8               this
  #28 = Utf8               Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;
  #29 = Utf8               getBaseUrl
  #30 = Utf8               ()Ljava/lang/String;
  #31 = Utf8               getQualityGate
  #32 = Utf8               (Ljava/lang/String;)Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;
  #33 = Utf8               qualityGateName
  #34 = String             #33            // qualityGateName
  #35 = Utf8               org/springframework/web/client/RestTemplate
  #36 = Class              #35            // org/springframework/web/client/RestTemplate
  #37 = Methodref          #36.#22        // org/springframework/web/client/RestTemplate."<init>":()V
  #38 = Utf8               java/lang/StringBuilder
  #39 = Class              #38            // java/lang/StringBuilder
  #40 = Methodref          #39.#22        // java/lang/StringBuilder."<init>":()V
  #41 = NameAndType        #29:#30        // getBaseUrl:()Ljava/lang/String;
  #42 = Methodref          #2.#41         // com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl.getBaseUrl:()Ljava/lang/String;
  #43 = Utf8               append
  #44 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
  #45 = NameAndType        #43:#44        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #46 = Methodref          #39.#45        // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #47 = Utf8               /api/quality-gate-profile/
  #48 = String             #47            // /api/quality-gate-profile/
  #49 = Utf8               toString
  #50 = NameAndType        #49:#30        // toString:()Ljava/lang/String;
  #51 = Methodref          #39.#50        // java/lang/StringBuilder.toString:()Ljava/lang/String;
  #52 = Utf8               com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate
  #53 = Class              #52            // com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate
  #54 = Utf8               getForObject
  #55 = Utf8               (Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
  #56 = NameAndType        #54:#55        // getForObject:(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
  #57 = Methodref          #36.#56        // org/springframework/web/client/RestTemplate.getForObject:(Ljava/lang/String;Ljava/lang/Class;[Ljava/lang/Object;)Ljava/lang/Object;
  #58 = Utf8               kotlin/collections/CollectionsKt
  #59 = Class              #58            // kotlin/collections/CollectionsKt
  #60 = Utf8               emptyList
  #61 = Utf8               ()Ljava/util/List;
  #62 = NameAndType        #60:#61        // emptyList:()Ljava/util/List;
  #63 = Methodref          #59.#62        // kotlin/collections/CollectionsKt.emptyList:()Ljava/util/List;
  #64 = Utf8               (Ljava/lang/Long;Ljava/lang/String;Ljava/util/List;Ljava/util/Date;Ljava/util/Date;)V
  #65 = NameAndType        #7:#64         // "<init>":(Ljava/lang/Long;Ljava/lang/String;Ljava/util/List;Ljava/util/Date;Ljava/util/Date;)V
  #66 = Methodref          #53.#65        // com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate."<init>":(Ljava/lang/Long;Ljava/lang/String;Ljava/util/List;Ljava/util/Date;Ljava/util/Date;)V
  #67 = Utf8               couplingQualityGate
  #68 = Utf8               Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;
  #69 = Utf8               java/lang/String
  #70 = Class              #69            // java/lang/String
  #71 = Utf8               Lorg/springframework/stereotype/Component;
  #72 = Utf8               Lkotlin/Metadata;
  #73 = Utf8               mv
  #74 = Integer            1
  #75 = Integer            6
  #76 = Integer            0
  #77 = Utf8               k
  #78 = Utf8               xi
  #79 = Integer            48
  #80 = Utf8               d1
  #81 = Utf8               \u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n
  #82 = Utf8               d2
  #83 = Utf8               Lcom/thoughtworks/archguard/report/domain/qualitygate/QualityGateClient;
  #84 = Utf8
  #85 = Utf8               archguard
  #86 = Utf8               QualityGateClientImpl.kt
  #87 = Utf8               RuntimeInvisibleAnnotations
  #88 = Utf8               Code
  #89 = Utf8               LineNumberTable
  #90 = Utf8               LocalVariableTable
  #91 = Utf8               RuntimeVisibleParameterAnnotations
  #92 = Utf8               RuntimeInvisibleParameterAnnotations
  #93 = Utf8               MethodParameters
  #94 = Utf8               StackMapTable
  #95 = Utf8               SourceFile
  #96 = Utf8               RuntimeVisibleAnnotations
{
  public com.thoughtworks.archguard.report.infrastructure.QualityGateClientImpl(java.lang.String);
    descriptor: (Ljava/lang/String;)V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=2, locals=2, args_size=2
         0: aload_1
         1: ldc           #14                 // String baseUrl
         3: invokestatic  #20                 // Method kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
         6: aload_0
         7: invokespecial #23                 // Method java/lang/Object."<init>":()V
        10: aload_0
        11: aload_1
        12: putfield      #26                 // Field baseUrl:Ljava/lang/String;
        15: return
      LineNumberTable:
        line 10: 6
        line 11: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      16     0  this   Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;
            0      16     1 baseUrl   Ljava/lang/String;
    RuntimeVisibleParameterAnnotations:
      parameter 0:
        0: #10(#11=s#12)
          org.springframework.beans.factory.annotation.Value(
            value="${client.host}"
          )
    RuntimeInvisibleParameterAnnotations:
      parameter 0:
        0: #13()
          org.jetbrains.annotations.NotNull
    MethodParameters:
      Name                           Flags
      baseUrl

  public java.lang.String getBaseUrl();
    descriptor: ()Ljava/lang/String;
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: getfield      #26                 // Field baseUrl:Ljava/lang/String;
         4: areturn
      LineNumberTable:
        line 11: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;
    RuntimeInvisibleAnnotations:
      0: #13()
        org.jetbrains.annotations.NotNull

  public com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate getQualityGate(java.lang.String);
    descriptor: (Ljava/lang/String;)Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=7, locals=3, args_size=2
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
      StackMapTable: number_of_entries = 1
        frame_type = 255 /* full_frame */
          offset_delta = 72
          locals = [ class com/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl, class java/lang/String, class com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate ]
          stack = [ class com/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate ]
      LineNumberTable:
        line 13: 6
        line 14: 52
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
           52      21     2 couplingQualityGate   Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;
            0      73     0  this   Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;
            0      73     1 qualityGateName   Ljava/lang/String;
    RuntimeInvisibleAnnotations:
      0: #13()
        org.jetbrains.annotations.NotNull
    RuntimeInvisibleParameterAnnotations:
      parameter 0:
        0: #13()
          org.jetbrains.annotations.NotNull
    MethodParameters:
      Name                           Flags
      qualityGateName
}
SourceFile: "QualityGateClientImpl.kt"
RuntimeVisibleAnnotations:
  0: #71()
    org.springframework.stereotype.Component
  1: #72(#73=[I#74,I#75,I#76],#77=I#74,#78=I#79,#80=[s#81],#82=[s#28,s#83,s#9,s#84,s#8,s#29,s#30,s#31,s#68,s#33,s#85])
    kotlin.Metadata(
      mv=[1,6,0]
      k=1
      xi=48
      d1=["\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0017\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0003H\u0016R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"]
      d2=["Lcom/thoughtworks/archguard/report/infrastructure/QualityGateClientImpl;","Lcom/thoughtworks/archguard/report/domain/qualitygate/QualityGateClient;","baseUrl","","(Ljava/lang/String;)V","getBaseUrl","()Ljava/lang/String;","getQualityGate","Lcom/thoughtworks/archguard/report/domain/qualitygate/CouplingQualityGate;","qualityGateName","archguard"]
    )
```