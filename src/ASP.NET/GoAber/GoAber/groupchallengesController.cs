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
    public class groupchallengesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: groupchallenges
        public ActionResult Index()
        {
            return View(db.groupchallenges.ToList());
        }

        // GET: groupchallenges/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            groupchallenge groupchallenge = db.groupchallenges.Find(id);
            if (groupchallenge == null)
            {
                return HttpNotFound();
            }
            return View(groupchallenge);
        }

        // GET: groupchallenges/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: groupchallenges/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idGroupChallenge,groupId,challengeId")] groupchallenge groupchallenge)
        {
            if (ModelState.IsValid)
            {
                db.groupchallenges.Add(groupchallenge);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(groupchallenge);
        }

        // GET: groupchallenges/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            groupchallenge groupchallenge = db.groupchallenges.Find(id);
            if (groupchallenge == null)
            {
                return HttpNotFound();
            }
            return View(groupchallenge);
        }

        // POST: groupchallenges/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idGroupChallenge,groupId,challengeId")] groupchallenge groupchallenge)
        {
            if (ModelState.IsValid)
            {
                db.Entry(groupchallenge).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(groupchallenge);
        }

        // GET: groupchallenges/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            groupchallenge groupchallenge = db.groupchallenges.Find(id);
            if (groupchallenge == null)
            {
                return HttpNotFound();
            }
            return View(groupchallenge);
        }

        // POST: groupchallenges/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            groupchallenge groupchallenge = db.groupchallenges.Find(id);
            db.groupchallenges.Remove(groupchallenge);
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
