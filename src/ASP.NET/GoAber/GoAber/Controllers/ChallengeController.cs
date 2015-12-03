using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using GoAber.Services;
using GoAber.Models.ViewModels;
using PagedList;

namespace GoAber
{
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
            ViewBag.AssignedChallengesCommunity = challengeService.getEnteredCommunityChallenges(appUser);
            ViewBag.AssignedChallengesGroup = challengeService.getEnteredGroupChallenges(appUser);
            ViewBag.GroupChallenges = challengeService.getUnEnteredGroupChallenges(appUser);
            ViewBag.CommunityChallenges = challengeService.getUnEnteredCommunityChallenges(appUser);
            ViewBag.CompletedComChallenges = challengeService.getCompletedCommunityChallenges(appUser);
            ViewBag.CompletedGroupChallenges = challengeService.getCompletedGroupChallenges(appUser);

            return View(ViewBag.CommunityChallenges);
        }

        // GET: Challenge
        public ActionResult AllChallenges()
        {
            return View(challengeService.getAllChallenges());
        }

        // GET: Challenge/Details/5
        public ActionResult Details(int? id)
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
                challengeService.createChallenge(challenge);

                challengeService.addChallengeToCommunities(challenge, communityChallenges, user.Team.community.Id);

                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        // GET: Challenge/Edit/5
        public ActionResult Edit(int? id)
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

        // POST: Challenge/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include = "Id,categoryUnit,startTime,endTime,name,communityStartedBy")] Challenge challenge)
        {
            if (ModelState.IsValid)
            {
                challengeService.editChallenge(challenge);
                return RedirectToAction("Index");
            }
            return View(challenge);
        }

        // GET: Challenge/Delete/5
        public ActionResult Delete(int? id)
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
        public ActionResult DeleteConfirmed(int id)
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
            ActivityDataService dataService = new ActivityDataService();

            // first get all acitivty data matching the unit
            var activityData = dataService.GetAllActivityData();
            activityData = activityData.Where(a => a.categoryunit.unit.Id == challenge.categoryUnit.unit.Id);

            IEnumerable<LeaderViewModel> model = challenge.groupchallenges.Select(c => new LeaderViewModel
            {
                Name = c.group.name,
                Total = activityData.Where(a => a.User.TeamId == c.group.Id).Sum(a => a.value).GetValueOrDefault(),
                NumMembers = activityData.Where(a => a.User.TeamId == c.group.Id).GroupBy(a => a.User.Id).Count()
            });

            model = model.OrderByDescending(m => m.Total);

            int pageNumber = (page ?? 1);
            return View("LeaderBoard", model.ToPagedList(pageNumber, pageSize));
        }

        public ActionResult ViewCommunityLeaderBoard(int? page, string id)
        {
            Challenge challenge = challengeService.getChallengeById(id);
            ActivityDataService dataService = new ActivityDataService();

            // first get all acitivty data matching the unit
            var activityData = dataService.GetAllActivityData();
            activityData = activityData.Where(a => a.categoryunit.unit.Id == challenge.categoryUnit.unit.Id);

            IEnumerable<LeaderViewModel> model = challenge.communityChallenges.Select(c => new LeaderViewModel
            {
                Name = c.community.name,
                Total = activityData.Where(a => a.User.Team.communityId == c.communityId).Sum(a => a.value).GetValueOrDefault(),
                NumMembers = activityData.Where(a => a.User.Team.communityId == c.communityId).GroupBy(a => a.User.Id).Count()
            });

            model = model.OrderByDescending(m => m.Total);

            int pageNumber = (page ?? 1);
            return View("LeaderBoard", model.ToPagedList(pageNumber, pageSize));
        }
    }
}
