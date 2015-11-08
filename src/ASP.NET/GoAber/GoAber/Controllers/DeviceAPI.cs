using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoAber.Controllers
{
    interface DeviceAPI
    {
        ActivityData getDayActivities(string ls_path, int userID, int day, int month, int year);
        ActivityData getDayHeart(string ls_path, int userID, int day, int month, int year);
    }
}
