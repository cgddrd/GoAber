
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Threading.Tasks;
using System.Web.Mvc;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using GoAber.Models;
using GoAber.Controllers;
using System.Web.Services.Protocols;
using GoAber.WebService;
using Microsoft.AspNet.Identity.EntityFramework;
using System.Xml.Serialization;

namespace GoAber
{
    /// <summary>
    /// Summary description for GoAberWS
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]
    public class GoAberWS : System.Web.Services.WebService
    {
        public AuthHeader Authentication;
        protected ApplicationDbContext io_db;
        protected AccountController io_ac;

        public GoAberWS()
        {
            io_db = new ApplicationDbContext();
            io_ac = new AccountController();
        }


        [SoapHeader("Authentication")]
        [WebMethod]
        [XmlInclude(typeof(WebService.ActivityData))]
        public bool AddActivityData(List<WebService.ActivityData> data)
        {
            WebServiceAuth lo_wsa = Authenticate();
            if (lo_wsa == null)
            {
                return false;
            }


            for (int i = 0; i < data.Count; i++)
            {
                Models.ActivityData lo_accmodel = new Models.ActivityData();
                CategoryUnit lo_cat = io_db.CategoryUnits.Find(new Object[] { data[i].categoryUnitId });

                lo_accmodel.ApplicationUserId = lo_wsa.ApplicationUserId;
                lo_accmodel.User = lo_wsa.user;
                lo_accmodel.categoryUnitId = data[i].categoryUnitId;
                lo_accmodel.categoryunit = lo_cat;
                lo_accmodel.date = data[i].date;
                lo_accmodel.lastUpdated = DateTime.Now;
                lo_accmodel.value = data[i].value;
                io_db.ActivityDatas.Add(lo_accmodel);
            }
            io_db.SaveChanges();
            return true;
        }

        //private bool IsAdmin(ApplicationUser ao_user)
        //{
        //    List<IdentityUserRole> lo_roles = ao_user.Roles.ToList();
        //    for (int i = 0; i < lo_roles.Count; i++)
        //    {
        //        var query = from r in io_db.Roles
        //                    where 
        //    }
        //}

        private WebServiceAuth Authenticate()
        {
            return io_db.WebServiceAuths.Find(new object[] { Authentication.authtoken });
        }

        private void Rollback()
        {
            io_db.Dispose();
            io_db = new ApplicationDbContext();
        }

      
    }
}
