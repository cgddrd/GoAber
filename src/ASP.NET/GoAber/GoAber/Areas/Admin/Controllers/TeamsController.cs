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
using GoAber.Services;

namespace GoAber.Areas.Admin.Controllers
{
    public class TeamsController : Controller
    {
        private TeamsService teamService = new TeamsService();
        private CommunitiesService communityService = new CommunitiesService();

        private const int pageSize = 100;

        // GET: Admin/Teams
        public ActionResult Index(int? page)
        {
            int pageNumber = (page ?? 1);
            var groups = teamService.getAllTeams();
            return View(groups.ToPagedList(pageNumber, pageSize));
        }

        // GET: Admin/Teams/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.findTeamById(id.Value);
            if (team == null)
            {
                return HttpNotFound();
            }
            return View(team);
        }

        // GET: Admin/Teams/Create
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
        public ActionResult Create([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                teamService.createTeam(team);
                return RedirectToAction("Index");
            }

            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.findTeamById(id.Value);
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
        public ActionResult Edit([Bind(Include = "Id,name,communityId")] Team team)
        {
            if (ModelState.IsValid)
            {
                teamService.updateTeam(team);
                return RedirectToAction("Index");
            }
            ViewBag.communityId = new SelectList(communityService.getAllCommunities(), "Id", "name", team.communityId);
            return View(team);
        }

        // GET: Admin/Teams/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Team team = teamService.findTeamById(id.Value);
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
            teamService.deleteTeam(id);
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            base.Dispose(disposing);
        }
    }
}
