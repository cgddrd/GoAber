using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class ActivityDataListViewModel
    {
        public IEnumerable<ActivityData> Data { get; set; }
        public FilterViewModel FilterParams { get; set; }
    }
}