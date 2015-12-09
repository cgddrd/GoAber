using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Auth;
using GoAber.Models;

namespace GoAber.Views
{
    [GAAuthorize]
    public class DataRemovalAuditsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: DataRemovalAudits
        public ActionResult Index()
        {
            var dataRemovalAudits = db.DataRemovalAudits.Include(d => d.User);
            return View(dataRemovalAudits.ToList());
        }

        // GET: DataRemovalAudits/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            DataRemovalAudit dataRemovalAudit = db.DataRemovalAudits.Find(id);
            if (dataRemovalAudit == null)
            {
                return HttpNotFound();
            }
            return View(dataRemovalAudit);
        }

        
    }
}
