using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Controllers
{
    public interface OAuthConnectivity
    {
        ActionResult StartOAuth();
        ActionResult Callback(string code);
    }
}