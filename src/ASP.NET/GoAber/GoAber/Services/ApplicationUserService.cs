using System;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.UI;
using GoAber.Areas.MyAccount.Models;
using GoAber.Controllers;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using GoAber.Models;
using GoAber.Services;

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

        // CG - This could potentially be static?
        public static string getNickname(string userId)
        {
            ApplicationDbContext db = new ApplicationDbContext();
            ApplicationUser user = db.Users.Where(u => u.Id.Equals(userId)).FirstOrDefault();

            if (user != null)
            {
                return user.Nickname;
            }

            return null;
        }


    }
}