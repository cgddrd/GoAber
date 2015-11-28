using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class FilterViewModel
    {
        public int Size { get; set; }
        public string Email { get; set; }
        public int CategoryUnitId { get; set; }
        public DateTime? FromDate { get; set; }
        public DateTime? ToDate { get; set; }
    }
}