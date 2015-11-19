using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;

namespace GoAber.Areas.Admin.Controllers
{
    public class WebServiceAuthsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Admin/WebServiceAuths
        public async Task<ActionResult> Index()
        {
            var webServiceAuths = db.WebServiceAuths.Include(w => w.user);
            return View(await webServiceAuths.ToListAsync());
        }

        // GET: Admin/WebServiceAuths/Details/5
        public async Task<ActionResult> Details(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            WebServiceAuth webServiceAuth = await db.WebServiceAuths.FindAsync(id);
            if (webServiceAuth == null)
            {
                return HttpNotFound();
            }
            return View(webServiceAuth);
        }

        // GET: Admin/WebServiceAuths/Create
        public ActionResult Create()
        {
            ViewBag.ApplicationUserId = new SelectList(db.Users, "Id", "Nickname");
            return View();
        }

        // POST: Admin/WebServiceAuths/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "appname,ApplicationUserId,expire")] WebServiceAuth webServiceAuth)
        {
            if (ModelState.IsValid)
            {
                webServiceAuth.authtoken = Guid.NewGuid().ToString();
                db.WebServiceAuths.Add(webServiceAuth);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.ApplicationUserId = new SelectList(db.Users, "Id", "Nickname", webServiceAuth.ApplicationUserId);
            return View(webServiceAuth);
        }


        // GET: Admin/WebServiceAuths/Delete/5
        public async Task<ActionResult> Delete(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            WebServiceAuth webServiceAuth = await db.WebServiceAuths.FindAsync(id);
            if (webServiceAuth == null)
            {
                return HttpNotFound();
            }
            return View(webServiceAuth);
        }

        // POST: Admin/WebServiceAuths/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(string id)
        {
            WebServiceAuth webServiceAuth = await db.WebServiceAuths.FindAsync(id);
            db.WebServiceAuths.Remove(webServiceAuth);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
