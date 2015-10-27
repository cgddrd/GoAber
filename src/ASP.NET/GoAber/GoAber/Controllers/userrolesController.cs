using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;

namespace GoAber
{
    public class userrolesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: userroles
        public ActionResult Index()
        {
            return View(db.userroles.ToList());
        }

        // GET: userroles/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userrole userrole = db.userroles.Find(id);
            if (userrole == null)
            {
                return HttpNotFound();
            }
            return View(userrole);
        }

        // GET: userroles/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: userroles/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idUserRole,type")] userrole userrole)
        {
            if (ModelState.IsValid)
            {
                db.userroles.Add(userrole);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(userrole);
        }

        // GET: userroles/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userrole userrole = db.userroles.Find(id);
            if (userrole == null)
            {
                return HttpNotFound();
            }
            return View(userrole);
        }

        // POST: userroles/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idUserRole,type")] userrole userrole)
        {
            if (ModelState.IsValid)
            {
                db.Entry(userrole).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(userrole);
        }

        // GET: userroles/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userrole userrole = db.userroles.Find(id);
            if (userrole == null)
            {
                return HttpNotFound();
            }
            return View(userrole);
        }

        // POST: userroles/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            userrole userrole = db.userroles.Find(id);
            db.userroles.Remove(userrole);
            db.SaveChanges();
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
