using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using Microsoft.Owin.Host.SystemWeb;

namespace GoAber
{
    public class AuditAttribute : ActionFilterAttribute
    {
        private ApplicationUserManager _userManager;
        protected ApplicationDbContext db = new ApplicationDbContext();

        public ApplicationUserManager UserManager
        {
            get
            {
                return _userManager ?? HttpContext.Current.GetOwinContext().GetUserManager<ApplicationUserManager>();
            }
            private set
            {
                _userManager = value;
            }
        }

        /// <summary>
        /// Saves a users actions to the database
        /// </summary>
        /// <param name="filterContext"></param>
        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            var request = filterContext.HttpContext.Request;

            ApplicationUser user = UserManager.FindById(HttpContext.Current.User.Identity.GetUserId());

            Audit audit = new Audit()
            {
                AuditId = Guid.NewGuid(),
                ApplicationUserId = user.Id,//(request.IsAuthenticated) ? filterContext.HttpContext.User.Identity.Name : "Anonymous",

                IpAddress = request.ServerVariables["HTTP_X_FORWARDED_FOR"] ?? request.UserHostAddress,

                RequestParams = request.Params.ToString(),
                UrlAccessed = request.RawUrl,
                Timestamp = DateTime.UtcNow
            };

            db.Audit.Add(audit);
            db.SaveChanges();

            base.OnActionExecuting(filterContext);
        }

    }

}