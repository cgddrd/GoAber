using System;
using System.Data;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity;
using GoAber.Services;
using PagedList;
using GoAber.Models.ViewModels;
using GoAber.ActionFilters;

namespace GoAber
{
    public class ActivityDatasController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private CategoryUnitService categoryUnitService = new CategoryUnitService();
        private ActivityDataService dataService = new ActivityDataService();
        private ApplicationUserManager _userManager;
        private const int pageSize = 100;

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
            return View(categoryUnitService.GetAllCategories());
        }

        // GET: ActivityDatas/MonthlySummary
        public ActionResult MonthlySummary()
        {
            return View(categoryUnitService.GetAllCategories());
        }

        // GET: ActivityDatas/AllTime
        public ActionResult AllTimeSummary()
        {
            return View(categoryUnitService.GetAllCategories());
        }

        // GET: ActivityDatas/Manage
        public ActionResult Manage(int? page)
        {
            int pageNumber = (page ?? 1);
            string userId = User.Identity.GetUserId();
            var data = dataService.FindActivityDataForUser(userId);

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            ActivityDataListViewModel model = new ActivityDataListViewModel();
            model.Data = data.ToPagedList(pageNumber, pageSize);
            model.FilterParams = new FilterViewModel();
            return View(model);
        }

        [HttpPost]
        [MultipleButton(Name = "action", Argument = "Filter")]
        [ValidateAntiForgeryToken]
        public ActionResult Manage(int? page, FilterViewModel filterParams)
        {
            string userId = User.Identity.GetUserId();
            ApplicationUser user = db.Users.Where(u => u.Id.Equals(userId)).Single();

            if (user == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.Unauthorized);
            }

            filterParams.Email = user.Email;
            var activityData = dataService.Filter(filterParams);
            int pageNumber = (page ?? 1);

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            ActivityDataListViewModel model = new ActivityDataListViewModel();
            model.Data = activityData.ToPagedList(pageNumber, pageSize);
            model.FilterParams = filterParams;
            //note: must manually redirect view here because of the MultipleButton annotation.
            return View("Manage", model);
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
            if (activityData.date.Value > DateTime.Today)
            {
                ModelState.AddModelError("date", "Date must be in the past or present.");
            }

            if (ModelState.IsValid)
            {
                dataService.CreateActivityDataForUser(activityData, User.Identity.GetUserId());
                return RedirectToAction("Manage");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            string userId = User.Identity.GetUserId();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", userId);
            return View(activityData);
        }

        // GET: ActivityDatas/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }

            ActivityData activityData = dataService.GetActivityDataById(id.Value);

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
            if (activityData.date.Value > DateTime.Today)
            {
                ModelState.AddModelError("date", "Date must be in the past or present.");
            }

            if (ModelState.IsValid)
            {
                dataService.EditActivityDataForUser(activityData, User.Identity.GetUserId());
                return RedirectToAction("Manage");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            string userId = User.Identity.GetUserId();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", userId);
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
        public ActionResult DeleteConfirmed(int id, string message)
        {
            string userId = User.Identity.GetUserId();
            dataService.DeleteActivityData(id, message, userId);
            return RedirectToAction("Manage");
        }

        [HttpPost]
        [MultipleButton(Name = "action", Argument = "BatchDelete")]
        [ValidateAntiForgeryToken]
        public ActionResult ConfirmBatchDelete(int? page, FilterViewModel filterParams)
        {
            string userId = User.Identity.GetUserId();
            ApplicationUser user = db.Users.Where(u => u.Id.Equals(userId)).Single();

            if (user == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.Unauthorized);
            }

            filterParams.Email = user.Email;
            var activityData = dataService.Filter(filterParams);
            filterParams.Size = activityData.Count();
            return View("BatchDelete", filterParams);
        }

        // POST: ActivityDatas/BatchDelete
        [HttpPost, ActionName("BatchDelete")]
        [ValidateAntiForgeryToken]
        public ActionResult BatchDelete(FilterViewModel filterParams, string message)
        {
            string userId = User.Identity.GetUserId();
            ApplicationUser user = db.Users.Where(u => u.Id.Equals(userId)).Single();

            if (user == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.Unauthorized);
            }

            filterParams.Email = user.Email;
            var activityData = dataService.Filter(filterParams);
            dataService.BatchDelete(activityData, message, userId);
            return RedirectToAction("Manage");
        }

        // GET: ActivityDatas/ViewUser/<userid>
        public ActionResult ViewUser(string id)
        {
            if (id == null)
            {
                return HttpNotFound();
            }

            //find user 
            ApplicationDbContext db = new ApplicationDbContext();
            ApplicationUser user = db.Users.Where(u => u.Id.Equals(id)).SingleOrDefault();

            if (user == null)
            {
                return HttpNotFound();
            }

            // create view data
            ViewUserSummary summary = new ViewUserSummary();
            summary.User = user;
            summary.CategoryViews = categoryUnitService.GetAllCategories();
            return View(summary);
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
