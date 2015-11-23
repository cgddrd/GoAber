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

    internal sealed class Configuration : DbMigrationsConfiguration<GoAber.Models.ApplicationDbContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(GoAber.Models.ApplicationDbContext context)
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
               new Community { name = "AberUni", endpointUrl = "http://aber.ac.uk"}
            );

            context.Communities.AddOrUpdate(x => x.Id,
              new Community { name = "BangorUni", endpointUrl = "http://bangor.ac.uk" }
            );

            context.SaveChanges();

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "AberCompSci", community = context.Communities.Where(b => b.name == "AberUni").FirstOrDefault() }
            );

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "AberIBERS", community = context.Communities.Where(b => b.name == "AberUni").FirstOrDefault() }
            );

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "BangorGroup1", community = context.Communities.Where(b => b.name == "BangorUni").FirstOrDefault() }
            );

            context.Teams.AddOrUpdate(x => x.Id,
                new Team { name = "BangorGroup2", community = context.Communities.Where(b => b.name == "BangorUni").FirstOrDefault() }
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
                    Team = context.Teams.Where(b => b.name == "BangorGroup1").FirstOrDefault()
                };

                userManager.Create(normalUser, "Hello123!");
                userManager.AddToRole(normalUser.Id, "Participant");
            }

            context.CategoryUnits.AddOrUpdate(x => x.Id,
                new CategoryUnit() { category = new Category() { name = "Walking" }, unit = new Unit() {name = "Steps"} }
                );

            context.CategoryUnits.AddOrUpdate(x => x.Id,
                new CategoryUnit() { category = new Category() { name = "HeartRate" }, unit = new Unit() { name = "Beats" } }
                );

            context.DeviceTypes.AddOrUpdate(x => x.Id,
                new DeviceType() { name = "Fitbit", tokenEndpoint = @"https://api.fitbit.com/oauth2/token", consumerKey = "e06d4e7dcbc6fc80c0d00b187b6fb2e1", consumerSecret = "bafe21eca0c10cfe54f21e9b685f041f", clientId = "229R69" , authorizationEndpoint = @"https://www.fitbit.com/oauth2/authorize", apiEndpoint = @"https://api.fitbit.com/1/user/-" }
            );


            context.DeviceTypes.AddOrUpdate(x => x.Id,
                new DeviceType() { name = "Jawbone", tokenEndpoint = @"https://jawbone.com/auth/oauth2/token", consumerSecret = "f0ca3e7da09288d18bc5b4053704f1a3e43d22da", clientId = "2mcFGghH9so",  authorizationEndpoint = @"https://jawbone.com/auth/oauth2/auth", apiEndpoint= @"https://jawbone.com/nudge/api/v.1.1/users/@me" }
            );

        }
    }
}
