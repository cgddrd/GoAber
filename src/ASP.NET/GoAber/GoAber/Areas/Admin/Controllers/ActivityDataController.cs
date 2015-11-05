using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber;
using System.Collections;

namespace GoAber.Areas.Admin.Controllers
{
    public class ActivityDataController : Controller
    {
        private GoAberEntities db = new GoAberEntities();

        // GET: Admin/ActivityData
        public ActionResult Index()
        {
            var activityDatas = db.ActivityDatas
                                    .Include(a => a.categoryunit)
                                    .Include(a => a.user);

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            return View(activityDatas.ToList());
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string email, int? idCategoryUnit, DateTime? fromDate = null, DateTime? toDate = null)
        {
            var activityData = db.ActivityDatas.Include(a => a.categoryunit).Include(a => a.user);
            activityData = ApplyFiltersToActivityData(activityData, email, idCategoryUnit, fromDate, toDate);

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            return View(activityData.ToList());
        }

        // GET: Admin/ActivityData/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ActivityData activityData = db.ActivityDatas
                                            .Include(a => a.categoryunit)
                                            .Include(a => a.categoryunit.category)
                                            .Include(a => a.categoryunit.unit)
                                            .SingleOrDefault(d => d.idActivityData == id);
            if (activityData == null)
            {
                return HttpNotFound();
            }
            return View(activityData);
        }

        // GET: Admin/ActivityData/Create
        public ActionResult Create()
        {
            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 1);
            ViewBag.userId = new SelectList(db.Users, "idUser", "email");
            return View();
        }

        // POST: Admin/ActivityData/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idActivityData,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                db.ActivityDatas.Add(activityData);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "idUser", "email", activityData.userId);
            return View(activityData);
        }

        // GET: Admin/ActivityData/Edit/5
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

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "idUser", "email", activityData.userId);
            return View(activityData);
        }

        // POST: Admin/ActivityData/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idActivityData,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                db.Entry(activityData).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "idUser", "email", activityData.userId);
            return View(activityData);
        }

        // GET: Admin/ActivityData/Delete/5
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

        // POST: Admin/ActivityData/Delete/5
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

        private IEnumerable CreateCategoryUnitList()
        {
            var categories = db.CategoryUnits.Select(c => new
            {
                idCategoryUnit = c.idCategoryUnit,
                category = c.category.name,
                unit = c.unit.name
            }).ToList();

            return categories;
        }

        private IQueryable<ActivityData> ApplyFiltersToActivityData(IQueryable<ActivityData> activityData, string email, int? categoryUnitId, DateTime? fromDate, DateTime? toDate)
        {
            if (!String.IsNullOrEmpty(email))
            {
                activityData = activityData.Where(a => a.user.email.Contains(email));
            }

            if(categoryUnitId.HasValue)
            {
                activityData = activityData.Where(a => a.categoryunit.idCategoryUnit == categoryUnitId);
            }
            
            if (fromDate.HasValue)
            {
                activityData = activityData.Where(a => a.date >= fromDate);
            }

            if (toDate.HasValue)
            {
                activityData = activityData.Where(a => a.date <= toDate);
            }

            return activityData;
        }
    }
}
