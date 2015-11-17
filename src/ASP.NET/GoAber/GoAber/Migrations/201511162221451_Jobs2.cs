namespace GoAber.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Jobs2 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Jobs", "status_flag", c => c.Boolean(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Jobs", "status_flag");
        }
    }
}
