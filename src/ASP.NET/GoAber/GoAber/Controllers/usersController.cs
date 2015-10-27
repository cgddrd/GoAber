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
    public class usersController : Controller
    {
        private goaberEntities db = new goaberEntities();

        // GET: users
        public ActionResult Index()
        {
            var users = db.users.Include(u => u.group).Include(u => u.usercredential).Include(u => u.userrole);
            return View(users.ToList());
        }

        // GET: users/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            user user = db.users.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            return View(user);
        }

        // GET: users/Create
        public ActionResult Create()
        {
            ViewBag.groupId = new SelectList(db.groups, "idGroup", "name");
            ViewBag.userCredentialsId = new SelectList(db.usercredentials, "idUserCredentials", "password");
            ViewBag.userRoleId = new SelectList(db.userroles, "idUserRole", "type");
            return View();
        }

        // POST: users/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idUser,email,nickname,userRoleId,userCredentialsId,groupId")] user user)
        {
            if (ModelState.IsValid)
            {
                db.users.Add(user);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.groupId = new SelectList(db.groups, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.usercredentials, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.userroles, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // GET: users/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            user user = db.users.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            ViewBag.groupId = new SelectList(db.groups, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.usercredentials, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.userroles, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // POST: users/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idUser,email,nickname,userRoleId,userCredentialsId,groupId")] user user)
        {
            if (ModelState.IsValid)
            {
                db.Entry(user).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.groupId = new SelectList(db.groups, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.usercredentials, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.userroles, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // GET: users/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            user user = db.users.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            return View(user);
        }

        // POST: users/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            user user = db.users.Find(id);
            db.users.Remove(user);
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
