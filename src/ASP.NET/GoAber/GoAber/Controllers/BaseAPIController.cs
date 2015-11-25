using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Controllers
{
    public class BaseAPIController : Controller
    {
        protected ActionResult ToJson(Object obj)
        {
            return Content(JsonConvert.SerializeObject(obj), "text/javascript");
        }
    }
}