namespace Chapi.Controller {
    [Route("api/[controller]")]
    class DynamicController {
        [HttpGet]
        [Route("dynamic")]
        public async Book GetDynamic(long? id)

        }
    }
}
