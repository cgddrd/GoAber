using System.Collections.Generic;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;

namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<ApplicationDbContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(ApplicationDbContext context)
        {

          //  if (System.Diagnostics.Debugger.IsAttached == false)
           //     System.Diagnostics.Debugger.Launch();

            // This method will be called after migrating to the latest version.
            // CG - Create the user roles, then some test users.

            var roleStore = new RoleStore<IdentityRole>(context);
            var roleManager = new RoleManager<IdentityRole>(roleStore);

            var userStore = new UserStore<ApplicationUser>(context);
            var userManager = new UserManager<ApplicationUser>(userStore);

            context.Communities.AddOrUpdate(x => x.Id,
               new Community { name = "AberUni", domain = "http://localhost:50121/", challengesEndpoint = "WebService/ChallengesWS/GoAberChallengesWS.asmx", home = true}
            );

            context.Communities.AddOrUpdate(x => x.Id,
              new Community { name = "BangorUni", domain = "http://localhost:8080/", challengesEndpoint = "GoAberChallengesWS/GoAberChallengesWS", home = false }
            );

            context.SaveChanges();

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "AberCompSci", community = context.Communities.Where(b => b.name == "AberUni").FirstOrDefault() }
            );

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "AberIBERS", community = context.Communities.Where(b => b.name == "AberUni").FirstOrDefault() }
            );

            context.SaveChanges();

            if (!context.Roles.Any(r => r.Name == "Administrator"))
            {
                var adminRole = new IdentityRole { Name = "Administrator" };
                roleManager.Create(adminRole);
            }

            if (!context.Roles.Any(r => r.Name == "Coordinator"))
            {
                var coordRole = new IdentityRole { Name = "Coordinator" };
                roleManager.Create(coordRole);
            }

            if (!context.Roles.Any(r => r.Name == "Participant"))
            {
                var participantRole = new IdentityRole { Name = "Participant" };
                roleManager.Create(participantRole);
            }

            //CG - Now create the default/test users.
            if (!context.Users.Any(u => u.UserName == "admin@test.com"))
            {
                var adminUser = new ApplicationUser
                {
                    // CG - NOTE: Username and Email properties must be currently set to the same value.
                    UserName = "admin@test.com",
                    Email = "admin@test.com",
                    Nickname = "adminuser",
                    DateOfBirth = DateTime.Now,
                    Team = context.Teams.Where(b => b.name == "AberIBERS").FirstOrDefault()
            };

                userManager.Create(adminUser, "Hello123!");
                userManager.AddToRole(adminUser.Id, "Administrator");

            }

            if (!context.Users.Any(u => u.UserName == "coord@test.com"))
            {
                var coordUser = new ApplicationUser
                {
                    UserName = "coord@test.com",
                    Email = "coord@test.com",
                    Nickname = "coorduser",
                    DateOfBirth = DateTime.Now,
                    Team = context.Teams.Where(b => b.name == "AberCompSci").FirstOrDefault()
                };

                userManager.Create(coordUser, "Hello123!");
                userManager.AddToRole(coordUser.Id, "Coordinator");
            }

            if (!context.Users.Any(u => u.UserName == "user1@test.com"))
            {
                var normalUser = new ApplicationUser
                {
                    UserName = "user1@test.com",
                    Email = "user1@test.com",
                    Nickname = "user1user",
                    DateOfBirth = DateTime.Now,
                    Team = context.Teams.Where(b => b.name == "AberCompSci").FirstOrDefault()
                };

                userManager.Create(normalUser, "Hello123!");
                userManager.AddToRole(normalUser.Id, "Participant");
            }


            CategoryUnit walking = new CategoryUnit()
            {
                category = new Category() { name = "Walking" },
                unit = new Unit() { name = "Steps" }
            };



            CategoryUnit heartbeat = new CategoryUnit() {
                category = new Category() { name = "HeartRate" },
                unit = new Unit() { name = "Beats" }
            };

            context.CategoryUnits.AddOrUpdate(x => x.Id, walking);
            context.CategoryUnits.AddOrUpdate(x => x.Id, heartbeat);

            context.DeviceTypes.AddOrUpdate(x => x.Id,
                new DeviceType() { name = "Fitbit", tokenEndpoint = @"https://api.fitbit.com/oauth2/token", consumerKey = "e06d4e7dcbc6fc80c0d00b187b6fb2e1", consumerSecret = "bafe21eca0c10cfe54f21e9b685f041f", clientId = "229R69" , authorizationEndpoint = @"https://www.fitbit.com/oauth2/authorize", apiEndpoint = @"https://api.fitbit.com/1/user/-" }
            );


            context.DeviceTypes.AddOrUpdate(x => x.Id,
                new DeviceType() { name = "Jawbone", tokenEndpoint = @"https://jawbone.com/auth/oauth2/token", consumerSecret = "f0ca3e7da09288d18bc5b4053704f1a3e43d22da", clientId = "2mcFGghH9so",  authorizationEndpoint = @"https://jawbone.com/auth/oauth2/auth", apiEndpoint= @"https://jawbone.com/nudge/api/v.1.1/users/@me" }
            );

            var user = context.Users.Where(x => x.Email == "admin@test.com").SingleOrDefault();
            Random rnd = new Random();
            context.ActivityDatas.AddOrUpdate(x => x.Id,
                new ActivityData { date = DateTime.Today, lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user},
                new ActivityData { date = DateTime.Today.AddDays(-1), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-2), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-3), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-4), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-5), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-6), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-7), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-8), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = walking, User = user }
            );

            context.ActivityDatas.AddOrUpdate(x => x.Id,
                new ActivityData { date = DateTime.Today, lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-1), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-2), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-3), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-4), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-5), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-6), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-7), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user },
                new ActivityData { date = DateTime.Today.AddDays(-8), lastUpdated = DateTime.Today, value = rnd.Next(0, 1000), categoryunit = heartbeat, User = user }
            );

            user = context.Users.Where(x => x.Email == "admin@test.com").SingleOrDefault();
            context.WebServiceAuths.AddOrUpdate(
                new WebServiceAuth { authtoken = "admin_token", appname = "TestApp", ApplicationUserId = user.Id }
            );
            user = context.Users.Where(x => x.Email == "user1@test.com").SingleOrDefault();
            context.WebServiceAuths.AddOrUpdate(
                new WebServiceAuth { authtoken = "user_token", appname = "TestApp", ApplicationUserId = user.Id }
            );

        }
    }
}
