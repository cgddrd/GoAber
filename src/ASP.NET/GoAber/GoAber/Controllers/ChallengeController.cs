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
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using GoAber.Services;
using GoAber.Models.ViewModels;
using PagedList;

namespace GoAber
{
    [GAAuthorize]
    public class ChallengeController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private ApplicationUserManager _userManager;
        private ChallengeService challengeService = new ChallengeService();
        private CommunitiesService communitiesService = new CommunitiesService();
        private TeamsService teamService = new TeamsService();
        private CategoryUnitService categoryUnitService = new CategoryUnitService();
        private const int pageSize = 100;
        // CG - We need to create our UserManager instance (copied from AccountController). 
        // This works because the OWIN context is shared application-wide. See: http://stackoverflow.com/a/27751581
        public ApplicationUserManager UserManager
        {
            get
            {
                return _userManager ?? HttpContext.GetOwinContext().GetUserManager<ApplicationUserManager>();
            }
            private set
            {
                _userManager = value;
            }
        }

        // GET: Challenge
        public ActionResult Index()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            
            //ViewBag.IsCoordinator = (ApplicationUserService.IsCurrentApplicationUserInRole("coordinator") || ApplicationUserService.IsCurrentApplicationUserInRole("administrator"));
            return View();
        }

        // GET: Challenge
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult AllChallenges()
        {
            return View(challengeService.getAllChallenges());
        }

        public ActionResult EnteredCommunity()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getEnteredCommunityChallenges(appUser));
        }

        public ActionResult EnteredGroup()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getEnteredGroupChallenges(appUser));
        }

        public ActionResult UnEnteredCommunity()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getUnEnteredCommunityChallenges(appUser));
        }

        public ActionResult UnEnteredGroup()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getUnEnteredGroupChallenges(appUser));
        }
        public ActionResult CompletedCommunity()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getCompletedCommunityChallenges(appUser));
        }

        public ActionResult CompletedGroup()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            return View(challengeService.getCompletedGroupChallenges(appUser));
        }



        // GET: Challenge/Details/5
        public ActionResult Details(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Challenge challenge = challengeService.getChallengeById(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // GET: Challenge/Create
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult CreateCommunity()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            IEnumerable<SelectListItem> communities = communitiesService.getAllCommunities().Select(c => new SelectListItem
            {
                Value = c.Id.ToString(),
                Text = c.name
            });
            ViewBag.communities = communities;


            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            return View();
        }

        public ActionResult CreateGroup()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());

            IEnumerable<SelectListItem> groups = teamService.GetTeamsByCommunity(appUser.Team.community).Select(c => new SelectListItem
            {
                Value = c.Id.ToString(),
                Text = c.name
            });
            ViewBag.groups = groups;

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            return View();
        }

        // POST: Challenge/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult CreateGroup([Bind(Include = "Id,categoryUnitId,startTime,endTime,name")] Challenge challenge, string[] groupChallenges)
        {
            ApplicationUser user = UserManager.FindById(User.Identity.GetUserId());
            if (ModelState.IsValid)
            {
                challengeService.createChallenge(challenge);

                challengeService.addChallengeToGroups(challenge, groupChallenges, user.Team.Id);

                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult CreateCommunity([Bind(Include = "Id,categoryUnitId,startTime,endTime,name")] Challenge challenge, string[] communityChallenges)
        {
            ApplicationUser user = UserManager.FindById(User.Identity.GetUserId());
            if (ModelState.IsValid)
            {

                List<string> errors = new List<string>();
                challengeService.setupRemoteChallenge(challenge, communityChallenges, user.Team.community.Id, ref errors);

                if (errors.Count > 0)
                {
                    ViewBag.errors = errors;
                    return View(challenge);
                }
                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        // GET: Challenge/Edit/5
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult Edit(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Challenge challenge = challengeService.getChallengeById(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }

            var categories = categoryUnitService.CreateCategoryUnitList();
            ViewBag.categoryUnits = new SelectList(categories, "idCategoryUnit", "unit", "category", 0);

            return View(challenge);
        }

        // POST: Challenge/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult Edit([Bind(Include = "Id,categoryUnitId,startTime,endTime,name")] Challenge challenge)
        {
            if (ModelState.IsValid)
            {
                challengeService.editChallenge(challenge);
                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        // GET: Challenge/Delete/5
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult Delete(string id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Challenge challenge = challengeService.getChallengeById(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // POST: Challenge/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        [GAAuthorize(Roles = "Administrator, Coordinator")]
        public ActionResult DeleteConfirmed(string id)
        {
            challengeService.deleteChallenge(id);
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


        public ActionResult EnterChallenge(string id)
        {
            challengeService.enterUserInToChallenge(UserManager.FindById(User.Identity.GetUserId()).Id, id);
            return RedirectToAction("Index");
        }

        public ActionResult LeaveChallenge(string id)
        {
            challengeService.removeUserFromChallenge(UserManager.FindById(User.Identity.GetUserId()).Id, id);
            return RedirectToAction("Index");
        }

        public ActionResult ViewGroupLeaderBoard(int? page, string id)
        {
            Challenge challenge = challengeService.getChallengeById(id);
            var model = challengeService.getGroupChallengeLeaders(challenge);

            int pageNumber = (page ?? 1);
            return View("LeaderBoard", model.ToPagedList(pageNumber, pageSize));
        }

        public ActionResult ViewCommunityLeaderBoard(int? page, string id)
        {
            Challenge challenge = challengeService.getChallengeById(id);
            var model = challengeService.getCommunityChallengeLeaders(challenge);

            int pageNumber = (page ?? 1);
            return View("LeaderBoard", model.ToPagedList(pageNumber, pageSize));
        }
    }
}
