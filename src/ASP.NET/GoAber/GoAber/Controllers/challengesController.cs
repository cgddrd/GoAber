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
    public class challengesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: challenges
        public ActionResult Index()
        {
            return View(db.challenges.ToList());
        }

        // GET: challenges/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            challenge challenge = db.challenges.Find(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // GET: challenges/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: challenges/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idChallenge,categoryUnit,startTime,endTime,name")] challenge challenge)
        {
            if (ModelState.IsValid)
            {
                db.challenges.Add(challenge);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(challenge);
        }

        // GET: challenges/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            challenge challenge = db.challenges.Find(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // POST: challenges/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idChallenge,categoryUnit,startTime,endTime,name")] challenge challenge)
        {
            if (ModelState.IsValid)
            {
                db.Entry(challenge).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        // GET: challenges/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            challenge challenge = db.challenges.Find(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // POST: challenges/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            challenge challenge = db.challenges.Find(id);
            db.challenges.Remove(challenge);
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
