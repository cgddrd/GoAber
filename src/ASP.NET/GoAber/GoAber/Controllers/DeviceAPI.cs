using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.Controllers
{
    interface DeviceAPI
    {
        ActivityData GetDayActivities(string ls_path, int userID, int day, int month, int year);
        ActivityData GetDayHeart(string ls_path, int userID, int day, int month, int year);
    }
}
