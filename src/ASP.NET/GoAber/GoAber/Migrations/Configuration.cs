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

            // This method will be called after migrating to the latest version.
            // CG - Create the user roles, then some test users.

            var roleStore = new RoleStore<IdentityRole>(context);
            var roleManager = new RoleManager<IdentityRole>(roleStore);

            var userStore = new UserStore<ApplicationUser>(context);
            var userManager = new UserManager<ApplicationUser>(userStore);

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
            if (!context.Users.Any(u => u.UserName == "admin@aber.ac.uk"))
            {
                var adminUser = new ApplicationUser
                {
                    // CG - NOTE: Username and Email properties must be currently set to the same value.
                    UserName = "admin@aber.ac.uk",
                    Email = "admin@aber.ac.uk",
                    Nickname = "adminuser",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(adminUser, "Juddy123!");
                userManager.AddToRole(adminUser.Id, "Administrator");

            }

            if (!context.Users.Any(u => u.UserName == "coord@aber.ac.uk"))
            {
                var coordUser = new ApplicationUser
                {
                    UserName = "coord@aber.ac.uk",
                    Email = "coord@aber.ac.uk",
                    Nickname = "coorduser",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(coordUser, "Juddy123!");
                userManager.AddToRole(coordUser.Id, "Coordinator");
            }

            if (!context.Users.Any(u => u.UserName == "user1@aber.ac.uk"))
            {
                var normalUser = new ApplicationUser
                {
                    UserName = "user1@aber.ac.uk",
                    Email = "user1@aber.ac.uk",
                    Nickname = "user1user",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(normalUser, "Juddy123!");
                userManager.AddToRole(normalUser.Id, "Participant");
            }

            context.CategoryUnits.AddOrUpdate(x => x.Id,
                new CategoryUnit() { category = new Category() { name = "Walking" }, unit = new Unit() {name = "Steps"} }
                );

            context.DeviceTypes.AddOrUpdate(x => x.Id,
                new DeviceType() { name = "fitbit", tokenEndpoint = @"https://api.fitbit.com/oauth2/token", consumerKey = "e06d4e7dcbc6fc80c0d00b187b6fb2e1", consumerSecret = "bafe21eca0c10cfe54f21e9b685f041f", clientId = "229R69" , authorizationEndpoint = @"https://www.fitbit.com/oauth2/authorize" }
            );


            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //
        }
    }
}
