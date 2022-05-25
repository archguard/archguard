// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.archguard.scanner.bytecode

interface CodeConstants {
    companion object {
        // ----------------------------------------------------------------------
        // BYTECODE VERSIONS
        // ----------------------------------------------------------------------
        const val BYTECODE_JAVA_LE_4 = 48
        const val BYTECODE_JAVA_5 = 49
        const val BYTECODE_JAVA_6 = 50
        const val BYTECODE_JAVA_7 = 51
        const val BYTECODE_JAVA_8 = 52
        const val BYTECODE_JAVA_9 = 53
        const val BYTECODE_JAVA_10 = 54
        const val BYTECODE_JAVA_11 = 55
        const val BYTECODE_JAVA_12 = 56
        const val BYTECODE_JAVA_13 = 57
        const val BYTECODE_JAVA_14 = 58
        const val BYTECODE_JAVA_15 = 59
        const val BYTECODE_JAVA_16 = 60
        const val BYTECODE_JAVA_17 = 61

        // ----------------------------------------------------------------------
        // VARIABLE TYPES
        // ----------------------------------------------------------------------
        const val TYPE_BYTE = 0
        const val TYPE_CHAR = 1
        const val TYPE_DOUBLE = 2
        const val TYPE_FLOAT = 3
        const val TYPE_INT = 4
        const val TYPE_LONG = 5
        const val TYPE_SHORT = 6
        const val TYPE_BOOLEAN = 7
        const val TYPE_OBJECT = 8
        const val TYPE_ADDRESS = 9
        const val TYPE_VOID = 10
        const val TYPE_ANY = 11
        const val TYPE_GROUP2EMPTY = 12
        const val TYPE_NULL = 13
        const val TYPE_NOTINITIALIZED = 14
        const val TYPE_BYTECHAR = 15
        const val TYPE_SHORTCHAR = 16
        const val TYPE_UNKNOWN = 17
        const val TYPE_GENVAR = 18

        // ----------------------------------------------------------------------
        // VARIABLE TYPE FAMILIES
        // ----------------------------------------------------------------------
        const val TYPE_FAMILY_UNKNOWN = 0
        const val TYPE_FAMILY_BOOLEAN = 1
        const val TYPE_FAMILY_INTEGER = 2
        const val TYPE_FAMILY_FLOAT = 3
        const val TYPE_FAMILY_LONG = 4
        const val TYPE_FAMILY_DOUBLE = 5
        const val TYPE_FAMILY_OBJECT = 6

        // ----------------------------------------------------------------------
        // ACCESS FLAGS
        // ----------------------------------------------------------------------
        const val ACC_PUBLIC = 0x0001
        const val ACC_PRIVATE = 0x0002
        const val ACC_PROTECTED = 0x0004
        const val ACC_STATIC = 0x0008
        const val ACC_FINAL = 0x0010
        const val ACC_SYNCHRONIZED = 0x0020
        const val ACC_OPEN = 0x0020
        const val ACC_NATIVE = 0x0100
        const val ACC_ABSTRACT = 0x0400
        const val ACC_STRICT = 0x0800
        const val ACC_VOLATILE = 0x0040
        const val ACC_BRIDGE = 0x0040
        const val ACC_TRANSIENT = 0x0080
        const val ACC_VARARGS = 0x0080
        const val ACC_SYNTHETIC = 0x1000
        const val ACC_ANNOTATION = 0x2000
        const val ACC_ENUM = 0x4000
        const val ACC_MANDATED = 0x8000
        const val ACC_MODULE = 0x8000

        // ----------------------------------------------------------------------
        // CLASS FLAGS
        // ----------------------------------------------------------------------
        const val ACC_SUPER = 0x0020
        const val ACC_INTERFACE = 0x0200

        // ----------------------------------------------------------------------
        // INSTRUCTION GROUPS
        // ----------------------------------------------------------------------
        const val GROUP_GENERAL = 1
        const val GROUP_JUMP = 2
        const val GROUP_SWITCH = 3
        const val GROUP_INVOCATION = 4
        const val GROUP_FIELDACCESS = 5
        const val GROUP_RETURN = 6

        // ----------------------------------------------------------------------
        // POOL CONSTANTS
        // ----------------------------------------------------------------------
        const val CONSTANT_Utf8 = 1
        const val CONSTANT_Integer = 3
        const val CONSTANT_Float = 4
        const val CONSTANT_Long = 5
        const val CONSTANT_Double = 6
        const val CONSTANT_Class = 7
        const val CONSTANT_String = 8
        const val CONSTANT_Fieldref = 9
        const val CONSTANT_Methodref = 10
        const val CONSTANT_InterfaceMethodref = 11
        const val CONSTANT_NameAndType = 12
        const val CONSTANT_MethodHandle = 15
        const val CONSTANT_MethodType = 16
        const val CONSTANT_Dynamic = 17
        const val CONSTANT_InvokeDynamic = 18
        const val CONSTANT_Module = 19
        const val CONSTANT_Package = 20

        // ----------------------------------------------------------------------
        // MethodHandle reference_kind values
        // ----------------------------------------------------------------------
        const val CONSTANT_MethodHandle_REF_getField = 1
        const val CONSTANT_MethodHandle_REF_getStatic = 2
        const val CONSTANT_MethodHandle_REF_putField = 3
        const val CONSTANT_MethodHandle_REF_putStatic = 4
        const val CONSTANT_MethodHandle_REF_invokeVirtual = 5
        const val CONSTANT_MethodHandle_REF_invokeStatic = 6
        const val CONSTANT_MethodHandle_REF_invokeSpecial = 7
        const val CONSTANT_MethodHandle_REF_newInvokeSpecial = 8
        const val CONSTANT_MethodHandle_REF_invokeInterface = 9

        // ----------------------------------------------------------------------
        // VM OPCODES
        // ----------------------------------------------------------------------
        const val opc_nop = 0
        const val opc_aconst_null = 1
        const val opc_iconst_m1 = 2
        const val opc_iconst_0 = 3
        const val opc_iconst_1 = 4
        const val opc_iconst_2 = 5
        const val opc_iconst_3 = 6
        const val opc_iconst_4 = 7
        const val opc_iconst_5 = 8
        const val opc_lconst_0 = 9
        const val opc_lconst_1 = 10
        const val opc_fconst_0 = 11
        const val opc_fconst_1 = 12
        const val opc_fconst_2 = 13
        const val opc_dconst_0 = 14
        const val opc_dconst_1 = 15
        const val opc_bipush = 16
        const val opc_sipush = 17
        const val opc_ldc = 18
        const val opc_ldc_w = 19
        const val opc_ldc2_w = 20
        const val opc_iload = 21
        const val opc_lload = 22
        const val opc_fload = 23
        const val opc_dload = 24
        const val opc_aload = 25
        const val opc_iload_0 = 26
        const val opc_iload_1 = 27
        const val opc_iload_2 = 28
        const val opc_iload_3 = 29
        const val opc_lload_0 = 30
        const val opc_lload_1 = 31
        const val opc_lload_2 = 32
        const val opc_lload_3 = 33
        const val opc_fload_0 = 34
        const val opc_fload_1 = 35
        const val opc_fload_2 = 36
        const val opc_fload_3 = 37
        const val opc_dload_0 = 38
        const val opc_dload_1 = 39
        const val opc_dload_2 = 40
        const val opc_dload_3 = 41
        const val opc_aload_0 = 42
        const val opc_aload_1 = 43
        const val opc_aload_2 = 44
        const val opc_aload_3 = 45
        const val opc_iaload = 46
        const val opc_laload = 47
        const val opc_faload = 48
        const val opc_daload = 49
        const val opc_aaload = 50
        const val opc_baload = 51
        const val opc_caload = 52
        const val opc_saload = 53
        const val opc_istore = 54
        const val opc_lstore = 55
        const val opc_fstore = 56
        const val opc_dstore = 57
        const val opc_astore = 58
        const val opc_istore_0 = 59
        const val opc_istore_1 = 60
        const val opc_istore_2 = 61
        const val opc_istore_3 = 62
        const val opc_lstore_0 = 63
        const val opc_lstore_1 = 64
        const val opc_lstore_2 = 65
        const val opc_lstore_3 = 66
        const val opc_fstore_0 = 67
        const val opc_fstore_1 = 68
        const val opc_fstore_2 = 69
        const val opc_fstore_3 = 70
        const val opc_dstore_0 = 71
        const val opc_dstore_1 = 72
        const val opc_dstore_2 = 73
        const val opc_dstore_3 = 74
        const val opc_astore_0 = 75
        const val opc_astore_1 = 76
        const val opc_astore_2 = 77
        const val opc_astore_3 = 78
        const val opc_iastore = 79
        const val opc_lastore = 80
        const val opc_fastore = 81
        const val opc_dastore = 82
        const val opc_aastore = 83
        const val opc_bastore = 84
        const val opc_castore = 85
        const val opc_sastore = 86
        const val opc_pop = 87
        const val opc_pop2 = 88
        const val opc_dup = 89
        const val opc_dup_x1 = 90
        const val opc_dup_x2 = 91
        const val opc_dup2 = 92
        const val opc_dup2_x1 = 93
        const val opc_dup2_x2 = 94
        const val opc_swap = 95
        const val opc_iadd = 96
        const val opc_ladd = 97
        const val opc_fadd = 98
        const val opc_dadd = 99
        const val opc_isub = 100
        const val opc_lsub = 101
        const val opc_fsub = 102
        const val opc_dsub = 103
        const val opc_imul = 104
        const val opc_lmul = 105
        const val opc_fmul = 106
        const val opc_dmul = 107
        const val opc_idiv = 108
        const val opc_ldiv = 109
        const val opc_fdiv = 110
        const val opc_ddiv = 111
        const val opc_irem = 112
        const val opc_lrem = 113
        const val opc_frem = 114
        const val opc_drem = 115
        const val opc_ineg = 116
        const val opc_lneg = 117
        const val opc_fneg = 118
        const val opc_dneg = 119
        const val opc_ishl = 120
        const val opc_lshl = 121
        const val opc_ishr = 122
        const val opc_lshr = 123
        const val opc_iushr = 124
        const val opc_lushr = 125
        const val opc_iand = 126
        const val opc_land = 127
        const val opc_ior = 128
        const val opc_lor = 129
        const val opc_ixor = 130
        const val opc_lxor = 131
        const val opc_iinc = 132
        const val opc_i2l = 133
        const val opc_i2f = 134
        const val opc_i2d = 135
        const val opc_l2i = 136
        const val opc_l2f = 137
        const val opc_l2d = 138
        const val opc_f2i = 139
        const val opc_f2l = 140
        const val opc_f2d = 141
        const val opc_d2i = 142
        const val opc_d2l = 143
        const val opc_d2f = 144
        const val opc_i2b = 145
        const val opc_i2c = 146
        const val opc_i2s = 147
        const val opc_lcmp = 148
        const val opc_fcmpl = 149
        const val opc_fcmpg = 150
        const val opc_dcmpl = 151
        const val opc_dcmpg = 152
        const val opc_ifeq = 153
        const val opc_ifne = 154
        const val opc_iflt = 155
        const val opc_ifge = 156
        const val opc_ifgt = 157
        const val opc_ifle = 158
        const val opc_if_icmpeq = 159
        const val opc_if_icmpne = 160
        const val opc_if_icmplt = 161
        const val opc_if_icmpge = 162
        const val opc_if_icmpgt = 163
        const val opc_if_icmple = 164
        const val opc_if_acmpeq = 165
        const val opc_if_acmpne = 166
        const val opc_goto = 167
        const val opc_jsr = 168
        const val opc_ret = 169
        const val opc_tableswitch = 170
        const val opc_lookupswitch = 171
        const val opc_ireturn = 172
        const val opc_lreturn = 173
        const val opc_freturn = 174
        const val opc_dreturn = 175
        const val opc_areturn = 176
        const val opc_return = 177
        const val opc_getstatic = 178
        const val opc_putstatic = 179
        const val opc_getfield = 180
        const val opc_putfield = 181
        const val opc_invokevirtual = 182
        const val opc_invokespecial = 183
        const val opc_invokestatic = 184
        const val opc_invokeinterface = 185
        const val opc_invokedynamic = 186
        const val opc_new = 187
        const val opc_newarray = 188
        const val opc_anewarray = 189
        const val opc_arraylength = 190
        const val opc_athrow = 191
        const val opc_checkcast = 192
        const val opc_instanceof = 193
        const val opc_monitorenter = 194
        const val opc_monitorexit = 195
        const val opc_wide = 196
        const val opc_multianewarray = 197
        const val opc_ifnull = 198
        const val opc_ifnonnull = 199
        const val opc_goto_w = 200
        const val opc_jsr_w = 201
        const val CLINIT_NAME = "<clinit>"
        const val INIT_NAME = "<init>"
    }
}