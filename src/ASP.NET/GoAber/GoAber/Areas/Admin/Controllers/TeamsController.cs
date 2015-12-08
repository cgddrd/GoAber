using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Auth;
using GoAber.Models;
using PagedList;
using GoAber.Services;

namespace GoAber.Areas.Admin.Controllers
{
    [GAAuthorize(Roles = "Administrator")]
    public class TeamsController : Controller
    {
        private TeamsService teamService = new TeamsService();
        private CommunitiesService communityService = new CommunitiesService();

        private const int pageSize = 100;

        // GET: Admin/Teams
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Index(int? page)
        {
            int pageNumber = (page ?? 1);
            var groups = teamService.GetAllTeams();
            return View(groups.ToPagedList(pageNumber, pageSize));
        }

        // GET: Admin/Teams/Details/5
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.FindTeamById(id.Value);
            if (team == null)
            {
                return HttpNotFound();
            }
            return View(team);
        }

        // GET: Admin/Teams/Create
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Create()
        {
            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name");
            return View();
        }

        // POST: Admin/Teams/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Create([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                teamService.CreateTeam(team);
                return RedirectToAction("Index");
            }

            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Edit/5
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.FindTeamById(id.Value);
            if (team == null)
            {
                return HttpNotFound();
            }
            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name", team.communityId);
            return View(team);
        }

        // POST: Admin/Teams/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [Audit]
        [GAAuthorize(Roles="Administrator")]
        public ActionResult Edit([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                teamService.UpdateTeam(team);
                return RedirectToAction("Index");
            }
            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Delete/5
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.FindTeamById(id.Value);
            if (team == null)
            {
                return HttpNotFound();
            }
            return View(team);
        }

        // POST: Admin/Teams/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        [Audit]
        [GAAuthorize(Roles = "Administrator")]
        public ActionResult DeleteConfirmed(int id)
        {
            teamService.DeleteTeam(id);
            return RedirectToAction("Index");
        }

        [Audit]
        protected override void Dispose(bool disposing)
        {
            base.Dispose(disposing);
        }
    }
}
