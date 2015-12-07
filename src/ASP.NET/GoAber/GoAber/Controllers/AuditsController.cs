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

namespace GoAber
{
    [GAAuthorize]
    public class AuditsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Audits
        public ActionResult Index()
        {
            var audit = db.Audit.Include(a => a.user);
            return View(audit.ToList());
        }

        // GET: Audits/Details/5
        public ActionResult Details(Guid? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Audit audit = db.Audit.Find(id);
            if (audit == null)
            {
                return HttpNotFound();
            }
            return View(audit);
        }

        
    }
}
