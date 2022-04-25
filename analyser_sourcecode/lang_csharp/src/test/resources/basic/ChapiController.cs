using System;

namespace Chapi.Controller {
    [RoutePrefix("api")]
    public class ChapiController {
        [HttpGet]
        [Route("book/")]
        public Book GetBook()
        {
            return new Book();
        }
    }
}
