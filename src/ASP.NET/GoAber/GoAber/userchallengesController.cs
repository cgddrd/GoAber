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
    public class userchallengesController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: userchallenges
        public ActionResult Index()
        {
            var userchallenges = db.userchallenges.Include(u => u.challenge).Include(u => u.user);
            return View(userchallenges.ToList());
        }

        // GET: userchallenges/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userchallenge userchallenge = db.userchallenges.Find(id);
            if (userchallenge == null)
            {
                return HttpNotFound();
            }
            return View(userchallenge);
        }

        // GET: userchallenges/Create
        public ActionResult Create()
        {
            ViewBag.challengeId = new SelectList(db.challenges, "idChallenge", "name");
            ViewBag.userId = new SelectList(db.users, "idUser", "email");
            return View();
        }

        // POST: userchallenges/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idUserChallenge,challengeId,userId")] userchallenge userchallenge)
        {
            if (ModelState.IsValid)
            {
                db.userchallenges.Add(userchallenge);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.challengeId = new SelectList(db.challenges, "idChallenge", "name", userchallenge.challengeId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", userchallenge.userId);
            return View(userchallenge);
        }

        // GET: userchallenges/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userchallenge userchallenge = db.userchallenges.Find(id);
            if (userchallenge == null)
            {
                return HttpNotFound();
            }
            ViewBag.challengeId = new SelectList(db.challenges, "idChallenge", "name", userchallenge.challengeId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", userchallenge.userId);
            return View(userchallenge);
        }

        // POST: userchallenges/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idUserChallenge,challengeId,userId")] userchallenge userchallenge)
        {
            if (ModelState.IsValid)
            {
                db.Entry(userchallenge).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.challengeId = new SelectList(db.challenges, "idChallenge", "name", userchallenge.challengeId);
            ViewBag.userId = new SelectList(db.users, "idUser", "email", userchallenge.userId);
            return View(userchallenge);
        }

        // GET: userchallenges/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            userchallenge userchallenge = db.userchallenges.Find(id);
            if (userchallenge == null)
            {
                return HttpNotFound();
            }
            return View(userchallenge);
        }

        // POST: userchallenges/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            userchallenge userchallenge = db.userchallenges.Find(id);
            db.userchallenges.Remove(userchallenge);
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
