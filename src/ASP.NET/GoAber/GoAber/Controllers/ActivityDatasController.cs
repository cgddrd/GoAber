using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;

namespace GoAber
{
    public class ActivityDatasController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: ActivityDatas
        public ActionResult Index()
        {
            var activityDatas = db.ActivityDatas.Include(a => a.categoryunit).Include(a => a.User);
            return View(activityDatas.ToList());
        }

        // GET: ActivityDatas/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ActivityData activityData = db.ActivityDatas.Find(id);
            if (activityData == null)
            {
                return HttpNotFound();
            }
            return View(activityData);
        }

        // GET: ActivityDatas/Create
        public ActionResult Create()
        {
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id");
            ViewBag.userId = new SelectList(db.Users, "Id", "email");
            return View();
        }

        // POST: ActivityDatas/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                db.ActivityDatas.Add(activityData);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.User.Id);
            return View(activityData);
        }

        // GET: ActivityDatas/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ActivityData activityData = db.ActivityDatas.Find(id);
            if (activityData == null)
            {
                return HttpNotFound();
            }
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.User.Id);
            return View(activityData);
        }

        // POST: ActivityDatas/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                db.Entry(activityData).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.categoryUnitId = new SelectList(db.CategoryUnits, "Id", "Id", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.User.Id);
            return View(activityData);
        }

        // GET: ActivityDatas/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ActivityData activityData = db.ActivityDatas.Find(id);
            if (activityData == null)
            {
                return HttpNotFound();
            }
            return View(activityData);
        }

        // POST: ActivityDatas/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            ActivityData activityData = db.ActivityDatas.Find(id);
            db.ActivityDatas.Remove(activityData);
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
