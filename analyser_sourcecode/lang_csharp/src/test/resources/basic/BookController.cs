using System;

namespace Chapi.Controller {
    [Produces("application/json")]
    [Route("api/chapi")]
    public class BookController {
        [HttpPost]
        [Route("book")]
        public async Book CreateBook([FromBody]CreateBookDto dto)

        }
    }
}
