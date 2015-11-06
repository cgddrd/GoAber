namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Init : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.ActivityDatas",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        categoryUnitId = c.Int(nullable: false),
                        userId = c.Int(nullable: false),
                        value = c.Int(),
                        lastUpdated = c.DateTime(),
                        date = c.DateTime(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.CategoryUnits", t => t.categoryUnitId, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.userId, cascadeDelete: true)
                .Index(t => t.categoryUnitId)
                .Index(t => t.userId);
            
            CreateTable(
                "dbo.CategoryUnits",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        categoryId = c.Int(nullable: false),
                        unitId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Categories", t => t.categoryId, cascadeDelete: true)
                .ForeignKey("dbo.Units", t => t.unitId, cascadeDelete: true)
                .Index(t => t.categoryId)
                .Index(t => t.unitId);
            
            CreateTable(
                "dbo.Categories",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Units",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        email = c.String(),
                        nickname = c.String(),
                        userRoleId = c.Int(nullable: false),
                        userCredentialsId = c.Int(),
                        groupId = c.Int(),
                        usercredential_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Teams", t => t.groupId)
                .ForeignKey("dbo.UserCredentials", t => t.usercredential_Id)
                .ForeignKey("dbo.UserRoles", t => t.userRoleId, cascadeDelete: true)
                .Index(t => t.userRoleId)
                .Index(t => t.groupId)
                .Index(t => t.usercredential_Id);
            
            CreateTable(
                "dbo.Teams",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                        communityId = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Communities", t => t.communityId)
                .Index(t => t.communityId);
            
            CreateTable(
                "dbo.Communities",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                        endpointUrl = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Challenges",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        categoryUnit = c.Int(nullable: false),
                        startTime = c.DateTime(nullable: false),
                        endTime = c.DateTime(),
                        name = c.String(),
                        communityStartedBy = c.Int(),
                        community_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Communities", t => t.community_Id)
                .Index(t => t.community_Id);
            
            CreateTable(
                "dbo.GroupChallenges",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        groupId = c.Int(nullable: false),
                        challengeId = c.Int(nullable: false),
                        team_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Challenges", t => t.challengeId, cascadeDelete: true)
                .ForeignKey("dbo.Teams", t => t.team_Id)
                .Index(t => t.challengeId)
                .Index(t => t.team_Id);
            
            CreateTable(
                "dbo.UserChallenges",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        challengeId = c.Int(nullable: false),
                        userId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Challenges", t => t.challengeId, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.userId, cascadeDelete: true)
                .Index(t => t.challengeId)
                .Index(t => t.userId);
            
            CreateTable(
                "dbo.UserCredentials",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        password = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.UserRoles",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        type = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.AspNetRoles",
                c => new
                    {
                        Id = c.String(nullable: false, maxLength: 128),
                        Name = c.String(nullable: false, maxLength: 256),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.Name, unique: true, name: "RoleNameIndex");
            
            CreateTable(
                "dbo.AspNetUserRoles",
                c => new
                    {
                        UserId = c.String(nullable: false, maxLength: 128),
                        RoleId = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => new { t.UserId, t.RoleId })
                .ForeignKey("dbo.AspNetRoles", t => t.RoleId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId)
                .Index(t => t.RoleId);
            
            CreateTable(
                "dbo.AspNetUsers",
                c => new
                    {
                        Id = c.String(nullable: false, maxLength: 128),
                        Nickname = c.String(),
                        DateOfBirth = c.DateTime(),
                        Email = c.String(maxLength: 256),
                        EmailConfirmed = c.Boolean(nullable: false),
                        PasswordHash = c.String(),
                        SecurityStamp = c.String(),
                        PhoneNumber = c.String(),
                        PhoneNumberConfirmed = c.Boolean(nullable: false),
                        TwoFactorEnabled = c.Boolean(nullable: false),
                        LockoutEndDateUtc = c.DateTime(),
                        LockoutEnabled = c.Boolean(nullable: false),
                        AccessFailedCount = c.Int(nullable: false),
                        UserName = c.String(nullable: false, maxLength: 256),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.UserName, unique: true, name: "UserNameIndex");
            
            CreateTable(
                "dbo.AspNetUserClaims",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.String(nullable: false, maxLength: 128),
                        ClaimType = c.String(),
                        ClaimValue = c.String(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
            CreateTable(
                "dbo.AspNetUserLogins",
                c => new
                    {
                        LoginProvider = c.String(nullable: false, maxLength: 128),
                        ProviderKey = c.String(nullable: false, maxLength: 128),
                        UserId = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => new { t.LoginProvider, t.ProviderKey, t.UserId })
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .Index(t => t.UserId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.AspNetUserRoles", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserLogins", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserClaims", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserRoles", "RoleId", "dbo.AspNetRoles");
            DropForeignKey("dbo.Users", "userRoleId", "dbo.UserRoles");
            DropForeignKey("dbo.Users", "usercredential_Id", "dbo.UserCredentials");
            DropForeignKey("dbo.Users", "groupId", "dbo.Teams");
            DropForeignKey("dbo.Teams", "communityId", "dbo.Communities");
            DropForeignKey("dbo.UserChallenges", "userId", "dbo.Users");
            DropForeignKey("dbo.UserChallenges", "challengeId", "dbo.Challenges");
            DropForeignKey("dbo.GroupChallenges", "team_Id", "dbo.Teams");
            DropForeignKey("dbo.GroupChallenges", "challengeId", "dbo.Challenges");
            DropForeignKey("dbo.Challenges", "community_Id", "dbo.Communities");
            DropForeignKey("dbo.ActivityDatas", "userId", "dbo.Users");
            DropForeignKey("dbo.CategoryUnits", "unitId", "dbo.Units");
            DropForeignKey("dbo.CategoryUnits", "categoryId", "dbo.Categories");
            DropForeignKey("dbo.ActivityDatas", "categoryUnitId", "dbo.CategoryUnits");
            DropIndex("dbo.AspNetUserLogins", new[] { "UserId" });
            DropIndex("dbo.AspNetUserClaims", new[] { "UserId" });
            DropIndex("dbo.AspNetUsers", "UserNameIndex");
            DropIndex("dbo.AspNetUserRoles", new[] { "RoleId" });
            DropIndex("dbo.AspNetUserRoles", new[] { "UserId" });
            DropIndex("dbo.AspNetRoles", "RoleNameIndex");
            DropIndex("dbo.UserChallenges", new[] { "userId" });
            DropIndex("dbo.UserChallenges", new[] { "challengeId" });
            DropIndex("dbo.GroupChallenges", new[] { "team_Id" });
            DropIndex("dbo.GroupChallenges", new[] { "challengeId" });
            DropIndex("dbo.Challenges", new[] { "community_Id" });
            DropIndex("dbo.Teams", new[] { "communityId" });
            DropIndex("dbo.Users", new[] { "usercredential_Id" });
            DropIndex("dbo.Users", new[] { "groupId" });
            DropIndex("dbo.Users", new[] { "userRoleId" });
            DropIndex("dbo.CategoryUnits", new[] { "unitId" });
            DropIndex("dbo.CategoryUnits", new[] { "categoryId" });
            DropIndex("dbo.ActivityDatas", new[] { "userId" });
            DropIndex("dbo.ActivityDatas", new[] { "categoryUnitId" });
            DropTable("dbo.AspNetUserLogins");
            DropTable("dbo.AspNetUserClaims");
            DropTable("dbo.AspNetUsers");
            DropTable("dbo.AspNetUserRoles");
            DropTable("dbo.AspNetRoles");
            DropTable("dbo.UserRoles");
            DropTable("dbo.UserCredentials");
            DropTable("dbo.UserChallenges");
            DropTable("dbo.GroupChallenges");
            DropTable("dbo.Challenges");
            DropTable("dbo.Communities");
            DropTable("dbo.Teams");
            DropTable("dbo.Users");
            DropTable("dbo.Units");
            DropTable("dbo.Categories");
            DropTable("dbo.CategoryUnits");
            DropTable("dbo.ActivityDatas");
        }
    }
}
