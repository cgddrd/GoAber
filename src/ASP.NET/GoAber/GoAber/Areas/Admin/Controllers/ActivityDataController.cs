﻿using System;
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

namespace GoAber.Areas.Admin.Controllers
{
    public class ActivityDataController : BaseController
    {
        private const int pageSize = 100;

        //private GoAberEntities db = new GoAberEntities();
        private ApplicationDbContext db = new ApplicationDbContext();

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
        public ActionResult Index(int? page)
        {
            var activityData = db.ActivityDatas
                                    .Include(a => a.categoryunit)
                                    .Include(a => a.User)
                                    .OrderBy(a => a.date);

            int pageNumber = (page ?? 1);
            
            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);
            return View(activityData.ToPagedList(pageNumber, pageSize));
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string email, int? idCategoryUnit, DateTime? fromDate = null, DateTime? toDate = null)
        {
            var activityData = db.ActivityDatas.Include(a => a.categoryunit).Include(a => a.User);
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
                                            .SingleOrDefault(d => d.Id == id);

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

            // CG - 'Users' refers to the ASP.NET Identity 'ApplicationUser' collection, NOT our own 'User' collection.
            // CG TODO: Add remaining properties to ApplicationUser so that we can get rid of 'GoAber.User' model.
            ViewBag.userId = new SelectList(db.Users, "Id", "email");

            return View();
        }

        // POST: Admin/ActivityData/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "ApplicationUserId,idActivityData,categoryUnitId,userId,value,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                activityData.lastUpdated = DateTime.Now;
                db.ActivityDatas.Add(activityData);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
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
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
            return View(activityData);
        }

        // POST: Admin/ActivityData/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "ApplicationUserId,Id,categoryUnitId,userId,value,lastUpdated,date")] ActivityData activityData)
        {
            if (ModelState.IsValid)
            {
                activityData.lastUpdated = DateTime.Now;
                db.Entry(activityData).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            var categories = CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", activityData.categoryUnitId);
            ViewBag.userId = new SelectList(db.Users, "Id", "email", activityData.ApplicationUserId);
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
                // CG - 'idCategoryUnit was previously set to: c.idCategoryUnit (which no longer exists).
                idCategoryUnit = c.Id,
                category = c.category.name,
                unit = c.unit.name
            }).ToList();

            return categories;
        }

        private IQueryable<ActivityData> ApplyFiltersToActivityData(IQueryable<ActivityData> activityData, string email, int? categoryUnitId, DateTime? fromDate, DateTime? toDate)
        {
            if (!String.IsNullOrEmpty(email))
            {
                activityData = activityData.Where(a => a.User.Email.Contains(email));
            }

            if (categoryUnitId.HasValue)
            {
                activityData = activityData.Where(a => a.categoryunit.Id == categoryUnitId);
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

        private IEnumerable CreateUserList()
        {
            var users = db.Users.Select(c => new
            {
                // CG - 'idCategoryUnit was previously set to: c.idCategoryUnit (which no longer exists).
                idUser = c.Id,
                email = c.Email
            }).ToList();

            return users;
        }
    }
}
