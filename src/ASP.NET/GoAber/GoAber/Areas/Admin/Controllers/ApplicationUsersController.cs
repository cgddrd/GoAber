using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;
using GoAber.Services;
using PagedList;

namespace GoAber.Areas.Admin.Controllers
{
    public class ApplicationUsersController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        private ApplicationUserService applicationUserService = new ApplicationUserService();
        private TeamsService teamsService = new TeamsService();

        private const int pageSize = 100;

        // GET: Admin/ApplicationUsers
        public ActionResult Index(int? page)
        {
            var applicationUsers = applicationUserService.GetAllApplicationUsers();

            int pageNumber = (page ?? 1);

           // var teams = categoryUnitService.CreateCategoryUnitList();
           // ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            var teamList = teamsService.CreateTeamList();
            ViewBag.TeamId = new SelectList(teamList, "TeamId", "Name", "CommunityName", 0);

            // CG - Use the ASP.NET 'TempData' dictionary to pass a value IMMEDIATELY from a re-direct back to this controller. (Set in the 'Delete' action)
            // See: http://stackoverflow.com/a/15958277 for more information.
            if (TempData != null)
            {
                if (TempData.ContainsKey("Error") && TempData["Error"] != null)
                {
                    ViewBag.FailureMessage = TempData["Error"].ToString();
                    TempData["Error"] = null;
                }
                
            }
           
            return View(applicationUsers.ToPagedList(pageNumber, pageSize));
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string email, string nickname, int? teamId)
        {
            var applicationUsers = applicationUserService.GetAllApplicationUsers();

            applicationUsers = ApplyFiltersToApplicationUsers((IQueryable<ApplicationUser>)applicationUsers, email, nickname, teamId);

            var teamList = teamsService.CreateTeamList();
            ViewBag.TeamId = new SelectList(teamList, "TeamId", "Name", "CommunityName", 0);

            return View(applicationUsers.ToPagedList(1, pageSize));
        }

        private IQueryable<ApplicationUser> ApplyFiltersToApplicationUsers(IQueryable<ApplicationUser> applicationUsers, string email, string nickname, int? teamId)
        {
            if (!String.IsNullOrEmpty(email))
            {
                applicationUsers = applicationUsers.Where(u => u.Email.Contains(email));
            }

            if (!String.IsNullOrEmpty(nickname))
            {
                applicationUsers = applicationUsers.Where(u => u.Nickname.Contains(nickname));
            }

            if (teamId.HasValue)
            {
                applicationUsers = applicationUsers.Where(u => u.Team.Id == teamId);
            }

            return applicationUsers;
        }

        // GET: Admin/ApplicationUsers/Details/5
        public ActionResult Details(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ApplicationUser applicationUser = db.Users.Find(id);
            if (applicationUser == null)
            {
                return HttpNotFound();
            }
            return View(applicationUser);
        }

        // GET: Admin/ApplicationUsers/Create
        public ActionResult Create()
        {
            ViewBag.TeamId = new SelectList(db.Teams, "Id", "name");
            return View();
        }

        // POST: Admin/ApplicationUsers/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,Nickname,DateOfBirth,TeamId,Email,EmailConfirmed,PasswordHash,SecurityStamp,PhoneNumber,PhoneNumberConfirmed,TwoFactorEnabled,LockoutEndDateUtc,LockoutEnabled,AccessFailedCount,UserName")] ApplicationUser applicationUser)
        {
            if (ModelState.IsValid)
            {
                db.Users.Add(applicationUser);
                db.SaveChanges();
                return RedirectToAction("Index");
            }

            ViewBag.TeamId = new SelectList(db.Teams, "Id", "name", applicationUser.TeamId);
            return View(applicationUser);
        }

        // GET: Admin/ApplicationUsers/Edit/5
        public ActionResult Edit(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ApplicationUser applicationUser = db.Users.Find(id);
            if (applicationUser == null)
            {
                return HttpNotFound();
            }

            var teamList = teamsService.CreateTeamList();
            ViewBag.TeamId = new SelectList(teamList, "TeamId", "Name", "CommunityName", applicationUser.TeamId);

            return View(applicationUser);
        }

        // POST: Admin/ApplicationUsers/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,Nickname,DateOfBirth,TeamId,Email,EmailConfirmed,PasswordHash,SecurityStamp,PhoneNumber,PhoneNumberConfirmed,TwoFactorEnabled,LockoutEndDateUtc,LockoutEnabled,AccessFailedCount,UserName")] ApplicationUser applicationUser)
        {
            var currentUser = db.Users.Find(applicationUser.Id);

            if (ModelState.IsValid)
            {
                //db.Entry(applicationUser).State = EntityState.Modified;
                //db.SaveChanges();

                // CG - Because we are not wanting to update all values for an ApplicationUser, we need to specifically set only the properties we want to update.
                // We therefore don't want to use the commented code above.
                // Note: As we are not updating all values of the ApplicationUser, we should really use a custom view model to hold the returned form values, rather than using the ApplicationUser model.
                if (currentUser != null)
                {
                    currentUser.DateOfBirth = applicationUser.DateOfBirth;
                    currentUser.Nickname = applicationUser.Nickname;
                    currentUser.Email = applicationUser.Email;
                    currentUser.TeamId = applicationUser.TeamId;
                    currentUser.UserName = applicationUser.UserName;


                    db.SaveChanges();
                }

                return RedirectToAction("Index");
            }

            
            var teamList = teamsService.CreateTeamList();

            ViewBag.TeamId = new SelectList(teamList, "TeamId", "Name", "CommunityName", applicationUser.TeamId);

            // ViewBag.TeamId = new SelectList(db.Teams, "Id", "name", applicationUser.TeamId);
            return View(applicationUser);
        }

        // GET: Admin/ApplicationUsers/Delete/5
        public ActionResult Delete(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            ApplicationUser applicationUser = db.Users.Find(id);
            if (applicationUser == null)
            {
                return HttpNotFound();
            }
            return View(applicationUser);
        }

        // POST: Admin/ApplicationUsers/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(string id)
        {

            // CG - Prevent an administrator user from deleting their own account whilst still logged in.
            if (ApplicationUserService.IsApplicationUserIdLoggedIn(id) && ApplicationUserService.IsCurrentApplicationUserInRole("Administrator"))
            {
                TempData["Error"] = "You are currently attempting to delete your own administrator account whilst still logged in. If you wish to delete this account, please sign out and log in using another administrator account.";

            }
            else
            {
                applicationUserService.DeleteApplicationUser(id);
            }

            
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
