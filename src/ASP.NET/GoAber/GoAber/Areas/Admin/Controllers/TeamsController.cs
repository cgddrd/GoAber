using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;

namespace GoAber.Areas.Admin.Controllers
{
    public class TeamsController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        // GET: Admin/Teams
        public ActionResult Index()
        {
            var groups = db.Groups.Include(t => t.community);
            return View(groups.ToList());
        }

        // GET: Admin/Teams/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = db.Groups.Find(id);
            if (team == null)
            {
                return HttpNotFound();
            }
            return View(team);
        }

        // GET: Admin/Teams/Create
        public ActionResult Create()
        {
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name");
            return View();
        }

        // POST: Admin/Teams/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                db.Groups.Add(team);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = db.Groups.Find(id);
            if (team == null)
            {
                return HttpNotFound();
            }
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", team.communityId);
            return View(team);
        }

        // POST: Admin/Teams/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                db.Entry(team).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.communityId = new SelectList(db.Communities, "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = db.Groups.Find(id);
            if (team == null)
            {
                return HttpNotFound();
            }
            return View(team);
        }

        // POST: Admin/Teams/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Team team = db.Groups.Find(id);
            db.Groups.Remove(team);
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
