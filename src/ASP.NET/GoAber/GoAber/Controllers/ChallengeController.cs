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

namespace GoAber
{
    public class ChallengeController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private ApplicationUserManager _userManager;

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
            var query = from d in db.Challenges
                        join c in db.UserChallenges on d equals c.challenge
                        join cC in db.CommunityChallenges on d equals cC.challenge
                        where c.ApplicationUserId == appUser.Id && appUser.Team.communityId == cC.communityId
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();

            ViewBag.AssignedChallengesCommunity = challenges;



            query = from d in db.Challenges
                        join c in db.UserChallenges on d equals c.challenge
                    join g in db.GroupChallenges on d equals g.challenge
                    where c.ApplicationUserId == appUser.Id && g.@group.Id == appUser.Team.Id
                    select d;
            IEnumerable<Challenge> challengesNotEnteredGroup = query.ToList();

            ViewBag.AssignedChallengesGroup = challengesNotEnteredGroup;



            query = from d in db.Challenges
                        join g in db.GroupChallenges on d equals g.challenge
                        
                        join c in db.UserChallenges on d equals c.challenge 
                            into t from rt in t.DefaultIfEmpty()
                    where g.@group.Id == appUser.Team.Id && rt.ApplicationUserId != appUser.Id
                    select d;
            
            IEnumerable<Challenge> challengesGroup = query.ToList();
            ViewBag.GroupChallenges = challengesGroup;

            query = from d in db.Challenges
                    join c in db.CommunityChallenges on d equals c.challenge
                    //join uC in db.UserChallenges on d equals uC.challenge
                    join uC in db.UserChallenges on d equals uC.challenge
                            into t from rt in t.DefaultIfEmpty()
                    where appUser.Team.communityId == c.communityId && rt.ApplicationUserId != appUser.Id//&& uC.ApplicationUserId != appUser.Id
                    select d;
            IEnumerable<Challenge> challengesCommunity = query.ToList();
            ViewBag.CommunityChallenges = challengesCommunity;

            return View(challenges.ToList());
        }

        // GET: Challenge
        public ActionResult AllChallenges()
        {
            return View(db.Challenges.ToList());
        }

        // GET: Challenge/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Challenge challenge = db.Challenges.Find(id);
            if (challenge == null)
            {
                return HttpNotFound();
            }
            return View(challenge);
        }

        // GET: Challenge/Create
        public ActionResult Create()
        {
            IEnumerable < SelectListItem > communities = db.Communities.Select(c => new SelectListItem
            {
                Value = c.Id.ToString(),
                Text = c.name
            });
            ViewBag.communities = communities; //new SelectList(communities, "idCommunity", "name", 0);


            IEnumerable<SelectListItem> groups = db.Teams.Select(c => new SelectListItem
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
                db.Challenges.Add(challenge);
                db.SaveChanges();

                if ( groupChallenges != null )
                { 
                    foreach (string item in groupChallenges)
                    {
                        GroupChallenge groupChallenge = new GroupChallenge()
                        {
                            groupId = Int32.Parse(item),
                            challengeId = challenge.Id
                        };
                        db.GroupChallenges.Add(groupChallenge);
                        db.SaveChanges();
                    }
                }

                if (communityChallenges != null )
                {
                    foreach (string item in communityChallenges)
                    {
                        CommunityChallenge communityChallenge = new CommunityChallenge()
                        {
                            communityId = Int32.Parse(item),
                            challengeId = challenge.Id
                        };
                        db.CommunityChallenges.Add(communityChallenge);
                        db.SaveChanges();
                    }
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
            Challenge challenge = db.Challenges.Find(id);
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
                db.Entry(challenge).State = EntityState.Modified;
                db.SaveChanges();
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
            Challenge challenge = db.Challenges.Find(id);
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
            Challenge challenge = db.Challenges.Find(id);
            db.Challenges.Remove(challenge);
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


        public ActionResult EnterChallenge(int? id)
        {
            Challenge challengeToEnter = db.Challenges.Find(id);
            UserChallenge userChallenge = new UserChallenge()
            {
                ApplicationUserId = UserManager.FindById(User.Identity.GetUserId()).Id,
                challengeId = challengeToEnter.Id
            };
            db.UserChallenges.Add(userChallenge);
            db.SaveChanges();
            return RedirectToAction("Index");
        }

        public ActionResult LeaveChallenge(int? id)
        {
            ApplicationUser appUser = UserManager.FindById(User.Identity.GetUserId());

            
            var query = from d in db.UserChallenges
                        where d.ApplicationUserId == appUser.Id && d.challengeId == id
                        select d;
            db.UserChallenges.Remove(query.FirstOrDefault());
            db.SaveChanges();
            return RedirectToAction("Index");
        }
        
    }
}
