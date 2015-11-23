using System;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;
using System.Web.UI;
using GoAber.Areas.MyAccount.Models;
using GoAber.Controllers;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using GoAber.Models;
using GoAber.Services;
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

        /// <summary>
        /// User manager - attached to application DB context
        /// </summary>
        protected UserManager<ApplicationUser> UserManager { get; set; }

        public ApplicationUserService()
        {
            this.UserManager = new UserManager<ApplicationUser>(new UserStore<ApplicationUser>(this.db));
        }

        public string GetNicknameById(string userId)
        {
            ApplicationDbContext db = new ApplicationDbContext();
            ApplicationUser user = db.Users.FirstOrDefault(u => u.Id.Equals(userId));

            return user?.Nickname;
        }

        //public static string GetCurrentUserNickname()
        //{
        //    ApplicationDbContext db = new ApplicationDbContext();

        //    var userId = HttpContext.Current.User.Identity.GetUserId();

        //    ApplicationUser user = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>().FindById(userId);

        //    return user?.Nickname;
        //}

        public static ApplicationUser GetCurrentUser()
        {
            ApplicationDbContext db = new ApplicationDbContext();

            var userId = HttpContext.Current.User.Identity.GetUserId();

            ApplicationUser user = HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>().FindById(userId);

            return user;
        }

        public ApplicationUser GetUserById(string userId)
        {

            ApplicationUser user = db.Users.FirstOrDefault(u => u.Id.Equals(userId));
            return user;

        }

        public ApplicationUser GetCurrentApplicationUser()
        {

            var userId = HttpContext.Current.User.Identity.GetUserId();

            ApplicationUser user = System.Web.HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>().FindById(userId);

            return user;
            
        }


    }
}