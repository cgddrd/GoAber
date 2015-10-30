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
    public class activitydatasController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: activitydatas
        public ActionResult Index()
        {
            var activitydatas = db.activitydatas.Include(a => a.categoryunit).Include(a => a.user);
            return View(activitydatas.ToList());
        }

        // GET: activitydatas/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            activitydata activitydata = db.activitydatas.Find(id);
            if (activitydata == null)
            {
                return HttpNotFound();
            }
            return View(activitydata);
        }

        // GET: activitydatas/Create
        public ActionResult Create()
        {
            ViewBag.categoryUnitId = new SelectList(db.categoryunits, "idCategoryUnit", "idCategoryUnit");
            ViewBag.userId = new SelectList(db.users, "idUser", "email");
            return View();
        }

        // POST: activitydatas/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idActivityData,categoryUnitId,userId,value,lastUpdated,date")] activitydata activitydata)
        {
            if (ModelState.IsValid)
            {
                db.activitydatas.Add(activitydata);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.categoryUnitId = new SelectList(db.categoryunits, "idCategoryUnit", "idCategoryUnit", activitydata.categoryUnitId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", activitydata.userId);
            return View(activitydata);
        }

        // GET: activitydatas/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            activitydata activitydata = db.activitydatas.Find(id);
            if (activitydata == null)
            {
                return HttpNotFound();
            }
            ViewBag.categoryUnitId = new SelectList(db.categoryunits, "idCategoryUnit", "idCategoryUnit", activitydata.categoryUnitId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", activitydata.userId);
            return View(activitydata);
        }

        // POST: activitydatas/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idActivityData,categoryUnitId,userId,value,lastUpdated,date")] activitydata activitydata)
        {
            if (ModelState.IsValid)
            {
                db.Entry(activitydata).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.categoryUnitId = new SelectList(db.categoryunits, "idCategoryUnit", "idCategoryUnit", activitydata.categoryUnitId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", activitydata.userId);
            return View(activitydata);
        }

        // GET: activitydatas/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            activitydata activitydata = db.activitydatas.Find(id);
            if (activitydata == null)
            {
                return HttpNotFound();
            }
            return View(activitydata);
        }

        // POST: activitydatas/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            activitydata activitydata = db.activitydatas.Find(id);
            db.activitydatas.Remove(activitydata);
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
