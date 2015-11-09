namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Init1 : DbMigration
    {
        public override void Up()
        {
            RenameColumn(table: "dbo.Devices", name: "user_Id", newName: "ApplicationUserId");
            RenameIndex(table: "dbo.Devices", name: "IX_user_Id", newName: "IX_ApplicationUserId");
            DropColumn("dbo.Devices", "userId");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Devices", "userId", c => c.Int(nullable: false));
            RenameIndex(table: "dbo.Devices", name: "IX_ApplicationUserId", newName: "IX_user_Id");
            RenameColumn(table: "dbo.Devices", name: "ApplicationUserId", newName: "user_Id");
        }
    }
}
