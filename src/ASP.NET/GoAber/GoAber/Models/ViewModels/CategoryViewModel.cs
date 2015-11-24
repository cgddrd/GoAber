using System.Collections.Generic;

namespace GoAber.Models.ViewModels
{
    public class CategoryViewModel
    {
        public string name { get; set; }
        public IEnumerable<string> units { get; set; }
    }
}