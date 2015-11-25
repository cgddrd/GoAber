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

namespace GoAber
{
    public class ChallengeController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private ApplicationUserManager _userManager;
        private ChallengeService challengeService = new ChallengeService();
        private CommunitiesService communitiesService = new CommunitiesService();
        private TeamsService teamService = new TeamsService();
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
        public ActionResult Create()
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
//db.Communities
            IEnumerable< SelectListItem > communities = communitiesService.getAllCommunities().Select(c => new SelectListItem
            {
                Value = c.Id.ToString(),
                Text = c.name
            });
            ViewBag.communities = communities; //new SelectList(communities, "idCommunity", "name", 0);
            /*
            var query = from d in db.Teams
                        join c in db.Communities on d.community equals c
                        where appUser.Team.communityId == c.Id 
                        select d;*/
            IEnumerable<SelectListItem> groups = teamService.GetTeamsByCommunity(appUser.Team.community).Select(c => new SelectListItem
            {
                Value = c.Id.ToString(),
                Text = c.name
            });
            ViewBag.groups = groups;// new SelectList(groups, "idTeam", "name", 0);

            return View();
        }

        // POST: Challenge/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include = "Id,categoryUnitId,startTime,endTime,name")] Challenge challenge, string[] groupChallenges, string[] communityChallenges)
        {
            if (ModelState.IsValid)
            {
                challengeService.createChallenge(challenge);
                //db.Challenges.Add(challenge);
                //db.SaveChanges();

                if ( groupChallenges != null )
                {
                    challengeService.addChallengeToGroups(challenge, groupChallenges);
                    /*
                    foreach (string item in groupChallenges)
                    {
                        GroupChallenge groupChallenge = new GroupChallenge()
                        {
                            groupId = Int32.Parse(item),
                            challengeId = challenge.Id
                        };
                        db.GroupChallenges.Add(groupChallenge);
                        db.SaveChanges();
                    }*/
                }

                if (communityChallenges != null )
                {
                    challengeService.addChallengeToCommunities(challenge, communityChallenges);
                    /*
                    foreach (string item in communityChallenges)
                    {
                        CommunityChallenge communityChallenge = new CommunityChallenge()
                        {
                            communityId = Int32.Parse(item),
                            challengeId = challenge.Id
                        };
                        db.CommunityChallenges.Add(communityChallenge);
                        db.SaveChanges();
                    }*/
                }
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
                //db.Entry(challenge).State = EntityState.Modified;
                //db.SaveChanges();
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
            /*Challenge challenge = db.Challenges.Find(id);
            db.Challenges.Remove(challenge);
            db.SaveChanges();*/
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
        

        public ActionResult EnterChallenge(int? id)
        {
            /* Challenge challengeToEnter = db.Challenges.Find(id);
             UserChallenge userChallenge = new UserChallenge()
             {
                 ApplicationUserId = UserManager.FindById(User.Identity.GetUserId()).Id,
                 challengeId = challengeToEnter.Id
             };
             db.UserChallenges.Add(userChallenge);
             db.SaveChanges();*/
            challengeService.enterUserInToChallenge(UserManager.FindById(User.Identity.GetUserId()).Id, id);
            return RedirectToAction("Index");
        }

        public ActionResult LeaveChallenge(int? id)
        {
           // ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());
            challengeService.removeUserFromChallenge(UserManager.FindById(User.Identity.GetUserId()).Id, id);
            /*
            var query = from d in db.UserChallenges
                        where d.ApplicationUserId == appUser.Id && d.challengeId == id
                        select d;
            db.UserChallenges.Remove(query.FirstOrDefault());
            db.SaveChanges();*/
            return RedirectToAction("Index");
        }
        
    }
}
