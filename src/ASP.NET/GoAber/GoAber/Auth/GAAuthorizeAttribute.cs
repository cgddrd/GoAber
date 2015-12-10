using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Auth
{
    /// <summary>
    /// Custom override of the MVC 'Authorize' attribute implemented to enable a custom redirect following detection of unauthroised user.
    /// Author: Connor Goddard
    /// 
    /// Modified from original source: http://stackoverflow.com/a/7447785
    /// </summary>
    public class GAAuthorizeAttribute : AuthorizeAttribute
    {

        protected override void HandleUnauthorizedRequest(AuthorizationContext filterContext)
        {
            // Check that the user is authenticated.
            if (!filterContext.HttpContext.User.Identity.IsAuthenticated)
            {
                base.HandleUnauthorizedRequest(filterContext);
            }
            else if (!Roles.Split(',').Any(filterContext.HttpContext.User.IsInRole))
            {
                // If the user is not in one of the roles specified, redirect to the unauthorised error page.
                filterContext.Result = new ViewResult
                {
                    ViewName = "~/Views/Shared/Unauthorized.cshtml"
                };
            }
            else
            {
                base.HandleUnauthorizedRequest(filterContext);
            }
        }

    }
}