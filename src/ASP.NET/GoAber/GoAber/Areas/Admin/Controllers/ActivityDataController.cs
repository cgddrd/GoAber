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
using GoAber.Models;
using PagedList;
using GoAber.Controllers;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using GoAber.ActionFilters;
using GoAber.Models.ViewModels;
using GoAber.Services;

namespace GoAber.Areas.Admin.Controllers
{
    public class ActivityDataController : BaseController
    {
        private const int pageSize = 100;

        //private GoAberEntities db = new GoAberEntities();
        private ApplicationDbContext db = new ApplicationDbContext();
        private ActivityDataService dataService = new ActivityDataService();
        private CategoryUnitService categoryUnitService = new CategoryUnitService();

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

        // GET: Admin/ActivityData
        [Audit]
        public ActionResult Index(int? page)
        {
            var activityData = dataService.GetAllActivityData();

            int pageNumber = (page ?? 1);

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            ActivityDataListViewModel model = new ActivityDataListViewModel();
            model.Data = activityData.ToPagedList(pageNumber, pageSize);
            model.FilterParams = new FilterViewModel();
            return View(model);
        }

        [HttpPost]
        [MultipleButton(Name = "action", Argument = "Filter")]
        [ValidateAntiForgeryToken]
        [Audit]
        public ActionResult Index(int? page, FilterViewModel filterParams)
        {
            var activityData = dataService.Filter(filterParams);
            int pageNumber = (page ?? 1);

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            ActivityDataListViewModel model = new ActivityDataListViewModel();
            model.Data = activityData.ToPagedList(pageNumber, pageSize);
            model.FilterParams = filterParams;
            return View("Index", model);
        }


        // GET: Admin/ActivityData/Details/5
        [Audit]
        public ActionResult Details(int? id)
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
            return View(activityData);
        }

        // GET: Admin/ActivityData/Create
        [Audit]
        public ActionResult Create()
        {
            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 1);

            ViewBag.userId = new SelectList(db.Users, "Id", "email");

            return View();
        }

        // POST: Admin/ActivityData/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Audit]
        public ActionResult Create([Bind(Include = "ApplicationUserId,idActivityData,categoryUnitId,userId,value,date")] ActivityData activityData)
        {
            if (activityData.date.Value > DateTime.Today)
            {
                ModelState.AddModelError("date", "Date must be in the past or present.");
            }

            if (ModelState.IsValid)
            {
                dataService.CreateActivityData(activityData);
                return RedirectToAction("Index");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
            return View(activityData);
        }

        // GET: Admin/ActivityData/Edit/5
        [Audit]
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
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
            return View(activityData);
        }

        // POST: Admin/ActivityData/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Audit]
        public ActionResult Edit([Bind(Include = "ApplicationUserId,Id,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (activityData.date.Value > DateTime.Today)
            {
                ModelState.AddModelError("date", "Date must be in the past or present.");
            }

            if (ModelState.IsValid)
            {
                dataService.EditActivityData(activityData);
                return RedirectToAction("Index");
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
            return View(activityData);
        }

        // GET: Admin/ActivityData/Delete/5
        [Audit]
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
		[Audit]
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id, string message)
        { 
            dataService.DeleteActivityData(id, message, User.Identity.GetUserId());
            return RedirectToAction("Index");
        }

        [Audit]
        [HttpPost]
        [MultipleButton(Name = "action", Argument = "BatchDelete")]
        [ValidateAntiForgeryToken]
        public ActionResult ConfirmBatchDelete(int? page, FilterViewModel filterParams)
        {
            var activityData = dataService.Filter(filterParams);
            filterParams.Size = activityData.Count();
            return View("BatchDelete", filterParams);
        }


        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private IEnumerable CreateUserList()
        {
            var users = db.Users.Select(c => new
            {
                idUser = c.Id,
                email = c.Email
            }).ToList();

            return users;
        }
    }
}
