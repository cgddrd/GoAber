using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using GoAber.Services;

namespace GoAber
{
    public class ActivityDatasController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private CategoryUnitService categoryUnitService = new CategoryUnitService();
        private ActivityDataService dataService = new ActivityDataService();
        private ApplicationUserManager _userManager;

        // CG - We need to create our UserManager instance (copied from AccountController). 
        // This works because the OWIN context is shared application-wide. See: http://stackoverflow.com/a/27751581
        public ApplicationUserManager UserManager
        {
            get
            {
                return _userManager ?? HttpContext.GetOwinContext().GetUserManager<ApplicationUserManager>();
            }
            private set
            {
                _userManager = value;
            }
        }

        // GET: ActivityDatas/WeeklySummary
        public ActionResult WeeklySummary()
        {
            return View();
        }

        // GET: ActivityDatas/MonthlySummary
        public ActionResult MonthlySummary()
        {
            return View();
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
            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            ViewBag.userId = new SelectList(db.Users, "Id", "email");
            return View();
        }

        // POST: ActivityDatas/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,categoryUnitId,value,lastUpdated,date")] ActivityData activityData)
        {

            if (ModelState.IsValid)
            {
                dataService.createActivityDataForUser(activityData, User.Identity.GetUserId());
                return RedirectToAction("Index");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
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

            ActivityData activityData = dataService.getActivityDataById(id.Value);

            if (activityData == null)
            {
                return HttpNotFound();
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.User.Id);
            return View(activityData);
        }

        // POST: ActivityDatas/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,categoryUnitId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                dataService.editActivityDataForUser(activityData, User.Identity.GetUserId());
                return RedirectToAction("Index");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
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

            ActivityData activityData = db.ActivityDatas.Find(id.Value);

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
            dataService.deleteActivityData(id);
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
