using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class ActivityDataStatisticsViewModel
    {
        public double Total { get; set; }
        public double Average { get; set; }
        public double Min { get; set; }
        public DateTime MinDate { get; set; }
        public double Max { get; set; }
        public DateTime MaxDate { get; set; }
    }
}