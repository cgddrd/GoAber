using GoAber.Models;
using GoAber.Models.ViewModels;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace GoAber.Services
{
    /// <summary>
    /// CategoryUnitService
    /// 
    /// Provides covinence methods used by multiple controllers who wish to access 
    /// Category and Unit information in a sensible way.
    /// </summary>
    public class CategoryUnitService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public IEnumerable<CategoryViewModel> GetAllCategories ()
        {
            var categories = db.CategoryUnits
                                .OrderBy(c => c.category.name)
                                .Select(c => c.category.name)
                                .Distinct();

            IEnumerable<CategoryViewModel> categoryUnits = categories.Select(c => new CategoryViewModel
            {
                name = c,
                units = db.CategoryUnits
                            .Where(u => u.category.name == c)
                            .OrderBy(u => u.unit.name)
                            .Select(u => u.unit)
                            .Distinct()
            }).ToList();

            return categoryUnits;
        }
       

        public IEnumerable CreateCategoryUnitList()
        {
            var categories = db.CategoryUnits.Select(c => new
            {
                idCategoryUnit = c.Id,
                category = c.category.name,
                unit = c.unit.name
            }).ToList();

            return categories;
        }
    }
}