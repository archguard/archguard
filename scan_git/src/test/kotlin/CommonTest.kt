import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

class CommonTest {
    @Test
    internal fun testClassName() {
        val name = "".javaClass.simpleName
        assertEquals("String", name)
    }


    @Test
    internal fun testPropertyName() {
        val user = User(2, "Alex")
        for (c in User::class.memberProperties) {
            println(c.returnType == Int::class.createType())

            println("${c.name} = ${c.get(user)}")
        }

    }

    @Test
    internal fun joinList() {
        val listOf = listOf<Any>(1, 3, "String")

        val joinToString = listOf.joinToString {
            if (it is String) "'$it'" else it.toString()
        }
        println("joinToString = $joinToString")
    }


    @Test
    internal fun testSequence() {
        val list = sequenceOf<Int>(1, 2, 3, 4, 5, 6)
        list.filter {
            println(">$it")
            it % 2 == 0
        }.map {
            println(it)
            it.toString()
        }.map {
            println("-----$it")
        }.toList()
    }
}

data class User(val id: Int, val name: String)