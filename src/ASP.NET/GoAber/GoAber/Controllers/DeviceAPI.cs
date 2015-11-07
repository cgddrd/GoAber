using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.Controllers
{
    interface DeviceAPI
    {
        activitydata getDayActivities(string ls_path, int userID, int day, int month, int year);
        activitydata getDayHeart(string ls_path, int userID, int day, int month, int year);
    }
}
