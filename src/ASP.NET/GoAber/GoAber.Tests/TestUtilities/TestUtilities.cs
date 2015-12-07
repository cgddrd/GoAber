using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Moq;

namespace GoAber.Tests.TestUtilities
{
    static class TestUtilities
    {

        public static DbSet<T> GetQueryableMockDbSet<T>(List<T> sourceList, Func<object[], T, bool> finder) where T : class
        {
            var queryable = sourceList.AsQueryable();

            var dbSet = new Mock<DbSet<T>>();

            dbSet.As<IQueryable<T>>().Setup(m => m.Provider).Returns(queryable.Provider);
            dbSet.As<IQueryable<T>>().Setup(m => m.Expression).Returns(queryable.Expression);
            dbSet.As<IQueryable<T>>().Setup(m => m.ElementType).Returns(queryable.ElementType);
            dbSet.As<IQueryable<T>>().Setup(m => m.GetEnumerator()).Returns(() => queryable.GetEnumerator());

            dbSet.Setup(d => d.Add(It.IsAny<T>())).Callback<T>((s) => sourceList.Add(s));

            dbSet.Setup(set => set.Remove(It.IsAny<T>())).Callback<T>(t => sourceList.Remove(t));
            dbSet.Setup(set => set.RemoveRange(It.IsAny<IEnumerable<T>>())).Callback<IEnumerable<T>>(ts =>
            {
                foreach (var t in ts) { sourceList.Remove(t); }
            });

            dbSet.Setup(s => s.Include(It.IsAny<string>())).Returns(dbSet.Object);
            dbSet.Setup(s => s.Find(It.IsAny<object[]>())).Returns((object[] keyValues) => dbSet.Object.SingleOrDefault(e => finder(keyValues, e)));

            return dbSet.Object;
        }

    }
}
