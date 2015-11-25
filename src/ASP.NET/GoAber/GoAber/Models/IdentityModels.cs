using System;
using System.ComponentModel.DataAnnotations;
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

        [Display(Name = "Nickname")]
        public string Nickname { get; set; }

        [Display(Name = "Date of Birth")]
        [DataType(DataType.Date)]
        // CG - In order for browser-rendered HTML5 date selectors to work properly, we need to use a date format that conforms with RFC-3339.
        // See: http://stackoverflow.com/a/12634470 for more information.
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateTime DateOfBirth { get; set; }

        [Display(Name = "Team")]
        public int? TeamId { get; set; }
        public virtual Team Team { get; set; }

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
        public DbSet<Category> Categories { get; set; }
        public DbSet<Challenge> Challenges { get; set; }
        public DbSet<Community> Communities { get; set; }
        public DbSet<Device> Devices { get; set; }
        public DbSet<DeviceType> DeviceTypes { get; set; }
        public DbSet<Team> Teams { get; set; }
        public DbSet<GroupChallenge> GroupChallenges { get; set; }
        public DbSet<Unit> Units { get; set; }
        public DbSet<DataRemovalAudit> DataRemovalAudits { get; set; }
        public DbSet<UserChallenge> UserChallenges { get; set; }
        public DbSet<Job> Jobs { get; set; }

        //public System.Data.Entity.DbSet<GoAber.Models.ApplicationUser> ApplicationUsers { get; set; }


       // public System.Data.Entity.DbSet<GoAber.Models.Job> Jobs { get; set; }

        public DbSet<Audit> Audit { get; set; }

        //public System.Data.Entity.DbSet<GoAber.Models.ApplicationUser> ApplicationUsers { get; set; }
        //public GoAber.Models.Team Group { get; set; }


        public System.Data.Entity.DbSet<GoAber.Models.WebServiceAuth> WebServiceAuths { get; set; }
    }
}