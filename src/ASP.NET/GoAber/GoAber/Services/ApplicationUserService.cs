using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Security.Claims;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using System.Web.Security;
using GoAber.Controllers;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using GoAber.Models;
using Microsoft.AspNet.Identity.EntityFramework;

namespace GoAber.Services
{

    /// <summary>
    /// ApplicationUserService
    /// 
    /// Provides convenience methods used by multiple controllers who wish to access 
    /// ApplicationUser data via a consistent approach.
    /// </summary>
    public class ApplicationUserService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public static ApplicationUser GetCurrentApplicationUser()
        {

            ApplicationDbContext db = new ApplicationDbContext();

            var userId = HttpContext.Current.User.Identity.GetUserId();

            ApplicationUser user = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>().FindById(userId);

            return user;
        }

        public static bool IsApplicationUserIdLoggedIn(string applicationUserId)
        {
            return GetCurrentApplicationUser().Id.Equals(applicationUserId);
        }

        public static bool IsCurrentApplicationUserInRole(string roleName)
        {
            return HttpContext.Current.User.IsInRole(roleName);
        }

        public static string GetAssignedRoleForApplicationUser(string applicationUserId)
        {
            return GetRolesForApplicationUser(applicationUserId).FirstOrDefault();
        }

        private static IList<string> GetRolesForApplicationUser(string applicationUserId)
        {
            var userManager = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();

            return userManager.GetRoles(applicationUserId);
        }

        public static void SetRoleToApplicationUser(string applicationUserId, string roleName)
        {
            var userManager = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();

            userManager.RemoveFromRoles(applicationUserId, GetRolesForApplicationUser(applicationUserId).ToArray());

            userManager.AddToRole(applicationUserId, roleName);

        }

        public static ActionResult LogoutCurrentApplicationUser()
        {
            // CG - Log the current user out.
            HttpContext.Current.GetOwinContext().Authentication.SignOut();

            // CG - As we are not extending from the 'BaseController' class, we must use this approach to return a 'redirect' ActionResult.
            // See: http://stackoverflow.com/a/6900386 for more information.
            return new RedirectToRouteResult(new RouteValueDictionary(new {action = "Index", controller = "Home", area = ""}));
        }

        public static ApplicationUser GetApplicationUserById(string userId)
        {

            ApplicationDbContext db = new ApplicationDbContext();

            ApplicationUser user = db.Users.FirstOrDefault(u => u.Id.Equals(userId));

            return user;
            
        }

        public IEnumerable<ApplicationUser> GetAllApplicationUsers()
        { 
            return db.Users.Include(u => u.Team).OrderBy(u => u.Email);
        }

        public bool DeleteCurrentApplicationUser()
        {
            return this.DeleteApplicationUser(GetCurrentApplicationUser().Id);
        }

        public bool DeleteApplicationUser(string applicationUserId)
        {
            try
            {
                ApplicationUser applicationUser = db.Users.Find(applicationUserId);

                // CG - If we want to delete an ApplicationUser, we need to make sure that we remove all of the FK relationships.
                if (applicationUser != null)
                {
                    db.ActivityDatas.RemoveRange(
                        db.ActivityDatas.Where(u => u.ApplicationUserId.Equals(applicationUser.Id)));

                    db.Audit.RemoveRange(db.Audit.Where(u => u.ApplicationUserId.Equals(applicationUser.Id)));

                    db.DataRemovalAudits.RemoveRange(
                        db.DataRemovalAudits.Where(u => u.ApplicationUserId.Equals(applicationUser.Id)));

                    db.WebServiceAuths.RemoveRange(
                        db.WebServiceAuths.Where(u => u.ApplicationUserId.Equals(applicationUser.Id)));

                    db.Users.Remove(applicationUser);

                    db.SaveChanges();

                    return true;
                }

            }
            catch (Exception ex)
            {
               System.Diagnostics.Debug.WriteLine("Error deleting user."); 
            }

            return false;
        }

        public IEnumerable GenerateApplicationUserList()
        {
            var applicationUsers = db.Users.Select(u => new
            {
                Id = u.Id,
                Email = u.Email,
                Nickname = u.Nickname,
                TeamId = u.Team.Id, 
                TeamName = u.Team.name

            }).ToList();

            return applicationUsers;
        }

        public IEnumerable GenerateApplicationUserRoleList()
        {

            var roleManager = new RoleManager<IdentityRole>(new RoleStore<IdentityRole>(new ApplicationDbContext()));

            var roles = roleManager.Roles.Select(t => new
            {
                Id = t.Id,
                RoleName = t.Name
            }
            ).ToList();

            return roles;

        }

        ~ApplicationUserService()
        {
            this.db.Dispose();
        }

    }
}