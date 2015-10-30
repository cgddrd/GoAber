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
    public class categoryunitsController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: categoryunits
        public ActionResult Index()
        {
            var categoryunits = db.categoryunits.Include(c => c.category).Include(c => c.unit);
            return View(categoryunits.ToList());
        }

        // GET: categoryunits/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            categoryunit categoryunit = db.categoryunits.Find(id);
            if (categoryunit == null)
            {
                return HttpNotFound();
            }
            return View(categoryunit);
        }

        // GET: categoryunits/Create
        public ActionResult Create()
        {
            ViewBag.categoryId = new SelectList(db.categories, "idCategory", "idCategory");
            ViewBag.unitId = new SelectList(db.units, "idUnit", "idUnit");
            return View();
        }

        // POST: categoryunits/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idCategoryUnit,categoryId,unitId")] categoryunit categoryunit)
        {
            if (ModelState.IsValid)
            {
                db.categoryunits.Add(categoryunit);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.categoryId = new SelectList(db.categories, "idCategory", "idCategory", categoryunit.categoryId);
            ViewBag.unitId = new SelectList(db.units, "idUnit", "idUnit", categoryunit.unitId);
            return View(categoryunit);
        }

        // GET: categoryunits/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            categoryunit categoryunit = db.categoryunits.Find(id);
            if (categoryunit == null)
            {
                return HttpNotFound();
            }
            ViewBag.categoryId = new SelectList(db.categories, "idCategory", "idCategory", categoryunit.categoryId);
            ViewBag.unitId = new SelectList(db.units, "idUnit", "idUnit", categoryunit.unitId);
            return View(categoryunit);
        }

        // POST: categoryunits/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idCategoryUnit,categoryId,unitId")] categoryunit categoryunit)
        {
            if (ModelState.IsValid)
            {
                db.Entry(categoryunit).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.categoryId = new SelectList(db.categories, "idCategory", "idCategory", categoryunit.categoryId);
            ViewBag.unitId = new SelectList(db.units, "idUnit", "idUnit", categoryunit.unitId);
            return View(categoryunit);
        }

        // GET: categoryunits/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            categoryunit categoryunit = db.categoryunits.Find(id);
            if (categoryunit == null)
            {
                return HttpNotFound();
            }
            return View(categoryunit);
        }

        // POST: categoryunits/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            categoryunit categoryunit = db.categoryunits.Find(id);
            db.categoryunits.Remove(categoryunit);
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
