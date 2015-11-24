using GoAber.Controllers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Areas.Admin.Controllers
{
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
