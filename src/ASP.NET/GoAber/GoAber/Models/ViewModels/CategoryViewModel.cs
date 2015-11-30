using System.Collections.Generic;

namespace GoAber.Models.ViewModels
{
    public class CategoryViewModel
    {
        public string name { get; set; }
        public IEnumerable<Unit> units { get; set; }
    }
}