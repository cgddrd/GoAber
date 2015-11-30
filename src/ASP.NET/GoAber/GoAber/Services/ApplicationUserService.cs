using System.Collections;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
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

        public static ActionResult LogoutCurrentApplicationUser()
        {
            // CG - Log the current user out.
            HttpContext.Current.GetOwinContext().Authentication.SignOut();

            // CG - As we are not extending from the 'BaseController' class, we must use this approach to return a 'redirect' ActionResult.
            // See: http://stackoverflow.com/a/6900386 for more information.
            return new RedirectToRouteResult(new RouteValueDictionary(new {action = "Index", controller = "Home", area = ""}));
        }

        public ApplicationUser GetUserById(string userId)
        {

            ApplicationUser user = db.Users.FirstOrDefault(u => u.Id.Equals(userId));

            return user;

        }

        public IEnumerable<ApplicationUser> GetAllApplicationUsers()
        { 
            return db.Users.Include(u => u.Team).OrderBy(u => u.Email);
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

        ~ApplicationUserService()
        {
            this.db.Dispose();
        }

    }
}