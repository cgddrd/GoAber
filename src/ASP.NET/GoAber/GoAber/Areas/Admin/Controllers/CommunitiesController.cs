using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;
using PagedList;
using WebGrease.Css.Extensions;
using GoAber.Services;

namespace GoAber.Areas.Admin.Controllers
{
    public class CommunitiesController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private const int pageSize = 100;
        private CommunitiesService io_comservice = new CommunitiesService();

        // GET: Admin/Communities
        public ActionResult Index(int? page)
        {
            int pageNumber = (page ?? 1);
            var communityData = db.Communities.OrderBy(a => a.name);
            return View(communityData.ToPagedList(pageNumber, pageSize));
        }

        // GET: Admin/Communities/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Community community = db.Communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // GET: Admin/Communities/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Admin/Communities/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,name,domain,home,challengesEndpoint")] Community community)
        {
            if (ModelState.IsValid)
            {
                community = io_comservice.RequestContract(community);
                db.Communities.Add(community);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            return View(community);
        }

        // GET: Admin/Communities/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Community community = db.Communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // POST: Admin/Communities/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,name,domain,home,challengesEndpoint")] Community community)
        {
            if (ModelState.IsValid)
            {
                db.Entry(community).State = EntityState.Modified;
                db.SaveChanges();
                return RedirectToAction("Index");
            }
            return View(community);
        }

        // GET: Admin/Communities/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Community community = db.Communities.Find(id);
            if (community == null)
            {
                return HttpNotFound();
            }
            return View(community);
        }

        // POST: Admin/Communities/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Community community = db.Communities.Find(id);

            // CG - When we delete a community, we want to also delete any of it's teams. 
            // HOWEVER, we DON'T neccesarily want to delete the users currently linked to those teams. 
            // Therefore, we first of all need to remove the relationship between those teams and their users,
            // before deleting the range of teams linked to the community, before finally deleting the community itself.
            //
            // It is important that this process is followed in-order, otherwise we end up with a ton of FK constraint errors.

            community.teams.ForEach(t => t.users.ToList().ForEach(u => t.users.Remove(u)));
            //CG - The above line is equivalent to the following:
            //foreach (var team in community.teams)
            //{
            //    foreach (var user in team.users.ToList())
            //    {
            //        team.users.Remove(user);
            //    }
            //}

            db.Teams.RemoveRange(community.teams);
            db.Communities.Remove(community);

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
