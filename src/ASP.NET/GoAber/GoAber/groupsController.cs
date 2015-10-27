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
    public class groupsController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: groups
        public ActionResult Index()
        {
            var groups = db.groups.Include(g => g.community);
            return View(groups.ToList());
        }

        // GET: groups/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            group group = db.groups.Find(id);
            if (group == null)
            {
                return HttpNotFound();
            }
            return View(group);
        }

        // GET: groups/Create
        public ActionResult Create()
        {
            ViewBag.communityId = new SelectList(db.communities, "idCommunity", "name");
            return View();
        }

        // POST: groups/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idGroup,name,communityId")] group group)
        {
            if (ModelState.IsValid)
            {
                db.groups.Add(group);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.communityId = new SelectList(db.communities, "idCommunity", "name", group.communityId);
            return View(group);
        }

        // GET: groups/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            group group = db.groups.Find(id);
            if (group == null)
            {
                return HttpNotFound();
            }
            ViewBag.communityId = new SelectList(db.communities, "idCommunity", "name", group.communityId);
            return View(group);
        }

        // POST: groups/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idGroup,name,communityId")] group group)
        {
            if (ModelState.IsValid)
            {
                db.Entry(group).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.communityId = new SelectList(db.communities, "idCommunity", "name", group.communityId);
            return View(group);
        }

        // GET: groups/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            group group = db.groups.Find(id);
            if (group == null)
            {
                return HttpNotFound();
            }
            return View(group);
        }

        // POST: groups/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            group group = db.groups.Find(id);
            db.groups.Remove(group);
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
