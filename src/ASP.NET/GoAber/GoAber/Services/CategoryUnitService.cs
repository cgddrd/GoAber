using GoAber.Models;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Web;

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