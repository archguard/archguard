@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class /* Controller name */Test {

    private lateinit var mockMvc: MockMvc

    // some mock beans

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(/* Controller mocks */).build()
    }

   // some test methods
}