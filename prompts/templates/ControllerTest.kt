// You Must use @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class PluginControllerTest {

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        // You can use MockMvcBuilders.standaloneSetup() to build mockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(PluginController()).build()
    }

    @Test
    fun shouldReturnPluginTypes() {
        mockMvc.perform(get("/api/plugin/type"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0]").value("DUBBO"))
    }
}
