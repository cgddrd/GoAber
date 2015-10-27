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
    public class communitiesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: communities
        public ActionResult Index()
        {
            return View(db.communities.ToList());
        }

        // GET: communities/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            community community = db.communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // GET: communities/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: communities/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idCommunity,name")] community community)
        {
            if (ModelState.IsValid)
            {
                db.communities.Add(community);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(community);
        }

        // GET: communities/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            community community = db.communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // POST: communities/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idCommunity,name")] community community)
        {
            if (ModelState.IsValid)
            {
                db.Entry(community).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(community);
        }

        // GET: communities/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            community community = db.communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // POST: communities/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            community community = db.communities.Find(id);
            db.communities.Remove(community);
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
