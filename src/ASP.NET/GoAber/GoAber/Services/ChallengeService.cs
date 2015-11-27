using GoAber.App_Code.Scheduling.Jobs;
using GoAber.Models;
using GoAber.Scheduling;
using GoAber.WebService.ChallengesWS;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.Services
{
    /// <summary>
    /// ChallengeService
    /// 
    /// Provides covinence methods used by multiple controllers who wish to access 
    /// ChallengeService information in a sensible way.
    /// </summary>
    public class ChallengeService
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        
        public Challenge getChallengeById(int? id)
        {
            return db.Challenges.Find(id);
        }

        public IEnumerable<Challenge> getAllChallenges()
        {
           return db.Challenges;
        }

        public IEnumerable<Challenge> getCompletedCommunityChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join c in db.CommunityChallenges on d equals c.challenge
                        join uC in db.UserChallenges on d equals uC.challenge
                                into t
                        from rt in t.DefaultIfEmpty()
                        where user.Team.communityId == c.communityId 
                        && rt.ApplicationUserId != user.Id
                        && d.complete
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();
            return challenges;
        }


        public IEnumerable<Challenge> getCompletedGroupChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join g in db.GroupChallenges on d equals g.challenge
                        join c in db.UserChallenges on d equals c.challenge
                            into t
                        from rt in t.DefaultIfEmpty()
                        where g.@group.Id == user.Team.Id 
                        && rt.ApplicationUserId != user.Id
                        && d.complete
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();
            return challenges;
        }


        public IEnumerable<Challenge> getEnteredCommunityChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join c in db.UserChallenges on d equals c.challenge
                        join cC in db.CommunityChallenges on d equals cC.challenge
                        where c.ApplicationUserId == user.Id && user.Team.communityId == cC.communityId
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();
            return challenges;
        }

        public IEnumerable<Challenge> getEnteredGroupChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join c in db.UserChallenges on d equals c.challenge
                        join g in db.GroupChallenges on d equals g.challenge
                        where c.ApplicationUserId == user.Id && g.@group.Id == user.Team.Id
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();

            return challenges;
        }

        public IEnumerable<Challenge> getUnEnteredGroupChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join g in db.GroupChallenges on d equals g.challenge
                        join c in db.UserChallenges on d equals c.challenge
                            into t
                        from rt in t.DefaultIfEmpty()
                        where g.@group.Id == user.Team.Id 
                        && rt.ApplicationUserId != user.Id
                        && !d.complete
                        select d;

            IEnumerable<Challenge> challenges = query.ToList();

            return challenges;
        }


        public IEnumerable<Challenge> getUnEnteredCommunityChallenges(ApplicationUser user)
        {
            var query = from d in db.Challenges
                        join c in db.CommunityChallenges on d equals c.challenge
                        join uC in db.UserChallenges on d equals uC.challenge
                                into t
                        from rt in t.DefaultIfEmpty()
                        where user.Team.communityId == c.communityId 
                        && rt.ApplicationUserId != user.Id
                        && !d.complete
                        select d;
            IEnumerable<Challenge> challenges = query.ToList();

            return challenges;
        }

        public void createChallenge(Challenge challenge)
        {
            db.Challenges.Add(challenge);
            db.SaveChanges();

            Job lo_cjob = new Job();
            lo_cjob.tasktype = JobType.Challenge;
            lo_cjob.schedtype = ScheduleType.Once;
            lo_cjob.id = challenge.Id + "_job";

            //TimeSpan lo_elapsedTime = ((DateTime)challenge.endTime).Subtract(DateTime.Now);
            DateTimeOffset ldao_date = DateTime.SpecifyKind((DateTime)challenge.endTime, DateTimeKind.Utc);
            
            lo_cjob.date = ldao_date;
            
            //string[] ls_args = new string[];
            if (ScheduleJobs.AddJob(lo_cjob, new string[] { challenge.Id.ToString() }))
            {
                db.Jobs.Add(lo_cjob);
                db.SaveChanges();
            }


        }

        public void addChallengeToGroups(Challenge challenge, string[] groupChallenges, int usersGroup)
        {
            foreach (string item in groupChallenges)
            {
                GroupChallenge groupChallenge = new GroupChallenge()
                {
                    groupId = Int32.Parse(item),
                    challengeId = challenge.Id
                };
                if (Int32.Parse(item) == usersGroup)
                {
                    groupChallenge.startedChallenge = true;
                }
                db.GroupChallenges.Add(groupChallenge);
                db.SaveChanges();
            }

            if(!groupChallenges.Contains(usersGroup.ToString()))
            {
                GroupChallenge groupChallenge = new GroupChallenge()
                {
                    groupId = usersGroup,
                    challengeId = challenge.Id,
                    startedChallenge = true
                };
                db.GroupChallenges.Add(groupChallenge);
                db.SaveChanges();
            }
        }

        public void addChallengeToCommunities(Challenge challenge, string[] communities, int usersGroup, bool local = true)
        {
            foreach (string item in communities)
            {
                CommunityChallenge communityChallenge = new CommunityChallenge()
                {
                    communityId = Int32.Parse(item),
                    challengeId = challenge.Id
                };
                if (Int32.Parse(item) == usersGroup)
                {
                    communityChallenge.startedChallenge = true;
                }
                db.CommunityChallenges.Add(communityChallenge);
                db.SaveChanges();

                if (local)
                {
                    bool lb_goremote = false;

                    for (int i = 0; i < communities.Length; i++)
                    {
                        if (int.Parse(communities[i]) != usersGroup)
                        {
                            lb_goremote = true;
                        }
                    }

                    if (!lb_goremote) return;

                    GoAberChallengeWSConsumer lo_chalconsumer = GoAberChallengeWSConsumer.GetInstance();

                    if (!lo_chalconsumer.AddChallenge(challenge, usersGroup))
                    {
                        Debug.WriteLine("Cross community challenge failed!");
                    }
                }

            }

            if (!communities.Contains(usersGroup.ToString()))
            {
                CommunityChallenge communityChallenge = new CommunityChallenge()
                {
                    communityId = usersGroup,
                    challengeId = challenge.Id,
                    startedChallenge = true
                };
                db.CommunityChallenges.Add(communityChallenge);
                db.SaveChanges();
            }
        }



        public void editChallenge(Challenge challenge)
        {
            db.Entry(challenge).State = EntityState.Modified;
            db.SaveChanges();
        }

        public void deleteChallenge(int id)
        {
            Challenge challenge = db.Challenges.Find(id);
            db.Challenges.Remove(challenge);
            db.SaveChanges();
        }

        public void enterUserInToChallenge(string userId, int? challengeId)
        {
            Challenge challengeToEnter = db.Challenges.Find(challengeId);
            UserChallenge userChallenge = new UserChallenge()
            {
                ApplicationUserId = userId,
                challengeId = challengeToEnter.Id
            };
            db.UserChallenges.Add(userChallenge);
            db.SaveChanges();
        }

        public void removeUserFromChallenge(string userId, int? challengeId)
        {
            var query = from d in db.UserChallenges
                        where d.ApplicationUserId == userId && d.challengeId == challengeId
                        select d;
            db.UserChallenges.Remove(query.FirstOrDefault());
            db.SaveChanges();
        }


        ~ChallengeService()
        {
            db.Dispose();
        }
    }
}