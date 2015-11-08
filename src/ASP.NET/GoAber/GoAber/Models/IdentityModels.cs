using System.Data.Entity;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;

namespace GoAber.Models
{
    // You can add profile data for the user by adding more properties to your ApplicationUser class, please visit http://go.microsoft.com/fwlink/?LinkID=317594 to learn more.
    public class ApplicationUser : IdentityUser
    {
        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<ApplicationUser> manager)
        {
            // Note the authenticationType must match the one defined in CookieAuthenticationOptions.AuthenticationType
            var userIdentity = await manager.CreateIdentityAsync(this, DefaultAuthenticationTypes.ApplicationCookie);
            // Add custom user claims here
            return userIdentity;
        }
    }

    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
         
        /*static ApplicationDbContext() {
            Database.SetInitializer(new MySqlInitializer());
        }*/

        public ApplicationDbContext()
            : base("DefaultConnection", throwIfV1Schema: false)
        {
        }

        public static ApplicationDbContext Create()
        {
            return new ApplicationDbContext();
        }

        public DbSet<ActivityData> ActivityDatas { get; set; }

        public DbSet<CategoryUnit> CategoryUnits { get; set; }

        public DbSet<UserRole> UserRoles { get; set; }

        public DbSet<User> Users1 { get; set; }

        public DbSet<Category> Categories { get; set; }
        public DbSet<Challenge> Challenges { get; set; }
        public DbSet<Community> Communities { get; set; }
        public DbSet<Device> Devices { get; set; }
        public DbSet<DeviceType> DeviceTypes { get; set; }
        public DbSet<Team> Groups { get; set; }
        public DbSet<GroupChallenge> GroupChallenges { get; set; }
        public DbSet<Unit> Units { get; set; }
        public DbSet<UserChallenge> UserChallenges { get; set; }
        public DbSet<UserCredential> UserCredentials { get; set; }
    }
}