using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class FilterViewModel
    {
        public int Size { get; set; }
        public string Email { get; set; }
        public int CategoryUnitId { get; set; }

        [Display(Name = "FromDate", ResourceType = typeof(Resources.Resources))]
        [DataType(DataType.Date)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:yyyy-MM-dd}")]
        public DateTime? FromDate { get; set; }

        [Display(Name = "ToDate", ResourceType = typeof(Resources.Resources))]
        [DataType(DataType.Date)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:yyyy-MM-dd}")]
        public DateTime? ToDate { get; set; }
    }
}