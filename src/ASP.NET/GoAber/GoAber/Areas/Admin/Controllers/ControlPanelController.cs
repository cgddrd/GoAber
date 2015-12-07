using GoAber.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using GoAber.Auth;

namespace GoAber.Areas.Admin.Controllers
{
    [GAAuthorize(Roles = "Administrator")]
    public class ControlPanelController : BaseController
    {
        // GET: Admin/ControlPanel
        [Audit]
        public ActionResult Index()
        {
            return View();
        }
    }
}
