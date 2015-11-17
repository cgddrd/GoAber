namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Jobs1 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Jobs", "secretid", c => c.String());
            AddColumn("dbo.Jobs", "minutes", c => c.Int(nullable: false));
            DropColumn("dbo.Jobs", "cronexp");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Jobs", "cronexp", c => c.String());
            DropColumn("dbo.Jobs", "minutes");
            DropColumn("dbo.Jobs", "secretid");
        }
    }
}
