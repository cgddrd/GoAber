using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;

namespace GoAber.Migrations
{
    using System;
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
            //  This method will be called after migrating to the latest version.

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
            if (!context.Users.Any(u => u.UserName == "admin"))
            {
                var adminUser = new ApplicationUser
                {
                    UserName = "admin",
                    Email = "admin@test.com",
                    Nickname = "adminuser",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(adminUser, "Juddy123!");
                userManager.AddToRole(adminUser.Id, "Administrator");
            }

            if (!context.Users.Any(u => u.UserName == "coord"))
            {
                var coordUser = new ApplicationUser
                {
                    UserName = "coord",
                    Email = "coord@test.com",
                    Nickname = "coorduser",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(coordUser, "Juddy123!");
                userManager.AddToRole(coordUser.Id, "Coordinator");
            }

            if (!context.Users.Any(u => u.UserName == "user1"))
            {
                var coordUser = new ApplicationUser
                {
                    UserName = "user1",
                    Email = "user1@test.com",
                    Nickname = "user1user",
                    DateOfBirth = DateTime.Now
                };

                userManager.Create(coordUser, "Juddy123!");
                userManager.AddToRole(coordUser.Id, "Participant");
            }

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
