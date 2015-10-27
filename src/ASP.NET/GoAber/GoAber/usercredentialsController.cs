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
    public class usercredentialsController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: usercredentials
        public ActionResult Index()
        {
            return View(db.usercredentials.ToList());
        }

        // GET: usercredentials/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            usercredential usercredential = db.usercredentials.Find(id);
            if (usercredential == null)
            {
                return HttpNotFound();
            }
            return View(usercredential);
        }

        // GET: usercredentials/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: usercredentials/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idUserCredentials,password")] usercredential usercredential)
        {
            if (ModelState.IsValid)
            {
                db.usercredentials.Add(usercredential);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(usercredential);
        }

        // GET: usercredentials/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            usercredential usercredential = db.usercredentials.Find(id);
            if (usercredential == null)
            {
                return HttpNotFound();
            }
            return View(usercredential);
        }

        // POST: usercredentials/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idUserCredentials,password")] usercredential usercredential)
        {
            if (ModelState.IsValid)
            {
                db.Entry(usercredential).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(usercredential);
        }

        // GET: usercredentials/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            usercredential usercredential = db.usercredentials.Find(id);
            if (usercredential == null)
            {
                return HttpNotFound();
            }
            return View(usercredential);
        }

        // POST: usercredentials/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            usercredential usercredential = db.usercredentials.Find(id);
            db.usercredentials.Remove(usercredential);
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
