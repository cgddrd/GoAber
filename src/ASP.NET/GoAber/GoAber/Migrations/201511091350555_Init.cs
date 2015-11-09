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
                        value = c.Int(),
                        lastUpdated = c.DateTime(),
                        date = c.DateTime(),
                        ApplicationUserId = c.String(maxLength: 128),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.CategoryUnits", t => t.categoryUnitId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetUsers", t => t.ApplicationUserId)
                .Index(t => t.categoryUnitId)
                .Index(t => t.ApplicationUserId);
            
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
                        Team_Id = c.Int(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Teams", t => t.Team_Id)
                .Index(t => t.UserName, unique: true, name: "UserNameIndex")
                .Index(t => t.Team_Id);
            
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
            
            CreateTable(
                "dbo.AspNetUserRoles",
                c => new
                    {
                        UserId = c.String(nullable: false, maxLength: 128),
                        RoleId = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => new { t.UserId, t.RoleId })
                .ForeignKey("dbo.AspNetUsers", t => t.UserId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetRoles", t => t.RoleId, cascadeDelete: true)
                .Index(t => t.UserId)
                .Index(t => t.RoleId);
            
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
                "dbo.Communities",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                        endpointUrl = c.String(),
                    })
                .PrimaryKey(t => t.Id);
            
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
                        user_Id = c.String(maxLength: 128),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Challenges", t => t.challengeId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetUsers", t => t.user_Id)
                .Index(t => t.challengeId)
                .Index(t => t.user_Id);
            
            CreateTable(
                "dbo.Devices",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        deviceTypeId = c.Int(nullable: false),
                        userId = c.Int(nullable: false),
                        accessToken = c.String(maxLength: 450),
                        refreshToken = c.String(maxLength: 450),
                        tokenExpiration = c.DateTime(),
                        user_Id = c.String(maxLength: 128),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.DeviceTypes", t => t.deviceTypeId, cascadeDelete: true)
                .ForeignKey("dbo.AspNetUsers", t => t.user_Id)
                .Index(t => t.deviceTypeId)
                .Index(t => t.user_Id);
            
            CreateTable(
                "dbo.DeviceTypes",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        name = c.String(),
                        tokenEndpoint = c.String(),
                        consumerKey = c.String(),
                        consumerSecret = c.String(),
                        clientId = c.String(),
                        authorizationEndpoint = c.String(),
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
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.AspNetUserRoles", "RoleId", "dbo.AspNetRoles");
            DropForeignKey("dbo.Devices", "user_Id", "dbo.AspNetUsers");
            DropForeignKey("dbo.Devices", "deviceTypeId", "dbo.DeviceTypes");
            DropForeignKey("dbo.UserChallenges", "user_Id", "dbo.AspNetUsers");
            DropForeignKey("dbo.UserChallenges", "challengeId", "dbo.Challenges");
            DropForeignKey("dbo.GroupChallenges", "team_Id", "dbo.Teams");
            DropForeignKey("dbo.GroupChallenges", "challengeId", "dbo.Challenges");
            DropForeignKey("dbo.AspNetUsers", "Team_Id", "dbo.Teams");
            DropForeignKey("dbo.Teams", "communityId", "dbo.Communities");
            DropForeignKey("dbo.Challenges", "community_Id", "dbo.Communities");
            DropForeignKey("dbo.ActivityDatas", "ApplicationUserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserRoles", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserLogins", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.AspNetUserClaims", "UserId", "dbo.AspNetUsers");
            DropForeignKey("dbo.CategoryUnits", "unitId", "dbo.Units");
            DropForeignKey("dbo.CategoryUnits", "categoryId", "dbo.Categories");
            DropForeignKey("dbo.ActivityDatas", "categoryUnitId", "dbo.CategoryUnits");
            DropIndex("dbo.AspNetRoles", "RoleNameIndex");
            DropIndex("dbo.Devices", new[] { "user_Id" });
            DropIndex("dbo.Devices", new[] { "deviceTypeId" });
            DropIndex("dbo.UserChallenges", new[] { "user_Id" });
            DropIndex("dbo.UserChallenges", new[] { "challengeId" });
            DropIndex("dbo.GroupChallenges", new[] { "team_Id" });
            DropIndex("dbo.GroupChallenges", new[] { "challengeId" });
            DropIndex("dbo.Teams", new[] { "communityId" });
            DropIndex("dbo.Challenges", new[] { "community_Id" });
            DropIndex("dbo.AspNetUserRoles", new[] { "RoleId" });
            DropIndex("dbo.AspNetUserRoles", new[] { "UserId" });
            DropIndex("dbo.AspNetUserLogins", new[] { "UserId" });
            DropIndex("dbo.AspNetUserClaims", new[] { "UserId" });
            DropIndex("dbo.AspNetUsers", new[] { "Team_Id" });
            DropIndex("dbo.AspNetUsers", "UserNameIndex");
            DropIndex("dbo.CategoryUnits", new[] { "unitId" });
            DropIndex("dbo.CategoryUnits", new[] { "categoryId" });
            DropIndex("dbo.ActivityDatas", new[] { "ApplicationUserId" });
            DropIndex("dbo.ActivityDatas", new[] { "categoryUnitId" });
            DropTable("dbo.AspNetRoles");
            DropTable("dbo.DeviceTypes");
            DropTable("dbo.Devices");
            DropTable("dbo.UserChallenges");
            DropTable("dbo.GroupChallenges");
            DropTable("dbo.Teams");
            DropTable("dbo.Communities");
            DropTable("dbo.Challenges");
            DropTable("dbo.AspNetUserRoles");
            DropTable("dbo.AspNetUserLogins");
            DropTable("dbo.AspNetUserClaims");
            DropTable("dbo.AspNetUsers");
            DropTable("dbo.Units");
            DropTable("dbo.Categories");
            DropTable("dbo.CategoryUnits");
            DropTable("dbo.ActivityDatas");
        }
    }
}
