using GoAber.Services;
using PagedList;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Controllers
{
    public class LeaderBoardsController : Controller
    {
        private CategoryUnitService categoryUnitService = new CategoryUnitService();
        private UserService userService = new UserService();
        private const int pageSize = 100;


        //GET: LeaderBoards/Index
        public ActionResult Index()
        {
            return View(categoryUnitService.GetAllCategories());
        }

        // GET: LeaderBoards/ParticipantLeaderBoard
        public ActionResult ParticipantLeaderBoard(int? page, int? unit)
        {
            if (unit.HasValue)
            {
                int pageNumber = (page ?? 1);
                var sortedUserViews = userService.GetSortedUsersForUnit(unit.Value);
                return View(sortedUserViews.ToPagedList(pageNumber, pageSize));
            }
            else
            {
                return HttpNotFound();
            }
        }


        // GET: LeaderBoards/GroupLeaderBoard
        public ActionResult GroupLeaderBoard(int? page, int? unit)
        {
            if (unit.HasValue)
            {
                int pageNumber = (page ?? 1);
                var sortedGroupViews = userService.GetSortedGroupsForUnit(unit.Value);
                return View(sortedGroupViews.ToPagedList(pageNumber, pageSize));
            }
            else
            {
                return HttpNotFound();
            }
        }
    }
}