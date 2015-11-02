using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber;

namespace GoAber.Controllers
{
    public class UsersController : Controller
    {
        private GoAberEntities db = new GoAberEntities();

        // GET: Users
        public ActionResult Index()
        {
            var users1 = db.Users1.Include(u => u.group).Include(u => u.usercredential).Include(u => u.userrole);
            return View(users1.ToList());
        }

        // GET: Users/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            User user = db.Users1.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            return View(user);
        }

        // GET: Users/Create
        public ActionResult Create()
        {
            ViewBag.groupId = new SelectList(db.Groups1, "idGroup", "name");
            ViewBag.userCredentialsId = new SelectList(db.UserCredentials2, "idUserCredentials", "password");
            ViewBag.userRoleId = new SelectList(db.UserRoles1, "idUserRole", "type");
            return View();
        }

        // POST: Users/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "idUser,email,nickname,userRoleId,userCredentialsId,groupId")] User user)
        {
            if (ModelState.IsValid)
            {
                db.Users1.Add(user);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.groupId = new SelectList(db.Groups1, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.UserCredentials2, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.UserRoles1, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // GET: Users/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            User user = db.Users1.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            ViewBag.groupId = new SelectList(db.Groups1, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.UserCredentials2, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.UserRoles1, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // POST: Users/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "idUser,email,nickname,userRoleId,userCredentialsId,groupId")] User user)
        {
            if (ModelState.IsValid)
            {
                db.Entry(user).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            ViewBag.groupId = new SelectList(db.Groups1, "idGroup", "name", user.groupId);
            ViewBag.userCredentialsId = new SelectList(db.UserCredentials2, "idUserCredentials", "password", user.userCredentialsId);
            ViewBag.userRoleId = new SelectList(db.UserRoles1, "idUserRole", "type", user.userRoleId);
            return View(user);
        }

        // GET: Users/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            User user = db.Users1.Find(id);
            if (user == null)
            {
                return HttpNotFound();
            }
            return View(user);
        }

        // POST: Users/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            User user = db.Users1.Find(id);
            db.Users1.Remove(user);
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
